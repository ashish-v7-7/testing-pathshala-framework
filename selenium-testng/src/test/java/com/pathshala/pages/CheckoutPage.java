package com.pathshala.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {

    private final By firstName = By.id("first-name");
    private final By lastName = By.id("last-name");
    private final By postalCode = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By finishButton = By.id("finish");
    private final By completeHeader = By.cssSelector("[data-test='complete-header']");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    @Step("Fill details: {first} {last}, {zip}")
    public CheckoutPage fillDetails(String first, String last, String zip) {
        type(firstName, first);
        type(lastName, last);
        type(postalCode, zip);
        click(continueButton);
        return this;
    }

    @Step("Finish the order")
    public CheckoutPage finish() {
        click(finishButton);
        return this;
    }

    @Step("Read the confirmation message")
    public String confirmationMessage() {
        return textOf(completeHeader);
    }
}
