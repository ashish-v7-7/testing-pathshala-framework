package com.pathshala.base;

import com.microsoft.playwright.*;
import com.pathshala.config.ConfigReader;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

/** One Playwright + Browser per test class, a fresh BrowserContext + Page per test. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(TraceOnFailure.class)
public abstract class BaseTest {

    protected static final String PASSWORD = "secret_sauce";

    private Playwright playwright;
    private Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeAll
    void launchBrowser() {
        playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
        browser = browserType().launch(new BrowserType.LaunchOptions().setHeadless(ConfigReader.headless()));
    }

    @BeforeEach
    void openPage() {
        context = browser.newContext(new Browser.NewContextOptions().setBaseURL(ConfigReader.baseUrl()));
        context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true));
        page = context.newPage();
    }

    @AfterEach
    void closePage() {
        context.close();
    }

    @AfterAll
    void closeBrowser() {
        playwright.close();
    }

    private BrowserType browserType() {
        return switch (ConfigReader.browser()) {
            case "firefox" -> playwright.firefox();
            case "webkit" -> playwright.webkit();
            default -> playwright.chromium();
        };
    }
}
