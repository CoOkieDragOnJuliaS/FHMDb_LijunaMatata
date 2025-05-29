package org.fhmdb.fhmdb_lijunamatata.utils;

/**
 * Interface for updating status messages in a UI component.
 * <p>
 * Implement this interface in controllers or views that want to display
 * status information to the user, such as success or error messages.
 * <p>
 * This abstraction helps decouple business logic from UI details and allows
 * easier testing by mocking or spying on the update behavior.
 */
public interface StatusUpdatable {
    /**
     * Updates the UI with a status message.
     *
     * @param message  The message to display to the user.
     * @param isError  Whether the message indicates an error (true) or success/info (false).
     */
    void updateStatus(String message, boolean isError);
}
