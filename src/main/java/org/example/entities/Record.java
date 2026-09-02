package org.example.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Povijesni zapis o jednoj rezervacij, koristi se za prikaz povijesti
 * promjena (npr. u sigurnosnoj kopiji) neovisno o trenutnom stanju rezervacije u bazi.
 */
public class Record implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Record.class);
    private Booking booking;
    private static LocalDate bookingDate;

    /** Prazan konstruktor, potreban za JSON-B/serijalizaciju. */
    public Record(){}
    /**
     * Stvara zapis o danoj rezervaciji i bilježi ga u log.
     *
     * @param booking     rezervacija na koju se zapis odnosi
     * @param bookingDate datum rezervacije
     */
    public Record(Booking booking, LocalDate bookingDate) {
        this.booking = booking;

        logger.info("Record kreiran za rezervaciju korisnika {}", booking.user.getUsername());
        logger.debug("Detalji recorda -> Datum: {}, Event: {}",bookingDate, booking.eventType);
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

    public String getBookingBand(){ return booking.getBand().toString(); }

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
