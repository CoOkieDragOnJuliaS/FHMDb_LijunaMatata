package org.fhmbd.fhmbd_lijunamatata.controller;

import org.fhmdb.fhmdb_lijunamatata.controller.FHMDbController;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FHMDbControllerTest {

    private FHMDbController movieController;
    private List<Movie> movies;

    @BeforeEach
    public void setUp() {
        //BeforeSetup
        this.movieController = new FHMDbController();
        this.movies = Movie.initializeMovies();
        movieController.setMovies(this.movies);
        //Update: Mocking the Buttons e.g. does not work because Mockito is not able to mock JavaFX private elements or final classes
        //Avoiding the UI elements and focusing on the testing of logic in the method is necessary
        //Another solution (maybe later purposes) is using WrapperClasses to wrap around the JavaFX elements needed to mock
    }

    @Test
    @DisplayName("Test updateMovieListView does not throw exceptions")
    public void testUpdateMovieListView() {
        assertDoesNotThrow(() -> movieController.updateMovieListView());
    }
}


