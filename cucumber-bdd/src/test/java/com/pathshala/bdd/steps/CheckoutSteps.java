package com.pathshala.bdd.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.pathshala.bdd.context.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CheckoutSteps {

    private final TestContext context;

    public CheckoutSteps(TestContext context) {
        this.context = context;
    }

    @When("I check out as {string} {string} with postal code {string}")
    public void iCheckOutAs(String first, String last, String zip) {
        context.cartPage().checkout();
        context.checkoutPage().fillInformation(first, last, zip);
    }

    @Then("I should see the order confirmation {string}")
    public void iShouldSeeTheOrderConfirmation(String message) {
        context.checkoutPage().finish();
        assertEquals(message, context.checkoutPage().confirmation());
    }

    @Then("I should see the checkout error {string}")
    public void iShouldSeeTheCheckoutError(String message) {
        assertEquals(message, context.checkoutPage().error());
    }
}
