package org.fhmdb.fhmdb_lijunamatata.ui;

import javafx.scene.control.Button;
import org.fhmdb.fhmdb_lijunamatata.database.MovieEntity;
import org.fhmdb.fhmdb_lijunamatata.exceptions.DatabaseException;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.fhmdb.fhmdb_lijunamatata.repositories.WatchlistRepository;
import org.fhmdb.fhmdb_lijunamatata.utils.ClickEventHandler;

/**
 * Custom UI cell for displaying a movie with an "Add to Watchlist" button.
 * The button is hidden if the movie is already in the watchlist.
 */
public class MovieCell extends AbstractMovieCell {
    private final ClickEventHandler<Movie> addToWatchlistClicked;
    private final Button watchlistButton;
    private final WatchlistRepository watchlistRepository;

    public MovieCell(ClickEventHandler<Movie> addToWatchlistClicked) {
        super();
        this.addToWatchlistClicked = addToWatchlistClicked;

        this.watchlistButton = new Button("Add to Watchlist");
        this.watchlistButton.getStyleClass().add("watchlist-buttons");

        try {
            this.watchlistRepository = WatchlistRepository.getInstance();
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to initialize WatchlistRepository", e);
        }

        // Click handler: add movie and hide button
        watchlistButton.setOnMouseClicked(mouseEvent -> {
            Movie movie = getItem();
            if (movie != null && addToWatchlistClicked != null) {
                addToWatchlistClicked.onClick(movie);
                watchlistButton.setVisible(false); // hide button after adding
            }
        });

        setActionButton(watchlistButton);
    }

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            watchlistButton.setVisible(false);
        } else {
            try {
                boolean alreadyInWatchlist = watchlistRepository.existsInWatchlist(
                        new MovieEntity(movie).getApiId()
                );
                watchlistButton.setVisible(!alreadyInWatchlist);
            } catch (DatabaseException e) {
                watchlistButton.setVisible(true); // fallback: show button
            }
        }
    }
}
