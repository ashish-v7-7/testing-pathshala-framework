package com.pathshala.api.tests;

import com.pathshala.api.data.TestDataFactory;
import com.pathshala.api.models.Booking;
import com.pathshala.api.models.BookingResponse;
import com.pathshala.api.specs.Specs;
import io.qameta.allure.Description;
import org.testng.annotations.Test;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;

public class BookingCrudTest extends BaseTest {

    @Test(description = "Create a booking")
    @Description("POST /booking returns 200 OK (not 201) with the new id and the same booking")
    public void createBooking_returnsSameBooking() {
        Booking booking = TestDataFactory.newBooking();
        BookingResponse created = createBooking(booking);
        assertEquals(created.booking(), booking);
    }

    @Test(description = "Get a booking by id")
    public void getBooking_returnsCreatedBooking() {
        BookingResponse created = createBooking();
        Booking fetched = bookings.get(created.bookingid())
                .then().spec(Specs.okJson())
                .extract().as(Booking.class);
        assertEquals(fetched, created.booking());
    }

    @Test(description = "Full update with token")
    public void updateBooking_replacesAllFields() {
        int id = createBooking().bookingid();
        Booking changed = TestDataFactory.newBooking();
        Booking updated = bookings.update(id, changed, token)
                .then().spec(Specs.okJson())
                .extract().as(Booking.class);
        assertEquals(updated, changed);
    }

    @Test(description = "Partial update with token")
    public void partialUpdate_changesOnlyGivenFields() {
        BookingResponse created = createBooking();
        bookings.partialUpdate(created.bookingid(), Map.of("firstname", "Patched"), token)
                .then().spec(Specs.okJson())
                .body("firstname", equalTo("Patched"))
                .body("lastname", equalTo(created.booking().lastname()));
    }

    @Test(description = "Delete then GET returns 404")
    @Description("DELETE /booking/{id} really returns 201 Created; a later GET returns 404")
    public void deleteBooking_thenGetReturns404() {
        int id = createBooking().bookingid();
        bookings.delete(id, token).then().statusCode(201);
        createdIds.remove(Integer.valueOf(id));
        bookings.get(id).then().statusCode(404);
    }
}
