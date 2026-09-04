package com.desgin.exception;

/**
 * User-defined exception thrown when database operations via Firestore fail.
 */
public class DatabaseOperationException extends Exception {

    public DatabaseOperationException(String message) {
        super(message);
    }

    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
