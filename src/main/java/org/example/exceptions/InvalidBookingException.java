package org.example.exceptions;

/**
 * Bačena kada uneseni podaci za rezervaciju nisu valjani
 * (npr. negativan kućni broj ili datum u prošlosti).
 */
public class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}