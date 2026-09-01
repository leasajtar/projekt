package org.example.screen;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.example.app.BookingApp;
import org.example.entities.*;
import org.example.exceptions.InvalidBookingException;
import org.example.repos.BookingRepos;
import org.example.repos.ItemRepos;
import org.example.repos.UserRepos;
import org.example.utility.BookingEditContext;
import org.example.utility.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AddBookingController {

    private static final Logger logger = LoggerFactory.getLogger(AddBookingController.class);

    @FXML private ComboBox<User> userDropdown;
    @FXML private ComboBox<Item> eventDropdown;
    @FXML private ComboBox<String> cityDropdown;
    @FXML private TextField addressInput;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<Integer> hourDropdown;
    @FXML private ComboBox<Integer> minuteDropdown;
    @FXML private Button addBookingBtn;

    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, String> userCol;
    @FXML private TableColumn<Booking, String> eventCol;
    @FXML private TableColumn<Booking, String> priceCol;
    @FXML private TableColumn<Booking, String> dateCol;
    @FXML private TableColumn<Booking, String> timeCol;
    @FXML private TableColumn<Booking, String> addressCol;

    private final UserRepos userRepo = new UserRepos();
    private final ItemRepos itemRepo = new ItemRepos();
    private final BookingRepos bookingRepo = new BookingRepos();

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    private Booking editingBooking;
    private boolean admin;

    @FXML
    public void initialize() {
        logger.info("Initializing AddBookingController");
        admin = Session.isAdmin();

        try {
            if (admin) {
                logger.info("Admin logged in");
                userDropdown.setItems(FXCollections.observableArrayList(userRepo.findAll()));
            } else if (Session.getCurrentPerson() instanceof User self) {
                logger.info("User logged in");
                userDropdown.setItems(FXCollections.observableArrayList(self));
                userDropdown.setValue(self);
                userDropdown.setDisable(true);
            }
            userDropdown.setConverter(new StringConverter<>() {
                @Override public String toString(User user) { return user != null ? user.getUsername() : ""; }
                @Override public User fromString(String s) { return null; }
            });

            eventDropdown.setItems(FXCollections.observableArrayList(itemRepo.findAll()));
            eventDropdown.setConverter(new StringConverter<>() {
                @Override public String toString(Item item) { return item != null ? item.getEventType() : ""; }
                @Override public Item fromString(String s) { return null; }
            });
        } catch (Exception e) {
            logger.error("Failed during controller initialize()", e);
            new Alert(Alert.AlertType.ERROR, "Failed during controller initialize():\n" + e.getMessage()).showAndWait();
        }

        cityDropdown.setItems(FXCollections.observableArrayList("Zagreb", "Split", "Osijek"));

        Clock clock = Clock.systemDefaultZone();
        datePicker.setValue(LocalDate.now(clock));
        hourDropdown.setItems(FXCollections.observableArrayList());
        minuteDropdown.setItems(FXCollections.observableArrayList());
        for (int i = 0; i < 24; i++) hourDropdown.getItems().add(i);
        for (int i = 0; i < 60; i++) minuteDropdown.getItems().add(i);

        userCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getUser().getUsername()));
        eventCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEventType().getEventType()));
        priceCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEventType().getPrice().toString()));
        dateCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDate().format(dateFormatter)));
        timeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTime().format(timeFormatter)));
        addressCol.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getLocation().adress() + ", " + d.getValue().getLocation().city()));

        editingBooking = BookingEditContext.consume();
        if (editingBooking != null) {
            prefillForEdit(editingBooking);
        }

        reloadBookingsTable();
    }

    private void prefillForEdit(Booking b) {
        if (admin && b.getUser() != null) {
            userDropdown.setValue(b.getUser());
        }
        eventDropdown.setValue(b.getEventType());
        if (b.getLocation() != null) {
            cityDropdown.setValue(b.getLocation().city());
            addressInput.setText(b.getLocation().adress());
        }
        datePicker.setValue(b.getDate());
        if (b.getTime() != null) {
            hourDropdown.setValue(b.getTime().getHour());
            minuteDropdown.setValue(b.getTime().getMinute());
        }
        if (addBookingBtn != null) {
            addBookingBtn.setText("Update Booking");
        }
    }

    private void reloadBookingsTable() {
        List<Booking> bookings = Session.isAdmin()
                ? bookingRepo.findAll()
                : bookingRepo.findByUser(Session.getCurrentPerson().getId());
        bookingTable.setItems(FXCollections.observableArrayList(bookings));
        logger.info("Booking table updated");
    }

    private void validateBooking(LocalDate date) throws InvalidBookingException {
        Clock clock = Clock.systemDefaultZone();
        if (date.isBefore(LocalDate.now(clock))) {
            throw new InvalidBookingException("Booking date cannot be in the past.");
        }
        logger.info("Booking validated");
    }

    @FXML
    private void handleAddBooking() {
        if (userDropdown.getValue() == null || eventDropdown.getValue() == null || cityDropdown.getValue() == null ||
                addressInput.getText().isBlank() ||
                datePicker.getValue() == null || hourDropdown.getValue() == null || minuteDropdown.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Please fill in all fields! ").showAndWait();
            return;
        }

        LocalDate date = datePicker.getValue();

        try {
            validateBooking(date);
        } catch (InvalidBookingException e) {
            new Alert(Alert.AlertType.WARNING, e.getMessage()).showAndWait();
            return;
        }

        LocalTime time = LocalTime.of(hourDropdown.getValue(), minuteDropdown.getValue());
        Location location = new Location(addressInput.getText(), cityDropdown.getValue());

        try {
            if (editingBooking != null) {
                editingBooking.setUser(userDropdown.getValue());
                editingBooking.setEventType(eventDropdown.getValue());
                editingBooking.setDate(date);
                editingBooking.setTime(time);
                editingBooking.setLocation(location);
                bookingRepo.update(editingBooking);
            } else {
                Booking booking = new Booking(userDropdown.getValue(), date, time, eventDropdown.getValue(), location, null);
                bookingRepo.insert(booking);
            }
        } catch (Exception e) {
            logger.error("Failed to save booking", e);
            new Alert(Alert.AlertType.ERROR, "Failed to save booking: " + e.getMessage()).showAndWait();
            return;
        }

        if (editingBooking != null) {
            logger.info("Booking updated successfully");
            new Alert(Alert.AlertType.INFORMATION, "Booking updated!").showAndWait();
            editingBooking = null;
            BookingApp.showMainApp("Booking.fxml");
            return;
        }

        new Alert(Alert.AlertType.INFORMATION, "Booking saved!").showAndWait();
        try {
            reloadBookingsTable();
        } catch (Exception e) {
            logger.error("Booking was saved, but the table could not be refreshed", e);
            new Alert(Alert.AlertType.WARNING,
                    "Booking was saved, but the table couldn't be refreshed automatically. " +
                            "Reopen this screen to see it.").showAndWait();
        }
    }
}