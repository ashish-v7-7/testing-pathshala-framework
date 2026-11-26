package com.pathshala.api.tests;

import com.pathshala.api.clients.AuthClient;
import com.pathshala.api.data.TestDataFactory;
import com.pathshala.api.specs.Specs;
import io.qameta.allure.Description;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.hasKey;

public class AuthTest extends BaseTest {

    @Test(description = "Update without token is forbidden")
    public void updateWithoutToken_returns403() {
        int id = createBooking().bookingid();
        bookings.update(id, TestDataFactory.newBooking(), null)
                .then().statusCode(403);
    }

    @DataProvider(name = "badCredentials")
    public Object[][] badCredentials() {
        return new Object[][] {
                {"admin", "wrong-password"},
                {"not-admin", "password123"},
                {"", ""}
        };
    }

    @Test(dataProvider = "badCredentials", description = "Login with bad credentials")
    @Description("restful-booker answers bad logins with 200 OK and a 'reason', never a token")
    public void login_withBadCredentials_givesNoToken(String username, String password) {
        AuthClient.login(username, password)
                .then().spec(Specs.okJson())
                .body("reason", equalTo("Bad credentials"))
                .body("$", not(hasKey("token")));
    }
}
