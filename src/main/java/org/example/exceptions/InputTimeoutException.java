package org.example.exceptions;

public class InputTimeoutException extends RuntimeException {
    public InputTimeoutException(String message) {
        super(message);
    }
}
