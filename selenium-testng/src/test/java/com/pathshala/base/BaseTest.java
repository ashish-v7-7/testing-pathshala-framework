package com.pathshala.base;

import com.pathshala.config.ConfigReader;
import com.pathshala.driver.DriverFactory;
import com.pathshala.pages.InventoryPage;
import com.pathshala.pages.LoginPage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

    protected static final String STANDARD_USER = "standard_user";
    protected static final String PASSWORD = ConfigReader.get("password");

    @BeforeMethod(alwaysRun = true)
    public void startBrowser() {
        DriverFactory.initDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void closeBrowser() {
        DriverFactory.quitDriver();
    }

    protected LoginPage loginPage() {
        return new LoginPage(DriverFactory.getDriver()).open();
    }

    protected InventoryPage loginAsStandardUser() {
        return loginPage().loginAs(STANDARD_USER, PASSWORD);
    }
}
