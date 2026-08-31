package org.example.utility;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import org.example.entities.Booking;
import org.example.entities.Item;
import org.example.entities.Location;
import org.example.entities.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class BookingViewUtil {
    private BookingViewUtil() {}
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public static ObservableValue<String> userValue(TableColumn.CellDataFeatures<Booking, String> c) {
        User user = c.getValue().getUser();
        return new SimpleStringProperty(user != null ? orNA(user.getUsername()) : "NA");
    }

    public static ObservableValue<String> eventValue(TableColumn.CellDataFeatures<Booking, String> c) {
        Item item = c.getValue().getEventType();
        return new SimpleStringProperty(item != null ? orNA(item.getEventType()) : "NA");
    }

    public static ObservableValue<String> dateValue(TableColumn.CellDataFeatures<Booking, String> c) {
        LocalDate date = c.getValue().getDate();
        return new SimpleStringProperty(date != null ? date.format(DATE_FMT) : "NA");
    }

    public static ObservableValue<String> timeValue(TableColumn.CellDataFeatures<Booking, String> c) {
        LocalTime time = c.getValue().getTime();
        return new SimpleStringProperty(time != null ? time.format(TIME_FMT) : "NA");
    }

    public static ObservableValue<String> addressValue(TableColumn.CellDataFeatures<Booking, String> c) {
        Location location = c.getValue().getLocation();
        return new SimpleStringProperty(location != null ? formatLocation(location) : "NA");
    }

    private static String formatLocation(Location location) {
        String combined = joinNonEmpty(orEmpty(location.adress()), orEmpty(location.city()));
        return combined.isEmpty() ? "NA" : combined;
    }

    private static String joinNonEmpty(String address, String city) {
        if (address.isEmpty()) return city;
        if (city.isEmpty()) return address;
        return address + ", " + city;
    }

    private static String orNA(String value) {
        return value != null ? value : "NA";
    }

    private static String orEmpty(String value) {
        return value != null ? value : "";
    }
}
