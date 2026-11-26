package com.pathshala.tests;

import static org.testng.Assert.assertEquals;

import com.pathshala.base.BaseTest;
import io.qameta.allure.Description;
import org.testng.annotations.Test;

public class CheckoutTest extends BaseTest {

    @Test(groups = "smoke")
    @Description("A user buys one product end to end")
    public void fullCheckoutShowsThankYou() {
        var cart = loginAsStandardUser()
                .addToCart("sauce-labs-backpack")
                .openCart();
        assertEquals(cart.itemCount(), 1);

        var checkout = cart.checkout()
                .fillDetails("Zara", "Khan", "110001")
                .finish();
        assertEquals(checkout.confirmationMessage(), "Thank you for your order!");
    }
}
