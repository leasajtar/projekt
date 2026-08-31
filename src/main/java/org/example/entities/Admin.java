package org.example.entities;

import org.example.entities.Person;

public class Admin extends Person {

    public Admin() {
        super();
    }

    public Admin(int id, String username, String password, String email, String phone) {
        super(id, username, password, email, phone);
    }

    @Override
    public boolean canManageAllBookings() {
        return true;
    }
}