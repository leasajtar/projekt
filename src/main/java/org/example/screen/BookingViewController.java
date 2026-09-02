package org.example.screen;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.app.BookingApp;
import org.example.entities.Booking;
import org.example.entities.Person;
import org.example.entities.User;
import org.example.repos.BookingRepos;
import org.example.utility.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/** Kontroler ekrana za pregled rezervacija, prikazuje sve
 * rezervacije {@link org.example.entities.Admin} ili samo vlastite {@link User}, uz filtriranje,
 * uređivanje, brisanje (samo za {@link org.example.entities.Admin}) i ručnu izradu sigurnosne kopije.
 */
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

    private final BookingRepos bookingRepos = new BookingRepos();

    /** Postavlja filtre, sakriva admin-only gumbe za obične korisnike, i učitava rezervacije. */
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


    /** Povezuje stupce tablice s pomoćnim metodama iz {@link BookingViewUtil}. */
    private void setupTableColumns() {
        userColTab.setCellValueFactory(BookingViewUtil::userValue);
        eventColTab.setCellValueFactory(BookingViewUtil::eventValue);
        dateColTab.setCellValueFactory(BookingViewUtil::dateValue);
        timeColTab.setCellValueFactory(BookingViewUtil::timeValue);
        addrColTab.setCellValueFactory(BookingViewUtil::addressValue);
    }


    /** Učitava rezervacije iz baze, sve za {@link org.example.entities.Admin}, samo vlastite za {@link User}. */
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

    /** Filtrira prikazane rezervacije prema odabranom tipu i unesenoj vrijednosti. */
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

        ObservableList<Booking> filteredBookings = allBookings.filtered(booking -> Util.filterSwitch(filter, value, booking));
        bookingTableView.setItems(filteredBookings);
    }

    /** Ponistava aktivni filter i vraća prikaz svih (dohvacenih) rezervacija. */
    @FXML
    private void handleClearFilter() {
        if (filterDropdown != null) filterDropdown.setValue(null);
        if (filterInput != null) filterInput.clear();
        bookingTableView.setItems(allBookings);
    }

    /** Salje odabranu rezervaciju na ekran za uredivanje preko {@link BookingEditContext}. */
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

    /** Brise odabranu rezervaciju nakon potvrde korisnika. */
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

    /** Ponovno ucitava tablicu rezervacija. */
    public void refresh() {
        loadBookings();
    }

    /** Pokrece asinkronu izradu sigurnosne kopije tablice rezervacija. */
    @FXML
    private void backupBookings() {
        BookingBackupService.backupNowAsync();
        new Alert(Alert.AlertType.INFORMATION, "Backup started ✅").showAndWait();
    }
}