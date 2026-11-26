package com.pathshala.bdd.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage {

    private final WebDriver driver;
    private final By cartItem = By.className("cart_item");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public int itemCount() {
        return driver.findElements(cartItem).size();
    }

    @Step("Start checkout")
    public void checkout() {
        driver.findElement(checkoutButton).click();
    }
}
