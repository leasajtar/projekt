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

/**Pomocne metode za stupce tablice rezervacija {@link org.example.screen.BookingViewController}.
 * Izdvojene radi citljivosti.}*/
public class BookingViewUtil {
    private BookingViewUtil() {}
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * @param c podaci retka tablice
     * @return korisničko ime vlasnika rezervacije, ili "NA" ako nije dostupno
     */
    public static ObservableValue<String> userValue(TableColumn.CellDataFeatures<Booking, String> c) {
        User user = c.getValue().getUser();
        return new SimpleStringProperty(user != null ? orNA(user.getUsername()) : "NA");
    }

    /**
     * @param c podaci retka tablice
     * @return naziv vrste događaja, ili "NA" ako nije dostupan
     */
    public static ObservableValue<String> eventValue(TableColumn.CellDataFeatures<Booking, String> c) {
        Item item = c.getValue().getEventType();
        return new SimpleStringProperty(item != null ? orNA(item.getEventType()) : "NA");
    }

    /**
     * @param c podaci retka tablice
     * @return formatirani datum (dd.MM.yyyy), ili "NA" ako nije dostupan
     */
    public static ObservableValue<String> dateValue(TableColumn.CellDataFeatures<Booking, String> c) {
        LocalDate date = c.getValue().getDate();
        return new SimpleStringProperty(date != null ? date.format(DATE_FMT) : "NA");
    }

    /**
     * @param c podaci retka tablice
     * @return formatirano vrijeme (HH:mm), ili "NA" ako nije dostupno
     */
    public static ObservableValue<String> timeValue(TableColumn.CellDataFeatures<Booking, String> c) {
        LocalTime time = c.getValue().getTime();
        return new SimpleStringProperty(time != null ? time.format(TIME_FMT) : "NA");
    }

    /**
     * @param c podaci retka tablice
     * @return spojenu adresu i grad, ili "NA" ako lokacija nije dostupna
     */
    public static ObservableValue<String> addressValue(TableColumn.CellDataFeatures<Booking, String> c) {
        Location location = c.getValue().getLocation();
        return new SimpleStringProperty(location != null ? formatLocation(location) : "NA");
    }

    /**
     * @param location lokacija koju treba formatirati
     * @return "adresa, grad" (ili samo jedno od njih ako je drugo prazno), "NA" ako su oba prazna
     */
    private static String formatLocation(Location location) {
        String combined = joinNonEmpty(orEmpty(location.adress()), orEmpty(location.city()));
        return combined.isEmpty() ? "NA" : combined;
    }

    /**
     * @param address adresa (može biti prazna)
     * @param city    grad (može biti prazan)
     * @return oba spojena zarezom, ili samo neprazno od njih dvoje
     */
    private static String joinNonEmpty(String address, String city) {
        if (address.isEmpty()) return city;
        if (city.isEmpty()) return address;
        return address + ", " + city;
    }

    /**
     * @param value vrijednost za provjeru
     * @return danu vrijednost, ili "NA" ako je {@code null}
     */
    private static String orNA(String value) {
        return value != null ? value : "NA";
    }

    /**
     * @param value vrijednost za provjeru
     * @return danu vrijednost, ili prazan string ako je {@code null}
     */
    private static String orEmpty(String value) {
        return value != null ? value : "";
    }
}
