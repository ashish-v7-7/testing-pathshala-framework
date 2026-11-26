package com.pathshala.pages;

import io.qameta.allure.Step;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class InventoryPage extends BasePage {

    private final By title = By.cssSelector("[data-test='title']");
    private final By cartBadge = By.cssSelector("[data-test='shopping-cart-badge']");
    private final By cartLink = By.cssSelector("[data-test='shopping-cart-link']");
    private final By sortDropdown = By.cssSelector("[data-test='product-sort-container']");
    private final By prices = By.cssSelector("[data-test='inventory-item-price']");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    @Step("Read the page title")
    public String title() {
        return textOf(title);
    }

    @Step("Add product '{productId}' to the cart")
    public InventoryPage addToCart(String productId) {
        click(By.id("add-to-cart-" + productId));
        return this;
    }

    @Step("Read the cart badge count")
    public int cartCount() {
        return Integer.parseInt(textOf(cartBadge));
    }

    @Step("Sort products by '{visibleText}'")
    public InventoryPage sortBy(String visibleText) {
        new Select(visible(sortDropdown)).selectByVisibleText(visibleText);
        return this;
    }

    @Step("Read all product prices")
    public List<Double> prices() {
        visible(prices);
        return driver.findElements(prices).stream()
                .map(WebElement::getText)
                .map(text -> Double.parseDouble(text.replace("$", "")))
                .toList();
    }

    @Step("Open the cart")
    public CartPage openCart() {
        click(cartLink);
        return new CartPage(driver);
    }
}
