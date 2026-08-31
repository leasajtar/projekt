package org.example.screen;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.app.BookingApp;
import org.example.entities.Booking;
import org.example.entities.Item;
import org.example.repos.BookingRepos;
import org.example.repos.ItemRepos;

import java.time.format.DateTimeFormatter;
import java.util.List;


public class OpeningScreenController {

    @FXML private TableView<Item> eventDetailsTbl;
    @FXML private TableColumn<Item, String> eventNameTblCol;
    @FXML private TableColumn<Item, String> eventPriceTblCol;
    @FXML private Label latestBookingLbl;

    private final ItemRepos itemRepos = new ItemRepos();

    private final BookingRepos bookingRepos = new BookingRepos();

    private final DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    public void initialize() {
        loadLatestBookingVirtualThread();
        eventNameTblCol.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getEventType())
        );

        eventPriceTblCol.setCellValueFactory(d ->
                new SimpleStringProperty(
                        d.getValue().getPrice() == null ? "" : "€"+d.getValue().getPrice().toPlainString()
                )
        );

        loadItemsAsync();
    }

    private void loadItemsAsync() {
        Task<List<Item>> task = new Task<>() {
            @Override
            protected List<Item> call() {
                return itemRepos.findAll();
            }
        };

        task.setOnSucceeded(e ->
                eventDetailsTbl.setItems(FXCollections.observableArrayList(task.getValue()))
        );

        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load items from DB: " + ex.getMessage()).showAndWait();
        });

        Thread t = new Thread(task, "load-items");
        t.setDaemon(true);
        t.start();
    }

    private void loadLatestBookingVirtualThread() {
        Thread.ofVirtual().start(() -> {
            try {
                Booking b = bookingRepos.findLatest();

                String text;
                if (b == null) {
                    text = "No bookings in database.";
                } else {
                    text = "Latest booking: " +
                            b.getUser().getUsername() + " | " +
                            b.getEventType().getEventType() + " (" + b.getEventType().getPrice() + ")" + " | " +
                            b.getDate().format(df) + " " + b.getTime().format(tf) + " | " +
                            b.getLocation().adress() + ", " + b.getLocation().city();
                }

                Platform.runLater(() -> latestBookingLbl.setText(text));

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() ->
                        latestBookingLbl.setText("Failed to load latest booking: " + e.getMessage())
                );
            }
        });
    }

    // tvoje postojeće metode za gumbe ostaju iste:
    @FXML private void goToBookings()   { BookingApp.showMainApp("Booking.fxml"); }
    @FXML private void goToPrice()      { BookingApp.showMainApp("Price.fxml"); }
    @FXML private void goToUserAdd()    { BookingApp.showMainApp("UserAdd.fxml"); }
    @FXML private void goToItemAdd()    { BookingApp.showMainApp("EventAdd.fxml"); }
    @FXML private void goToBookingAdd() { BookingApp.showMainApp("BookingAdd.fxml"); }

}
