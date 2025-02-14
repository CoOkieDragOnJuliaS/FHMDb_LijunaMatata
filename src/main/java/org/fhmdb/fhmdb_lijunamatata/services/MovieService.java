package org.fhmdb.fhmdb_lijunamatata.services;

import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;

import java.util.ArrayList;
import java.util.List;


public class MovieService {

    // Sorts the movies based on their titles in ascending or descending order
    public List<Movie> sortMovies(List<Movie> sortedMovies, boolean sort) {
        if (sort) {
            sortedMovies.sort((movie1, movie2) -> movie1.getTitle().compareTo(movie2.getTitle())); // Ascending
        } else {
            sortedMovies.sort((movie1, movie2) -> movie2.getTitle().compareTo(movie1.getTitle())); // Descending
        }
        return sortedMovies;
    }

    // Filters movies based on search text and selected genre
    public List<Movie> filterMovies(List<Movie> movies, String searchText, Genre genre) {
        List<Movie> filteredMovies = new ArrayList<>();
        for (Movie movie : movies) {
            boolean matchesSearchText = movie.getTitle().toLowerCase().contains(searchText.toLowerCase());
            boolean matchesGenre = (genre == null || movie.getGenres().contains(genre));
            if (matchesSearchText && matchesGenre) {
                filteredMovies.add(movie);
            }
        }
        return filteredMovies;
    }
}
