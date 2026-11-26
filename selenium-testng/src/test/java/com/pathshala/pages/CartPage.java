package com.pathshala.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {

    private final By cartItem = By.className("cart_item");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    @Step("Count items in the cart")
    public int itemCount() {
        visible(cartItem);
        return driver.findElements(cartItem).size();
    }

    @Step("Start checkout")
    public CheckoutPage checkout() {
        click(checkoutButton);
        return new CheckoutPage(driver);
    }
}
