package org.fhmdb.fhmdb_lijunamatata.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.fhmdb.fhmdb_lijunamatata.database.MovieEntity;
import org.fhmdb.fhmdb_lijunamatata.exceptions.DatabaseException;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.fhmdb.fhmdb_lijunamatata.repositories.WatchlistRepository;
import org.fhmdb.fhmdb_lijunamatata.ui.WatchlistCell;
import org.fhmdb.fhmdb_lijunamatata.utils.ClickEventHandler;

import java.util.List;
import java.util.logging.Logger;

public class WatchlistController {
    private ObservableList<Movie> watchlistMovies;

    @FXML
    private ListView<Movie> watchlistView;

    @FXML
    private Label statusLabel;

    private ClickEventHandler<Movie> onRemoveFromWatchlistClicked;


    //WatchlistRepository
    private WatchlistRepository watchlistRepository;

    Logger logger = Logger.getLogger(FHMDbController.class.getName());

    public WatchlistController() {
        try {
            this.watchlistRepository = new WatchlistRepository();
        } catch (DatabaseException sqlException) {
            logger.severe(sqlException.getMessage());
            updateStatusLabel("Access to Database was not successful!", true);
        }
    }

    public WatchlistController(WatchlistRepository watchlistRepository) {
        this.watchlistRepository = watchlistRepository;
    }

    @FXML
    public void initialize() {
        initializeClickHandlers();
        initializeStatusLabel();
        initializeWatchlistView();
    }

    protected void initializeClickHandlers() {
        onRemoveFromWatchlistClicked = (clickedMovie) -> {
            try {
                MovieEntity movieEntity = new MovieEntity(clickedMovie);
                watchlistRepository.removeFromWatchlist(movieEntity.getApiId());
                refreshWatchlist();
                logger.info("Removing movie from watchlist: " + clickedMovie.getTitle());
                updateStatusLabel("Removed " + clickedMovie.getTitle() + " from Watchlist!", false);
            } catch (DatabaseException dbException) {
                logger.severe(dbException.getMessage());
                updateStatusLabel("Movie " + clickedMovie.getTitle() + " could not be added to the watchlist!", true);
            }
        };
    }

    private void refreshWatchlist() {
        initializeWatchlistView();
    }


    /**
     * Adding a new instance to statusLabel and set it to not visible by updating it
     */
    private void initializeStatusLabel() {
        updateStatusLabel("", false);
    }

    private void initializeWatchlistView() {
        watchlistView.setItems(this.watchlistMovies);
        watchlistView.setCellFactory(watchlistView -> new WatchlistCell(onRemoveFromWatchlistClicked));
    }

    private void initializeWatchlistMovies() {
        updateStatusLabel("Loading watchlist...", false);
        List<MovieEntity> watchlistMovieEntities = watchlistRepository.getWatchlistMovies();
        List<Movie> movies = MovieEntity.toMovies(watchlistMovieEntities);
        /* also possible:
        List<Movie> movies = watchlistMovieEntities.stream()
                .map(MovieEntity::toMovie)
                .toList();
        */
        this.watchlistMovies = FXCollections.observableArrayList(movies);
        if (this.watchlistMovies.isEmpty()) {
            updateStatusLabel("No movies in watchlist!", false);
        } else {
            updateStatusLabel(String.format("%d Movies in watchlist!", watchlistMovies.size()), false);
        }
    }

    /**
     * Updates the status label with a given message.
     * Ensures the update runs on the JavaFX UI thread.
     *
     * @param message The message to display.
     * @param isError If true, the label is made visible; otherwise, it's hidden when empty message and not an error.
     */
    public void updateStatusLabel(String message, boolean isError) {
        if (statusLabel != null) {
            logger.info("Updating status label...");
            statusLabel.setText(message);
            statusLabel.setVisible(isError || !message.isEmpty());
        }
    }
}
