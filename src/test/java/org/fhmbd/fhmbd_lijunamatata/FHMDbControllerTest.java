package org.fhmbd.fhmbd_lijunamatata;

import org.fhmdb.fhmdb_lijunamatata.controller.FHMDbController;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;
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
    @DisplayName("Test initialize Movies: Movies not empty")
    public void testInitializeMovies_notEmpty() {
        assertFalse(this.movies.isEmpty());
    }

    @Test
    @DisplayName("Test initialize Movies: First Movie has expected title")
    public void testInitializeMovies_firstMovieTitle_equals_expected() {
        String expectedTitle = "Life Is Beautiful";
        assertEquals(expectedTitle, this.movies.get(0).getTitle());
    }

    @Test
    @DisplayName("Test initialize Movies: First Movie has expected description")
    public void testInitializeMovies_firstMovieDescription_equals_expected() {
        String expectedDescription = "When an open-minded Jewish librarian and his son become victims of" +
                " the Holocaust, he uses a perfect mixture of will, humor, and imagination to protect his son from " +
                "the dangers around their camp.";
        assertEquals(expectedDescription, this.movies.get(0).getDescription());
    }

    @Test
    @DisplayName("Test initialize Movies: First Movie has expected genres")
    public void testInitializeMovies_firstMovieGenres_equals_expected() {
        List<Genre> expectedGenres = List.of(Genre.DRAMA, Genre.ROMANCE);
        assertEquals(expectedGenres, this.movies.get(0).getGenres());
    }

    @Test
    @DisplayName("Test updateMovieListView does not throw exceptions")
    public void testUpdateMovieListView() {
        assertDoesNotThrow(() -> movieController.updateMovieListView());
    }
}


