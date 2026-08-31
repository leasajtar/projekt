package org.example.entities;

import jakarta.json.bind.annotation.JsonbTransient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class User extends Person {
    private static final Logger logger = LoggerFactory.getLogger(User.class);

    public User() {
        super();
    }

    public User(UserBuilder builder) {
        super(builder.id, builder.username, builder.password, builder.email, builder.phone);
        logger.info("Korisnik '{}' je kreiran.", username);
        logger.debug("Detalji korisnika -> Email: {}, Telefon: {}", email, phone);
    }

    @Override
    public boolean canManageAllBookings() {
        return false;
    }

    @JsonbTransient
    @Override
    public String getPassword() {
        logger.trace("Pozvan getPassword() za korisnika '{}'", username);
        return super.getPassword();
    }

    public static class UserBuilder {
        private static final Logger logger = LoggerFactory.getLogger(UserBuilder.class);
        final int id;
        final String username;
        final String password;

        String email = "";
        String phone = "";

        public UserBuilder(int id, String username, String password) {
            this.id = id;
            this.username = username;
            this.password = password;
            logger.debug("Inicijaliziran UserBuilder za '{}'", username);
        }

        public UserBuilder email(String email) {
            this.email = email;
            logger.debug("Postavljen email '{}' za korisnika '{}'", email, username);
            return this;
        }

        public UserBuilder phone(String phone) {
            this.phone = phone;
            logger.debug("Postavljen telefon '{}' za korisnika '{}'", phone, username);
            return this;
        }

        public User build() {
            User user = new User(this);
            logger.info("UserBuilder završio kreiranje korisnika '{}'", username);
            return user;
        }
    }
}