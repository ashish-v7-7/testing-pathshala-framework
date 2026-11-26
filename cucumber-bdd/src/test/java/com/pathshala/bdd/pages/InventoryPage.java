package com.pathshala.bdd.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InventoryPage {

    private final WebDriver driver;
    private final By title = By.className("title");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public String title() {
        return driver.findElement(title).getText();
    }

    @Step("Add {product} to the cart")
    public void addToCart(String product) {
        driver.findElement(By.id("add-to-cart-" + slug(product))).click();
    }

    @Step("Remove {product} from the cart")
    public void removeFromCart(String product) {
        driver.findElement(By.id("remove-" + slug(product))).click();
    }

    public int cartBadgeCount() {
        var badges = driver.findElements(cartBadge);
        return badges.isEmpty() ? 0 : Integer.parseInt(badges.get(0).getText());
    }

    @Step("Open the cart")
    public void openCart() {
        driver.findElement(cartLink).click();
    }

    private String slug(String product) {
        return product.toLowerCase().replace(' ', '-');
    }
}
