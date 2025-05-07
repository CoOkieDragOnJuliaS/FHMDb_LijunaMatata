package org.fhmdb.fhmdb_lijunamatata.controller;

import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.fhmdb.fhmdb_lijunamatata.services.MovieService;
import org.fhmdb.fhmdb_lijunamatata.utils.ClickEventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
    mock objects (created in the previous step) into the constructor, fields, or setter methods of that instance.
    The injection is based on type matching: if a field or constructor parameter has a type that matches the type of
     a mock object, Mockito will inject the mock into that field or parameter.
     */
    @InjectMocks
    private FHMDbController movieController;

    List<Movie> initialMovies;
    //logger to log and debug during testing
    Logger logger = Logger.getLogger(FHMDbControllerTest.class.getName());

    @BeforeEach
    public void setUp() {
        //BeforeSetup
        /* Without MockitoAnnotations.openMocks(this), your @Mock and @InjectMocks annotated fields would remain null
        . This would lead to NullPointerException errors when you try to use them in your tests.
         */
        MockitoAnnotations.openMocks(this);

        //Testing with the initialized movies from testbaste in Movie class
        //The controller does not know about the MovieAPI
        initialMovies = Movie.initializeMoviesTestbase();
        movieController = new FHMDbController();
        //Creating a spy (mocked object) before making calls to the controller itself
        movieController = spy(movieController);

        movieController.setMovies(initialMovies);

        //Try to mock updateStatusLabel to isolate tests and interaction with JavaFX UI
        doNothing().when(movieController).updateStatusLabel(anyString(), anyBoolean());

        initializeOnAddToWatchlistField();
        initializeOnRemoveToWatchlistField();
        //Update: Mocking the Buttons e.g. does not work because Mockito is not able to mock JavaFX private elements or final classes
        //Avoiding the UI elements and focusing on the testing of logic in the method is necessary
        //Another solution (maybe later purposes) is using WrapperClasses to wrap around the JavaFX elements needed to moc
    }

    /**
     * Initializes the ClickEventHandler for adding a movie to the watchlist.
     *  * <p>
     *  * This method creates and assigns a {@link ClickEventHandler} lambda to the
     *  * {@code onAddToWatchlistClicked} field. The handler updates the status label
     *  * for testing purposes.
     *  * </p>
     *  * <p>
     *  * This method is a mirrored test-initialization which is normally used
     *  * in the application itself when the correct button is clicked.
     *  * </p>
     */
    private void initializeOnAddToWatchlistField() {
        try {
            Field addToWatchlistField = FHMDbController.class.getDeclaredField("onAddToWatchlistClicked");
            addToWatchlistField.setAccessible(true);

            ClickEventHandler<Movie> testHandler = movie -> {
                movieController.updateStatusLabel("Added " + movie.getTitle() + " to Watchlist!", false);
            };
            addToWatchlistField.set(movieController, testHandler);
        }catch(NoSuchFieldException | IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Initializes the ClickEventHandler for removing a movie from the watchlist.
     *  * <p>
     *  * This method creates and assigns a {@link ClickEventHandler} lambda to the
     *  * {@code onRemoveFromWatchlistClicked} field. The handler updates the status label
     *  * for testing purposes.
     *  * </p>
     */
    private void initializeOnRemoveToWatchlistField() {
        try {
            Field removeFromWatchlistField = FHMDbController.class.getDeclaredField("onRemoveFromWatchlistClicked");
            removeFromWatchlistField.setAccessible(true);

            ClickEventHandler<Movie> testHandler = movie -> {
                movieController.updateStatusLabel("Removed " + movie.getTitle() + " from Watchlist!", false);
            };
            removeFromWatchlistField.set(movieController, testHandler);
        }catch(NoSuchFieldException | IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }

    @Test
    @DisplayName("updateMovieListView should not throw exception")
    public void updateMovieListView_doesnt_throw_exception() {
        assertDoesNotThrow(() -> movieController.updateMovieListView());
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
    @DisplayName("sortMovies with value in filtered Movies should call movieService's sortMovies method")
    public void sortMovies_with_filteredMovies_calls_moviesServiceSortMovies() {
        movieController.setFilteredMovies(initialMovies);
        movieController.sortMovies();

        /*The verify() method in Mockito is used to confirm that a specific method was called on a mock object during
        the execution of your test. */
        verify(movieService).sortMovies(anyList(), anyBoolean());
    }


    @Test
    @DisplayName("sortMovies with null for filtered Movies should not call movieService's sortMovies method")
    public void sortMovies_with_null_filteredMovies_doesnt_call_moviesServiceSortMethod() {
        movieController.setFilteredMovies(null);
        movieController.sortMovies();

        /*The verify() method in Mockito is used to confirm that a specific method was called on a mock object during
        the execution of your test. */
        verify(movieService, never()).sortMovies(anyList(), anyBoolean());
    }

    @Test
    @DisplayName("filterMovies should call movieService's filterMovies method")
    public void filterMovies_calls_movieServiceFilterMovies() {
        movieController.filterMovies();

        /*The verify() method in Mockito is used to confirm that a specific method was called on a mock object during
        the execution of your test. */
        try {
            verify(movieService).fetchFilteredMovies(any(), any(), any(), any());
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }


    @Test
    @DisplayName("Test filterMovies should call movieService's filterMovies method with correct parameter types")
    public void filterMovies_calls_movieServiceFilterMovies_withCorrectParameterTypes() {
        movieController.setFilterElements("", Genre.DRAMA, 1990, 2.5);
        movieController.filterMovies();
        /*The verify() method in Mockito is used to confirm that a specific method was called on a mock object during
        the execution of your test. */
        try {
            verify(movieService).fetchFilteredMovies(anyString(), any(Genre.class), anyInt(), anyDouble());
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

    @Test
    @DisplayName("Testing of onAddToWatchlistClicked method")
    public void onAddToWatchlistClicked_methodCall() {
        Movie dummyMovie = new Movie();
        try {
            //A field with the name onAddToWatchlistClicked is searched for and stored in the Field variable
            Field addToWatchlistField = FHMDbController.class.getDeclaredField("onAddToWatchlistClicked");
            //setAccessible(true) indicates that, even though the method is private in the Controller, it makes it testable
            addToWatchlistField.setAccessible(true);
            //get the current/actual value of the field declared above from the movieController instance
            Object onAddToWatchlistClicked = addToWatchlistField.get(movieController);

            dummyMovie = new Movie("test-id", "test-name", List.of(Genre.ACTION, Genre.DRAMA), 2023, "Ein neuer Film",
                    "fake_url", 120, List.of("Director Name"), List.of("Writer Name"), List.of("Main Cast Name"), 1.0);

            //Execution of ClickEventHandler and simulating a UI handled click
            if(onAddToWatchlistClicked instanceof ClickEventHandler) {
                ((ClickEventHandler<Movie>) onAddToWatchlistClicked).onClick(dummyMovie);
            }else {
                throw new AssertionError("onAddToWatchlistClicked() is not a ClickEventHandler");
            }
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            logger.severe(exception.getMessage());
        }

        //verify updateStatusLabel was called
        verify(movieController).updateStatusLabel("Added " + dummyMovie.getTitle() + " to Watchlist!", false);

        //TODO: Verify data-layer method is called
    }


    @Test
    @DisplayName("Testing of onRemoveFromWatchlistClicked method")
    public void onRemoveFromWatchlistClicked_methodCall() {
        Movie dummyMovie = new Movie();
        try {
            Field removeFromWatchlistField = FHMDbController.class.getDeclaredField("onRemoveFromWatchlistClicked");
            removeFromWatchlistField.setAccessible(true);
            Object onRemoveFromWatchlistClicked = removeFromWatchlistField.get(movieController);

            dummyMovie = new Movie("test-id", "test-name", List.of(Genre.ACTION, Genre.DRAMA), 2023, "Ein neuer Film",
                    "fake_url", 120, List.of("Director Name"), List.of("Writer Name"), List.of("Main Cast Name"), 1.0);

            //Execution of ClickEventHandler and simulating a UI handled click
            if(onRemoveFromWatchlistClicked instanceof ClickEventHandler) {
                ((ClickEventHandler<Movie>) onRemoveFromWatchlistClicked).onClick(dummyMovie);
            }else {
                throw new AssertionError("onRemoveFromWatchlistClicked() is not a ClickEventHandler");
            }
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            logger.severe(exception.getMessage());
        }

        //verify updateStatusLabel was called
        verify(movieController).updateStatusLabel("Removed " + dummyMovie.getTitle() + " from Watchlist!", false);

        //TODO: Verify data-layer method is called
    }
}


