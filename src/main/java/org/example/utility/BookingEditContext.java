package org.example.utility;

import org.example.entities.Booking;

/**
 * Kratkotrajni "prijenosnik" koji nosi rezervaciju koja se trenutno uređuje
 * s ekrana za pregled (Booking.fxml) na ekran za dodavanje/uređivanje (BookingAdd.fxml).
 */
public final class BookingEditContext {

    private static Booking bookingToEdit;

    private BookingEditContext() {}

    public static void edit(Booking booking) {
        bookingToEdit = booking;
    }

    /** Vraća rezervaciju koja se uređuje i odmah čisti stanje (jednokratna upotreba). */
    public static Booking consume() {
        Booking b = bookingToEdit;
        bookingToEdit = null;
        return b;
    }
}