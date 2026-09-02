package org.example.app;

import javafx.application.Application;

/**
 * Zasebna ulazna točka bez izravnog nasljeđivanja {@link Application}, korisna
 * za pokretanje putem "fat jar"-a ili jlink/jpackage alata koji ne podržavaju
 * izravno pokretanje JavaFX klase.
 */
public class Launcher {
    /**
     * Pokreće JavaFX aplikaciju {@link BookingApp}.
     *
     * @param args argumenti komandne linije, prosljeđuju se dalje aplikaciji
     */
    public static void main(String[] args) {
        Application.launch(BookingApp.class, args);
    }
}