package com.pathshala.api.clients;

import com.pathshala.api.config.ConfigReader;
import com.pathshala.api.specs.Specs;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.Map;

import static io.qameta.allure.model.Parameter.Mode.MASKED;
import static io.restassured.RestAssured.given;

public final class AuthClient {

    private static String cachedToken;

    private AuthClient() {
    }

    public static synchronized String token() {
        if (cachedToken == null) {
            cachedToken = login(ConfigReader.get("username"), ConfigReader.get("password"))
                    .then().spec(Specs.okJson())
                    .extract().path("token");
        }
        return cachedToken;
    }

    @Step("Login as {username}")
    public static Response login(String username, @Param(mode = MASKED) String password) {
        return given().spec(Specs.request())
                .body(Map.of("username", username, "password", password))
                .post("/auth");
    }
}
