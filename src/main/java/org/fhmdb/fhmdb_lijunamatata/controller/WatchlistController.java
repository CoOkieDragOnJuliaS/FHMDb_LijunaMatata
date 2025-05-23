package org.fhmdb.fhmdb_lijunamatata.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.fhmdb.fhmdb_lijunamatata.database.MovieEntity;
import org.fhmdb.fhmdb_lijunamatata.exceptions.DatabaseException;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.fhmdb.fhmdb_lijunamatata.observer.WatchlistObserver;
import org.fhmdb.fhmdb_lijunamatata.repositories.WatchlistRepository;
import org.fhmdb.fhmdb_lijunamatata.ui.WatchlistCell;
import org.fhmdb.fhmdb_lijunamatata.utils.ClickEventHandler;

import java.util.List;
import java.util.logging.Logger;

public class WatchlistController implements WatchlistObserver {

    private ObservableList<Movie> watchlistMovies;

    @FXML
    private ListView<Movie> watchlistView;

    @FXML
    private Label statusLabel;

    private ClickEventHandler<Movie> onRemoveFromWatchlistClicked;

    private WatchlistRepository watchlistRepository;

    Logger logger = Logger.getLogger(WatchlistController.class.getName());

    public WatchlistController() {
        try {
            this.watchlistRepository = WatchlistRepository.getInstance();
        } catch (DatabaseException e) {
            logger.severe("Database initialization failed: " + e.getMessage());
            // Note: Do not call updateStatusLabel directly from the constructor,
            // as the FXML components (e.g., statusLabel) may not be initialized yet.
        }
    }

    public WatchlistController(WatchlistRepository watchlistRepository) {
        this.watchlistRepository = watchlistRepository;
    }

    @FXML
    public void initialize() {
        initializeClickHandlers();
        initializeStatusLabel();

        if (watchlistRepository != null) {
            watchlistRepository.addObserver(this);
            refreshWatchlist();
        } else {
            updateStatusLabel("Watchlist repository not initialized", true);
        }
    }

    protected void initializeClickHandlers() {
        onRemoveFromWatchlistClicked = (clickedMovie) -> {
            try {
                MovieEntity movieEntity = new MovieEntity(clickedMovie);
                watchlistRepository.removeFromWatchlist(movieEntity.getApiId());
                logger.info("Removing movie from watchlist: " + clickedMovie.getTitle());
                updateStatusLabel("Removed " + clickedMovie.getTitle() + " from Watchlist!", false);
                refreshWatchlist();
            } catch (DatabaseException e) {
                logger.severe("Error removing movie from watchlist: " + e.getMessage());
                updateStatusLabel("Failed to remove movie from watchlist: " + e.getMessage(), true);
            }
        };
    }

    protected void refreshWatchlist() {
        try {
            initializeWatchlistMovies();
        } catch (DatabaseException e) {
            logger.severe("Failed to load watchlist movies: " + e.getMessage());
            updateStatusLabel("Failed to load watchlist movies: " + e.getMessage(), true);
        }
        initializeWatchlistView();
    }

    private void initializeStatusLabel() {
        updateStatusLabel("", false);
    }

    private void initializeWatchlistView() {
        if (watchlistMovies == null) {
            watchlistMovies = FXCollections.observableArrayList();
        }
        watchlistView.getItems().clear();
        watchlistView.setItems(watchlistMovies);
        watchlistView.setCellFactory(view -> new WatchlistCell(onRemoveFromWatchlistClicked));
    }

    private void initializeWatchlistMovies() throws DatabaseException {
        updateStatusLabel("Loading watchlist...", false);
        List<MovieEntity> watchlistMovieEntities = watchlistRepository.getWatchlistMovies();
        List<Movie> movies = MovieEntity.toMovies(watchlistMovieEntities);
        this.watchlistMovies = FXCollections.observableArrayList(movies);
        if (watchlistMovies.isEmpty()) {
            updateStatusLabel("No movies in watchlist!", false);
        } else {
            updateStatusLabel(watchlistMovies.size() + " Movies in watchlist!", false);
        }
    }

    public void updateStatusLabel(String message, boolean isError) {
        if (statusLabel != null) {
            logger.info("Updating status label: " + message);
            statusLabel.setText(message);
            statusLabel.setVisible(isError || !message.isEmpty());
        }
    }

    public void setWatchlistMovies(List<Movie> watchlistMovies) {
        this.watchlistMovies = FXCollections.observableList(watchlistMovies);
    }

    @Override
    public void onWatchlistChanged(List<Movie> updatedWatchlist) {
        logger.info("Watchlist updated. Number of movies: " + updatedWatchlist.size());
        setWatchlistMovies(updatedWatchlist);
        initializeWatchlistView();
        updateStatusLabel("Watchlist updated: " + updatedWatchlist.size() + " movies", false);
    }
}
