package org.fhmdb.fhmdb_lijunamatata.api;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.fhmdb.fhmdb_lijunamatata.exceptions.MovieApiException;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MovieAPITest {

    private MovieAPI movieAPI;
    private MockWebServer mockWebServer;

    @BeforeEach
    void setUp() throws IOException {
        // Start a mock web server before each test
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // Override base URL to use mock server for testing
        movieAPI = new MovieAPI() {
            @Override
            protected String getBaseUrl() {
                return mockWebServer.url("/movies").toString();
            }
        };
    }

    @AfterEach
    void tearDown() throws IOException {
        // Shutdown mock server after each test
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("Test fetching all movies - valid response")
    void fetchAllMovies_returnsMovieList() throws IOException, MovieApiException {
        // Provide a mock JSON movie response
        String jsonResponse =
                """
                [
                  {
                    "id": "1",
                    "title": "Inception",
                    "genres": [],
                    "releaseYear": 2010,
                    "description": "A mind-bending thriller",
                    "imgUrl": "",
                    "lengthInMinutes": 148,
                    "directors": [],
                    "writers": [],
                    "mainCast": [],
                    "rating": 8.8
                  }
                ]
                """;

        // Enqueue a mocked HTTP 200 OK response with the movie
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .addHeader("Content-Type", "application/json"));

        // Call the API
        List<Movie> movies = movieAPI.fetchAllMovies();

        // Validate the response
        assertNotNull(movies, "Movies should not be null");
        assertEquals(1, movies.size(), "There should be one movie in the response");
        assertEquals("Inception", movies.get(0).getTitle(), "The movie title should be 'Inception'");
    }

    @Test
    @DisplayName("Test fetchMovies - API 403 error response")
    void fetchMovies_handles403Error() {
        // Simulate a 403 Forbidden response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(403)
                .setBody("")
                .addHeader("Content-Type", "application/json"));

        // Expect MovieApiException
        MovieApiException exception = assertThrows(MovieApiException.class, () ->
                movieAPI.fetchMovies(null, null, null, null));

        assertTrue(exception.getMessage().contains("403"),
                "Exception should indicate HTTP 403 error");
    }

    @ParameterizedTest
    @DisplayName("Test fetchMovies - various API error responses")
    @ValueSource(ints = {400, 401, 408, 500, 502, 503, 511, 519})
    void fetchMovies_handlesVariousStatusCodes(int statusCode) {
        // Simulate HTTP error responses
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(statusCode)
                .setBody("")
                .addHeader("Content-Type", "application/json"));

        // Expect IOException for any of these status codes
        assertThrows(MovieApiException.class, () ->
                movieAPI.fetchMovies(null, null, null, null));
    }

    @Test
    @DisplayName("Test fetchMovies with filters - valid response")
    void fetchMovies_withFilters_returnsFilteredMovie() throws IOException, MovieApiException {
        // Provide a mock response with a filtered movie
        String jsonResponse =
                """
                [
                  {
                    "id": "2",
                    "title": "The Matrix",
                    "genres": [],
                    "releaseYear": 1999,
                    "description": "A sci-fi classic",
                    "imgUrl": "",
                    "lengthInMinutes": 136,
                    "directors": [],
                    "writers": [],
                    "mainCast": [],
                    "rating": 8.7
                  }
                ]
                """;

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .addHeader("Content-Type", "application/json"));

        List<Movie> movies = movieAPI.fetchMovies(null, Genre.ACTION, 1999, null);

        assertNotNull(movies, "Movies should not be null");
        assertEquals(1, movies.size(), "There should be one movie in the response");
        assertEquals("The Matrix", movies.get(0).getTitle(), "The movie title should be 'The Matrix'");
    }
}
