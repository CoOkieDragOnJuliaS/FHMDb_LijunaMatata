package org.fhmdb.fhmdb_lijunamatata.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.scene.control.Label;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
    private final Label statusLabel;
    private final boolean isTesting; // Test mode flag

    /**
     * Constructor to initialize the API client, JSON parser, and status label.
     *
     * @param statusLabel Label component for displaying loading messages.
     * @param isTesting   If true, disables UI updates to avoid JavaFX issues in tests.
     */
    public MovieAPI(Label statusLabel, boolean isTesting) {
        this.client = new OkHttpClient();
        this.gson = new Gson();
        this.statusLabel = statusLabel;
        this.isTesting = isTesting;
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
    public List<Movie> fetchMovies(String query, String genre, Integer releaseYear, Double ratingFrom) throws IOException {

        updateStatusLabel("Loading movies...", true);

        HttpUrl.Builder urlBuilder = HttpUrl.parse(getBaseUrl()).newBuilder();

        // based on exercise task: 2. Wenn User*innen einen Wert im Freitextfeld und beim Genre selektieren, sollen
        //dementsprechende Parameter in der URL gesetzt werden.

        if (query != null) urlBuilder.addQueryParameter("query", query);
        if (genre != null) urlBuilder.addQueryParameter("genre", genre);
        if (releaseYear != null) urlBuilder.addQueryParameter("releaseYear", String.valueOf(releaseYear));
        if (ratingFrom != null) urlBuilder.addQueryParameter("ratingFrom", String.valueOf(ratingFrom));

        String finalUrl = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        try (Response response = getClient().newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                updateStatusLabel("Error loading movies. Try again.", false);
                throw new IOException("Unexpected response: " + response);
            }

            String jsonResponse = response.body().string();
            Type movieListType = new TypeToken<List<Movie>>() {}.getType();
            List<Movie> movies = gson.fromJson(jsonResponse, movieListType);

            if (movies == null || movies.isEmpty()) {
                updateStatusLabel("No movies found.", false);
                return List.of();
            }

            updateStatusLabel("", false);
            return movies;
        } catch (IOException e) {
            updateStatusLabel("Error loading movies. Check your internet connection.", false);
            throw e;
        }
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

    /**
     * Updates the status label with a given message.
     * Ensures the update runs on the JavaFX UI thread.
     *
     * @param message  The message to display.
     * @param isLoading If true, the label is made visible; otherwise, it's hidden when empty.
     */
    private void updateStatusLabel(String message, boolean isLoading) {
        if (isTesting) return; // Turn off UI updates in test mode

        Platform.runLater(() -> {
            if (statusLabel != null) {
                statusLabel.setText(message);
                statusLabel.setVisible(isLoading || !message.isEmpty());
            }
        });
    }
}