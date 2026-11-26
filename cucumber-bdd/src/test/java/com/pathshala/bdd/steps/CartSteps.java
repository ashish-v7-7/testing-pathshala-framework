package com.pathshala.bdd.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.pathshala.bdd.context.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CartSteps {

    private final TestContext context;

    public CartSteps(TestContext context) {
        this.context = context;
    }

    @When("I add {string} to the cart")
    public void iAddToTheCart(String product) {
        context.inventoryPage().addToCart(product);
    }

    @When("I remove {string} from the cart")
    public void iRemoveFromTheCart(String product) {
        context.inventoryPage().removeFromCart(product);
    }

    @When("I open the cart")
    public void iOpenTheCart() {
        context.inventoryPage().openCart();
    }

    @Then("the cart badge should show {int}")
    public void theCartBadgeShouldShow(int count) {
        assertEquals(count, context.inventoryPage().cartBadgeCount());
    }

    @Then("the cart should contain {int} items")
    public void theCartShouldContainItems(int count) {
        assertEquals(count, context.cartPage().itemCount());
    }
}
