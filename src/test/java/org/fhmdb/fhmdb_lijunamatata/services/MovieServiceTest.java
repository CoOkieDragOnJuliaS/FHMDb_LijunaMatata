package org.fhmdb.fhmdb_lijunamatata.services;

import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
        List<Movie> sortedMovies = movieService.sortMovies(movies, true);
        assertEquals("Avatar", sortedMovies.get(0).getTitle());
        assertEquals("The Wolf of Wall Street", sortedMovies.get(sortedMovies.size() - 1).getTitle());
    }

    @Test
    @DisplayName("Test sorting in descending order")
    public void testSortMoviesDescending() {
        List<Movie> sortedMovies = movieService.sortMovies(movies, false);
        assertEquals("The Wolf of Wall Street", sortedMovies.get(0).getTitle());
        assertEquals("Avatar", sortedMovies.get(sortedMovies.size() - 1).getTitle());
    }

    @Test
    @DisplayName("Test Filtering Movies by Genre")
    void testFilterMoviesByGenre() {
        List<Movie> filteredMovies = movieService.filterMovies(movies, "", Genre.DRAMA);
        assertEquals(4, filteredMovies.size(),
                "The number of filtered movies with DRAMA genre should be 4.");
    }

    @Test
    @DisplayName("Test Filtering Movies by Search Text")
    void testFilterMoviesBySearchText() {
        List<Movie> filteredMovies = movieService.filterMovies(movies, "Puss", null);
        assertEquals(1, filteredMovies.size(),
                "Only 'Puss in Boots' should match the search text 'Puss'.");
    }

    @Test
    @DisplayName("Test Filtering Movies by Search Text and Genre")
    void testFilterMoviesBySearchTextAndGenre() {
        List<Movie> filteredMovies = movieService.filterMovies(movies, "life", Genre.DRAMA);
        assertEquals(2, filteredMovies.size(),
                "Only 'Life Is Beautiful' and 'The Wolf of Wall Steet' should match the search text 'life' and DRAMA genre.");
    }

    @Test
    @DisplayName("Test Filtering Movies with Empty Search Text")
    void testFilterMoviesWithEmptySearchText() {
        List<Movie> filteredMovies = movieService.filterMovies(movies, "", null);
        assertEquals(5, filteredMovies.size(),
                "All movies should be included when search text is empty.");
    }

    @Test
    @DisplayName("Test Filtering Movies with Empty Genre")
    void testFilterMoviesWithEmptyGenre() {
        List<Movie> filteredMovies = movieService.filterMovies(movies, "", null);
        assertEquals(5, filteredMovies.size(),
                "All movies should be included when no genre is selected.");
    }

    @Test
    @DisplayName("Test Filtering Movies with Partial Search Text (Uppercase)")
    void testFilterMoviesWithPartialSearchTextUppercase() {
        List<Movie> filteredMovies = movieService.filterMovies(movies, "puss", null);
        assertEquals(1, filteredMovies.size(),
                "Only 'Puss in Boots' should match the partial search text 'puss' ignoring case.");
    }

    @Test
    @DisplayName("Test Filtering Movies with Search Text Not Found")
    void testFilterMoviesWithSearchTextNotFound() {
        List<Movie> filteredMovies = movieService.filterMovies(movies, "NotInAnyTitle", null);
        assertEquals(0, filteredMovies.size(),
                "No movies should be returned when the search text doesn't match any title.");
    }

    @Test
    @DisplayName("Test Filtering Movies with Multiple Genres")
    void testFilterMoviesWithMultipleGenres() {
        List<Movie> filteredMovies = movieService.filterMovies(movies, "", Genre.COMEDY);
        assertEquals(1, filteredMovies.size(),
                "Only 'Puss in Boots' should match the Comedy genre.");
    }


    @Test
    @DisplayName("Test Sorting Movies with Empty List")
    void testSortMoviesWithEmptyList() {
        List<Movie> emptyMovies = new ArrayList<>();
        List<Movie> sortedMovies = movieService.sortMovies(emptyMovies, true);
        assertTrue(sortedMovies.isEmpty(), "The sorted list should be empty when no movies are available.");
    }

    @Test
    @DisplayName("Test Filtering Movies by Exact Search Text")
    void testFilterMoviesByExactSearchText() {
        List<Movie> filteredMovies = movieService.filterMovies(movies, "Avatar", null);
        assertEquals(1, filteredMovies.size(),
                "Only 'Avatar' should match the exact search text 'Avatar'.");
    }
}
