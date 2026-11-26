package com.pathshala.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class CartPage {

    private final Page page;
    private final Locator items;

    public CartPage(Page page) {
        this.page = page;
        this.items = page.getByTestId("inventory-item");
    }

    @Step("Go to checkout")
    public CheckoutPage checkout() {
        page.getByTestId("checkout").click();
        return new CheckoutPage(page);
    }

    public Locator items() {
        return items;
    }
}
