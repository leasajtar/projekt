package org.example.entities;

import jakarta.json.bind.annotation.JsonbTransient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Obični korisnik sustava. Za razliku od {@link Admin}, smije vidjeti i
 * upravljati samo vlastitim rezervacijama. Instance se grade isključivo
 * preko ugniježđenog {@link UserBuilder}-a (builder pattern).
 */
public class User extends Person {
    private static final Logger logger = LoggerFactory.getLogger(User.class);

    /** Prazan konstruktor, potreban za JSON-B/JDBC mapiranje. */
    public User() {
        super();
    }

    /**
     * Stvara korisnika iz podataka prikupljenih u {@link UserBuilder}-u i bilježi ga u log.
     *
     * @param builder builder s popunjenim podacima korisnika
     */
    public User(UserBuilder builder) {
        super(builder.id, builder.username, builder.password, builder.email, builder.phone);
        logger.info("Korisnik '{}' je kreiran.", username);
        logger.debug("Detalji korisnika -> Email: {}, Telefon: {}", email, phone);
    }

    /**
     * Obični korisnik uvijek vraća (false) — smije upravljati samo svojim rezervacijama.
     */
    @Override
    public boolean canManageAllBookings() {
        return false;
    }

    /**
     * Označeno kao {@code @JsonbTransient} kako lozinka nikad ne bi bila
     * uključena u JSON serijalizaciju korisnika.
     */
    @JsonbTransient
    @Override
    public String getPassword() {
        logger.trace("Pozvan getPassword() za korisnika '{}'", username);
        return super.getPassword();
    }

    /**
     * Builder za postupno, čitljivo stvaranje {@link User} objekata
     * (korisničko ime i lozinka su obavezni, email i telefon opcionalni).
     */
    public static class UserBuilder {
        private static final Logger logger = LoggerFactory.getLogger(UserBuilder.class);
        final int id;
        final String username;
        final String password;

        String email = "";
        String phone = "";

        /**
         * @param id       identifikatorski broj korisnika
         * @param username korisničko ime
         * @param password lozinka
         */
        public UserBuilder(int id, String username, String password) {
            this.id = id;
            this.username = username;
            this.password = password;
            logger.debug("Inicijaliziran UserBuilder za '{}'", username);
        }

        /**
         * @param email email adresa korisnika
         * @return ovaj builder, radi ulančavanja poziva
         */
        public UserBuilder email(String email) {
            this.email = email;
            logger.debug("Postavljen email '{}' za korisnika '{}'", email, username);
            return this;
        }

        /**
         * @param phone broj telefona korisnika
         * @return ovaj builder, radi ulančavanja poziva
         */
        public UserBuilder phone(String phone) {
            this.phone = phone;
            logger.debug("Postavljen telefon '{}' za korisnika '{}'", phone, username);
            return this;
        }

        /** @return izgrađenog {@link User} korisnika s podacima prikupljenim u ovom builderu */
        public User build() {
            User user = new User(this);
            logger.info("UserBuilder završio kreiranje korisnika '{}'", username);
            return user;
        }
    }
}