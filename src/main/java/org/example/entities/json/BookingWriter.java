package org.example.entities.json;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.example.entities.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/** Zapisuje rezervacije u {@code data/booking.json} (JSON par uz {@link BookingReader}). */
public class BookingWriter {

    private BookingWriter() {}

    private static final Logger logger = LoggerFactory.getLogger(BookingWriter.class);

    private static final Path BOOKING_PATH = Paths.get("data/booking.json");

    /**
     * Sprema listu rezervacija u {@code data/booking.json}, prepisujući postojeći sadržaj.
     * @param bookings rezervacije koje treba zapisati
     */
    public static void writeBookings(List<Booking> bookings) {

        try (Jsonb jsonb = JsonbBuilder.create(); BufferedWriter writer = Files.newBufferedWriter(BOOKING_PATH)) {
            jsonb.toJson(bookings, writer);
            logger.info("Booking data successfully saved to booking.json");

        } catch (Exception e) {
            logger.error("Error writing booking data", e);
        }
    }
}