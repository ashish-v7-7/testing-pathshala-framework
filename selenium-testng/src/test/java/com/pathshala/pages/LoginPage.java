package com.pathshala.pages;

import com.pathshala.config.ConfigReader;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By username = By.id("user-name");
    private final By password = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open the login page")
    public LoginPage open() {
        driver.get(ConfigReader.get("base.url"));
        return this;
    }

    @Step("Log in as {user}")
    public InventoryPage loginAs(String user, String pass) {
        type(username, user);
        type(password, pass);
        click(loginButton);
        return new InventoryPage(driver);
    }

    @Step("Read the error message")
    public String errorMessage() {
        return textOf(errorMessage);
    }
}
