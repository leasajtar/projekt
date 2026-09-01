package org.example.exceptions;

/** Neoznačena iznimka koja označava da je isteklo vrijeme čekanja na korisnički unos. */
public class InputTimeoutException extends RuntimeException {
    /** @param message opis greške */
    public InputTimeoutException(String message) {
        super(message);
    }
}
