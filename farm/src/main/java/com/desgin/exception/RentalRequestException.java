package com.desgin.exception;

/**
 * User-defined exception thrown when rental request creation or status updates fail.
 */
public class RentalRequestException extends Exception {

    public RentalRequestException(String message) {
        super(message);
    }

    public RentalRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
