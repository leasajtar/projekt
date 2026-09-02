package org.example.screen;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.entities.Booking;
import org.example.entities.Item;
import org.example.entities.User;

import java.util.List;

/** Spremnik listi rezervacija, korisnika i stavki, dijeljen između JavaFX ekrana. */
public class BookingController {

    private static final ObservableList<Booking> bookings =
            FXCollections.observableArrayList();

    private static final ObservableList<User> users =
            FXCollections.observableArrayList();

    private static final ObservableList<Item> items =
            FXCollections.observableArrayList();

    private BookingController() {}

    public static ObservableList<Booking> getBookings() {
        return bookings;
    }

    public static void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public static void removeBooking(Booking booking) {
        bookings.remove(booking);
    }

    public static void setBookings(List<Booking> bookingList) {
        bookings.setAll(bookingList);
    }

    public static ObservableList<User> getUsers() {
        return users;
    }

    public static void setUsers(List<User> userList) {
        users.setAll(userList);
    }

    public static ObservableList<Item> getItems() {
        return items;
    }

    public static void setItems(List<Item> itemList) {
        items.setAll(itemList);
    }
}
