# Testing Ki Pathshala - Real Test Automation Project

The real project built step by step in **Season 5** of the Testing Ki Pathshala YouTube course:
four test automation frameworks, Allure reports, and CI/CD with GitHub Actions and Jenkins.

| Module | What it tests | Stack |
|---|---|---|
| [`selenium-testng`](selenium-testng) | https://www.saucedemo.com (web UI) | Selenium 4, TestNG, Page Objects, parallel, retry |
| [`playwright-java`](playwright-java) | https://www.saucedemo.com (web UI) | Playwright Java, JUnit, tracing on failure |
| [`restassured-api`](restassured-api) | https://restful-booker.herokuapp.com (REST API) | REST Assured, TestNG, POJOs, JSON schema |
| [`cucumber-bdd`](cucumber-bdd) | https://www.saucedemo.com (web UI) | Cucumber (Gherkin), Selenium, PicoContainer |

Every module writes Allure results; CI merges them into one Allure report.

## Run locally

Java 21 and Maven are needed. Browsers run headless by default.

```bash
cd selenium-testng      # or playwright-java, restassured-api, cucumber-bdd
mvn test                # run the tests
mvn allure:report       # HTML report in target/site/allure-maven-plugin/index.html
```

Each module's README explains its structure, flags and the intentional failing demo.

## CI/CD

- **GitHub Actions**: [`.github/workflows/tests.yml`](.github/workflows/tests.yml) runs all four modules in a
  matrix on every push, pull request, nightly and on demand, then publishes one combined Allure report to
  GitHub Pages.
- **Jenkins**: [`Jenkinsfile`](Jenkinsfile) (parameters, nightly trigger, parallel stages, Allure plugin) and a
  ready-to-run local Jenkins in Docker under [`ci/jenkins`](ci/jenkins).

The practice sites are public demo applications. The logins in the config files are their published demo
credentials.
