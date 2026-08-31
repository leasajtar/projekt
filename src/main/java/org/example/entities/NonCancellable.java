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
        String paymentYN;
        if (this.payment) {
            paymentYN = "PAYMENT MADE";
        } else {
            paymentYN = "PAYMENT NOT MADE";
        }

        PrintStream var10000 = System.out;
        String var10001 = this.user.getUsername();
        var10000.println(var10001 + " - " + String.valueOf(this.date) + " - " + String.valueOf(this.time) + " - " +
                String.valueOf(this.eventType) + " - CANNOT CANCEL - " + paymentYN);
    }

    public BigDecimal popust() {
        return this.eventType.getPrice().multiply(new BigDecimal("0.9"));
    }
}
