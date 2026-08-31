package org.example.enteties;

import java.io.Serializable;

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
