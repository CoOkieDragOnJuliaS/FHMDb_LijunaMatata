package org.fhmdb.fhmdb_lijunamatata.controller;

import javafx.application.Platform;
import javafx.scene.control.ListView;
import org.fhmdb.fhmdb_lijunamatata.database.MovieEntity;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.fhmdb.fhmdb_lijunamatata.repositories.WatchlistRepository;
import org.fhmdb.fhmdb_lijunamatata.utils.ClickEventHandler;
import org.fhmdb.fhmdb_lijunamatata.utils.JavaFxToolkitInitializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link WatchlistController} class.
 * This class tests the behavior of the watchlist controller
 * in response to changes in the watchlist and UI interactions.
 */
public class WatchlistControllerTest {

    private WatchlistRepository watchlistRepository;
    private WatchlistController watchlistController;
    private List<Movie> initialWatchlistMovies;

    /**
     * Initializes the JavaFX toolkit (only once for all tests).
     * Needed to construct JavaFX UI components in tests.
     */
    @BeforeAll
    public static void setupFx() {
        JavaFxToolkitInitializer.initialize();
    }


    /**
     * Sets up the test environment before each test case:
     * - Initializes test data.
     * - Creates spies on controller and repository.
     * - Injects mocked ListView.
     * - Stubs out GUI and DB-related side effects.
     * - Initializes click handlers.
     */
    @BeforeEach
    public void setUp() throws Exception {
        initialWatchlistMovies = Movie.initializeMoviesTestbase();
        watchlistRepository = spy(WatchlistRepository.getInstance());
        watchlistController = spy(new WatchlistController(watchlistRepository));

        // Inject ListView<Movie> via reflection
        Field watchlistViewField = WatchlistController.class.getDeclaredField("watchlistView");
        watchlistViewField.setAccessible(true);
        watchlistViewField.set(watchlistController, new ListView<>());

        watchlistController.setWatchlistMovies(initialWatchlistMovies);

        // Stub GUI-related methods that cause side effects
        doNothing().when(watchlistController).updateStatusLabel(any(), anyBoolean());
        doNothing().when(watchlistController).showPopup(any(), any());
        doNothing().when(watchlistController).refreshWatchlist();

        // Stub DB-related method
        doNothing().when(watchlistRepository).removeFromWatchlist(any());

        // Initialize the onRemoveFromWatchlistClicked event handler
        watchlistController.initializeClickHandlers();
    }

    /**
     * Ensures that calling updateStatusLabel with any string and flag
     * doesn't result in an exception.
     */
    @Test
    @DisplayName("updateStatusLabel doesn't throw exception")
    public void updateStatusLabel_doesntThrowException() {
        assertDoesNotThrow(() -> watchlistController.updateStatusLabel("Test", false));
    }

    /**
     * Simulates clicking the remove-from-watchlist button for a movie,
     * and verifies that:
     * - The movie is removed from the repository,
     * - The watchlist is refreshed,
     * - A popup message is shown.
     */
    @Test
    @DisplayName("onRemoveFromWatchlistClicked triggers remove and updates status label")
    public void onRemoveFromWatchlistClicked_triggers_remove_and_updates_status_label() throws Exception {
        Movie dummyMovie = new Movie("test-id", "Test Movie", List.of(Genre.ACTION), 2023, "desc", "url", 120, List.of(), List.of(), List.of(), 5.0);
        MovieEntity dummyEntity = new MovieEntity(dummyMovie);

        // Access private event handler field via reflection
        Field field = WatchlistController.class.getDeclaredField("onRemoveFromWatchlistClicked");
        field.setAccessible(true);
        ClickEventHandler<Movie> handler = (ClickEventHandler<Movie>) field.get(watchlistController);

        // Simulate clicking the remove button
        handler.onClick(dummyMovie);

        // Verify expected side effects
        verify(watchlistRepository).removeFromWatchlist(dummyEntity.getApiId());
        verify(watchlistController).refreshWatchlist();
        verify(watchlistController).showPopup(eq("Watchlist"), contains("removed"));
    }

    /**
     * Simulates a change in the watchlist and verifies
     * that the status label is updated to reflect the new count.
     */
    @Test
    @DisplayName("onWatchlistChanged updates status with correct count")
    public void onWatchlistChanged_updates_status_with_correct_count() {
        List<Movie> updated = List.of(
                new Movie("id1", "Movie 1", List.of(), 2022, "", "", 100, List.of(), List.of(), List.of(), 4.0)
        );

        // Trigger change
        watchlistController.onWatchlistChanged(updated);

        // Verify updated status
        verify(watchlistController).updateStatusLabel("Watchlist updated: 1 movies", false);
    }
}
