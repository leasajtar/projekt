package org.example.exceptions;

/**
 * Oznacena iznimka bacena kada uneseni podaci za rezervaciju nisu valjani
 * (npr. negativan kućni broj ili datum u prošlosti).
 */
public class InvalidBookingException extends Exception {
    /** @param message opis greške */
    public InvalidBookingException(String message) {
        super(message);
    }
}