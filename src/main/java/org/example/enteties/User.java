package org.example.enteties;

import jakarta.json.bind.annotation.JsonbTransient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class User implements Serializable
{
    public static final Logger logger = LoggerFactory.getLogger(User.class);
    public int id;
    public String username;
    public String password;
    public String email;
    public String phone;

    public User(){};
    public User(UserBuilder builder)
    {
        this.id=builder.id;
        this.username = builder.username;
        this.password = builder.password;
        this.email = builder.email;
        this.phone = builder.phone;
        logger.info("Korisnik '{}' je kreiran.", username);
        logger.debug("Detalji korisnika -> Email: {}, Telefon: {}", email, phone);

    }


    public String getUsername()
    {
        logger.trace("Pozvan getUsername() za korisnika '{}'", username);
        return username;
    }
    @JsonbTransient
    public String getPassword()
    {
        logger.trace("Pozvan getPassword() za korisnika '{}'", username);
        return password;
    }
    public String getEmail()
    {
        logger.trace("Pozvan getEmail() za korisnika '{}'", username);
        return email;
    }
    public String getPhone()
    {
        logger.trace("Pozvan getPhone() za korisnika '{}'", username);
        return phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public static class UserBuilder {
        private static final Logger logger = LoggerFactory.getLogger(UserBuilder.class);
        final int id;
        final String username;
        final String password;

        String email="";
        String phone="";
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