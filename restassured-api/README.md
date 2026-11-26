# restassured-api

API tests for the public practice API **https://restful-booker.herokuapp.com**
(REST Assured + TestNG + Jackson + Allure). Season 5, Episodes 6-7 of Testing Ki Pathshala.

## What it tests (11 test runs, 9 methods)

| Test class | What it checks | Real status from restful-booker |
|---|---|---|
| HealthCheckTest | `GET /ping` is up | 201 Created |
| BookingCrudTest | create, get by id, full update (PUT + token), partial update (PATCH + token), delete then GET | create 200 (not 201), delete 201, GET after delete 404 |
| AuthTest | PUT without token; bad logins via `@DataProvider` (3 rows) | 403; bad login is 200 with `{"reason":"Bad credentials"}` |
| SchemaTest | `GET /booking/{id}` matches `schemas/booking-schema.json` | 200 |
| FailingDemoTest | group `failing-demo`: expects 200 from `/ping`, fails on purpose | 201 |

Every test creates its own booking with random data (`TestDataFactory`), and `BaseTest` deletes the created
bookings in `@AfterClass`. No hard-coded ids. The auth token is fetched once and cached (`AuthClient.token()`).
Every request and response is attached to Allure by the `AllureRestAssured` filter in `Specs`.

## Run

```bash
mvn clean test                          # 11 tests, failing-demo group excluded (testng.xml)
mvn allure:report                       # -> target/site/allure-maven-plugin/index.html
mvn test -Dsuite=failing-demo.xml       # only the intentional failure (BUILD FAILURE is expected)
mvn test -DbaseUri=https://other.host -DreadTimeoutMs=60000   # any config.properties key can be overridden
```

`mvn allure:report` builds the classic **Allure 2 report (2.46.2)** with Suites, Behaviors, Graphs and Timeline
(`reportVersion` in the pom; the plugin downloads the Allure commandline into `.allure/` on first run).
Open it over http, e.g. `cd target/site/allure-maven-plugin && python3 -m http.server 8000 --bind 127.0.0.1`.
Every request/response appears as an "HTTP exchange" attachment inside each step.

## Keeping secrets out of the report

- `@Param(mode = MASKED)` on the `token` (BookingClient) and `password` (AuthClient.login) step parameters: the
  report shows `******`. The raw value is still in `target/allure-results` JSON, which is git-ignored.
- `new AllureRestAssured().configureHttpExchange(exchange -> exchange.redactCookie("token"))` in `Specs`: the token
  cookie is shown as `*****`. Allure already redacts the `Authorization`, `Proxy-Authorization`, `Cookie` and
  `Set-Cookie` headers by default, but REST Assured's `.cookie(...)` is recorded as a separate cookie entry, which is
  not redacted unless you name it.
- Not covered: JSON bodies are never redacted, so the `POST /auth` exchange in each class's "Set up" shows the
  (public demo) password and the returned token.
- `allure.hostName` / `allure.threadName` are fixed in surefire (`pathshala-runner`, `test-worker`) so the report
  never shows the machine's real hostname.

## Structure

```
pom.xml, testng.xml, failing-demo.xml
src/test/java/com/pathshala/api/
  config/ConfigReader        config.properties + -D overrides
  specs/Specs                RequestSpecBuilder (base URI, JSON, timeouts, Allure filter), ResponseSpecBuilder
  clients/AuthClient         POST /auth, token cached once
  clients/BookingClient      create / get / update / partialUpdate / delete
  models/Booking, BookingDates, BookingResponse   Jackson POJOs as Java records
  data/TestDataFactory       unique random bookings
  tests/...                  BaseTest + test classes
src/test/resources/
  config.properties, allure.properties, simplelogger.properties, schemas/booking-schema.json
```

## Gotchas found while building

- `setAccept(ContentType.JSON)` sends `application/json, application/javascript, text/javascript, text/json`;
  restful-booker answers that with **418 I'm a teapot**. Use `setAccept("application/json")`.
- maven-surefire-plugin 3.6.x runs TestNG on the JUnit Platform and rejects `suiteXmlFiles`, so this module
  stays on 3.5.6 to keep `testng.xml`.
- The API is shared and hosted on Heroku: a run takes about 40 s; the first call can be slow when the app wakes up.
  Timeouts: connect 10 s, read 30 s (`config.properties`). The data resets periodically.

## Versions (latest stable on Maven Central, 2026-11-26)

| Library / plugin | Version |
|---|---|
| Java | 21 |
| io.rest-assured:rest-assured, json-schema-validator | 6.3.0 (7.0.0-RC1 skipped) |
| org.testng:testng | 7.12.0 |
| com.fasterxml.jackson.core:jackson-databind | 2.23.0 |
| io.qameta.allure:allure-bom (allure-testng, allure-rest-assured) | 3.1.1 |
| org.aspectj:aspectjweaver | 1.9.27 |
| org.slf4j:slf4j-simple | 2.0.20 |
| maven-compiler-plugin | 3.16.0 |
| maven-surefire-plugin | 3.5.6 (see gotchas) |
| io.qameta.allure:allure-maven | 3.2.0, `reportVersion` 2.46.2 (Allure 2 report) |
