package org.example.entities;

/**
 *Admin je administratorska uloga u sustavu. Za razliku od {@link User}, admin moze vidjeti i pravljati rezervacijama
 * svih korisnika te povijest aplikacije.
 * */

public class Admin extends Person {

    /**Stvara novi Admin objekt*/
    public Admin() {
        super();
    }

    /**
     * Stvara administratora s potpunim podatcima.
     * @param id        identifikacijski broj admina
     * @param username  korisnicko ime
     * @param password  lozinka
     * @param email     email adresa(obavezno za razliku od {@link User})
     * @param phone     broj mobitela(obavezno za razliku od {@link User})
     * */
    public Admin(int id, String username, String password, String email, String phone) {
        super(id, username, password, email, phone);
    }

    /**Nasljedena klasa od {@link Person}, za admin klasu uvijek vraca true*/
    @Override
    public boolean canManageAllBookings() {
        return true;
    }
}