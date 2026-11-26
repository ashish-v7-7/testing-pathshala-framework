package com.pathshala.bdd.context;

import com.pathshala.bdd.pages.CartPage;
import com.pathshala.bdd.pages.CheckoutPage;
import com.pathshala.bdd.pages.InventoryPage;
import com.pathshala.bdd.pages.LoginPage;
import org.openqa.selenium.WebDriver;

public class TestContext {

    private WebDriver driver;
    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    public void start(WebDriver driver) {
        this.driver = driver;
        this.loginPage = new LoginPage(driver);
        this.inventoryPage = new InventoryPage(driver);
        this.cartPage = new CartPage(driver);
        this.checkoutPage = new CheckoutPage(driver);
    }

    public WebDriver driver() { return driver; }
    public LoginPage loginPage() { return loginPage; }
    public InventoryPage inventoryPage() { return inventoryPage; }
    public CartPage cartPage() { return cartPage; }
    public CheckoutPage checkoutPage() { return checkoutPage; }
}
