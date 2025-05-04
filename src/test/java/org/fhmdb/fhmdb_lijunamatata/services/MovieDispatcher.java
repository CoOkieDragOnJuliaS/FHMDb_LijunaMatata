package org.fhmdb.fhmdb_lijunamatata.services;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Mock dispatcher for handling movie-related HTTP requests in tests.
 * Provides functionality to filter movies by various criteria and handle JSON serialization/deserialization.
 */
public class MovieDispatcher extends Dispatcher {
    private static final Logger logger = Logger.getLogger(MovieDispatcher.class.getName());
    private final String jsonResponse;
    private final List<Movie> allMovies;
    private final Gson gson;

    /**
     * Creates a new MovieDispatcher that handles movie-related requests with mock data.
     * @param jsonResponse The JSON string containing the mock movie data
     */
    public MovieDispatcher(String jsonResponse) {
        this.jsonResponse = jsonResponse;

        /* Configure Gson with a custom deserializer to properly handle movie list deserialization,
         especially the conversion of genre strings to Genre enum values */
        this.gson = new GsonBuilder()
                //register a custom deserializer for the type List<Movie> with Gson.
                // This allows you to define how JSON data should be converted into a List<Movie> object, enabling you to handle specific deserialization logic as needed.
            .registerTypeAdapter(new TypeToken<List<Movie>>(){}.getType(), new JsonDeserializer<List<Movie>>() {
                @Override
                public List<Movie> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    List<Movie> movies = new ArrayList<>();
                    JsonArray jsonArray = json.getAsJsonArray();
                    
                    // Iterate through each movie object in the JSON array
                    for (JsonElement element : jsonArray) {
                        JsonObject movieObj = element.getAsJsonObject();
                        
                        /* Convert genre strings to Genre enum values
                        Handles format differences (e.g., "Science-Fiction" -> SCIENCE_FICTION) */
                        List<Genre> genres = new ArrayList<>();
                        if (movieObj.has("genres") && !movieObj.get("genres").isJsonNull()) {
                            JsonArray genresArray = movieObj.getAsJsonArray("genres");
                            for (JsonElement genreElement : genresArray) {
                                String genreStr = genreElement.getAsString();
                                try {
                                    genres.add(Genre.valueOf(genreStr.toUpperCase().replace("-", "_")));
                                } catch (IllegalArgumentException e) {
                                    logger.warning("Unknown genre: " + genreStr);
                                }
                            }
                        }
                        
                        // Create a new Movie instance with all parsed fields
                        Movie movie = new Movie(
                            movieObj.get("id").getAsString(),
                            movieObj.get("title").getAsString(),
                            genres,
                            movieObj.get("releaseYear").getAsInt(),
                            movieObj.get("description").getAsString(),
                            movieObj.get("imgUrl").getAsString(),
                            movieObj.get("lengthInMinutes").getAsInt(),
                            context.deserialize(movieObj.get("directors"), new TypeToken<List<String>>(){}.getType()),
                            context.deserialize(movieObj.get("writers"), new TypeToken<List<String>>(){}.getType()),
                            context.deserialize(movieObj.get("mainCast"), new TypeToken<List<String>>(){}.getType()),
                            movieObj.get("rating").getAsDouble()
                        );
                        movies.add(movie);
                    }
                    return movies;
                }
            })
            .create();
            
        // Parse the initial JSON response into our movie list
        this.allMovies = gson.fromJson(jsonResponse, new TypeToken<List<Movie>>(){}.getType());
    }

    @NotNull
    @Override
    public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
        try {
            if (recordedRequest.getRequestUrl() == null) {
                return new MockResponse().setResponseCode(400);
            }

            // Extract query parameters from the URL
            String query = recordedRequest.getRequestUrl().queryParameter("query");
            String genreStr = recordedRequest.getRequestUrl().queryParameter("genre");
            String releaseYearStr = recordedRequest.getRequestUrl().queryParameter("releaseYear");
            String ratingFromStr = recordedRequest.getRequestUrl().queryParameter("ratingFrom");

            // If no filters are applied, return all movies
            if ((query == null || query.isEmpty()) && 
                genreStr == null && 
                releaseYearStr == null && 
                ratingFromStr == null) {
                return new MockResponse()
                    .setResponseCode(200)
                    .setBody(jsonResponse)
                    .addHeader("Content-Type", "application/json");
            }

            // Parse filter parameters, converting strings to appropriate types
            final Genre genre = (genreStr != null && !genreStr.isEmpty()) ? Genre.valueOf(genreStr) : null;
            final Integer releaseYear = (releaseYearStr != null && !releaseYearStr.isEmpty()) ? Integer.parseInt(releaseYearStr) : null;
            final Double ratingFrom = (ratingFromStr != null && !ratingFromStr.isEmpty()) ? Double.parseDouble(ratingFromStr) : null;
            final String searchQuery = (query != null && !query.isEmpty()) ? query.toLowerCase() : null;

            // Apply filters to the movie list using stream operations
            List<Movie> filteredMovies = allMovies.stream()
                .filter(movie -> {
                    if (searchQuery == null) return true;
                    String title = movie.getTitle().toLowerCase();
                    String description = movie.getDescription().toLowerCase();
                    return title.contains(searchQuery) || description.contains(searchQuery);
                })
                .filter(movie -> {
                    if (genre == null) return true;
                    return movie.getGenres() != null && movie.getGenres().contains(genre);
                })
                .filter(movie -> releaseYear == null || movie.getReleaseYear() == releaseYear)
                .filter(movie -> ratingFrom == null || movie.getRating() >= ratingFrom)
                .collect(Collectors.toList());

            //return the response if everything is working correctly with the filteredMovies as body
            return new MockResponse()
                .setResponseCode(200)
                .setBody(gson.toJson(filteredMovies))
                .addHeader("Content-Type", "application/json");

        } catch (IllegalArgumentException e) {
            logger.warning("Invalid parameter: " + e.getMessage());
            //return 400 if there is an invalid parameter in the URL
            return new MockResponse()
                .setResponseCode(400)
                .setBody("Invalid parameter: " + e.getMessage())
                .addHeader("Content-Type", "text/plain");
        } catch (Exception e) {
            logger.severe("Error processing request: " + e.getMessage());
            //return 500 is there is a bigger processing error while processing the URL
            return new MockResponse()
                .setResponseCode(500)
                .setBody("Error processing request: " + e.getMessage())
                .addHeader("Content-Type", "text/plain");
        }
    }
}
