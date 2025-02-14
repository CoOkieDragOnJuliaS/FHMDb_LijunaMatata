package org.fhmdb.fhmdb_lijunamatata.services;

import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Provides utility methods for sorting and filtering movies.
 * The methods allow sorting movies by their titles and filtering them by search text and genre.
 *
 * @author Marat Davletshin
 * @date 14.02.2025
 */
public class MovieService {

    /**
     * Sorts a list of movies based on their titles in either ascending or descending order.
     * The sorting order is determined by the {@code sort} flag.
     *
     * @param sortedMovies The list of movies to be sorted.
     * @param sort A boolean flag that determines the sorting order.
     *             {@code true} for ascending order, {@code false} for descending order.
     * @return The sorted list of movies.
     */
    public List<Movie> sortMovies(List<Movie> sortedMovies, boolean sort) {
        if (sort) {
            // Sort in ascending order by title
            sortedMovies.sort(Comparator.comparing(Movie::getTitle));
        } else {
            // Sort in descending order by title
            sortedMovies.sort((movie1, movie2) -> movie2.getTitle().compareTo(movie1.getTitle()));
        }
        return sortedMovies;
    }

    /**
     * Filters a list of movies based on the provided search text and selected genre.
     * The method checks if the movie's title contains the search text (ignoring case)
     * and if the movie's genre matches the selected genre.
     *
     * @param movies The list of movies to be filtered.
     * @param searchText The text to search for in movie titles.
     * @param genre The genre to filter movies by. If {@code null}, no genre filtering is applied.
     * @return A list of movies that match both the search text and selected genre.
     */
    public List<Movie> filterMovies(List<Movie> movies, String searchText, Genre genre) {
        List<Movie> filteredMovies = new ArrayList<>();
        for (Movie movie : movies) {
            boolean matchesSearchText = isMatchesSearchText(searchText, movie);
            boolean matchesGenre = isMatchesGenre(genre, movie);
            if (matchesSearchText && matchesGenre) {
                filteredMovies.add(movie);
            }
        }
        return filteredMovies;
    }

    /**
     * Checks if the movie's genres contain the selected genre.
     * Returns {@code true} if the movie matches the selected genre or if no genre is selected.
     *
     * @param genre The genre to check against.
     * @param movie The movie to check.
     * @return {@code true} if the movie matches the selected genre or if no genre is selected; {@code false} otherwise.
     */
    private static boolean isMatchesGenre(Genre genre, Movie movie) {
        return (genre == null || movie.getGenres().contains(genre));
    }

    /**
     * Checks if the movie's title contains the search text, ignoring case.
     *
     * @param searchText The text to search for in the movie's title.
     * @param movie The movie whose title is to be checked.
     * @return {@code true} if the movie's title contains the search text; {@code false} otherwise.
     */
    private static boolean isMatchesSearchText(String searchText, Movie movie) {
        return movie.getTitle().toLowerCase().contains(searchText.toLowerCase());
    }
}
