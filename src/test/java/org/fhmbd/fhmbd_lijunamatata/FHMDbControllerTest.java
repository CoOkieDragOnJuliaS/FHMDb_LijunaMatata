package org.fhmbd.fhmbd_lijunamatata;

import org.fhmdb.fhmdb_lijunamatata.controller.FHMDbController;
import org.fhmdb.fhmdb_lijunamatata.Genre;
import org.fhmdb.fhmdb_lijunamatata.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public class FHMDbControllerTest {

    //TODO: Testing (TDD Principle)
    /*
    - Testing of Initializing movies
    - Testing of Filtering by Genre
    - Testing of Filtering by Query (positive Entry)
    - Testing Filtering by Query (Empty String)
    - Testing if List is correct with Filtering and without Filtering Genre
    - Sorting Ascending
    - Sorting Descending

    //Don't forget Documentation JavaDoc!!!
     */
    private FHMDbController controller;
    private List<Movie> movies;

    @BeforeEach
    public void setUp() {
        //BeforSetup
        controller = new FHMDbController();
        movies = Movie.initializeMovies();
    }

    /*
    --> this is an example test
    @Test
    public void test() {
        assertTrue(true);
    }
    */

    @Test
    @DisplayName("Test initialize Movies: Movies not empty")
    public void testInitializeMovies_notEmpty() {
        assertFalse(movies.isEmpty());
    }

    @Test
    @DisplayName("Test initialize Movies: First Movie has expected title")
    public void testInitializeMovies_firstMovieTitle_equals_expected() {
        String expectedTitle = "Life Is Beautiful";
        assertEquals(expectedTitle, movies.get(0).getTitle());
    }

    @Test
    @DisplayName("Test initialize Movies: First Movie has expected description")
    public void testInitializeMovies_firstMovieDescription_equals_expected() {
        String expectedDescription = "When an open-minded Jewish librarian and his son become victims of" +
                " the Holocaust, he uses a perfect mixture of will, humor, and imagination to protect his son from " +
                "the dangers around their camp.";
        assertEquals(expectedDescription, movies.get(0).getDescription());
    }

    @Test
    @DisplayName("Test initialize Movies: First Movie has expected genres")
    public void testInitializeMovies_firstMovieGenres_equals_expected() {
        List<Genre> expectedGenres = List.of(Genre.DRAMA, Genre.ROMANCE);
        assertEquals(expectedGenres, movies.get(0).getGenres());
    }

    @Test
    @DisplayName("Test sorting in ascending order")
    public void testSortAscending() {
        movies.sort(Comparator.comparing(Movie::getTitle));
        assertEquals("Avatar", movies.get(0).getTitle());
        assertEquals("The Wolf of Wall Street", movies.get(movies.size() - 1).getTitle());
    }

    @Test
    @DisplayName("Test sorting in descending order")
    public void testSortDescending() {
        movies.sort(Comparator.comparing(Movie::getTitle).reversed());
        assertEquals("The Wolf of Wall Street", movies.get(0).getTitle());
        assertEquals("Avatar", movies.get(movies.size() - 1).getTitle());
    }

    @Test
    @DisplayName("Test updateMovieListView does not throw exceptions")
    public void testUpdateMovieListView() {
        assertDoesNotThrow(() -> controller.updateMovieListView());
    }
}
