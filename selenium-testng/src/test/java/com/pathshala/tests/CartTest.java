package com.pathshala.tests;

import static org.testng.Assert.assertEquals;

import com.pathshala.base.BaseTest;
import io.qameta.allure.Description;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {

    @Test(groups = "smoke")
    @Description("Adding two products shows 2 on the cart badge")
    public void addToCartUpdatesBadge() {
        var inventory = loginAsStandardUser()
                .addToCart("sauce-labs-backpack")
                .addToCart("sauce-labs-bike-light");
        assertEquals(inventory.cartCount(), 2);
    }
}
