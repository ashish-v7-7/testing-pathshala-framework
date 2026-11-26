package com.pathshala.bdd.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pathshala.bdd.context.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginSteps {

    private final TestContext context;

    public LoginSteps(TestContext context) {
        this.context = context;
    }

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        context.loginPage().open();
    }

    @When("I log in as {string} with password {string}")
    public void iLogInAs(String username, String password) {
        context.loginPage().loginAs(username, password);
    }

    @Given("I am logged in as {string}")
    public void iAmLoggedInAs(String username) {
        iAmOnTheLoginPage();
        iLogInAs(username, "secret_sauce");
    }

    @Then("I should see the products page")
    public void iShouldSeeTheProductsPage() {
        assertEquals("Products", context.inventoryPage().title());
    }

    @Then("I should see the message {string}")
    public void iShouldSeeTheMessage(String message) {
        assertTrue(context.loginPage().showsText(message), "Expected to see: " + message);
    }

    @Then("the page title should be {string}")
    public void thePageTitleShouldBe(String expected) {
        assertEquals(expected, context.inventoryPage().title());
    }
}
