package org.example.screen;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.example.entities.BackupData;
import org.example.entities.Booking;
import org.example.entities.Item;
import org.example.entities.User;
import org.example.utility.HistoryMonitor;
import org.example.utility.UtilityBackUp;

import java.util.ArrayList;
import java.util.List;

/**Kontrolor ekrana za povijest/nadzor aplikacije. Dostupan samo {@link org.example.entities.Admin}.
 * Prikazuje popis uzivo osvjezenih aktivnosti {@link HistoryMonitor} i sadrzaj
 * posljednje sigurnosne kopije iz {@code backup.bin} */
public class HistoryController {

    @FXML private Button saveSnapshotBtn;
    @FXML private Button loadHistoryBtn;
    @FXML private ListView<String> activityListView;
    @FXML private ListView<String> backupListView;

    /** Pokrece atomatsko osvjezavanje aktivnosti i ucitava oba popisa pri ucitavanju*/
    @FXML
    public void initialize() {
        HistoryMonitor.startAutoRefresh(5);
        refreshActivityView();
        loadBackupHistory();
    }

    /** Osvjezava prikaz popisa uzivo prema stanju iz {@link HistoryMonitor}*/
    @FXML
    private void refreshActivityView() {
        activityListView.setItems(FXCollections.observableArrayList(HistoryMonitor.snapshotOfActivity()));
    }

    /** Pokrece snimanje snapshota u {@code backup.bin} na virtualnoj niti, a po zavrsetku
     * osvjezava prikaz i obavjestava korisnika.
     * */
    @FXML
    private void handleSaveSnapshot() {
        Thread.ofVirtual().start(() -> {
            HistoryMonitor.saveSnapshot();
            Platform.runLater(() -> {
                refreshActivityView();
                new Alert(Alert.AlertType.INFORMATION, "Snapshot saved to backup.bin ✅").showAndWait();
            });
        });
    }

    /** Ucitava sadrzaj {@code backup.bin} i prikazuje ga u obliku citljivih redaka. */
    @FXML
    private void loadBackupHistory() {
        BackupData data = UtilityBackUp.loadBackup();

        if (data == null) {
            backupListView.setItems(FXCollections.observableArrayList());
            return;
        }

        backupListView.setItems(FXCollections.observableArrayList(describe(data)));
    }

    /**Pretvara ucitani snapshot u listu citljivih redaka teksta za prikaz u {@link ListView}.
     *
     * @param data snapshot podataka uccitan iz {@code backup.bin}
     * @return redci teksta koji opisuju korisnike, rezervacije i stavke iz snapshota
     */
    private List<String> describe(BackupData data) {
        List<String> lines = new ArrayList<>();

        lines.add("=== Users (" + size(data.getUsers()) + ") =");
        for (User u : safe(data.getUsers())) {
            lines.add("User: " + u.getUsername() + " (" + u.getEmail() + ")");
        }

        lines.add("=== Bookings (" + size(data.getBookings()) + ") =");
        for (Booking b : safe(data.getBookings())) {
            lines.add("Booking: " + b.getUser().getUsername() + " -> " +
                    b.getEventType().getEventType() + " on " + b.getDate());
        }

        lines.add("=== Items (" + size(data.getItems()) + ") =");
        for (Item i : safe(data.getItems())) {
            lines.add("Item: " + i.getEventType() + " (" + i.getPrice() + ")");
        }

        return lines;
    }

    /** @param list lista ciju velicinu treba provjeriti (moye biti {@code null})
     * @return velicinu liste, ili 0 ako je {@code null}
     */
    private <T> int size(List<T> list) {
        return list == null ? 0 : list.size();
    }

    /**
     * @param list lista koju treba osigurati od {@code null} vrijednosti
     * @return danu listu, ili praznu listu ako je {@code null}
     */
    private <T> List<T> safe(List<T> list) {
        return list == null ? List.of() : list;
    }
}