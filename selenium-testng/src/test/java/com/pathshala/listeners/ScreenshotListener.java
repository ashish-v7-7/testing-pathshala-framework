package com.pathshala.listeners;

import static io.qameta.allure.AttachmentOptions.withFileExtension;

import com.pathshala.driver.DriverFactory;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ScreenshotListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        attachScreenshot("Screenshot on failure");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (result.wasRetried()) {
            attachScreenshot("Screenshot before retry");
        }
    }

    private void attachScreenshot(String name) {
        WebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            ((JavascriptExecutor) driver).executeAsyncScript("document.fonts.ready.then(arguments[0])");
            byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.attachment(name, "image/png", new ByteArrayInputStream(png), withFileExtension("png"));
        }
    }
}
