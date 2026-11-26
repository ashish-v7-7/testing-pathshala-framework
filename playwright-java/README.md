# playwright-java

Web UI tests for [Sauce Demo](https://www.saucedemo.com) with **Playwright for Java + JUnit + Allure**.
Taught in Season 5, Episode 5: "Playwright Framework - Fixtures, Tracing, Parallel".

## What it tests

| Class | Test |
|---|---|
| `LoginTest` | valid login lands on Products; locked-out user sees an error; `@ParameterizedTest` with 3 bad-credential rows |
| `CartTest` | adding 2 products shows `2` on the cart badge; full checkout shows "Thank you for your order!" |
| `InventoryTest` | sort "Price (low to high)" really sorts the prices |
| `FailingDemoTest` | tagged `failing-demo`, fails **on purpose**, skipped by default |

8 tests run by default (the parameterized test counts as 3).

## How to run

```bash
mvn clean test                         # all tests, headless chromium, in parallel
mvn clean test -Dheadless=false        # watch the browser
mvn clean test -Dbrowser=firefox       # chromium (default) | firefox | webkit
mvn clean test -Dtest=LoginTest        # one class
mvn clean test -Pfailing-demo          # ONLY the intentional failure (build fails, evidence is saved)
mvn allure:report                      # HTML report -> target/site/allure-maven-plugin/index.html
mvn allure:serve                       # build the report and open it in a browser
```

One report with the green tests AND the failure demo: `mvn clean test`, then `mvn test -Pfailing-demo`
(no `clean`, so the results add up), then `mvn allure:report`.

The first run downloads the Playwright browsers from Playwright's CDN (once, into the OS cache).
The report uses the Allure 2 UI (`<reportVersion>2.46.2</reportVersion>` in the allure-maven plugin); the plugin
downloads that Allure commandline into `.allure/` the first time (git-ignored).
Surefire sets `allure.hostName` / `allure.threadName` so the report shows neutral host and thread labels.

Any key in `src/test/resources/config.properties` (`baseUrl`, `browser`, `headless`) can be overridden with `-Dkey=value`.

## Framework design

- **BaseTest** follows the Playwright docs ("Test Runners" page): `@TestInstance(PER_CLASS)`, so each test class
  gets its **own Playwright + Browser** (`@BeforeAll`), and each test gets a **fresh BrowserContext + Page**
  (`@BeforeEach`). It also sets `selectors().setTestIdAttribute("data-test")`, because Sauce Demo uses `data-test="..."`,
  so `page.getByTestId("login-button")` works.
- **Allure**: `@Feature` per test class (Behaviors tab), `@DisplayName`, `@Description`, and `@Step` in page objects.
- **Page Objects** (`LoginPage`, `InventoryPage`, `CartPage`, `CheckoutPage`) hold Playwright `Locator`s and
  `@Step` methods that show up as steps in Allure.
- **Web-first assertions**: `assertThat(locator).hasText(...)` from `PlaywrightAssertions` waits and retries
  automatically - no sleeps, no explicit waits.
- **Parallel** (`junit-platform.properties`): test classes run at the same time, the tests inside one class
  run one after another. This is safe because Playwright objects are not thread-safe, and here each class
  (and so each thread) owns its own Playwright instance.
- **Tracing + screenshot only on failure** (`TraceOnFailure` extension): tracing starts for every test; right after
  the test body, a passed test throws the trace away, and a failed test saves `target/traces/<Class>_<test>.zip`
  and attaches a screenshot of the page plus the trace zip to Allure.

We use a clean `BaseTest` instead of `@UsePlaywright`, because the Playwright docs still label the `@UsePlaywright`
fixtures as *experimental*, and they give no hook to keep a trace only on failure.

### Open a trace

```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI \
  -D exec.args="show-trace target/traces/FailingDemoTest_DEMO_expects_a_wrong_page_title_fails_on_purpose_.zip"
```

This opens the Trace Viewer window (a desktop window, so run it on your own machine, not in CI).
Or drag the zip (also downloadable from the failed test in Allure) onto https://trace.playwright.dev;
the trace stays in your browser.

## Structure

```
pom.xml
src/test/resources/
  config.properties            baseUrl, browser, headless
  junit-platform.properties    parallel settings + Allure parameter support
  allure.properties            results -> target/allure-results
src/test/java/com/pathshala/
  config/ConfigReader.java
  base/BaseTest.java           Playwright/Browser per class, Context/Page per test
  base/TraceOnFailure.java     trace zip + Allure screenshot on failure
  pages/                       LoginPage, InventoryPage, CartPage, CheckoutPage
  tests/                       LoginTest, CartTest, InventoryTest, FailingDemoTest
```

## Versions (latest stable on Maven Central, 2026-11-26)

| Library / plugin | Version |
|---|---|
| Java | 21 |
| com.microsoft.playwright:playwright | 1.65.1 |
| JUnit (junit-bom) | 6.1.3 |
| Allure Java (allure-bom, `allure-jupiter`) | 3.1.1 |
| AspectJ weaver (for `@Step`) | 1.9.27 |
| allure-maven plugin | 3.2.0 |
| Allure report UI (allure-commandline, `reportVersion`) | 2.46.2 |
| maven-surefire-plugin | 3.6.1 |
| maven-compiler-plugin | 3.16.0 |
| exec-maven-plugin | 3.7.0 |
| slf4j-simple (logger for Allure) | 2.0.20 |

Note: in Allure Java 3.x the JUnit adapter is `allure-jupiter` (it replaced `allure-junit5`, which is no longer
published) and it works with JUnit 5 and JUnit 6.
