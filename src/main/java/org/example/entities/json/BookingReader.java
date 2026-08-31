package org.example.entities.json;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import org.example.entities.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookingReader{

    private BookingReader() {}

    private static final Logger LOGGER =
            LoggerFactory.getLogger(BookingReader.class);


    private static final Path BOOKING_PATH =
            Paths.get("data/booking.json");

    public static List<Booking> readBookings() {

        LOGGER.debug(
                "Attempting to read from: {}",
                BOOKING_PATH.toAbsolutePath()
        );

        if (!Files.exists(BOOKING_PATH)) {
            LOGGER.warn(
                    "File does not exist at: {}",
                    BOOKING_PATH.toAbsolutePath()
            );

            return new ArrayList<>();
        }

        JsonbConfig config = new JsonbConfig()
                .withFormatting(true);

        try (
                BufferedReader reader =
                        Files.newBufferedReader(BOOKING_PATH);

                Jsonb jsonb =
                        JsonbBuilder.create(config)
        ) {

            Booking[] bookingArray =
                    jsonb.fromJson(reader, Booking[].class);

            LOGGER.debug(
                    "JSON parsed successfully. Found {} bookings",
                    bookingArray.length
            );

            for (Booking booking : bookingArray) {
                LOGGER.trace(
                        "Booking -> user: {}, event: {}, date: {}, time: {}",
                        booking.getUser() != null
                                ? booking.getUser().getUsername()
                                : "NULL",

                        booking.getEventType() != null
                                ? booking.getEventType().getEventType()
                                : "NULL",

                        booking.date,
                        booking.time
                );
            }

            return new ArrayList<>(
                    Arrays.asList(bookingArray)
            );

        } catch (Exception e) {
            LOGGER.error(
                    "Error reading booking data",
                    e
            );

            return new ArrayList<>();
        }
    }
}