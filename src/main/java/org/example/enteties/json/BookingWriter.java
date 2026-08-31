package org.example.enteties.json;

import jakarta.json.bind.*;
import org.example.enteties.Booking;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class BookingWriter {
    public static void writeBookings(List<Booking> bookings) {
        try {
            Jsonb jsonb = JsonbBuilder.create();

            String json = jsonb.toJson(bookings);
            Files.writeString(Paths.get("data/booking.json"), json);

            System.out.println("✅ Booking data successfully saved to booking.json!");

        } catch (Exception e) {
            System.err.println("Error writing booking data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
