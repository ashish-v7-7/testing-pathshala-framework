package com.pathshala.api.tests;

import com.pathshala.api.specs.Specs;
import io.qameta.allure.Description;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class HealthCheckTest {

    @Test(description = "API is up")
    @Description("restful-booker answers GET /ping with 201 Created (its real, slightly odd, health status)")
    public void pingReturns201() {
        given().spec(Specs.request())
                .get("/ping")
                .then().statusCode(201);
    }
}
