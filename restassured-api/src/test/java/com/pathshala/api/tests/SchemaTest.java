package com.pathshala.api.tests;

import org.testng.annotations.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class SchemaTest extends BaseTest {

    @Test(description = "Booking response matches JSON schema")
    public void getBooking_matchesSchema() {
        int id = createBooking().bookingid();
        bookings.get(id)
                .then().statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/booking-schema.json"));
    }
}
