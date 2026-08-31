package org.example.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Record implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Record.class);
    private Booking booking;
    private static LocalDate bookingDate;

    public Record(){};
    public Record(Booking booking, LocalDate bookingDate) {
        this.booking = booking;
        Record.bookingDate = bookingDate;
        logger.info("Record kreiran za rezervaciju korisnika {}", booking.user.getUsername());
        logger.debug("Detalji recorda -> Datum: {}, Event: {}", booking.eventType);
    }

    public Booking getBookings() {
        return this.booking;
    }

    public String getItemName() {
        return booking.getEventType().toString();
    }

    public BigDecimal getItemPrice() {
        return booking.eventType.getPrice();
    }

    public String getBookingBand(){
        return getBookingBand().toString();
    }

    public static LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public static void setBookingDate(LocalDate bookingDate) {
        Record.bookingDate = bookingDate;
    }
}
