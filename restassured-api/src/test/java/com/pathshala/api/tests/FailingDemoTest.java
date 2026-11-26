package com.pathshala.api.tests;

import com.pathshala.api.specs.Specs;
import io.qameta.allure.Description;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class FailingDemoTest {

    @Test(groups = "failing-demo", description = "Intentional failure: wrong expected status")
    @Description("Expects 200 from /ping, but restful-booker really returns 201, so this fails on purpose")
    public void pingReturns200_failsOnPurpose() {
        given().spec(Specs.request())
                .get("/ping")
                .then().statusCode(200);
    }
}
