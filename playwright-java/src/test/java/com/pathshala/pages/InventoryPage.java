package com.pathshala.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import java.util.List;

public class InventoryPage {

    private final Page page;
    private final Locator title;
    private final Locator cartBadge;
    private final Locator prices;

    public InventoryPage(Page page) {
        this.page = page;
        this.title = page.getByTestId("title");
        this.cartBadge = page.getByTestId("shopping-cart-badge");
        this.prices = page.getByTestId("inventory-item-price");
    }

    @Step("Add '{product}' to the cart")
    public InventoryPage addToCart(String product) {
        page.getByTestId("add-to-cart-" + product).click();
        return this;
    }

    @Step("Sort products by '{option}'")
    public InventoryPage sortBy(String option) {
        page.getByTestId("product-sort-container").selectOption(option);
        return this;
    }

    @Step("Open the cart")
    public CartPage openCart() {
        page.getByTestId("shopping-cart-link").click();
        return new CartPage(page);
    }

    public List<Double> priceValues() {
        return prices.allTextContents().stream()
                .map(text -> Double.parseDouble(text.replace("$", "")))
                .toList();
    }

    public Locator title() { return title; }
    public Locator cartBadge() { return cartBadge; }
    public Locator prices() { return prices; }
}
