package org.fhmdb.fhmdb_lijunamatata.observer;

import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import java.util.List;

/**
 * This interface defines the Observer for the Watchlist.
 * Any class that wants to be notified about changes in the watchlist
 * (e.g., when a movie is added or removed) must implement this interface.
 */
public interface WatchlistObserver {

    /**
     * Called when the watchlist is updated.
     * The updated list of movies is passed as a parameter.
     *
     * @param updatedWatchlist the current state of the watchlist
     */
    void onWatchlistChanged(List<Movie> updatedWatchlist);
}
