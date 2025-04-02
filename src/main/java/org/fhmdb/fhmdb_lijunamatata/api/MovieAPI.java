package org.fhmdb.fhmdb_lijunamatata.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.fhmdb.fhmdb_lijunamatata.exceptions.HttpExceptionHandler;
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
     * @throws IOException If the API request fails.
     */
    public List<Movie> fetchMovies(String query, Genre genre, Integer releaseYear, Double ratingFrom) throws IOException {
        String finalUrl = buildUrl(query, genre, releaseYear, ratingFrom);
        Request request = new Request.Builder()
                .url(finalUrl)
                .header("User-Agent", "LiJuna_MaTata_FMHDb")
                .build();

        try (Response response = getClient().newCall(request).execute()) {
            return parseResponse(response);
        }
    }

    /**
     * Builds the url by concatenating the parameters in the correct format to pass to the HTTP request in fetchMovies
     *
     * @param query
     * @param genre
     * @param releaseYear
     * @param ratingFrom
     * @return the final URL as String
     */
    private String buildUrl(String query, Genre genre, Integer releaseYear, Double ratingFrom) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(getBaseUrl()).newBuilder();

        // based on exercise task: 2. Wenn User*innen einen Wert im Freitextfeld und beim Genre selektieren, sollen
        //dementsprechende Parameter in der URL gesetzt werden.

        if (query != null) urlBuilder.addQueryParameter("query", query);
        if (genre != null) urlBuilder.addQueryParameter("genre", String.valueOf(genre));
        if (releaseYear != null) urlBuilder.addQueryParameter("releaseYear", String.valueOf(releaseYear));
        if (ratingFrom != null) urlBuilder.addQueryParameter("ratingFrom", String.valueOf(ratingFrom));

        return urlBuilder.build().toString();

    }

    /**
     * Parses the JSON response of the HTTP request into a List of Movies
     *
     * @param response
     * @return a list of movies as List<Movie>
     * @throws IOException
     */
    private List<Movie> parseResponse(Response response) throws IOException {
        if (!response.isSuccessful() || response.body() == null) {
            HttpExceptionHandler.handle(response);
        }

        assert response.body() != null;
        // TODO: Please explain these lines to get an understanding
        String jsonResponse = response.body().string();
        Type movieListType = new TypeToken<List<Movie>>() {}.getType();
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
    public List<Movie> fetchAllMovies() throws IOException {
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