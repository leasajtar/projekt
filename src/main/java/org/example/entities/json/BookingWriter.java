package org.example.entities.json;

import jakarta.json.bind.*;
import org.example.entities.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class BookingWriter {
    private static final Logger logger = LoggerFactory.getLogger(BookingWriter.class);

    public static void writeBookings(List<Booking> bookings) {
        try {
            Jsonb jsonb = JsonbBuilder.create();
            String json = jsonb.toJson(bookings);
            Files.writeString(Paths.get("data/booking.json"), json);

            logger.info("Booking data successfully saved to booking.json");
        } catch (Exception e) {
            logger.error("Error writing booking data", e);
        }
    }
}