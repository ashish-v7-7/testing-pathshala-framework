package com.pathshala.bdd.hooks;

import com.pathshala.bdd.context.TestContext;
import com.pathshala.bdd.driver.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks {

    private final TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    @Before
    public void startBrowser() {
        context.start(DriverFactory.create());
    }

    @After
    public void stopBrowser(Scenario scenario) {
        if (scenario.isFailed()) {
            scenario.attach(screenshot(), "image/png", "Screenshot on failure");
        }
        context.driver().quit();
    }

    private byte[] screenshot() {
        ((JavascriptExecutor) context.driver()).executeAsyncScript("document.fonts.ready.then(arguments[0])");
        return ((TakesScreenshot) context.driver()).getScreenshotAs(OutputType.BYTES);
    }
}
