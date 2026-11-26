package com.pathshala.bdd.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage {

    private final WebDriver driver;
    private final By firstName = By.id("first-name");
    private final By lastName = By.id("last-name");
    private final By postalCode = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By finishButton = By.id("finish");
    private final By completeHeader = By.className("complete-header");
    private final By error = By.cssSelector("[data-test='error']");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Fill in {first} {last}, {zip} and continue")
    public void fillInformation(String first, String last, String zip) {
        driver.findElement(firstName).sendKeys(first);
        driver.findElement(lastName).sendKeys(last);
        driver.findElement(postalCode).sendKeys(zip);
        driver.findElement(continueButton).click();
    }

    @Step("Finish the order")
    public void finish() {
        driver.findElement(finishButton).click();
    }

    public String confirmation() {
        return driver.findElement(completeHeader).getText();
    }

    public String error() {
        return driver.findElement(error).getText();
    }
}
