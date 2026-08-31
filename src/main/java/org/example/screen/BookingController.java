package org.example.screen;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.enteties.Booking;
import org.example.enteties.Item;
import org.example.enteties.User;

import java.util.List;

public class BookingController {

    private static BookingController instance;

    private ObservableList<Booking> bookings;
    private ObservableList<User> users;
    private ObservableList<Item> items;

    private BookingController() {
        bookings = FXCollections.observableArrayList();
        users = FXCollections.observableArrayList();
        items = FXCollections.observableArrayList();
    }

    public static BookingController getInstance() {
        if (instance == null) {
            instance = new BookingController();
        }
        return instance;
    }

    public ObservableList<Booking> getBookings() {
        return bookings;
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public void removeBooking(Booking booking) {
        bookings.remove(booking);
    }

    public void setBookings(List<Booking> bookingList) {
        bookings.setAll(bookingList);
    }

    public ObservableList<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> userList) {
        users.setAll(userList);
    }

    public ObservableList<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> itemList) {
        items.setAll(itemList);
    }
}
