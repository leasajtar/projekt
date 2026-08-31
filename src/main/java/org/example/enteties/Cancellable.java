package org.example.enteties;

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
        logger.debug("Detalji -> Event: {}, Datum: {}, Lokacija: {}", eventType.getEventType(), date);
    }

    @Override
    public void info() {
        logger.trace("Pozvana metoda info() u klasi Booking");
        logger.info("Rezervacija za korisnika: {} - događaj: {}",
                user.getUsername(), eventType.getEventType());
        String paymentYN;
        if (this.paymentMade) {
            paymentYN = "PAYMENT MADE";
        } else {
            paymentYN = "PAYMENT NOT MADE";
        }

        String var10001 = this.user.getUsername();
        System.out.println(var10001 + " - " + String.valueOf(this.date) + " - " + String.valueOf(this.time) + " - " +
                String.valueOf(this.eventType) + " - CAN CANCEL - " + paymentYN + " available refund: " + String.valueOf(this.refund));
    }

    public void cancel() {
        System.out.println("Cancelling " + this.eventType.getEventType());
    }
}
