package org.example.exceptions;

/**
 * Označena iznimka koja označava da uneseni brojčani podatak nije
 * valjan (npr. cijena manja ili jednaka nuli).
 */
public class InvalidNumberInputException extends Exception {
    /** @param message opis greške */
    public InvalidNumberInputException(String message) {
        super(message);
    }
}
