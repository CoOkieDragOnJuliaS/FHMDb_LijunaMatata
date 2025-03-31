package org.fhmdb.fhmdb_lijunamatata.services;

import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MovieServiceTest {

    private MovieService movieService;
    private List<Movie> movies;

    @BeforeEach
    void setUp() {
        movieService = new MovieService();
        movies = Movie.initializeMoviesTestbase();
    }

    @Test
    @DisplayName("Test sorting in ascending order")
    public void testSortMoviesAscending() {
        List<Movie> expectedMovies = List.of(
                movies.get(8),  // Amélie
                movies.get(1),  // Inception
                movies.get(7),  // Interstellar
                movies.get(0),  // Life Is Beautiful
                movies.get(2),  // Parasite
                movies.get(4),  // Pulp Fiction
                movies.get(5),  // Spirited Away
                movies.get(3),  // The Godfather
                movies.get(6),  // The Shawshank Redemption
                movies.get(9)   // Whiplash
        );

        List<Movie> testMovies = new ArrayList<>(movies);
        List<Movie> sortedMovies = movieService.sortMovies(testMovies, true);
        assertEquals(expectedMovies, sortedMovies, "Movies should be sorted in ascending order by title");
    }

    @Test
    @DisplayName("Test sorting in descending order")
    public void testSortMoviesDescending() {
        List<Movie> expectedMovies = List.of(
                movies.get(9),  // Whiplash
                movies.get(6),  // The Shawshank Redemption
                movies.get(3),  // The Godfather
                movies.get(5),  // Spirited Away
                movies.get(4),  // Pulp Fiction
                movies.get(2),  // Parasite
                movies.get(0),  // Life Is Beautiful
                movies.get(7),  // Interstellar
                movies.get(1),  // Inception
                movies.get(8)    // Amélie
        );

        List<Movie> testMovies = new ArrayList<>(movies);
        List<Movie> sortedMovies = movieService.sortMovies(testMovies, false);
        assertEquals(expectedMovies, sortedMovies, "Movies should be sorted in descending order by title");
    }

    @Test
    @DisplayName("Test filtering by genre DRAMA")
    void testFilterMoviesByGenre() {
        List<Movie> expectedMovies = List.of(
                movies.get(0),  // Life Is Beautiful
                movies.get(2),  // Parasite
                movies.get(3),  // The Godfather
                movies.get(4),  // Pulp Fiction
                movies.get(6),  // The Shawshank Redemption
                movies.get(7),  // Interstellar
                movies.get(9)   // Whiplash
        );

        List<Movie> testMovies = new ArrayList<>(movies);
        List<Movie> filteredMovies = movieService.filterMovies(testMovies, "", Genre.DRAMA);
        assertEquals(expectedMovies, filteredMovies, "Filtering by DRAMA should return correct movies");
    }

    @Test
    @DisplayName("Test filtering by search text 'pu'")
    void testFilterMoviesBySearchText() {
        //
        List<Movie> expectedMovies = List.of(
                movies.get(4)  // Pulp Fiction
        );

        List<Movie> testMovies = new ArrayList<>(movies);
        List<Movie> filteredMovies = movieService.filterMovies(testMovies, "Pu", null);
        assertEquals(expectedMovies, filteredMovies, "Filtering by 'Pu ' should return 'Pulp Fiction'");
    }

    @Test
    @DisplayName("Test filtering by search text 'life' and genre DRAMA")
    void testFilterMoviesBySearchTextAndGenre() {
        List<Movie> expectedMovies = List.of(
                movies.get(0) // Life Is Beautiful
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

    //Stream methods TDD
    @Test
    @DisplayName("Test to get the most frequent actor of a list of movies")
    void testGetMostFrequentActor() {
        String expectedActor = "Tim Robbins";
        assertEquals(expectedActor, movieService.getMostPopularActor(this.movies));
    }

    @Test
    @DisplayName("Test to get the longest movie title of a list of movies")
    void testGetLongestMovieTitle() {
        String expectedMovieTitle = "The Shawshank Redemption";
        assertEquals(expectedMovieTitle.length(), movieService.getLongestMovieTitle(this.movies));
    }

    @Test
    @DisplayName("Test to get the count of movies from a specific director")
    void testGetCountOfMoviesFromDirector() {
        int expectedCount = 2;
        assertEquals(expectedCount, movieService.countMoviesFromDirector(this.movies, "Christopher Nolan"));
    }

    @Test
    @DisplayName("Test to get the movies between 2 specific years")
    void testGetMoviesBetweenTwoYears() {
        List<Movie> expectedMovies = List.of(this.movies.get(7), this.movies.get(9));
        assertEquals(expectedMovies, movieService.getMoviesBetweenYears(this.movies, 2011, 2018));
    }
}