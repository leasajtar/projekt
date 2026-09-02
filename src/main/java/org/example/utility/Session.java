package org.example.utility;

import org.example.entities.Person;

/** Drži referencu na trenutno prijavljenu osobu (Admin ili User) tijekom rada aplikacije. */
public final class Session {

    private static Person currentPerson;

    private Session() {}

    /** @param person osoba koja se upravo prijavila */
    public static void login(Person person) {
        currentPerson = person;
    }

    /** Odjavljuje trenutno prijavljenu osobu (postavlja je na {@code null}). */
    public static void logout() {
        currentPerson = null;
    }

    /** @return trenutno prijavljenu osobu, ili {@code null} ako nitko nije prijavljen */
    public static Person getCurrentPerson() {
        return currentPerson;
    }

    /** @return {@code true} ako je trenutno prijavljena osoba administrator */
    public static boolean isAdmin() {
        return currentPerson != null && currentPerson.canManageAllBookings();
    }
}