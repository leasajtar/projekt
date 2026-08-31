package org.example.utility;

import org.example.entities.Person;

/** Drži referencu na trenutno prijavljenu osobu (Admin ili User) tijekom rada aplikacije. */
public final class Session {

    private static Person currentPerson;

    private Session() {}

    public static void login(Person person) {
        currentPerson = person;
    }

    public static void logout() {
        currentPerson = null;
    }

    public static Person getCurrentPerson() {
        return currentPerson;
    }

    public static boolean isAdmin() {
        return currentPerson != null && currentPerson.canManageAllBookings();
    }
}