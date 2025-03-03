package org.fhmdb.fhmdb_lijunamatata.services;

import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import static org.junit.jupiter.api.Assertions.*;

public class MovieServiceTest {

    private MovieService movieService;
    private List<Movie> movies;

    @BeforeEach
    void setUp() {
        movieService = new MovieService();
        movies = Movie.initializeMovies();
    }

    @Test
    @DisplayName("Test sorting in ascending order")
    public void testSortMoviesAscending() {
        List<Movie> expectedMovies = List.of(
                movies.get(3), // Avatar
                movies.get(0), // Life Is Beautiful
                movies.get(2), // Puss in Boots
                movies.get(1), // The Usual Suspects
                movies.get(4)  // The Wolf of Wall Street
        );

        List<Movie> testMovies = new ArrayList<>(movies);
        List<Movie> sortedMovies = movieService.sortMovies(testMovies, true);
        assertEquals(expectedMovies, sortedMovies, "Movies should be sorted in ascending order by title");
    }

    @Test
    @DisplayName("Test sorting in descending order")
    public void testSortMoviesDescending() {
        List<Movie> expectedMovies = List.of(
                movies.get(4), // The Wolf of Wall Street
                movies.get(1), // The Usual Suspects
                movies.get(2), // Puss in Boots
                movies.get(0), // Life Is Beautiful
                movies.get(3)  // Avatar
        );

        List<Movie> testMovies = new ArrayList<>(movies);
        List<Movie> sortedMovies = movieService.sortMovies(testMovies, false);
        assertEquals(expectedMovies, sortedMovies, "Movies should be sorted in descending order by title");
    }

    @Test
    @DisplayName("Test filtering by genre DRAMA")
    void testFilterMoviesByGenre() {
        List<Movie> expectedMovies = List.of(
                movies.get(0), // Life Is Beautiful
                movies.get(1), // The Usual Suspects
                movies.get(3), // Avatar
                movies.get(4)  // The Wolf of Wall Street
        );

        List<Movie> testMovies = new ArrayList<>(movies);
        List<Movie> filteredMovies = movieService.filterMovies(testMovies, "", Genre.DRAMA);
        assertEquals(expectedMovies, filteredMovies, "Filtering by DRAMA should return correct movies");
    }

    @Test
    @DisplayName("Test filtering by search text 'Puss'")
    void testFilterMoviesBySearchText() {
        List<Movie> expectedMovies = List.of(movies.get(2)); // Puss in Boots

        List<Movie> testMovies = new ArrayList<>(movies);
        List<Movie> filteredMovies = movieService.filterMovies(testMovies, "Puss", null);
        assertEquals(expectedMovies, filteredMovies, "Filtering by 'Puss' should return 'Puss in Boots'");
    }

    @Test
    @DisplayName("Test filtering by search text 'life' and genre DRAMA")
    void testFilterMoviesBySearchTextAndGenre() {
        List<Movie> expectedMovies = List.of(
                movies.get(0), // Life Is Beautiful
                movies.get(4)  // The Wolf of Wall Street
        );

        List<Movie> testMovies = new ArrayList<>(movies);
        List<Movie> filteredMovies = movieService.filterMovies(testMovies, "life", Genre.DRAMA);
        assertEquals(expectedMovies, filteredMovies, "Filtering by 'life' and DRAMA should return correct movies");
    }

    @Test
    @DisplayName("Test filtering with empty search text")
    void testFilterMoviesWithEmptySearchText() {
        List<Movie> expectedMovies = new ArrayList<>(movies);

        List<Movie> testMovies = new ArrayList<>(movies);
        List<Movie> filteredMovies = movieService.filterMovies(testMovies, "", null);
        assertEquals(expectedMovies, filteredMovies, "All movies should be returned when search text is empty");
    }

    @Test
    @DisplayName("Test sorting movies with an empty list")
    void testSortMoviesWithEmptyList() {
        List<Movie> expectedMovies = new ArrayList<>();
        List<Movie> emptyMovies = new ArrayList<>();
        List<Movie> sortedMovies = movieService.sortMovies(emptyMovies, true);
        assertEquals(expectedMovies, sortedMovies, "Sorting an empty list should return an empty list");
    }
}