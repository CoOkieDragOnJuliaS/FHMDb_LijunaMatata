package org.fhmdb.fhmdb_lijunamatata.controller;

import javafx.application.Platform;
import org.fhmdb.fhmdb_lijunamatata.database.MovieEntity;
import org.fhmdb.fhmdb_lijunamatata.exceptions.DatabaseException;
import org.fhmdb.fhmdb_lijunamatata.exceptions.MovieApiException;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.fhmdb.fhmdb_lijunamatata.repositories.WatchlistRepository;
import org.fhmdb.fhmdb_lijunamatata.services.MovieService;
import org.fhmdb.fhmdb_lijunamatata.state.SortContext;
import org.fhmdb.fhmdb_lijunamatata.utils.ClickEventHandler;
import org.fhmdb.fhmdb_lijunamatata.utils.JavaFxToolkitInitializer;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class FHMDbControllerTest {

    @Mock
    private MovieService movieService;

    private WatchlistRepository watchlistRepository;
    private FHMDbController movieController;

    List<Movie> initialMovies;
    Logger logger = Logger.getLogger(FHMDbControllerTest.class.getName());



    // Initialize JavaFX toolkit for JavaFX-based code to work in tests
    @BeforeAll
    public static void setupFx() {
        JavaFxToolkitInitializer.initialize();
    }



    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        initialMovies = Movie.initializeMoviesTestbase();

        watchlistRepository = spy(WatchlistRepository.getInstance());
        movieController = spy(new FHMDbController(watchlistRepository, movieService));
        movieController.setMovies(initialMovies);

        // Stub GUI-related methods to avoid real UI updates during testing
        doNothing().when(movieController).updateStatusLabel(anyString(), anyBoolean());
        doNothing().when(movieController).showPopup(anyString(), anyString());

        // Inject a custom ClickEventHandler to simulate add-to-watchlist logic
        Field handlerField = FHMDbController.class.getDeclaredField("onAddToWatchlistClicked");
        handlerField.setAccessible(true);
        ClickEventHandler<Movie> handler = movie -> {
            try {
                watchlistRepository.addToWatchlist(new MovieEntity(movie));
            } catch (DatabaseException e) {
                throw new RuntimeException(e);
            }
            movieController.updateStatusLabel("Added " + movie.getTitle() + " to Watchlist!", false);
            movieController.showPopup("Watchlist", "✅ " + movie.getTitle() + " added to watchlist!");
        };
        handlerField.set(movieController, handler);
    }

    @Test
    @DisplayName("updateMovieListView should not throw exception")
    public void updateMovieListView_doesnt_throw_exception() {
        // Ensures no exception is thrown during movie list update
        assertDoesNotThrow(() -> movieController.updateMovieListView("", "", 0, 0));
    }

    @Test
    @DisplayName("updateSortButtonText should not throw exception")
    public void updateSortButtonText_doesnt_throw_exception() {
        assertDoesNotThrow(() -> movieController.updateSortButtonText());
    }

    @Test
    @DisplayName("updateStatusLabel exception test")
    public void updateStatusLabel_doesnt_throw_exception() {
        assertDoesNotThrow(() -> movieController.updateStatusLabel("", false));
    }

    @Test
    @DisplayName("sortMovies should cycle through sort states and update UI")
    public void sortMovies_cycles_through_sort_states() {
        // Prepares test data: movies in B, A order
        Movie movieA = new Movie("1", "A Movie", List.of(Genre.ACTION), 2023, "", "", 120, List.of(), List.of(), List.of(), 8.0);
        Movie movieB = new Movie("2", "B Movie", List.of(Genre.DRAMA), 2023, "", "", 120, List.of(), List.of(), List.of(), 9.0);
        List<Movie> testMovies = List.of(movieB, movieA);

        movieController.setFilteredMovies(testMovies);
        SortContext sortContext = movieController.getSortContext();

        // Initial state: Unsorted
        Assertions.assertTrue(sortContext.isUnsorted());

        // First sort: Ascending
        movieController.sortMovies();
        List<Movie> sortedMovies = movieController.getFilteredMovies();
        Assertions.assertEquals("A Movie", sortedMovies.get(0).getTitle());
        Assertions.assertEquals("B Movie", sortedMovies.get(1).getTitle());
        verify(movieController).updateMovieListView(eq(""), eq(""), eq(0), eq(0.0));
        verify(movieController).updateSortButtonText();

        // Second sort: Descending
        movieController.sortMovies();
        sortedMovies = movieController.getFilteredMovies();
        Assertions.assertEquals("B Movie", sortedMovies.get(0).getTitle());
        Assertions.assertEquals("A Movie", sortedMovies.get(1).getTitle());
        verify(movieController, times(2)).updateSortButtonText();

        // Third sort: Back to Unsorted
        movieController.sortMovies();
        Assertions.assertTrue(sortContext.isUnsorted());
    }

    @Test
    @DisplayName("sortMovies with null filteredMovies should not change state")
    public void sortMovies_with_null_filteredMovies_does_nothing() {
        // No exception or state change when movie list is null
        movieController.setFilteredMovies(null);
        movieController.sortMovies();
        Assertions.assertTrue(movieController.getSortContext().isUnsorted());
        verify(movieController, never()).updateMovieListView(any(), any(), anyInt(), anyDouble());
    }

    @Test
    @DisplayName("filterMovies should call movieService.fetchFilteredMovies and handle API response")
    public void filterMovies_calls_movieServiceFilterMovies() throws MovieApiException {
        List<Movie> mockMovies = List.of(
                new Movie("1", "Test Movie", List.of(Genre.ACTION), 2023, "", "", 120, List.of(), List.of(), List.of(), 8.0)
        );
        when(movieService.fetchFilteredMovies(any(), any(), any(), any())).thenReturn(mockMovies);

        movieController.filterMovies();

        verify(movieService).fetchFilteredMovies(any(), any(), any(), any());
        verify(movieController).updateMovieListView(eq(""), eq(""), eq(0), eq(0.0));
    }

    @Test
    @DisplayName("filterMovies falls back to local filtering when API call fails")
    public void filterMovies_fallsBackToLocalFiltering() throws MovieApiException {
        when(movieService.fetchFilteredMovies(any(), any(), any(), any())).thenThrow(new MovieApiException("API Error"));
        when(movieService.filterMovies(any(), any(), any(), any(), any())).thenReturn(initialMovies);

        movieController.filterMovies();

        verify(movieService).fetchFilteredMovies(any(), any(), any(), any());
        verify(movieService).filterMovies(any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("onAddToWatchlistClicked triggers repository and status label")
    public void onAddToWatchlistClicked_methodCall() throws DatabaseException, NoSuchFieldException, IllegalAccessException {
        Movie dummyMovie = new Movie("test-id", "test-name", List.of(Genre.ACTION, Genre.DRAMA), 2023, "Description",
                "fake_url", 120, List.of("Director"), List.of("Writer"), List.of("Actor"), 1.0);

        // Use reflection to access and call the click handler
        Field field = FHMDbController.class.getDeclaredField("onAddToWatchlistClicked");
        field.setAccessible(true);
        ClickEventHandler<Movie> handler = (ClickEventHandler<Movie>) field.get(movieController);

        handler.onClick(dummyMovie);

        verify(movieController).updateStatusLabel("Added " + dummyMovie.getTitle() + " to Watchlist!", false);
        verify(watchlistRepository).addToWatchlist(argThat(entity -> entity.getApiId().equals(dummyMovie.getId())));
    }

    @Test
    @DisplayName("onWatchlistChanged updates status label correctly")
    void onWatchlistChanged_updatesStatusLabel() {
        List<Movie> updatedWatchlist = List.of(
                new Movie("id1", "Movie 1", List.of(), 2020, "", "", 0, List.of(), List.of(), List.of(), 0.0)
        );
        movieController.onWatchlistChanged(updatedWatchlist);
        verify(movieController).updateStatusLabel("Watchlist updated: 1 movies", false);
    }
}
