package org.example.screen;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.entities.Booking;
import org.example.repos.BookingRepos;
import org.example.utility.Session;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PriceController {

    @FXML private Button minBtn;
    @FXML private Button maxBtn;
    @FXML private Label resultLabel;

    private final BookingRepos bookingRepo = new BookingRepos();
    private List<Booking> bookings;

    @FXML
    public void initialize() {
        bookings = Session.isAdmin()
                ? bookingRepo.findAll()
                : bookingRepo.findByUser(Session.getCurrentPerson().getId());
    }

    @FXML
    private void handleMinPrice() {
        List<Booking> soonest = soonestThree();
        BigDecimal min = bookings.stream()
                .map(this::priceOf).filter(p -> p != null)
                .min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        resultLabel.setText("Minimum price: " + min + "\n" + bookingsWithPrice(soonest, min));
    }

    @FXML
    private void handleMaxPrice() {
        List<Booking> soonest = soonestThree();
        BigDecimal max = bookings.stream()
                .map(this::priceOf).filter(p -> p != null)
                .max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        resultLabel.setText("Maximum price: " + max + "\n" + bookingsWithPrice(soonest, max));
    }

    private List<Booking> soonestThree() {
        return bookings.stream()
                .filter(b -> b.getDate() != null && b.getDate().isAfter(LocalDate.now(Clock.systemDefaultZone())))
                .sorted(java.util.Comparator.comparing(Booking::getDate))
                .limit(3)
                .toList();
    }

    private BigDecimal priceOf(Booking b) {
        return (b.getEventType() != null && b.getEventType().getPrice() != null) ? b.getEventType().getPrice() : null;
    }

    private String bookingsWithPrice(List<Booking> pool, BigDecimal price) {
        return pool.stream()
                .filter(b -> { BigDecimal p = priceOf(b); return p != null && p.compareTo(price) == 0; })
                .map(b -> b.getUser().getUsername() + " - " + b.getEventType().getEventType())
                .collect(Collectors.joining("\n"));
    }
}