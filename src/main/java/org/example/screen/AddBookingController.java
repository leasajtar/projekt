package org.example.screen;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.example.repos.BookingRepos;
import org.example.repos.ItemRepos;
import org.example.repos.UserRepos;
import org.example.enteties.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AddBookingController {

    @FXML private ComboBox<User> userDropdown;
    @FXML private ComboBox<Item> eventDropdown;

    @FXML private ComboBox<String> cityDropdown;
    @FXML private TextField streetInput;
    @FXML private TextField houseInput;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<Integer> hourDropdown;
    @FXML private ComboBox<Integer> minuteDropdown;

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

    @FXML
    public void initialize() {

        try {
            userDropdown.setItems(FXCollections.observableArrayList(userRepo.findAll()));
            userDropdown.setConverter(new StringConverter<>() {
                @Override
                public String toString(User user) {
                    return user != null ? user.getUsername() : "";
                }

                @Override
                public User fromString(String s) {
                    return null;
                }
            });

            eventDropdown.setItems(FXCollections.observableArrayList(itemRepo.findAll()));
            eventDropdown.setConverter(new StringConverter<>() {
                @Override
                public String toString(Item item) {
                    return item != null ? item.getEventType() : "";
                }

                @Override
                public Item fromString(String s) {
                    return null;
                }
            });
        }catch(Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Failed during controller initialize():\n" + e.getMessage()
            ).showAndWait();
        }

        cityDropdown.setItems(FXCollections.observableArrayList("Zagreb", "Split", "Osijek"));

        datePicker.setValue(LocalDate.now());
        hourDropdown.setItems(FXCollections.observableArrayList());
        minuteDropdown.setItems(FXCollections.observableArrayList());
        for (int i = 0; i < 24; i++) hourDropdown.getItems().add(i);
        for (int i = 0; i < 60; i++) minuteDropdown.getItems().add(i);

        userCol.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getUser().getUsername())
        );
        eventCol.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getEventType().getEventType())
        );
        priceCol.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getEventType().getPrice().toString())
        );
        dateCol.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getDate().format(dateFormatter))
        );
        timeCol.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getTime().format(timeFormatter))
        );
        addressCol.setCellValueFactory(d ->
                new SimpleStringProperty(
                        d.getValue().getLocation().adress() + ", " + d.getValue().getLocation().city()
                )
        );

        reloadBookingsTable();
    }

    private void reloadBookingsTable() {
        bookingTable.setItems(FXCollections.observableArrayList(
                bookingRepo.findAll(userRepo, itemRepo)
        ));
    }

    @FXML
    private void handleAddBooking() {

        if (userDropdown.getValue() == null ||
                eventDropdown.getValue() == null ||
                cityDropdown.getValue() == null ||
                streetInput.getText().isBlank() ||
                houseInput.getText().isBlank() ||
                datePicker.getValue() == null ||
                hourDropdown.getValue() == null ||
                minuteDropdown.getValue() == null) {

            new Alert(Alert.AlertType.WARNING, "Please fill in all fields! ").showAndWait();
            return;
        }


        LocalDate date = datePicker.getValue();
        if (date.isBefore(LocalDate.now())) {
            new Alert(Alert.AlertType.WARNING, "Please select a valid date! ").showAndWait();
            return;
        }

        LocalTime time = LocalTime.of(hourDropdown.getValue(), minuteDropdown.getValue());

        Location location = new Location(
                streetInput.getText() + " " + houseInput.getText(),
                cityDropdown.getValue()
        );

        Booking booking = new Booking(
                userDropdown.getValue(),
                date,
                time,
                eventDropdown.getValue(),
                location,
                null
        );

        try {
            bookingRepo.insert(booking);

            new Alert(Alert.AlertType.INFORMATION, "Booking saved!").showAndWait();
            reloadBookingsTable();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to save booking: " + e.getMessage()).showAndWait();
        }
    }
}
