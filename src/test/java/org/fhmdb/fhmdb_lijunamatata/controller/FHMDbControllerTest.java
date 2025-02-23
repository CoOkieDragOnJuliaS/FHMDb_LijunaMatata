package org.fhmdb.fhmdb_lijunamatata.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.fhmdb.fhmdb_lijunamatata.controller.FHMDbController;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.fhmdb.fhmdb_lijunamatata.services.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class FHMDbControllerTest {

    /* For each field annotated with @Mock, Mockito creates a mock object of the declared type. The mock object is a
    dynamically generated class that implements the interface or extends the class you specify.
     */
    @Mock
    private MovieService movieService;

    /*For fields annotated with @InjectMocks, Mockito creates an instance of the class and then tries to inject the
    mock objects (created in the previous step) into the constructor, fields, or setter methods of that instance. The injection is based on type matching: if a field or constructor parameter has a type that matches the type of a mock object, Mockito will inject the mock into that field or parameter.
     */
    @InjectMocks
    private FHMDbController movieController;

    ObservableList<Movie> initialMovies;

    @BeforeEach
    public void setUp() {
        //BeforeSetup
        /* Without MockitoAnnotations.openMocks(this), your @Mock and @InjectMocks annotated fields would remain null
        . This would lead to NullPointerException errors when you try to use them in your tests.
         */
        MockitoAnnotations.openMocks(this);

        initialMovies = FXCollections.observableArrayList(Movie.initializeMovies());
        movieController.setMovies(initialMovies);

        //Update: Mocking the Buttons e.g. does not work because Mockito is not able to mock JavaFX private elements or final classes
        //Avoiding the UI elements and focusing on the testing of logic in the method is necessary
        //Another solution (maybe later purposes) is using WrapperClasses to wrap around the JavaFX elements needed to moc
    }

    @Test
    @DisplayName("Test updateMovieListView does not throw exceptions")
    public void testUpdateMovieListView() {
        assertDoesNotThrow(() -> movieController.updateMovieListView());
    }

    @Test
    @DisplayName("Test updateSortButtonText does not throw exceptions")
    public void testUpdateSortButtonText() {
        assertDoesNotThrow(() -> movieController.updateSortButtonText());
    }

    @Test
    @DisplayName("Test sortMovies with value filtered Movies calls movieService's sortMovies method")
    public void testSortMovies_givenFilteredMovies_calls_moviesServiceSortMovies() {
        movieController.setFilteredMovies(initialMovies);
        movieController.sortMovies();

        /*The verify() method in Mockito is used to confirm that a specific method was called on a mock object during
        the execution of your test. */
        verify(movieService).sortMovies(anyList(), anyBoolean());

    }


    @Test
    @DisplayName("Test sortMovies with no value for filtered Movies does not call movieService's sortMovies method")
    public void testSortMovies_givenNoFilteredMovies_doesNotCall_moviesServiceSortMovies() {
        movieController.setFilteredMovies(null);
        movieController.sortMovies();

        /*The verify() method in Mockito is used to confirm that a specific method was called on a mock object during
        the execution of your test. */
        verify(movieService, never()).sortMovies(anyList(), anyBoolean());

    }


    @Test
    @DisplayName("Test filterMovies calls movieService's filterMovies method with correct parameter types")
    public void testFilterMovies_calls_movieServiceFilterMovies_withCorrectParameterTypes() {
        movieController.filterMovies();

        /*The verify() method in Mockito is used to confirm that a specific method was called on a mock object during
        the execution of your test. */
        verify(movieService).filterMovies(anyList(), anyString(), any());

    }

}


