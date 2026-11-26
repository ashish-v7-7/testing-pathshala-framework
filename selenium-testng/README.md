# selenium-testng

A Selenium 4 + TestNG + Allure UI test framework for the public practice shop
[saucedemo.com](https://www.saucedemo.com). It is taught in Season 5, Episodes 3 and 4.

## What it tests

| Test class | Test | What it checks |
|---|---|---|
| `LoginTest` | `validLoginShowsProducts` | `standard_user` logs in and sees "Products" |
| `LoginTest` | `lockedOutUserSeesError` | `locked_out_user` sees "Epic sadface: Sorry, this user has been locked out." |
| `LoginTest` | `userCanLogin` (x4, `@DataProvider`) | `standard_user`, `problem_user`, `performance_glitch_user`, `visual_user` can all log in |
| `CartTest` | `addToCartUpdatesBadge` | Adding 2 products shows `2` on the cart badge |
| `CheckoutTest` | `fullCheckoutShowsThankYou` | Cart -> checkout -> details -> finish shows "Thank you for your order!" |
| `SortTest` | `sortByPriceLowToHigh` | "Price (low to high)" sorts prices ascending |
| `FailingDemoTest` | `wrongTitleFailsOnPurpose` | Fails on purpose (group `failing-demo`), excluded by default |

The default run executes **9 tests** (6 test methods, one of them runs 4 times with data).

## How to run

Needs JDK 21, Maven and Google Chrome. Selenium Manager downloads the right driver by itself.

```bash
mvn clean test                      # all tests, headless Chrome, 3 in parallel
mvn allure:report                   # builds target/site/allure-maven-plugin/index.html
mvn allure:serve                    # builds the report and opens it in a browser
```

Flags (any key in `config.properties` can be overridden with `-D`):

```bash
mvn test -Dheadless=false           # watch the browser
mvn test -Dbrowser=firefox          # Firefox instead of Chrome
mvn test -Dtimeout.seconds=20       # longer explicit waits
```

### The intentional failure demo

`FailingDemoTest` is in the TestNG group `failing-demo`. `testng.xml` excludes that group, so the normal run stays
green. To see a real failure:

```bash
mvn clean test -Pfailing-demo       # runs testng-failing.xml (only the failing-demo group)
mvn allure:report
```

The test expects the title "All Products" but the page says "Products". It uses `RetryAnalyzer`, so TestNG runs it
twice: the first attempt is marked skipped ("Screenshot before retry"), the second fails ("Screenshot on failure").
Both screenshots are attached to Allure by `ScreenshotListener`. Allure shows the first attempt under the
**Retries** tab. The retry is only applied to this demo test on purpose: retries can hide real bugs, so the
normal tests do not retry.

## Structure

```
selenium-testng/
  pom.xml                       dependencies, Surefire + AspectJ agent, Allure plugin, failing-demo profile
  testng.xml                    default suite: parallel="methods" thread-count="3", excludes failing-demo
  testng-failing.xml            only the failing-demo group
  src/test/resources/
    config.properties           base.url, browser, headless, timeout.seconds, password
    allure.properties           allure-results -> target/allure-results
  src/test/java/com/pathshala/
    config/ConfigReader         reads config.properties, -D system properties win
    driver/DriverFactory        ThreadLocal<WebDriver>, chrome (default) or firefox, headless flag
    base/BaseTest               @BeforeMethod starts the browser, @AfterMethod quits it
    pages/                      BasePage (WebDriverWait helpers), LoginPage, InventoryPage, CartPage, CheckoutPage
    listeners/ScreenshotListener ITestListener: screenshot -> Allure on failure
    listeners/RetryAnalyzer     IRetryAnalyzer: retry a failed test once
    tests/                      LoginTest, CartTest, CheckoutTest, SortTest, FailingDemoTest
```

Page methods carry Allure `@Step` annotations, so every report shows readable steps like
"Log in as standard_user". Tests carry `@Description`.

## Versions

Checked on Maven Central on 2026-11-26 (latest stable, no RC/beta/milestone):

| Library / plugin | Version |
|---|---|
| Java | 21 |
| selenium-java | 4.54.0 |
| testng | 7.12.0 |
| allure-testng (allure-bom) | 3.1.1 |
| aspectjweaver | 1.9.27 |
| slf4j-simple | 2.0.20 |
| maven-compiler-plugin | 3.16.0 |
| maven-surefire-plugin | 3.5.6 (see note) |
| allure-maven | 3.2.0 |
| Allure report (allure-commandline) | 2.46.2 |

Notes:
- **Surefire 3.6.x** is the newest, but it runs TestNG through the JUnit Platform and no longer reads
  `testng.xml` suite files. This framework is built around `testng.xml` (parallel settings, listeners, groups),
  so it uses the newest 3.5.x release.
- **maven-compiler-plugin 3.16.0** prints a harmless "I/O error reading dependency state ... target/classes"
  warning when a project has no `src/main` code. `maven.compiler.useIncrementalCompilation=false` in the pom removes it.
- **Allure Java 3.x** needs Java 17+ and TestNG 7.10+. The attachment API is `Allure.attachment(...)`
  (the old `Allure.addAttachment(...)` is gone).
- **allure-maven 3.x** uses the Allure 3 report by default. This project sets `<reportVersion>2.46.2</reportVersion>`
  to get the classic Allure 2 report (Overview, Suites, Behaviors, Graphs, Timeline), the same UI in every
  Season 5 module. The first `mvn allure:report` downloads that Allure version into `.allure/` in this folder
  (git-ignored). No global install is needed.
- `pom.xml` sets `allure.hostName` / `allure.threadName` for Surefire, so the report shows `pathshala-runner`
  instead of the computer's real host name.
- The Surefire console prints `Run 1: PASS` for the first (retried) attempt of the failing demo. That is a Surefire
  display quirk: TestNG and Allure mark the first attempt as skipped (retried) and the second as failed.
