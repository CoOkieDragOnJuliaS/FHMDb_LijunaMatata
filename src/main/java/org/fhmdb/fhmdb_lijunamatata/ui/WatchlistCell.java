package org.fhmdb.fhmdb_lijunamatata.ui;

import javafx.scene.control.Button;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.fhmdb.fhmdb_lijunamatata.utils.ClickEventHandler;

/**
 * @author Julia Sass, Lilie Lin
 * @date 11.05.2025
 * This class is the design cell for the Watchlits object inside the UI
 */
public class WatchlistCell extends AbstractMovieCell {
    private final ClickEventHandler<Movie> removeFromWatchlistClicked;

    public WatchlistCell(ClickEventHandler<Movie> removeFromWatchlistClicked) {
        super();
        this.removeFromWatchlistClicked = removeFromWatchlistClicked;

        // Create and configure remove button
        Button removeButton = new Button("Remove from Watchlist");
        removeButton.setOnMouseClicked(mouseEvent -> {
            if (this.removeFromWatchlistClicked != null) {
                this.removeFromWatchlistClicked.onClick(getItem());
            }
        });
        removeButton.getStyleClass().add("watchlist-buttons");

        // Add the button to the layout
        setActionButton(removeButton);
    }

}
