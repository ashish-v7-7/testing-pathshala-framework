package com.pathshala.tests;

import static org.testng.Assert.assertEquals;

import com.pathshala.base.BaseTest;
import io.qameta.allure.Description;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(groups = "smoke")
    @Description("A standard user logs in and lands on the Products page")
    public void validLoginShowsProducts() {
        assertEquals(loginAsStandardUser().title(), "Products");
    }

    @Test
    @Description("A locked-out user sees the locked-out error message")
    public void lockedOutUserSeesError() {
        var login = loginPage();
        login.loginAs("locked_out_user", PASSWORD);
        assertEquals(login.errorMessage(), "Epic sadface: Sorry, this user has been locked out.");
    }

    @DataProvider(name = "users")
    public Object[][] users() {
        return new Object[][] {
            {"standard_user"}, {"problem_user"}, {"performance_glitch_user"}, {"visual_user"}
        };
    }

    @Test(dataProvider = "users")
    @Description("Every valid user can log in")
    public void userCanLogin(String username) {
        assertEquals(loginPage().loginAs(username, PASSWORD).title(), "Products");
    }
}
