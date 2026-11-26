package com.pathshala.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class CheckoutPage {

    private final Page page;
    private final Locator completeHeader;

    public CheckoutPage(Page page) {
        this.page = page;
        this.completeHeader = page.getByTestId("complete-header");
    }

    @Step("Enter shipping info: {first} {last}, {zip}")
    public CheckoutPage fillInfo(String first, String last, String zip) {
        page.getByTestId("firstName").fill(first);
        page.getByTestId("lastName").fill(last);
        page.getByTestId("postalCode").fill(zip);
        page.getByTestId("continue").click();
        return this;
    }

    @Step("Finish the order")
    public CheckoutPage finish() {
        page.getByTestId("finish").click();
        return this;
    }

    public Locator completeHeader() {
        return completeHeader;
    }
}
