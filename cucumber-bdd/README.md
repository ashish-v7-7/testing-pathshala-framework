# cucumber-bdd

BDD tests for https://www.saucedemo.com written in Gherkin, run by Cucumber JVM on the
JUnit Platform, driven by Selenium 4 (headless Chrome), reported with Allure.

## What it tests

| Feature | Scenarios | Tags |
|---|---|---|
| `login.feature` | valid login; Scenario Outline for `standard_user`, `locked_out_user`, a wrong user | `@smoke`, `@regression` |
| `cart.feature` | add one item, add two items, remove an item (Background: log in) | `@smoke`, `@regression` |
| `checkout.feature` | complete checkout, postal code required (Background: log in + add item) | `@smoke`, `@regression` |
| `login.feature` | intentional failure (expects the wrong page title) | `@failing-demo` |

9 scenarios run by default. `@failing-demo` is excluded by default.

## How to run

```bash
mvn test                                           # all scenarios except @failing-demo
mvn test -Dcucumber.filter.tags="@smoke"           # only smoke (3 scenarios)
mvn test -Dcucumber.filter.tags="@regression"      # only regression (9 scenarios)
mvn test -Dcucumber.filter.tags="@failing-demo"    # the intentional failure (BUILD FAILURE + screenshot)
mvn test -Dheadless=false                          # watch the browser
mvn test -Dbrowser=firefox                         # chrome (default) or firefox
mvn allure:report                                  # HTML report -> target/site/allure-maven-plugin/index.html
```

The Allure report is a JS app, so serve it over http, e.g.
`python3 -m http.server 8080 --directory target/site/allure-maven-plugin`.
The first `mvn allure:report` downloads the Allure 2 command line into `.allure/`.

Scenarios left out by a tag filter are reported by Surefire as "skipped". That is how the
Cucumber JUnit Platform engine reports filtered scenarios, so `Skipped: 1` in a default run is expected.

## Structure

```
src/test/java/com/pathshala/bdd
  RunCucumberTest.java    @Suite + @IncludeEngines("cucumber") + @SelectPackages("features")
  config/ConfigReader     config.properties, overridable with -Dkey=value
  driver/DriverFactory    Chrome/Firefox, headless by default, Selenium Manager finds the driver
  context/TestContext     shared per scenario by PicoContainer: driver + page objects
  hooks/Hooks             @Before starts the browser, @After attaches a screenshot on failure and quits
  pages/                  LoginPage, InventoryPage, CartPage, CheckoutPage (with Allure @Step)
  steps/                  LoginSteps, CartSteps, CheckoutSteps (constructor-injected TestContext)
src/test/resources
  features/               login.feature, cart.feature, checkout.feature
  junit-platform.properties  glue, plugins, default tag filter "not @failing-demo"
  config.properties       baseUrl, browser, headless
  allure.properties       results directory
```

## Versions (latest stable on Maven Central, 2026-11-26)

| Library / plugin | Version |
|---|---|
| Java | 21 |
| cucumber-java, cucumber-junit-platform-engine, cucumber-picocontainer (cucumber-bom) | 8.1.0 |
| JUnit Platform (junit-bom, the version Cucumber 8.1.0 is built with) | 6.1.3 |
| selenium-java | 4.54.0 |
| allure-cucumber8-jvm (allure-bom) | 3.1.1 |
| aspectjweaver (for Allure @Step) | 1.9.27 |
| slf4j-nop (silences the SLF4J "no provider" warning) | 2.0.20 |
| allure-maven plugin | 3.2.0 (report built with Allure 2.46.2) |
| maven-surefire-plugin | 3.6.1 |
| maven-compiler-plugin | 3.16.0 |
