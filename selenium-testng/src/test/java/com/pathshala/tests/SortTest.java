package com.pathshala.tests;

import static org.testng.Assert.assertEquals;

import com.pathshala.base.BaseTest;
import io.qameta.allure.Description;
import java.util.Comparator;
import java.util.List;
import org.testng.annotations.Test;

public class SortTest extends BaseTest {

    @Test
    @Description("Sorting by price low to high orders the prices ascending")
    public void sortByPriceLowToHigh() {
        List<Double> prices = loginAsStandardUser()
                .sortBy("Price (low to high)")
                .prices();
        List<Double> expected = prices.stream().sorted(Comparator.naturalOrder()).toList();
        assertEquals(prices, expected);
    }
}
