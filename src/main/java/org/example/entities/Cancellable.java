package org.example.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Rezervacija plaćena karticom koja se može otkazati ({@link Card}), uz povrat
 * od 50% cijene događaja.
 */
public final class Cancellable extends Booking implements Card, Serializable {
    boolean paymentMade;
    private BigDecimal refund;

    /**
     * Stvaranje nove rezervacije.
     *
     * @param user      korisnik koji je izvršio rezervaciju
     * @param date      datum
     * @param time      vrijeme
     * @param eventType vrsta rezerviranog
     * @param location  lokacija
     * @param paymentMade je li placanje izvrseno
     * @param band      naziv benda/izvođača (može biti null)
     */

    public Cancellable(User user, LocalDate date, LocalTime time, Item eventType, Location location, boolean paymentMade, String  band) {
        super(user, date, time, eventType, location, band);
        this.refund = this.eventType.getPrice().multiply(new BigDecimal("0.5"));
        this.paymentMade = paymentMade;
        logger.info("Cancellable rezervacija kreirana, plaćeno: {}", paymentMade);
        logger.debug("Detalji -> Event: {}, Datum: {}, Lokacija: {}", eventType.getEventType(), date, location);
    }

    /**Nadjacana metoda iz {@link Booking} koja vraca informacije o rezervaciji.*/
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