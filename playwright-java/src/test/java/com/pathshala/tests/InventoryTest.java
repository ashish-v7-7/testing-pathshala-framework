package com.pathshala.tests;

import com.pathshala.base.BaseTest;
import com.pathshala.pages.InventoryPage;
import com.pathshala.pages.LoginPage;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Feature("Products")
class InventoryTest extends BaseTest {

    @Test
    @DisplayName("Sort by price: low to high")
    void sortLowToHigh() {
        InventoryPage inventory = new LoginPage(page).open()
                .loginAs("standard_user", PASSWORD)
                .sortBy("lohi");

        assertThat(inventory.prices().first()).hasText("$7.99");
        List<Double> prices = inventory.priceValues();
        assertEquals(prices.stream().sorted().toList(), prices);
    }
}
