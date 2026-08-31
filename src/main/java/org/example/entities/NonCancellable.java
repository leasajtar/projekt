package org.example.entities;


import java.io.PrintStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class NonCancellable extends Booking implements Cash, Serializable {
    private Integer nubOfDecorations;
    boolean payment;

    public NonCancellable(User user, LocalDate date, LocalTime time, Item eventType, Location location, boolean payment, Integer nubOfDecorations, String band) {
        super(user, date, time, eventType, location,  band);
        this.payment = payment;
        this.nubOfDecorations = nubOfDecorations;
        logger.info("NonCancellable rezervacija kreirana, plaćeno: {}, dekoracije: {}", payment, nubOfDecorations);
        logger.debug("Detalji -> Event: {}, Datum: {}, Lokacija: {}, Dekoracije: {}",
                eventType.getEventType(), date, nubOfDecorations);
    }

    public void info() {
        String paymentYN = this.payment ? "PAYMENT MADE" : "PAYMENT NOT MADE";

        logger.info("{} - {} - {} - {} - CANNOT CANCEL - {}",
                this.user.getUsername(), this.date, this.time, this.eventType, paymentYN);
    }

    public BigDecimal popust() {
        return this.eventType.getPrice().multiply(new BigDecimal("0.9"));
    }
}
