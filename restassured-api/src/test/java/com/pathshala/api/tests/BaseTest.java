package com.pathshala.api.tests;

import com.pathshala.api.clients.AuthClient;
import com.pathshala.api.clients.BookingClient;
import com.pathshala.api.data.TestDataFactory;
import com.pathshala.api.models.Booking;
import com.pathshala.api.models.BookingResponse;
import com.pathshala.api.specs.Specs;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTest {

    protected final BookingClient bookings = new BookingClient();
    protected final List<Integer> createdIds = new ArrayList<>();
    protected String token;

    @BeforeClass(alwaysRun = true)
    public void getToken() {
        token = AuthClient.token();
    }

    protected BookingResponse createBooking(Booking booking) {
        BookingResponse created = bookings.create(booking)
                .then().spec(Specs.okJson())
                .extract().as(BookingResponse.class);
        createdIds.add(created.bookingid());
        return created;
    }

    protected BookingResponse createBooking() {
        return createBooking(TestDataFactory.newBooking());
    }

    @AfterClass(alwaysRun = true)
    public void cleanUp() {
        createdIds.forEach(id -> bookings.delete(id, token));
    }
}
