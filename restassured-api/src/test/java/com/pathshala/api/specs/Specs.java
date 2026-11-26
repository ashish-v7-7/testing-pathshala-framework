package com.pathshala.api.specs;

import com.pathshala.api.config.ConfigReader;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public final class Specs {

    private Specs() {
    }

    public static RequestSpecification request() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigReader.get("baseUri"))
                .setContentType(ContentType.JSON)
                // ContentType.JSON would send 4 Accept types; restful-booker answers that with 418
                .setAccept("application/json")
                .setConfig(withTimeouts())
                .addFilter(new AllureRestAssured()
                        .configureHttpExchange(exchange -> exchange.redactCookie("token")))
                .build();
    }

    public static ResponseSpecification okJson() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
    }

    private static RestAssuredConfig withTimeouts() {
        return RestAssuredConfig.config().httpClient(HttpClientConfig.httpClientConfig()
                .setParam("http.connection.timeout", ConfigReader.getInt("connectTimeoutMs"))
                .setParam("http.socket.timeout", ConfigReader.getInt("readTimeoutMs")));
    }
}
