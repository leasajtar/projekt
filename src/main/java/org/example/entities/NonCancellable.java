package org.example.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class NonCancellable extends Booking implements Cash, Serializable {
    boolean payment;

    public NonCancellable(User user, LocalDate date, LocalTime time, Item eventType, Location location, boolean payment, String band) {
        super(user, date, time, eventType, location,  band);
        this.payment = payment;
        logger.info("NonCancellable rezervacija kreirana, plaćeno: {}", payment);
        logger.debug("Detalji -> Event: {}, Datum: {}, Lokacija: {}",
                eventType.getEventType(), date, location);
    }

    @Override
    public void info() {
        String paymentYN = this.payment ? "PAYMENT MADE" : "PAYMENT NOT MADE";

        logger.info("{} - {} - {} - {} - CANNOT CANCEL - {}",
                this.user.getUsername(), this.date, this.time, this.eventType, paymentYN);
    }

    public BigDecimal popust() {
        return this.eventType.getPrice().multiply(new BigDecimal("0.9"));
    }
}
