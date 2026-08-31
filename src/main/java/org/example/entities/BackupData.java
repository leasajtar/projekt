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
}
