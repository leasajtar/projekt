package org.example.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class BackupData implements Serializable {
    protected transient List<User> users;
    protected transient List<Booking> bookings;
    protected transient Set<Record> records;
    protected transient List<Item> items;

    public BackupData(List<User> u, List<Booking> b, Set<Record> r, List<Item> i) {
        this.users = u;
        this.bookings = b;
        this.records = r;
        this.items = i;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public Set<Record> getRecords() {
        return records;
    }

    public void setRecords(Set<Record> records) {
        this.records = records;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
