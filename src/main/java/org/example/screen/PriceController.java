package org.example.screen;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.collections.Relation;
import org.example.entities.Booking;
import org.example.entities.Item;
import org.example.entities.User;
import org.example.repos.BookingRepos;
import org.example.utility.Session;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Kontroler ekrana za prikaz minimalne/maksimalne cijene, nad svim
 * rezervacijama za {@link org.example.entities.Admin}, ili samo vlastitim za {@link User}.
 */
public class PriceController {

    @FXML private Button minBtn;
    @FXML private Button maxBtn;
    @FXML private Label resultLabel;

    private final BookingRepos bookingRepo = new BookingRepos();
    private List<Booking> bookings;

    /** Učitava rezervacije relevantne za trenutno prijavljenu osobu. */
    @FXML
    public void initialize() {
        bookings = Session.isAdmin()
                ? bookingRepo.findAll()
                : bookingRepo.findByUser(Session.getCurrentPerson().getId());
    }

    /** Pronalazi najnižu cijenu među rezervacijama i prikazuje pripadajuće nadolazeće rezervacije. */
    @FXML
    private void handleMinPrice() {
        List<Booking> soonest = soonestThree();
        BigDecimal min = bookings.stream()
                .map(this::priceOf).filter(p -> p != null)
                .min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        resultLabel.setText("Minimum price: " + min + "\n" + bookingsWithPrice(soonest, min));
    }

    /** Pronalazi najvišu cijenu među rezervacijama i prikazuje pripadajuće nadolazeće rezervacije. */
    @FXML
    private void handleMaxPrice() {
        List<Booking> soonest = soonestThree();
        BigDecimal max = bookings.stream()
                .map(this::priceOf).filter(p -> p != null)
                .max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        resultLabel.setText("Maximum price: " + max + "\n" + bookingsWithPrice(soonest, max));
    }

    /** @return do tri nadolazeće (buduće) rezervacije, sortirane po najbližem datumu */
    private List<Booking> soonestThree() {
        return bookings.stream()
                .filter(b -> b.getDate() != null && b.getDate().isAfter(LocalDate.now(Clock.systemDefaultZone())))
                .sorted(java.util.Comparator.comparing(Booking::getDate))
                .limit(3)
                .toList();
    }

    /**
     * @param b rezervacija čiju cijenu treba dohvatiti
     * @return cijenu događaja vezanog uz rezervaciju, ili {@code null} ako nije dostupna
     */
    private BigDecimal priceOf(Booking b) {
        return (b.getEventType() != null && b.getEventType().getPrice() != null) ? b.getEventType().getPrice() : null;
    }

    /** Pretvara podudarajuće rezervacije u parove (korisnik, vrsta događaja) pomoću generičke klase {@link Relation}. */
    private List<Relation<User, Item>> matchingRelations(List<Booking> pool, BigDecimal price) {
        return pool.stream()
                .filter(b -> { BigDecimal p = priceOf(b); return p != null && p.compareTo(price) == 0; })
                .map(b -> new Relation<>(b.getUser(), b.getEventType()))
                .toList();
    }

    /**
     * @param pool  rezervacije unutar kojih se traži podudaranje
     * @param price cijena po kojoj se filtrira
     * @return čitljiv tekst s korisnicima i događajima koji odgovaraju danoj cijeni, jedan po retku
     */
    private String bookingsWithPrice(List<Booking> pool, BigDecimal price) {
        return matchingRelations(pool, price).stream()
                .map(r -> r.getFirst().getUsername() + " - " + r.getSecond().getEventType())
                .collect(Collectors.joining("\n"));
    }
}