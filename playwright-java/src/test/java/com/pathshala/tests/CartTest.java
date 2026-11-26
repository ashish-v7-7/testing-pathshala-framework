package com.pathshala.tests;

import com.pathshala.base.BaseTest;
import com.pathshala.pages.CartPage;
import com.pathshala.pages.CheckoutPage;
import com.pathshala.pages.InventoryPage;
import com.pathshala.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Feature("Cart and Checkout")
class CartTest extends BaseTest {

    @Test
    @DisplayName("Adding two products shows 2 on the cart badge")
    void addToCartUpdatesBadge() {
        InventoryPage inventory = new LoginPage(page).open().loginAs("standard_user", PASSWORD);
        inventory.addToCart("sauce-labs-backpack").addToCart("sauce-labs-bike-light");

        assertThat(inventory.cartBadge()).hasText("2");
    }

    @Test
    @DisplayName("Checkout completes with a thank-you message")
    @Description("Happy path: login, add a product, cart, shipping info, finish")
    void checkoutComplete() {
        CartPage cart = new LoginPage(page).open()
                .loginAs("standard_user", PASSWORD)
                .addToCart("sauce-labs-backpack")
                .openCart();
        assertThat(cart.items()).hasCount(1);

        CheckoutPage checkout = cart.checkout().fillInfo("Zara", "Khan", "110001").finish();

        assertThat(checkout.completeHeader()).hasText("Thank you for your order!");
    }
}
