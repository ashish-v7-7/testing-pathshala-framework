package com.pathshala.api.data;

import com.pathshala.api.models.Booking;
import com.pathshala.api.models.BookingDates;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class TestDataFactory {

    private static final List<String> NAMES = List.of("Zara", "Veer", "Meera", "Raju", "Tilli", "Laddu");
    private static final List<String> NEEDS = List.of("Breakfast", "Lunch", "Late checkout");

    private TestDataFactory() {
    }

    public static Booking newBooking() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        LocalDate checkin = LocalDate.now().plusDays(random.nextInt(10, 60));
        return new Booking(
                pick(NAMES) + random.nextInt(1000, 9999),
                "Tester",
                random.nextInt(100, 5000),
                random.nextBoolean(),
                new BookingDates(checkin.toString(), checkin.plusDays(3).toString()),
                pick(NEEDS));
    }

    private static String pick(List<String> values) {
        return values.get(ThreadLocalRandom.current().nextInt(values.size()));
    }
}
