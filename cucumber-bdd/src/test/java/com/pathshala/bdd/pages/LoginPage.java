package com.pathshala.bdd.pages;

import com.pathshala.bdd.config.ConfigReader;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private final WebDriver driver;
    private final By username = By.id("user-name");
    private final By password = By.id("password");
    private final By loginButton = By.id("login-button");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Open the login page")
    public void open() {
        driver.get(ConfigReader.get("baseUrl"));
    }

    @Step("Log in as {user}")
    public void loginAs(String user, String pass) {
        driver.findElement(username).sendKeys(user);
        driver.findElement(password).sendKeys(pass);
        driver.findElement(loginButton).click();
    }

    public boolean showsText(String text) {
        return !driver.findElements(By.xpath("//*[contains(text(), '" + text + "')]")).isEmpty();
    }
}
