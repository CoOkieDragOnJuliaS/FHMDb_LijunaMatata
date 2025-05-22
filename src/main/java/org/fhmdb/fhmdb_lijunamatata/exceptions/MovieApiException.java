package org.fhmdb.fhmdb_lijunamatata.exceptions;

/**
 * Custom checked exception representing errors from the external Movie API.

 * Extends Exception (checked) so callers must handle or propagate this explicitly.
 * Used for issues like network failures or invalid API responses.
 */
public class MovieApiException extends Exception {
    /**
     * Constructor with error message.
     * @param message the error description
     */
    public MovieApiException(String message) {
        super(message);
    }

    /**
     * Constructor with error message and root cause.
     * @param message the error description
     * @param cause the original exception causing this error
     */
    public MovieApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
