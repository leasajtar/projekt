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

public class HistoryController {

    @FXML private Button saveSnapshotBtn;
    @FXML private Button loadHistoryBtn;
    @FXML private ListView<String> activityListView;
    @FXML private ListView<String> backupListView;

    @FXML
    public void initialize() {
        HistoryMonitor.startAutoRefresh(5);
        refreshActivityView();
        loadBackupHistory();
    }

    @FXML
    private void refreshActivityView() {
        activityListView.setItems(FXCollections.observableArrayList(HistoryMonitor.snapshotOfActivity()));
    }

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

    @FXML
    private void loadBackupHistory() {
        BackupData data = UtilityBackUp.loadBackup();

        if (data == null) {
            backupListView.setItems(FXCollections.observableArrayList());
            return;
        }

        backupListView.setItems(FXCollections.observableArrayList(describe(data)));
    }

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

    private <T> int size(List<T> list) {
        return list == null ? 0 : list.size();
    }

    private <T> List<T> safe(List<T> list) {
        return list == null ? List.of() : list;
    }
}