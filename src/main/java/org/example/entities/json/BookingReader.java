package org.example.entities.json;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import org.example.entities.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookingReader {

    private static final Logger logger = LoggerFactory.getLogger(BookingReader.class);
    private static final Path BOOKING_PATH = Paths.get("data/booking.json");

    public static List<Booking> readBookings() {
        try {
            logger.debug("Attempting to read from: {}", BOOKING_PATH.toAbsolutePath());

            File file = BOOKING_PATH.toFile();
            if (!file.exists()) {
                logger.warn("File does not exist at: {}", BOOKING_PATH.toAbsolutePath());
                return new ArrayList<>();
            }

            String json = Files.readString(BOOKING_PATH);
            logger.debug("File read successfully. Content length: {}", json.length());

            JsonbConfig config = new JsonbConfig().withFormatting(true);
            Jsonb jsonb = JsonbBuilder.create(config);

            Booking[] bookingArray = jsonb.fromJson(json, Booking[].class);

            logger.debug("JSON parsed successfully. Found {} bookings", bookingArray.length);
            for (Booking b : bookingArray) {
                logger.trace("Booking -> user: {}, event: {}, date: {}, time: {}",
                        b.user != null ? b.user.getUsername() : "NULL",
                        b.eventType != null ? b.eventType.getEventType() : "NULL",
                        b.date, b.time);
            }

            return new ArrayList<>(Arrays.asList(bookingArray));

        } catch (Exception e) {
            logger.error("Error reading booking data", e);
            return new ArrayList<>();
        }
    }
}