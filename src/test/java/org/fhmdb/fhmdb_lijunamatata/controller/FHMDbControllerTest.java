package org.fhmdb.fhmdb_lijunamatata.controller;

import org.fhmdb.fhmdb_lijunamatata.exceptions.DatabaseException;
import org.fhmdb.fhmdb_lijunamatata.exceptions.MovieApiException;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.fhmdb.fhmdb_lijunamatata.repositories.WatchlistRepository;
import org.fhmdb.fhmdb_lijunamatata.services.MovieService;
import org.fhmdb.fhmdb_lijunamatata.utils.ClickEventHandler;
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
    @DisplayName("sortMovies with filteredMovies should call movieService.sortMovies")
    public void sortMovies_with_filteredMovies_calls_moviesServiceSortMovies() {
        movieController.setFilteredMovies(initialMovies);
        movieController.sortMovies();
        verify(movieService).sortMovies(anyList(), anyBoolean());
    }

    @Test
    @DisplayName("sortMovies with null filteredMovies should not call movieService.sortMovies")
    public void sortMovies_with_null_filteredMovies_doesnt_call_moviesServiceSortMethod() {
        movieController.setFilteredMovies(null);
        movieController.sortMovies();
        verify(movieService, never()).sortMovies(anyList(), anyBoolean());
    }

    @Test
    @DisplayName("filterMovies should call movieService.fetchFilteredMovies")
    public void filterMovies_calls_movieServiceFilterMovies() throws MovieApiException {
        movieController.filterMovies();
        verify(movieService).fetchFilteredMovies(any(), any(), any(), any());
    }

    @Test
    @DisplayName("filterMovies calls fetchFilteredMovies with correct parameter types")
    public void filterMovies_calls_movieServiceFilterMovies_withCorrectParameterTypes() throws MovieApiException {
        movieController.setFilterElements("", Genre.DRAMA, 1990, 2.5);
        movieController.filterMovies();
        verify(movieService).fetchFilteredMovies(anyString(), any(Genre.class), anyInt(), anyDouble());
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
