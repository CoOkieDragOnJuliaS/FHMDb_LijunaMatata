package org.fhmdb.fhmdb_lijunamatata.exceptions;

/**
 * Custom checked exception representing database-related errors.

 * Extends Exception (checked) to enforce explicit handling or propagation.
 * Used for signaling failures during database operations like queries or updates.
 */
public class DatabaseException extends Exception {
    /**
     * Constructor with error message.
     * @param message the error description
     */
    public DatabaseException(String message) {
        super(message);
    }

    /**
     * Constructor with error message and root cause.
     * @param message the error description
     * @param cause the original exception causing this error
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
