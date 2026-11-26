package com.pathshala.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class LoginPage {

    private final Page page;
    private final Locator username;
    private final Locator password;
    private final Locator loginButton;
    private final Locator error;

    public LoginPage(Page page) {
        this.page = page;
        this.username = page.getByTestId("username");
        this.password = page.getByTestId("password");
        this.loginButton = page.getByTestId("login-button");
        this.error = page.getByTestId("error");
    }

    @Step("Open the login page")
    public LoginPage open() {
        page.navigate("/");
        return this;
    }

    @Step("Log in as '{user}'")
    public InventoryPage loginAs(String user, String pass) {
        username.fill(user);
        password.fill(pass);
        loginButton.click();
        return new InventoryPage(page);
    }

    public Locator error() {
        return error;
    }
}
