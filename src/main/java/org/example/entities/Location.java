package org.example.entities;

import java.io.Serializable;

/**
 * Lokacija događaja.
 *
 * @param city   grad u kojem se događaj održava
 * @param adress adresa (ulica i kućni broj) na kojoj se događaj održava
 */
public record Location(String city, String adress) implements Serializable {
    @Override
    public String city() {
        return city;
    }

    @Override
    public String adress() {
        return adress;
    }
}
