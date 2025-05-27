package org.fhmdb.fhmdb_lijunamatata.controller;

import org.fhmdb.fhmdb_lijunamatata.exceptions.DatabaseException;
import org.fhmdb.fhmdb_lijunamatata.exceptions.MovieApiException;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.fhmdb.fhmdb_lijunamatata.repositories.WatchlistRepository;
import org.fhmdb.fhmdb_lijunamatata.services.MovieService;
import org.fhmdb.fhmdb_lijunamatata.state.SortContext;
import org.fhmdb.fhmdb_lijunamatata.utils.ClickEventHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        initialMovies = Movie.initializeMoviesTestbase();
        try {
            watchlistRepository = WatchlistRepository.getInstance();
        } catch (DatabaseException e) {
            logger.severe(e.getMessage());
        }

        watchlistRepository = spy(watchlistRepository);
        movieController = spy(new FHMDbController(watchlistRepository, movieService));
        movieController.setMovies(initialMovies);
        doNothing().when(movieController).updateStatusLabel(anyString(), anyBoolean());

        initializeOnAddToWatchlistField();
        movieController.initializeClickHandlers();

    }


    private void initializeOnAddToWatchlistField() {
        try {
            Field field = FHMDbController.class.getDeclaredField("onAddToWatchlistClicked");
            field.setAccessible(true);
            ClickEventHandler<Movie> handler = movie -> movieController.updateStatusLabel("Added " + movie.getTitle() + " to Watchlist!", false);
            field.set(movieController, handler);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }

    @Test
    @DisplayName("updateMovieListView should not throw exception")
    public void updateMovieListView_doesnt_throw_exception() {
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
        // Given
        Movie movieA = new Movie("1", "A Movie", List.of(Genre.ACTION), 2023, "", "", 120, List.of(), List.of(), List.of(), 8.0);
        Movie movieB = new Movie("2", "B Movie", List.of(Genre.DRAMA), 2023, "", "", 120, List.of(), List.of(), List.of(), 9.0);
        List<Movie> testMovies = List.of(movieB, movieA); // Initial order: B, A
        
        movieController.setFilteredMovies(testMovies);
        SortContext sortContext = movieController.getSortContext();
        
        // Initial state should be unsorted
        Assertions.assertTrue(sortContext.isUnsorted());
        
        // When - First click (should sort ascending - A, B)
        movieController.sortMovies();
        
        // Then - Verify movies are sorted in ascending order
        List<Movie> sortedMovies = movieController.getFilteredMovies();
        Assertions.assertEquals("A Movie", sortedMovies.get(0).getTitle());
        Assertions.assertEquals("B Movie", sortedMovies.get(1).getTitle());
        
        // Verify UI was updated
        verify(movieController).updateMovieListView(eq(""), eq(""), eq(0), eq(0.0));
        verify(movieController).updateSortButtonText();
        
        // When - Second click (should sort descending - B, A)
        movieController.sortMovies();
        
        // Then - Verify movies are sorted in descending order
        sortedMovies = movieController.getFilteredMovies();
        Assertions.assertEquals("B Movie", sortedMovies.get(0).getTitle());
        Assertions.assertEquals("A Movie", sortedMovies.get(1).getTitle());
        
        // Verify button text was updated
        verify(movieController, times(2)).updateSortButtonText();
        
        // When - Third click (should go back to unsorted - B, A)
        movieController.sortMovies();
        
        // Then - Should be back to unsorted state with original order
        Assertions.assertTrue(sortContext.isUnsorted());
        sortedMovies = movieController.getFilteredMovies();
        Assertions.assertEquals("B Movie", sortedMovies.get(0).getTitle());
        Assertions.assertEquals("A Movie", sortedMovies.get(1).getTitle());
    }
    
    @Test
    @DisplayName("sortMovies with null filteredMovies should not change state")
    public void sortMovies_with_null_filteredMovies_does_nothing() {
        // Given
        movieController.setFilteredMovies(null);
        
        // When
        movieController.sortMovies();
        
        // Then - No state change should occur
        Assertions.assertTrue(movieController.getSortContext().isUnsorted());
        verify(movieController, never()).updateMovieListView(any(), any(), anyInt(), anyDouble());
    }

    @Test
    @DisplayName("filterMovies should call movieService.fetchFilteredMovies and handle API response")
    public void filterMovies_calls_movieServiceFilterMovies() throws MovieApiException {
        // Given
        List<Movie> mockMovies = List.of(
            new Movie("1", "Test Movie", List.of(Genre.ACTION), 2023, "", "", 120, List.of(), List.of(), List.of(), 8.0)
        );
        when(movieService.fetchFilteredMovies(any(), any(), any(), any()))
            .thenReturn(mockMovies);
        
        // When
        movieController.filterMovies();
        
        // Then
        verify(movieService).fetchFilteredMovies(any(), any(), any(), any());
        verify(movieController).updateMovieListView(eq(""), eq(""), eq(0), eq(0.0));
    }

    @Test
    @DisplayName("filterMovies falls back to local filtering when API call fails")
    public void filterMovies_fallsBackToLocalFiltering() throws MovieApiException {
        // Given
        when(movieService.fetchFilteredMovies(any(), any(), any(), any()))
            .thenThrow(new MovieApiException("API Error"));
        when(movieService.filterMovies(any(), any(), any(), any(), any()))
            .thenReturn(initialMovies);
        
        // When
        movieController.filterMovies();
        
        // Then
        verify(movieService).fetchFilteredMovies(any(), any(), any(), any());
        verify(movieService).filterMovies(any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("onAddToWatchlistClicked triggers repository and status label")
    public void onAddToWatchlistClicked_methodCall() throws DatabaseException {
        Movie dummyMovie = new Movie("test-id", "test-name", List.of(Genre.ACTION, Genre.DRAMA), 2023, "Description",
                "fake_url", 120, List.of("Director"), List.of("Writer"), List.of("Actor"), 1.0);

        try {
            Field field = FHMDbController.class.getDeclaredField("onAddToWatchlistClicked");
            field.setAccessible(true);
            ClickEventHandler<Movie> handler = (ClickEventHandler<Movie>) field.get(movieController);
            handler.onClick(dummyMovie);
        } catch (Exception e) {
            throw new AssertionError(e);
        }

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
