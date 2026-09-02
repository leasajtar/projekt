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
import org.example.collections.EntityCollection;
import org.example.entities.Booking;
import org.example.entities.Item;
import org.example.repos.BookingRepos;
import org.example.repos.ItemRepos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

/**Kontroler pocetnog ekrana. Virtualna nit dohvaca posljednju rezervaciju iz baze, a na zasebnoj,
 * pozadinskoj niti, se ucitava katalog stavki {@link EntityCollection} sortiran po cijeni */
public class OpeningScreenController {

    private static final Logger logger = LoggerFactory.getLogger(OpeningScreenController.class);

    @FXML private TableView<Item> eventDetailsTbl;
    @FXML private TableColumn<Item, String> eventNameTblCol;
    @FXML private TableColumn<Item, String> eventPriceTblCol;
    @FXML private Label latestBookingLbl;

    private final ItemRepos itemRepos = new ItemRepos();
    private final BookingRepos bookingRepos = new BookingRepos();

    private final DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");

    /** Postavlja tablicu stavki, pokrece dohvat posljednje rezervacije i asinkrono ucitavanje kataloga. */
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

    /** Ucitava sve stavke iz baze na pozadinskoj niti, slaze ih u
     * {@link EntityCollection} i sortira po cijeni prije prikaza u tablici.*/
    private void loadItemsAsync() {
        Task<List<Item>> task = new Task<>() {
            @Override
            protected List<Item> call() {
                EntityCollection<Item> catalog = new EntityCollection<>(Item::getId);
                catalog.addAll(itemRepos.findAll());
                return catalog.sortedBy(Comparator.comparing(Item::getPrice));
            }
        };

        task.setOnSucceeded(e ->
                eventDetailsTbl.setItems(FXCollections.observableArrayList(task.getValue()))
        );

        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            logger.error("Failed to load items from DB", ex);
            new Alert(Alert.AlertType.ERROR, "Failed to load items from DB: " + ex.getMessage()).showAndWait();
        });

        Thread t = new Thread(task, "load-items");
        t.setDaemon(true);
        t.start();
    }

    /** Dohvaca posljednju unesenu rezervaciju na virtualnoj niti i prikazuje je na oznaci. */
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
                logger.error("Failed to load latest booking", e);
                Platform.runLater(() ->
                        latestBookingLbl.setText("Failed to load latest booking: " + e.getMessage())
                );
            }
        });
    }

    @FXML private void goToBookings()   { BookingApp.showMainApp("Booking.fxml"); }
    @FXML private void goToPrice()      { BookingApp.showMainApp("Price.fxml"); }
    @FXML private void goToUserAdd()    { BookingApp.showMainApp("UserAdd.fxml"); }
    @FXML private void goToItemAdd()    { BookingApp.showMainApp("EventAdd.fxml"); }
    @FXML private void goToBookingAdd() { BookingApp.showMainApp("BookingAdd.fxml"); }
}