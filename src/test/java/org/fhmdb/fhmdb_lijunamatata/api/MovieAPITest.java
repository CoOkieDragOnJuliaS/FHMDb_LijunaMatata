package org.fhmdb.fhmdb_lijunamatata.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MovieAPITest {

    private MovieAPI movieAPI;
    private MockWebServer mockWebServer;
    private Gson gson;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        gson = new Gson();

        movieAPI = new MovieAPI() {
            @Override
            protected String getBaseUrl() {
                return mockWebServer.url("/movies").toString();
            }
        };
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("Test fetching all movies - valid response")
    void fetchAllMovies_returnsMovieList() throws IOException {
        String jsonResponse = "[{\"id\":\"1\",\"title\":\"Inception\",\"genres\":[],\"releaseYear\":2010," +
                "\"description\":\"A mind-bending thriller\",\"imgUrl\":\"\",\"lengthInMinutes\":148,\"directors" +
                "\":[],\"writers\":[],\"mainCast\":[],\"rating\":8.8}]";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .addHeader("Content-Type", "application/json"));

        List<Movie> movies = movieAPI.fetchAllMovies();

        assertNotNull(movies, "Movies should not be null");
        assertEquals(1, movies.size(), "There should be one movie in the response");
        assertEquals("Inception", movies.get(0).getTitle(), "The movie title should be 'Inception'");
    }

    @Test
    @DisplayName("Test fetchMovies - API error response")
    void fetchMovies_handlesApiError() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(403)
                .setBody("")
                .addHeader("Content-Type", "application/json"));

        IOException exception = assertThrows(IOException.class, () -> movieAPI.fetchMovies(null, null,
                                                                                null, null));
        assertTrue(exception.getMessage().contains("Unexpected response"), "Exception should indicate API error");
    }

    @Test
    @DisplayName("Test fetchMovies with filters - valid response")
    void fetchMovies_withFilters() throws IOException {
        String jsonResponse = "[{\"id\":\"2\",\"title\":\"The Matrix\",\"genres\":[],\"releaseYear\":1999," +
                "\"description\":\"A sci-fi classic\",\"imgUrl\":\"\",\"lengthInMinutes\":136,\"directors" +
                "\":[],\"writers\":[],\"mainCast\":[],\"rating\":8.7}]";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .addHeader("Content-Type", "application/json"));

        List<Movie> movies = movieAPI.fetchMovies(null, "Action", 1999, null);

        assertNotNull(movies, "Movies should not be null");
        assertEquals(1, movies.size(), "There should be one movie in the response");
        assertEquals("The Matrix", movies.get(0).getTitle(), "The movie title should be 'The Matrix'");
    }
}
