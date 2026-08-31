package org.example.entities;


import java.io.Serializable;

/**
 * Apstraktni tip za bilo koga tko se može prijaviti u sustav.
 * Konkretne uloge ({@link Admin}, {@link User}) određuju svoja prava
 * kroz implementaciju metode {@link #canManageAllBookings()}.
 */
public abstract class Person implements Serializable {

    protected int id;
    protected String username;
    protected String password;
    protected String email;
    protected String phone;

    protected Person() {
    }

    protected Person(int id, String username, String password, String email, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    /**
     * @return {@code true} ako osoba smije vidjeti i upravljati svim
     * rezervacijama u sustavu; {@code false} ako smije upravljati
     * samo svojima.
     */
    public abstract boolean canManageAllBookings();

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}