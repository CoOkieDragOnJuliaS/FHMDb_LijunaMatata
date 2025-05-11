package org.fhmdb.fhmdb_lijunamatata.controller;

import org.fhmdb.fhmdb_lijunamatata.database.MovieEntity;
import org.fhmdb.fhmdb_lijunamatata.exceptions.DatabaseException;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.fhmdb.fhmdb_lijunamatata.repositories.WatchlistRepository;
import org.fhmdb.fhmdb_lijunamatata.utils.ClickEventHandler;
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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        initialWatchlistMovies = Movie.initializeMoviesTestbase();

        try {
            watchlistRepository = new WatchlistRepository();
        } catch (DatabaseException e) {
            logger.severe(e.getMessage());
        }

        watchlistRepository = spy(watchlistRepository);
        watchlistController = spy(new WatchlistController(watchlistRepository));
        watchlistController.setWatchlistMovies(initialWatchlistMovies);

        doNothing().when(watchlistController).updateStatusLabel(anyString(), anyBoolean());

        initializeOnRemoveFromWatchlistField();
        watchlistController.initializeClickHandlers();

        doNothing().when(watchlistController).refreshWatchlist();
        doNothing().when(watchlistRepository).removeFromWatchlist(anyString());
    }

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
    public void onRemoveFromWatchlistClicked_methodCall() {
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
}
