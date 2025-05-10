package org.fhmdb.fhmdb_lijunamatata.services;

import okhttp3.mockwebserver.MockWebServer;
import org.fhmdb.fhmdb_lijunamatata.api.MovieAPI;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the MovieService class.
 * Uses a mock API to verify filtering, sorting, and analytical methods.
 */
public class MovieServiceTest {
    Logger logger = Logger.getLogger(MovieServiceTest.class.getName());

    private MovieService movieService;
    private MockWebServer mockWebServer;
    private MovieAPI movieAPI;
    private static List<Movie> movies;

    /**
     * Initialize mock web server and override MovieAPI base URL for tests.
     * Handles IOException and logs any setup failures.
     */
    @BeforeEach
    void setUp() {
        try {
            mockWebServer = new MockWebServer();
            mockWebServer.start();

            // Override getBaseUrl to return the mock URL
            movieAPI = new MovieAPI() {
                @Override
                protected String getBaseUrl() {
                    return mockWebServer.url("/movies").toString();
                }
            };

            movieService = new MovieService(movieAPI);
            setDispatcher(); // Loads test JSON data
        } catch (IOException e) {
            logger.severe("Setup failed: " + e.getMessage());
        }
    }

    /**
     * Shutdown the mock web server after each test to avoid port binding issues.
     */
    @AfterEach
    void tearDown() throws IOException {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    /**
     * Fetches and sorts movies by title.
     * If fetching fails, logs the exception and proceeds with empty list.
     */
    private void fetchAllMovies() {
        try {
            movies = movieService.fetchFilteredMovies("", null, null, null);
            if (movies != null) {
                movies.sort(Comparator.comparing(Movie::getTitle));
            }
        } catch (Exception e) {
            logger.severe("Error fetching movies: " + e.getMessage());
        }
    }

    /**
     * Wrapper for fetchFilteredMovies with runtime exception on failure.
     * This is helpful in tests where checked exceptions are not ideal.
     */
    private List<Movie> fetchFilteredMovies(String query, Genre genre, Integer releaseYear, Double ratingFrom) {
        try {
            return movieService.fetchFilteredMovies(query, genre, releaseYear, ratingFrom);
        } catch (Exception e) {
            logger.severe("Filtering failed: " + e.getMessage());
            throw new RuntimeException(e); // throws unchecked to fail fast
        }
    }

    /**
     * Loads JSON from resource file and sets dispatcher for mock responses.
     * Throws RuntimeException if test data is missing.
     */
    private void setDispatcher() {
        String jsonResponse = getJsonResponse();
        if (jsonResponse != null) {
            mockWebServer.setDispatcher(new MovieDispatcher(jsonResponse));
            fetchAllMovies();
        } else {
            throw new RuntimeException("Test JSON data not found");
        }
    }

    /**
     * Loads mock movie data from a JSON test file.
     * Handles IOException and returns null if file is not found.
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
            System.err.println("Failed to read test data: " + e.getMessage());
            return null;
        }
    }

    // === TEST CASES ===

    @Test
    @DisplayName("Should sort movies ascending by title")
    public void testSortMoviesAscending() {
        List<String> expected = List.of("Bibibap", "Inception", "Parasite", "The Matrix", "The Shawshank Redemption");
        List<String> actual = movieService.sortMovies(new ArrayList<>(movies), true).stream()
                .map(Movie::getTitle).toList();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Should sort movies descending by title")
    public void testSortMoviesDescending() {
        List<String> expected = List.of("The Shawshank Redemption", "The Matrix", "Parasite", "Inception", "Bibibap");
        List<String> actual = movieService.sortMovies(new ArrayList<>(movies), false).stream()
                .map(Movie::getTitle).toList();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Should filter by DRAMA genre")
    public void testFilterMoviesByGenre() {
        List<String> expected = List.of("Bibibap", "Parasite", "The Shawshank Redemption");
        List<String> actual = fetchFilteredMovies("", Genre.DRAMA, null, null)
                .stream().map(Movie::getTitle).sorted().toList();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Should filter by search text 'pu'")
    public void testFilterMoviesBySearchText() {
        List<String> expected = List.of("The Matrix");
        List<String> actual = fetchFilteredMovies("pu", null, null, null).stream()
                .map(Movie::getTitle).toList();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Should filter by text 'real' and genre ACTION")
    public void testFilterMoviesByTextAndGenre() {
        List<String> expected = List.of("The Matrix");
        List<String> actual = fetchFilteredMovies("real", Genre.ACTION, null, null).stream()
                .map(Movie::getTitle).toList();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Empty query returns all movies")
    public void testEmptySearchTextReturnsAll() {
        fetchAllMovies();
        List<Movie> result = fetchFilteredMovies("", null, null, null);
        result.sort(Comparator.comparing(Movie::getTitle));
        assertEquals(movies, result);
    }

    @Test
    @DisplayName("Null query returns all movies")
    public void testNullSearchTextReturnsAll() {
        fetchAllMovies();
        List<Movie> result = fetchFilteredMovies(null, null, null, null);
        result.sort(Comparator.comparing(Movie::getTitle));
        assertEquals(movies, result);
    }

    @Test
    @DisplayName("Sorting empty list returns empty list")
    public void testSortEmptyList() {
        assertEquals(List.of(), movieService.sortMovies(List.of(), true));
    }

    @Test
    @DisplayName("Should return most frequent actor")
    public void testMostPopularActor() {
        fetchAllMovies();
        assertEquals("Leonardo DiCaprio", movieService.getMostPopularActor(movies));
    }

    @Test
    @DisplayName("Should return length of longest movie title")
    public void testLongestMovieTitle() {
        fetchAllMovies();
        assertEquals("The Shawshank Redemption".length(), movieService.getLongestMovieTitle(movies));
    }

    @Test
    @DisplayName("Should count movies from Christopher Nolan")
    public void testMoviesFromDirector() {
        fetchAllMovies();
        assertEquals(1, movieService.countMoviesFromDirector(movies, "Christopher Nolan"));
    }

    @Test
    @DisplayName("Should return movies between 2010 and 2019")
    public void testMoviesBetweenYears() {
        fetchAllMovies();
        List<String> expected = List.of("Bibibap", "Inception", "Parasite");
        List<String> actual = movieService.getMoviesBetweenYears(movies, 2010, 2019)
                .stream().map(Movie::getTitle).toList();
        assertEquals(expected, actual);
    }
}
