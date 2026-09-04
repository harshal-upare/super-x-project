package com.desgin.exception;

/**
 * User-defined exception thrown when machinery data fails validation
 * (e.g., missing name, invalid rental price, missing category or location).
 */
public class MachineryValidationException extends Exception {

    public MachineryValidationException(String message) {
        super(message);
    }

    public MachineryValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
