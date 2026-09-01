package org.example.exceptions;

/**
 * Neoznačena iznimka koja označava grešku pri radu s bazom podataka
 * (npr. nedostajući generirani ključ nakon umetanja retka).
 */
public class DatabaseException extends RuntimeException {

    /** @param message opis greške */
    public DatabaseException(String message) {
        super(message);
    }

    /**
     * @param message opis greške
     * @param cause   izvorna iznimka koja je uzrokovala ovu grešku
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
