package org.fhmdb.fhmdb_lijunamatata.services;

import okhttp3.mockwebserver.MockWebServer;
import org.fhmdb.fhmdb_lijunamatata.api.MovieAPI;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for MovieService functionality.
 * Uses MockWebServer to simulate API responses and test movie filtering and sorting operations.
 */
public class MovieServiceTest {
    Logger logger = Logger.getLogger(MovieServiceTest.class.getName());

    private MovieService movieService;
    private MockWebServer mockWebServer;
    private MovieAPI movieAPI;
    private static List<Movie> movies;

    /**
     * Sets up the test environment before each test.
     * Initializes MockWebServer, MovieAPI, and MovieService with test data.
     */
    @BeforeEach
    void setUp() {
        try {
            mockWebServer = new MockWebServer();
            mockWebServer.start();

            movieAPI = new MovieAPI() {
                @Override
                protected String getBaseUrl() {
                    return mockWebServer.url("/movies").toString();
                }
            };

            movieService = new MovieService(movieAPI);
            setDispatcher();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Cleans up resources after each test.
     */
    @AfterEach
    void tearDown() throws IOException {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    /**
     * Fetches all movies from the API and sorts them by title.
     */
    private void fetchAllMovies() {
        try {
            movies = movieService.fetchFilteredMovies("", null, null, null);
            if (movies != null) {
                movies.sort(Comparator.comparing(Movie::getTitle));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Helper method to fetch filtered movies based on various criteria.
     */
    private List<Movie> fetchFilteredMovies(String query, Genre genre, Integer releaseYear, Double ratingFrom) {
        try {
            return movieService.fetchFilteredMovies(query, genre, releaseYear, ratingFrom);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("Failed to fetch movies: " + e.getMessage());
        }
    }

    /**
     * Sets up the mock dispatcher with test data from JSON file.
     */
    private void setDispatcher() {
        String jsonResponse = getJsonResponse();
        if (jsonResponse != null) {
            mockWebServer.setDispatcher(new MovieDispatcher(jsonResponse));
            fetchAllMovies();
        } else {
            throw new RuntimeException("Failed to load test data");
        }
    }

    /**
     * Loads test movie data from JSON file.
     */
    private static String getJsonResponse() {
        String path = "org.fhmdb.fhmdb_lijunamatata/jsonMovieResponse.txt";
        try (InputStream inputStream = MovieServiceTest.class.getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                System.err.println("Resource not found: " + path);
                return null;
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Error reading JSON from file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Tests if movies are correctly sorted in ascending order by title.
     */
    @Test
    @DisplayName("Test sorting in ascending order")
    public void testSortMoviesAscending() {
        List<String> expectedTitles = List.of(
                "Bibibap",
                "Inception",
                "Parasite",
                "The Matrix",
                "The Shawshank Redemption"
        );
        List<Movie> testMovies = new ArrayList<>(movies);
        List<Movie> sortedMovies = movieService.sortMovies(testMovies, true);
        List<String> sortedTitles = sortedMovies.stream().map(Movie::getTitle).collect(Collectors.toList());
        assertEquals(expectedTitles, sortedTitles, "Movies should be sorted in ascending order by title");
    }

    /**
     * Tests if movies are correctly sorted in descending order by title.
     */
    @Test
    @DisplayName("Test sorting in descending order")
    void testSortMoviesDescending() {
        List<String> expectedTitles = List.of(
                "The Shawshank Redemption",
                "The Matrix",
                "Parasite",
                "Inception",
                "Bibibap"
        );

        List<Movie> testMovies = new ArrayList<>(movies);
        List<Movie> sortedMovies = movieService.sortMovies(testMovies, false);
        List<String> sortedTitles = sortedMovies.stream().map(Movie::getTitle).collect(Collectors.toList());
        assertEquals(expectedTitles, sortedTitles, "Movies should be sorted in descending order by title");
    }

    /**
     * Tests if movies are correctly filtered by genre (DRAMA).
     */
    @Test
    @DisplayName("Test filtering by genre DRAMA")
    void testFilterMoviesByGenre() {
        List<String> expectedTitles = List.of(
                "Bibibap",
                "Parasite",
                "The Shawshank Redemption"
        );
        List<Movie> filteredMovies = fetchFilteredMovies("", Genre.DRAMA, null, null);
        List<String> filteredTitles = filteredMovies.stream().map(Movie::getTitle).sorted().collect(Collectors.toList());
        assertEquals(expectedTitles, filteredTitles, "Filtering by DRAMA should return correct movies");
    }

    /**
     * Tests if movies are correctly filtered by search text.
     */
    @Test
    @DisplayName("Test filtering by search text")
    void testFilterMoviesBySearchText() {
        List<String> expectedTitles = List.of(
                "The Matrix"
        );

        List<Movie> filteredMovies = fetchFilteredMovies("pu", null, null, null);
        List<String> filteredTitles = filteredMovies.stream().map(Movie::getTitle).collect(Collectors.toList());
        assertEquals(expectedTitles, filteredTitles, "Filtering by 'pu' should return 'The Matrix'");
    }

    /**
     * Tests if movies are correctly filtered by both search text and genre.
     */
    @Test
    @DisplayName("Test filtering by search text 'real' and genre Action")
    void testFilterMoviesBySearchTextAndGenre() {
        List<String> expectedTitles = List.of(
                "The Matrix"
        );

        List<Movie> filteredMovies = fetchFilteredMovies("real", Genre.ACTION, null, null);
        List<String> filteredTitles = filteredMovies.stream().map(Movie::getTitle).collect(Collectors.toList());
        assertEquals(expectedTitles, filteredTitles, "Filtering by 'real' and ACTION should return correct movies");
    }

    /**
     * Tests if all movies are returned when search text is empty.
     */
    @Test
    @DisplayName("Test filtering with empty search text")
    void testFilterMoviesWithEmptySearchText() {
        fetchAllMovies();
        List<Movie> filteredMovies = fetchFilteredMovies("", null, null, null);
        filteredMovies.sort(Comparator.comparing(Movie::getTitle));
        assertEquals(movies, filteredMovies, "All movies should be returned when search text is empty");
    }

    /**
     * Tests if all movies are returned when search text is null.
     */
    @Test
    @DisplayName("Test filtering with null search text")
    void testFilterMoviesWithNullSearchText() {
        fetchAllMovies();
        List<Movie> filteredMovies = fetchFilteredMovies(null, null, null, null);
        filteredMovies.sort(Comparator.comparing(Movie::getTitle));
        assertEquals(movies, filteredMovies, "All movies should be returned when search text is null");
    }

    /**
     * Tests if sorting an empty movie list returns an empty list.
     */
    @Test
    @DisplayName("Test sorting movies with an empty list")
    void testSortMoviesWithEmptyList() {
        List<Movie> expectedMovies = new ArrayList<>();
        List<Movie> emptyMovies = new ArrayList<>();
        List<Movie> sortedMovies = movieService.sortMovies(emptyMovies, true);
        assertEquals(expectedMovies, sortedMovies, "Sorting an empty list should return an empty list");
    }

    /**
     * Tests if the most frequent actor is correctly identified.
     */
    @Test
    @DisplayName("Test to get the most frequent actor of a list of movies")
    void testGetMostFrequentActor() {
        fetchAllMovies();
        String expectedActor = "Leonardo DiCaprio";
        List<Movie> actualMovies = new ArrayList<>(movies);
        assertEquals(expectedActor, movieService.getMostPopularActor(actualMovies));
    }

    /**
     * Tests if the longest movie title length is correctly calculated.
     */
    @Test
    @DisplayName("Test to get the longest movie title of a list of movies")
    void testGetLongestMovieTitle() {
        fetchAllMovies();
        String expectedMovieTitle = "The Shawshank Redemption";
        List<Movie> actualMovies = new ArrayList<>(movies);
        assertEquals(expectedMovieTitle.length(), movieService.getLongestMovieTitle(actualMovies));
    }

    /**
     * Tests if the count of movies by a specific director is correct.
     */
    @Test
    @DisplayName("Test to get the count of movies from a specific director")
    void testGetCountOfMoviesFromDirector() {
        fetchAllMovies();
        int expectedCount = 1;
        logger.info("Christopher Nolan did in the testMovieDatabase 1 movie, Inception!");
        List<Movie> actualMovies = new ArrayList<>(movies);
        assertEquals(expectedCount, movieService.countMoviesFromDirector(actualMovies, "Christopher Nolan"));
    }

    /**
     * Tests if movies are correctly filtered by release year range.
     */
    @Test
    @DisplayName("Test to get the movies between 2 specific years")
    void testGetMoviesBetweenTwoYears() {
        fetchAllMovies();
        List<String> expectedTitles = List.of(
                "Bibibap",
                "Inception",
                "Parasite"
        );

        List<Movie> actualMovies = new ArrayList<>(movies);
        List<Movie> filteredMovies = movieService.getMoviesBetweenYears(actualMovies, 2010, 2019);
        List<String> filteredTitles = filteredMovies.stream().map(Movie::getTitle).collect(Collectors.toList());
        assertEquals(expectedTitles, filteredTitles);
    }
}