package org.fhmdb.fhmdb_lijunamatata.controller;

import javafx.application.Platform;
import javafx.scene.control.ListView;
import org.fhmdb.fhmdb_lijunamatata.database.MovieEntity;
import org.fhmdb.fhmdb_lijunamatata.exceptions.DatabaseException;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.fhmdb.fhmdb_lijunamatata.repositories.WatchlistRepository;
import org.fhmdb.fhmdb_lijunamatata.utils.ClickEventHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class WatchlistControllerTest {

    private WatchlistRepository watchlistRepository;
    private WatchlistController watchlistController;
    List<Movie> initialWatchlistMovies;
    Logger logger = Logger.getLogger(WatchlistControllerTest.class.getName());

    @BeforeAll
    public static void initToolkit() {
        // Initialize JavaFX Toolkit once for all tests (needed to create JavaFX controls)
        Platform.startup(() -> {});
    }

    @BeforeEach
    public void setUp() throws DatabaseException, NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        initialWatchlistMovies = Movie.initializeMoviesTestbase();

        try {
            watchlistRepository = new WatchlistRepository();
        } catch (DatabaseException e) {
            logger.severe(e.getMessage());
        }

        watchlistRepository = spy(watchlistRepository);
        watchlistController = spy(new WatchlistController(watchlistRepository));

        // Use reflection to set the private 'watchlistView' field with a new ListView<Movie>
        Field watchlistViewField = WatchlistController.class.getDeclaredField("watchlistView");
        watchlistViewField.setAccessible(true);
        watchlistViewField.set(watchlistController, new ListView<Movie>());

        // Set initial movies in the controller
        watchlistController.setWatchlistMovies(initialWatchlistMovies);

        // Stub out updateStatusLabel to avoid actual UI updates during tests
        doNothing().when(watchlistController).updateStatusLabel(anyString(), anyBoolean());

        initializeOnRemoveFromWatchlistField();
        watchlistController.initializeClickHandlers();

        // Stub methods that interact with DB or UI to do nothing
        doNothing().when(watchlistController).refreshWatchlist();
        doNothing().when(watchlistRepository).removeFromWatchlist(anyString());
    }

    /**
     * Sets up the onRemoveFromWatchlistClicked field with a stub handler
     * so we can verify interactions in tests.
     */
    private void initializeOnRemoveFromWatchlistField() {
        try {
            Field field = WatchlistController.class.getDeclaredField("onRemoveFromWatchlistClicked");
            field.setAccessible(true);
            ClickEventHandler<Movie> handler = movie -> watchlistController.updateStatusLabel("Removed " + movie.getTitle() + " from Watchlist!", false);
            field.set(watchlistController, handler);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }

    @Test
    @DisplayName("updateStatusLabel should not throw exception")
    public void updateStatusLabel_doesnt_throw_exception() {
        assertDoesNotThrow(() -> watchlistController.updateStatusLabel("", false));
    }

    @Test
    @DisplayName("onRemoveFromWatchlistClicked triggers repository and status label")
    public void onRemoveFromWatchlistClicked_methodCall() throws DatabaseException {
        Movie dummyMovie = new Movie("test-id", "test-name", List.of(Genre.ACTION, Genre.DRAMA), 2023, "Description",
                "fake_url", 120, List.of("Director"), List.of("Writer"), List.of("Actor"), 1.0);

        MovieEntity dummyEntity = new MovieEntity(dummyMovie);
        try {
            Field field = WatchlistController.class.getDeclaredField("onRemoveFromWatchlistClicked");
            field.setAccessible(true);
            ClickEventHandler<Movie> handler = (ClickEventHandler<Movie>) field.get(watchlistController);
            handler.onClick(dummyMovie);
        } catch (Exception e) {
            throw new AssertionError(e);
        }

        verify(watchlistController).updateStatusLabel("Removed " + dummyMovie.getTitle() + " from Watchlist!", false);
        verify(watchlistRepository).removeFromWatchlist(dummyEntity.getApiId());
    }

    @Test
    @DisplayName("onWatchlistChanged updates status label correctly")
    public void onWatchlistChanged_updatesStatusLabel() {
        List<Movie> updatedWatchlist = List.of(
                new Movie("id1", "Movie 1", List.of(), 2020, "", "", 0, List.of(), List.of(), List.of(), 0.0)
        );
        watchlistController.onWatchlistChanged(updatedWatchlist);
        verify(watchlistController).updateStatusLabel("Watchlist updated: 1 movies", false);
    }
}
