package org.example.screen;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.app.BookingApp;
import org.example.entities.Booking;
import org.example.entities.Person;
import org.example.repos.BookingRepos;
import org.example.utility.BookingBackupService;
import org.example.utility.BookingEditContext;
import org.example.utility.Session;
import org.example.utility.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingViewController {

    private static final Logger logger = LoggerFactory.getLogger(BookingViewController.class);

    @FXML private ComboBox<String> filterDropdown;
    @FXML private TextField filterInput;
    @FXML private Button filterButton;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;
    @FXML private Button backupBtn;

    @FXML private TableView<Booking> bookingTableView;
    @FXML private TableColumn<Booking, String> userColTab;
    @FXML private TableColumn<Booking, String> eventColTab;
    @FXML private TableColumn<Booking, String> dateColTab;
    @FXML private TableColumn<Booking, String> timeColTab;
    @FXML private TableColumn<Booking, String> addrColTab;

    private ObservableList<Booking> allBookings;
    private ObservableList<Booking> filteredBookings;

    private final BookingRepos bookingRepos = new BookingRepos();

    @FXML
    public void initialize() {
        boolean admin = Session.isAdmin();

        if (filterDropdown != null) {
            filterDropdown.setItems(FXCollections.observableArrayList("Username", "Event", "Date", "City"));
            filterDropdown.setPromptText("Filter Type");
        }
        if (filterInput != null) {
            filterInput.setPromptText("Enter value");
        }
        if (deleteBtn != null) {
            deleteBtn.setVisible(admin);
            deleteBtn.setManaged(admin);
        }
        if (backupBtn != null) {
            backupBtn.setVisible(admin);
            backupBtn.setManaged(admin);
        }

        setupTableColumns();
        loadBookings();
    }

    private void setupTableColumns() {
        userColTab.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getUser() != null && c.getValue().getUser().getUsername() != null
                        ? c.getValue().getUser().getUsername() : "N/A"));

        eventColTab.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getEventType() != null && c.getValue().getEventType().getEventType() != null
                        ? c.getValue().getEventType().getEventType() : "N/A"));

        dateColTab.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDate() != null
                        ? c.getValue().getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) : "N/A"));

        timeColTab.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getTime() != null
                        ? c.getValue().getTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "N/A"));

        addrColTab.setCellValueFactory(c -> {
            if (c.getValue().getLocation() == null) return new SimpleStringProperty("N/A");
            String address = c.getValue().getLocation().adress() != null ? c.getValue().getLocation().adress() : "";
            String city = c.getValue().getLocation().city() != null ? c.getValue().getLocation().city() : "";
            String combined = (address + (address.isEmpty() || city.isEmpty() ? "" : ", ") + city).trim();
            return new SimpleStringProperty(combined.isEmpty() ? "N/A" : combined);
        });
    }

    private void loadBookings() {
        try {
            Person current = Session.getCurrentPerson();
            List<Booking> bookings = Session.isAdmin()
                    ? bookingRepos.findAll()
                    : bookingRepos.findByUser(current.getId());

            allBookings = FXCollections.observableArrayList(bookings);
            bookingTableView.setItems(allBookings);
        } catch (Exception e) {
            logger.error("Failed to load bookings from database", e);
            allBookings = FXCollections.observableArrayList();
            bookingTableView.setItems(allBookings);
            new Alert(Alert.AlertType.ERROR, "Failed to load bookings from database:\n" + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void handleFilter() {
        if (filterDropdown == null || filterInput == null) return;

        String filter = filterDropdown.getValue();
        String value = filterInput.getText();

        if (filter == null || value == null || value.isBlank()) {
            new Alert(Alert.AlertType.WARNING, "Please choose a filter type and enter a value.").showAndWait();
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

    @FXML
    private void handleEdit() {
        Booking selected = bookingTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select a booking to edit first.").showAndWait();
            return;
        }
        BookingEditContext.edit(selected);
        BookingApp.showMainApp("BookingAdd.fxml");
    }

    @FXML
    private void handleDelete() {
        Booking selected = bookingTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select a booking to delete first.").showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete this booking for " + selected.getUser().getUsername() + "?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(choice -> {
            if (choice == ButtonType.YES) {
                bookingRepos.delete(selected.getId());
                loadBookings();
            }
        });
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