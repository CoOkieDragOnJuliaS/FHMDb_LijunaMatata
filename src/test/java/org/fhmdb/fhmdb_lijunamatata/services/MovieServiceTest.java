package org.fhmdb.fhmdb_lijunamatata.services;

import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MovieServiceTest {
    Logger logger = Logger.getLogger(MovieServiceTest.class.getName());

    private MovieService movieService;
    private List<Movie> movies;

    @BeforeEach
    void setUp() {
        movieService = new MovieService();
        try {
            movies = Movie.initializeMovies();
            movies.sort(Comparator.comparing(Movie::getTitle));
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }

    @Test
    @DisplayName("Test sorting in ascending order")
    public void testSortMoviesAscending() {
        List<String> expectedTitles = List.of(
                "12 Angry Men",
                "Avatar",
                "City of God",
                "Django Unchained",
                "Forrest Gump",
                "Goodfellas",
                "Inception",
                "It\"s a Wonderful Life",
                "Knives Out",
                "Life Is Beautiful",
                "Once Upon a Time in Hollywood",
                "One Flew Over the Cuckoo\"s Nest",
                "Pulp Fiction",
                "Puss in Boots",
                "Saving Private Ryan",
                "Schindler's List",
                "Seven",
                "Spirited Away",
                "Star Wars: Episode V - The Empire Strikes Back",
                "The Dark Knight",
                "The Godfather",
                "The Good, the Bad and the Ugly",
                "The Lion King",
                "The Lord of the Rings: The Return of the King",
                "The Lord of the Rings: The Two Towers",
                "The Matrix",
                "The Shawshank Redemption",
                "The Silence of the Lambs",
                "The Usual Suspects",
                "The Wolf of Wall Street",
                "Toy Story"
        );
        List<Movie> testMovies = new ArrayList<>(movies);
        List<Movie> sortedMovies = movieService.sortMovies(testMovies, true);
        List<String> sortedTitles = sortedMovies.stream().map(Movie::getTitle).collect(Collectors.toList());
        assertEquals(expectedTitles, sortedTitles, "Movies should be sorted in ascending order by title");
    }

    @Test
    @DisplayName("Test sorting in descending order")
    public void testSortMoviesDescending() {
        List<String> expectedTitles = List.of(
                "Toy Story",
                "The Wolf of Wall Street",
                "The Usual Suspects",
                "The Silence of the Lambs",
                "The Shawshank Redemption",
                "The Matrix",
                "The Lord of the Rings: The Two Towers",
                "The Lord of the Rings: The Return of the King",
                "The Lion King",
                "The Good, the Bad and the Ugly",
                "The Godfather",
                "The Dark Knight",
                "Star Wars: Episode V - The Empire Strikes Back",
                "Spirited Away",
                "Seven",
                "Schindler's List",
                "Saving Private Ryan",
                "Puss in Boots",
                "Pulp Fiction",
                "One Flew Over the Cuckoo\"s Nest",
                "Once Upon a Time in Hollywood",
                "Life Is Beautiful",
                "Knives Out",
                "It\"s a Wonderful Life",
                "Inception",
                "Goodfellas",
                "Forrest Gump",
                "Django Unchained",
                "City of God",
                "Avatar",
                "12 Angry Men"
        );

        List<Movie> testMovies = new ArrayList<>(movies);
        List<Movie> sortedMovies = movieService.sortMovies(testMovies, false);
        List<String> sortedTitles = sortedMovies.stream().map(Movie::getTitle).collect(Collectors.toList());
        assertEquals(expectedTitles, sortedTitles, "Movies should be sorted in descending order by title");

    }

    @Test
    @DisplayName("Test filtering by genre DRAMA")
    void testFilterMoviesByGenre() {
        List<String> expectedTitles = List.of(
                "The Godfather",
                "The Shawshank Redemption",
                "The Dark Knight",
                "Schindler's List",
                "Pulp Fiction",
                "The Lord of the Rings: The Return of the King",
                "12 Angry Men",
                "The Lord of the Rings: The Two Towers",
                "One Flew Over the Cuckoo\"s Nest",
                "Goodfellas",
                "Seven",
                "The Silence of the Lambs",
                "It\"s a Wonderful Life",
                "Saving Private Ryan",
                "City of God",
                "Life Is Beautiful",
                "Forrest Gump",
                "The Lion King",
                "Knives Out",
                "Once Upon a Time in Hollywood",
                "Django Unchained",
                "The Wolf of Wall Street"
        );

        try {
            List<Movie> filteredMovies = movieService.fetchFilteredMovies("", Genre.DRAMA, null, null);
            List<String> filteredTitles = filteredMovies.stream().map(Movie::getTitle).collect(Collectors.toList());
            assertEquals(expectedTitles, filteredTitles, "Filtering by DRAMA should return correct movies");
        }catch(IOException e){
            logger.severe(e.getMessage());
        }

    }

    @Test
    @DisplayName("Test filtering by search text 'pu'")
    void testFilterMoviesBySearchText() {
        //
        List<String> expectedTitles = List.of(
                "Pulp Fiction",
                "Puss in Boots"
        );

        try {
            List<Movie> filteredMovies = movieService.fetchFilteredMovies("Pu", null, null, null);
            List<String> filteredTitles = filteredMovies.stream().map(Movie::getTitle).collect(Collectors.toList());
            assertEquals(expectedTitles, filteredTitles, "Filtering by 'pu' should return 'Pulp Fiction'");
        }catch(IOException e){
            logger.severe(e.getMessage());
        }

    }

    @Test
    @DisplayName("Test filtering by search text 'life' and genre DRAMA")
    void testFilterMoviesBySearchTextAndGenre() {
        List<String> expectedTitles = List.of(
                "It\"s a Wonderful Life",
                "Life Is Beautiful"
        );

        try {
            List<Movie> filteredMovies = movieService.fetchFilteredMovies("life", Genre.DRAMA, null, null);
            List<String> filteredTitles = filteredMovies.stream().map(Movie::getTitle).collect(Collectors.toList());
            assertEquals(expectedTitles, filteredTitles, "Filtering by 'life' and DRAMA should return correct movies");
        }catch(IOException e){
            logger.severe(e.getMessage());
        }

    }

    @Test
    @DisplayName("Test filtering with empty search text")
    void testFilterMoviesWithEmptySearchText() {
        List<Movie> expectedMovies = new ArrayList<>(movies);

        try {
            List<Movie> filteredMovies = movieService.fetchFilteredMovies("", null, null, null);
            filteredMovies.sort(Comparator.comparing(Movie::getTitle));
            assertEquals(expectedMovies, filteredMovies , "All movies should be returned when search text is empty");
        }catch(IOException e){
            logger.severe(e.getMessage());
        }
    }

    @Test
    @DisplayName("Test filtering with null search text")
    void testFilterMoviesWithNullSearchText() {
        List<Movie> expectedMovies = new ArrayList<>(movies);

        try {
            List<Movie> filteredMovies = movieService.fetchFilteredMovies(null, null, null, null);
            filteredMovies.sort(Comparator.comparing(Movie::getTitle));
            assertEquals(expectedMovies, filteredMovies, "All movies should be returned when search text is empty");
        }catch(IOException e) {
            logger.severe(e.getMessage());
        }
    }

    @Test
    @DisplayName("Test sorting movies with an empty list")
    void testSortMoviesWithEmptyList() {
        List<Movie> expectedMovies = new ArrayList<>();
        List<Movie> emptyMovies = new ArrayList<>();
        List<Movie> sortedMovies = movieService.sortMovies(emptyMovies, true);
        assertEquals(expectedMovies, sortedMovies, "Sorting an empty list should return an empty list");
    }

    //Stream methods TDD
    @Test
    @DisplayName("Test to get the most frequent actor of a list of movies")
    void testGetMostFrequentActor() {
        String expectedActor = "Leonardo DiCaprio";
        assertEquals(expectedActor, movieService.getMostPopularActor(this.movies));
    }


    @Test
    @DisplayName("Test to get the longest movie title of a list of movies")
    void testGetLongestMovieTitle() {
        String expectedMovieTitle = "Star Wars: Episode V - The Empire Strikes Back";
        assertEquals(expectedMovieTitle.length(), movieService.getLongestMovieTitle(this.movies));
    }

    @Test
    @DisplayName("Test to get the count of movies from a specific director")
    void testGetCountOfMoviesFromDirector() {
        int expectedCount = 2;
        assertEquals(expectedCount, movieService.countMoviesFromDirector(this.movies, "Christopher Nolan"));
    }

    @Test
    @DisplayName("Test to get the movies between 2 specific years")
    void testGetMoviesBetweenTwoYears() {
        List<String> expectedTitles = List.of(
                "Django Unchained",
                "Inception",
                "Knives Out",
                "Once Upon a Time in Hollywood",
                "Puss in Boots",
                "The Wolf of Wall Street"
        );

        List<Movie> filteredMovies = movieService.getMoviesBetweenYears(this.movies, 2010, 2019);
        List<String> filteredTitles = filteredMovies.stream().map(Movie::getTitle).collect(Collectors.toList());
        assertEquals(expectedTitles, filteredTitles);
    }
}