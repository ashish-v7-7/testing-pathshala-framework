package com.pathshala.api.clients;

import com.pathshala.api.specs.Specs;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.qameta.allure.model.Parameter.Mode.MASKED;
import static io.restassured.RestAssured.given;

public class BookingClient {

    @Step("Create booking")
    public Response create(Object booking) {
        return given().spec(Specs.request()).body(booking).post("/booking");
    }

    @Step("Get booking {id}")
    public Response get(int id) {
        return given().spec(Specs.request()).get("/booking/{id}", id);
    }

    @Step("Update booking {id}")
    public Response update(int id, Object booking, @Param(mode = MASKED) String token) {
        return authorized(token).body(booking).put("/booking/{id}", id);
    }

    @Step("Partially update booking {id}")
    public Response partialUpdate(int id, Map<String, Object> fields, @Param(mode = MASKED) String token) {
        return authorized(token).body(fields).patch("/booking/{id}", id);
    }

    @Step("Delete booking {id}")
    public Response delete(int id, @Param(mode = MASKED) String token) {
        return authorized(token).delete("/booking/{id}", id);
    }

    private RequestSpecification authorized(String token) {
        RequestSpecification spec = given().spec(Specs.request());
        return token == null ? spec : spec.cookie("token", token);
    }
}
