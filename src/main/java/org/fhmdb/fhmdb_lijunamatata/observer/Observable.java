package org.fhmdb.fhmdb_lijunamatata.observer;

import org.fhmdb.fhmdb_lijunamatata.exceptions.DatabaseException;

/**
 * Interface for implementing the Observer pattern in the Watchlist system.
 * <p>
 * An Observable object maintains a list of observers and notifies them
 * of changes, such as when the watchlist is modified.
 * <p>
 * This interface helps decouple components like repositories and controllers,
 * enabling better modularity and testability.
 */
public interface Observable {

    /**
     * Registers a new observer that should be notified when the state changes.
     *
     * @param observer The observer to be added.
     */
    void addObserver(WatchlistObserver observer);

    /**
     * Removes a previously registered observer.
     *
     * @param observer The observer to be removed.
     */
    void removeObserver(WatchlistObserver observer);

    /**
     * Notifies all registered observers of a change.
     *
     * @throws DatabaseException If an error occurs while retrieving the updated data.
     */
    void notifyObservers() throws DatabaseException;
}
