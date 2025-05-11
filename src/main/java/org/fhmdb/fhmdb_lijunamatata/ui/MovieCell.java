package org.fhmdb.fhmdb_lijunamatata.ui;

import javafx.scene.control.Button;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.fhmdb.fhmdb_lijunamatata.utils.ClickEventHandler;

/**
 * @author Julia, Lilie
 * @date 11.05.2025
 * This class is the design cell for the Movie object inside the UI
 */
public class MovieCell extends AbstractMovieCell {
    private final ClickEventHandler<Movie> addToWatchlistClicked;

    public MovieCell(ClickEventHandler<Movie> addToWatchlistClicked) {
        super();
        this.addToWatchlistClicked = addToWatchlistClicked;

        // Create and configure watchlist button
        Button watchlistButton = new Button("Add to Watchlist");
        watchlistButton.setOnMouseClicked(mouseEvent -> {
            if (this.addToWatchlistClicked != null) {
                this.addToWatchlistClicked.onClick(getItem());
            }
        });
        watchlistButton.getStyleClass().add("watchlist-buttons");

        // Add the button to the layout
        setActionButton(watchlistButton);
    }

}
