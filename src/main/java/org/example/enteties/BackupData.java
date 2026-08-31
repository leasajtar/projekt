package org.example.enteties;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class BackupData implements Serializable {
    public List<User> users;
    public List<Booking> bookings;
    public Set<Record> records;
    public List<Item> items;

    public BackupData(List<User> u, List<Booking> b, Set<Record> r, List<Item> i) {
        this.users = u;
        this.bookings = b;
        this.records = r;
        this.items = i;
    }
}
