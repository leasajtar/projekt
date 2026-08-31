package org.example.screen;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.utility.BookingBackupService;
import org.example.utility.Util;
import org.example.enteties.Booking;
import org.example.repos.BookingRepos;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingViewController {

    @FXML
    private ComboBox<String> filterDropdown;
    @FXML
    private TextField filterInput;
    @FXML
    private Button filterButton;

    @FXML
    private TableView<Booking> bookingTableView;
    @FXML
    private TableColumn<Booking, String> userColTab;
    @FXML
    private TableColumn<Booking, String> eventColTab;
    @FXML
    private TableColumn<Booking, String> dateColTab;
    @FXML
    private TableColumn<Booking, String> timeColTab;
    @FXML
    private TableColumn<Booking, String> addrColTab;

    private ObservableList<Booking> allBookings;
    private ObservableList<Booking> filteredBookings;

    private final BookingRepos bookingRepos = new BookingRepos();

    @FXML
    public void initialize() {
        if (filterDropdown != null) {
            filterDropdown.setItems(FXCollections.observableArrayList(
                    "Username", "Event", "Date", "City"
            ));
            filterDropdown.setPromptText("Filter Type");
        }

        if (filterInput != null) {
            filterInput.setPromptText("Enter value");
        }

        setupTableColumns();
        loadBookings();
    }

    private void setupTableColumns() {
        userColTab.setCellValueFactory(c -> {
            if (c.getValue().getUser() != null && c.getValue().getUser().getUsername() != null) {
                return new SimpleStringProperty(c.getValue().getUser().getUsername());
            }
            return new SimpleStringProperty("N/A");
        });

        eventColTab.setCellValueFactory(c -> {
            if (c.getValue().getEventType() != null && c.getValue().getEventType().getEventType() != null) {
                return new SimpleStringProperty(c.getValue().getEventType().getEventType());
            }
            return new SimpleStringProperty("N/A");
        });

        dateColTab.setCellValueFactory(c -> {
            if (c.getValue().getDate() != null) {
                return new SimpleStringProperty(
                        c.getValue().getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                );
            }
            return new SimpleStringProperty("N/A");
        });

        timeColTab.setCellValueFactory(c -> {
            if (c.getValue().getTime() != null) {
                return new SimpleStringProperty(
                        c.getValue().getTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                );
            }
            return new SimpleStringProperty("N/A");
        });

        addrColTab.setCellValueFactory(c -> {
            if (c.getValue().getLocation() != null) {
                String address = c.getValue().getLocation().adress() != null
                        ? c.getValue().getLocation().adress()
                        : "";
                String city = c.getValue().getLocation().city() != null
                        ? c.getValue().getLocation().city()
                        : "";

                if (!address.isEmpty() && !city.isEmpty()) {
                    return new SimpleStringProperty(address + ", " + city);
                } else if (!address.isEmpty()) {
                    return new SimpleStringProperty(address);
                } else if (!city.isEmpty()) {
                    return new SimpleStringProperty(city);
                }
            }
            return new SimpleStringProperty("N/A");
        });
    }

    private void loadBookings() {
        try {
            List<Booking> bookings = bookingRepos.findAll();
            allBookings = FXCollections.observableArrayList(bookings);
            bookingTableView.setItems(allBookings);
        } catch (Exception e) {
            e.printStackTrace();
            allBookings = FXCollections.observableArrayList();
            bookingTableView.setItems(allBookings);

            new Alert(Alert.AlertType.ERROR,
                    "Failed to load bookings from database:\n" + e.getMessage()
            ).showAndWait();
        }
    }

    @FXML
    private void handleFilter() {
        if (filterDropdown == null || filterInput == null) return;

        String filter = filterDropdown.getValue();
        String value = filterInput.getText();

        if (filter == null || value == null || value.isBlank()) {
            bookingTableView.setItems(allBookings);
            return;
        }

        filteredBookings = allBookings.filtered(booking -> Util.filterSwitch(filter, value, booking));

        bookingTableView.setItems(filteredBookings);
    }

    @FXML
    private void handleClearFilter() {
        if (filterDropdown != null) filterDropdown.setValue(null);
        if (filterInput != null) filterInput.clear();
        bookingTableView.setItems(allBookings);
    }

    public void refresh() {
        loadBookings();
    }
    @FXML
    private void backupBookings() {
        BookingBackupService.backupNowAsync();
        new Alert(Alert.AlertType.INFORMATION, "Backup started ✅").showAndWait();
    }


}