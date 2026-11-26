package com.pathshala.tests;

import com.pathshala.base.BaseTest;
import com.pathshala.pages.InventoryPage;
import com.pathshala.pages.LoginPage;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Feature("Login")
class LoginTest extends BaseTest {

    @Test
    @DisplayName("Valid user lands on the Products page")
    void validLogin() {
        InventoryPage inventory = new LoginPage(page).open().loginAs("standard_user", PASSWORD);

        assertThat(page).hasURL(Pattern.compile(".*/inventory\\.html"));
        assertThat(inventory.title()).hasText("Products");
    }

    @Test
    @DisplayName("Locked-out user sees an error")
    void lockedOutUser() {
        LoginPage login = new LoginPage(page).open();
        login.loginAs("locked_out_user", PASSWORD);

        assertThat(login.error()).containsText("Sorry, this user has been locked out.");
    }

    @ParameterizedTest(name = "[{index}] {2}")
    @DisplayName("Bad credentials show the right error")
    @CsvSource({
            "standard_user, wrong_pass,   Username and password do not match",
            "'',            secret_sauce, Username is required",
            "standard_user, '',           Password is required"
    })
    void invalidLogin(String user, String pass, String expectedError) {
        LoginPage login = new LoginPage(page).open();
        login.loginAs(user, pass);

        assertThat(login.error()).containsText(expectedError);
    }
}
