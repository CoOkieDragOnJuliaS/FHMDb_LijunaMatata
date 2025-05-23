package org.fhmdb.fhmdb_lijunamatata.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.fhmdb.fhmdb_lijunamatata.exceptions.MovieApiException;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * MovieAPI class is responsible for fetching movie data from an external API.
 * It handles building API request URLs, making HTTP requests, and parsing JSON responses into movie objects.
 */
public class MovieAPI {

    private static final String BASE_URL = "https://prog2.fh-campuswien.ac.at/movies";
    private final OkHttpClient client;
    private final Gson gson;

    /**
     * Constructor to initialize the API client, JSON parser, and status label.
     *
     * // @param statusLabel Label component for displaying loading messages.
     * // @param isTesting   If true, disables UI updates to avoid JavaFX issues in tests.
     */
    //public MovieAPI(Label statusLabel, boolean isTesting) {
    public MovieAPI() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    /**
     * Returns the base URL of the API.
     * This method is protected so that it can be overridden in tests.
     *
     * @return The base URL as a string.
     */
    protected String getBaseUrl() {
        return BASE_URL;
    }

    /**
     * Fetches movies from the API based on search filters.
     * If both a query and genre are provided, both are included in the request.
     *
     * @param query       The search query (e.g., movie title or keywords). Can be null.
     * @param genre       The selected genre for filtering. Can be null.
     * @param releaseYear The year of release for filtering. Can be null.
     * @param ratingFrom  The minimum rating for filtering. Can be null.
     * @return A list of movies matching the filters, or an empty list if none are found.
     * @throws MovieApiException If the API request fails.
     */
    public List<Movie> fetchMovies(String query, Genre genre, Integer releaseYear, Double ratingFrom) throws MovieApiException {
        String finalUrl = buildUrl(query, genre, releaseYear, ratingFrom);
        Request request = new Request.Builder()
                .url(finalUrl)
                .header("User-Agent", "LiJuna_MaTata_FMHDb")
                .build();

        try (Response response = getClient().newCall(request).execute()) {
            return parseResponse(response);
        } catch (IOException e) {
        throw new MovieApiException("Failed to fetch movies from API", e);
        }
    }

    /**
     * Builds the final API URL using the Builder Pattern.
     * This method utilizes the MovieAPIRequestBuilder to dynamically add query parameters
     * such as search query, genre, release year, and rating threshold.
     *
     * @param query        The search query (e.g., title or keyword). Can be null.
     * @param genre        The genre filter. Can be null.
     * @param releaseYear  The release year filter. Can be null.
     * @param ratingFrom   The minimum rating filter. Can be null.
     * @return A fully constructed URL as a String.
     * @throws MovieApiException If the base URL is invalid or building the URL fails.
     */
    private String buildUrl(String query, Genre genre, Integer releaseYear, Double ratingFrom) throws MovieApiException {
        try {
            return new MovieAPIRequestBuilder(getBaseUrl())
                    .query(query)
                    .genre(genre != null ? genre.name() : null)
                    .releaseYear(releaseYear != null ? String.valueOf(releaseYear) : null)
                    .ratingFrom(ratingFrom != null ? String.valueOf(ratingFrom) : null)
                    .build();

        } catch (IllegalArgumentException e) {
            throw new MovieApiException("Failed to build API URL", e);
        }
    }

    /**
     * Parses the JSON response of the HTTP request into a List of Movies
     *
     * @param response
     * @return a list of movies as List<Movie>
     * @throws IOException
     */
    private List<Movie> parseResponse(Response response) throws IOException, MovieApiException {
        if (!response.isSuccessful() || response.body() == null) {
            throw new MovieApiException("Error fetching movies: HTTP " + response.code());
            // HttpExceptionHandler.handle(response);
        }

        // Converts the response body (which contains JSON) into a raw JSON string
        String jsonResponse = response.body().string();
        // Defines the type of the object we want to deserialize into: a List of Movie objects
        // TypeToken is used to preserve generic type information at runtime
        Type movieListType = new TypeToken<List<Movie>>() {}.getType();
        // Uses Gson to convert the JSON string into a List<Movie> based on the specified type.
        List<Movie> movies = gson.fromJson(jsonResponse, movieListType);

        if (movies == null || movies.isEmpty()) {
            return List.of();
        }
        return movies;
    }

    /**
     * Fetches all movies without any filters.
     *
     * @return A list of all movies from the API.
     * @throws IOException If the API request fails.
     */
    public List<Movie> fetchAllMovies() throws IOException, MovieApiException {
        return fetchMovies(null, null, null, null);
    }

    /**
     * Allows overriding OkHttpClient in tests.
     *
     * @return OkHttpClient instance.
     */
    public OkHttpClient getClient() {
        return client;
    }
}