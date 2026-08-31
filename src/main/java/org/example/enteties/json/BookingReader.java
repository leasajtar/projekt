package org.example.enteties.json;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import org.example.enteties.Booking;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookingReader {

    private static final Path BOOKING_PATH = Paths.get("data/booking.json");

    public static List<Booking> readBookings() {
        try {
            System.out.println("=== DEBUGGING JSON READER ===");
            System.out.println("Attempting to read from: " + BOOKING_PATH.toAbsolutePath());

            // Check if file exists
            File file = BOOKING_PATH.toFile();
            if (!file.exists()) {
                System.err.println("❌ FILE DOES NOT EXIST at: " + BOOKING_PATH.toAbsolutePath());
                System.err.println("Current working directory: " + new File(".").getAbsolutePath());
                return new ArrayList<>();
            }

            System.out.println("✅ File exists!");

            String json = Files.readString(BOOKING_PATH);
            System.out.println("✅ File read successfully. Content length: " + json.length());
            System.out.println("First 200 chars: " + json.substring(0, Math.min(200, json.length())));

            // Configure JSONB - date/time formats are now in @JsonbDateFormat annotations
            JsonbConfig config = new JsonbConfig()
                    .withFormatting(true);

            Jsonb jsonb = JsonbBuilder.create(config);

            // Deserialize array of Booking objects
            Booking[] bookingArray = jsonb.fromJson(json, Booking[].class);

            System.out.println("✅ JSON parsed successfully. Found " + bookingArray.length + " bookings");

            // Debug each booking
            for (int i = 0; i < bookingArray.length; i++) {
                Booking b = bookingArray[i];
                System.out.println("Booking " + i + ":");
                System.out.println("  User: " + (b.user != null ? b.user.getUsername() : "NULL"));
                System.out.println("  Band: " + b.band);
                System.out.println("  Date: " + b.date);
                System.out.println("  Time: " + b.time);
                System.out.println("  Event: " + (b.eventType != null ? b.eventType.getEventType() : "NULL"));
                System.out.println("  Location: " + (b.location != null ? b.location.city() : "NULL"));
            }

            return new ArrayList<>(Arrays.asList(bookingArray));

        } catch (Exception e) {
            System.err.println("❌ Error reading booking data:");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}