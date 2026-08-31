package org.example.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public final class Cancellable extends Booking implements Card, Serializable {
    boolean paymentMade;
    private BigDecimal refund;

    public Cancellable(User user, LocalDate date, LocalTime time, Item eventType, Location location, boolean paymentMade, String  band) {
        super(user, date, time, eventType, location, band);
        this.refund = this.eventType.getPrice().multiply(new BigDecimal("0.5"));
        this.paymentMade = paymentMade;
        logger.info("Cancellable rezervacija kreirana, plaćeno: {}", paymentMade);
        logger.debug("Detalji -> Event: {}, Datum: {}, Lokacija: {}", eventType.getEventType(), date, location);
    }

    @Override
    public void info() {
        logger.trace("Pozvana metoda info() u klasi Booking");
        logger.info("Rezervacija za korisnika: {} - događaj: {}",
                user.getUsername(), eventType.getEventType());

        String paymentYN = this.paymentMade ? "PAYMENT MADE" : "PAYMENT NOT MADE";

        logger.info("{} - {} - {} - {} - CAN CANCEL - {} available refund: {}",
                this.user.getUsername(), this.date, this.time, this.eventType, paymentYN, this.refund);
    }

    public void cancel() {
        logger.info("Cancelling {}", this.eventType.getEventType());
    }
}