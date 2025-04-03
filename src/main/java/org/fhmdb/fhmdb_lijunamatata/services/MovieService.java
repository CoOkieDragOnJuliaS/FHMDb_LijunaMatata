package org.fhmdb.fhmdb_lijunamatata.services;

import org.fhmdb.fhmdb_lijunamatata.api.MovieAPI;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Provides utility methods for sorting and filtering movies.
 * The methods allow sorting movies by their titles and filtering them by search text and genre.
 *
 * @author Marat Davletshin
 * @date 14.02.2025
 */
public class MovieService {

    Logger logger = Logger.getLogger(MovieService.class.getName());

    /**
     * Sorts the given list of movies either in ascending or descending order by title
     *
     * @param movies      The list of movies to be sorted.
     * @param isAscending Determines the sorting order:
     *                    true for ascending, false for descending.
     * @return A list of movies sorted in ascending or descending order
     */
    public List<Movie> sortMovies(List<Movie> movies, boolean isAscending) {
        // Create a new list to avoid modifying the original list
        List<Movie> sortedMovies = new ArrayList<>(movies);
        if (isAscending) {
            sortedMovies.sort(Comparator.comparing(Movie::getTitle));
        } else {
            sortedMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
        }
        return sortedMovies; // Return the new sorted list
    }


    /**
     * Fetches a list of movies based on the provided search text and selected genres
     *
     * @param searchText  The text to search for in movie titles. If {@code null}, empty query string filtering is applied.
     * @param genre       The genre to filter movies by. If {@code null}, no genre filtering is applied.
     * @param releaseYear The release to filter movies by. If {@code null}, no releaseYear filtering is applied.
     * @param rating      The rating to filter movies by. If {@code null}, no rating filtering is applied.
     * @return A list of movies that matches all criteria.
     */
    public List<Movie> fetchFilteredMovies(String searchText, Genre genre, Integer releaseYear,
                                           Double rating) throws IOException {
        return new MovieAPI().fetchMovies(searchText, genre, releaseYear, rating);
    }


    /**
     * Streams through a list of movies to get the actor who appears most frequently in the main cast
     *
     * @param movies
     * @return the most popular actor as String
     */
    public String getMostPopularActor(List<Movie> movies) {
        /*
        short version:
        return movies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
         */
        //create a flat map of a stream of the list of actors inside the movie
        //collect it by grouping it by actors and the count of appearance in a new Map<String,Long>
        Map<String, Long> actorCount = movies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()));

        //Stream through the new map of actors and their appearance count and get the max values & compare them
        //map them to get the key value of the Map (which is the actor) and return the value
        return actorCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Streams through a list of movies and counts the length of the longest title of the movies
     *
     * @param movies
     * @return the count of the characters used in the longest title
     */
    public int getLongestMovieTitle(List<Movie> movies) {
        //Stream through movies, map to get the title and compare the length with comparingInt()
        String movie = "";
        try {
            movie = movies.stream()
                    .map(Movie::getTitle)
                    .map(String::trim)
                    .max(Comparator.comparingInt(String::length))
                    .orElse("");
        } catch (NoSuchElementException e) {
            logger.info("No movie found with longest title");
        }
        return movie.length();
    }

    /**
     * Streams through a list of movies and the String value director to get the movies from the specific director
     *
     * @param movies
     * @param director
     * @return the count of the movies from the director which is set as a parameter
     */
    public long countMoviesFromDirector(List<Movie> movies, String director) {
        //stream through the movies and filter by looking into the directors list to see if it contains the specific director
        return movies.stream()
                .filter(movie -> movie.getDirectors().contains(director))
                .count();
    }

    /**
     * Streams through a list of movies and uses the startYear and endYear parameters to get the movies which are between 2 years
     *
     * @param movies
     * @param startYear
     * @param endYear
     * @return a list of movies between the specific years of the parameters
     */
    public List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        //stream through movies and filter with releaseYear >= startYear and <= endYear
        return movies.stream()
                .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                .collect(Collectors.toList());
    }
}
