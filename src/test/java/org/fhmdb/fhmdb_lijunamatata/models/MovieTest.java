package org.fhmdb.fhmdb_lijunamatata.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MovieTest {
    Movie movie;
    private List<Movie> movies;

    @BeforeEach
    public void setUp() {
        //BeforeSetup
        this.movie = new Movie();
        this.movies = Movie.initializeMoviesTestbase();

    }

    @Test
    @DisplayName("Test initialize Movies: Movies not empty")
    public void testInitializeMovies_notEmpty() {
        assertFalse(this.movies.isEmpty());
    }

    @Test
    @DisplayName("Test initialize Movies: First Movie has expected title")
    public void testInitializeMovies_firstMovieTitle_equals_expected() {
        String expectedTitle = "Life Is Beautiful";
        assertEquals(expectedTitle, this.movies.get(0).getTitle());
    }

    @DisplayName("Test initialize Movies: All Movies have expected title")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4})
    public void testInitializeMovies_allMovieTitles_equal_expected(int index) {
        List<String> expectedTitles = new ArrayList<>(List.of("Life Is Beautiful", "Inception", "Parasite", "The Godfather", "Pulp Fiction"));
        assertEquals(expectedTitles.get(index), this.movies.get(index).getTitle());
    }

    @Test
    @DisplayName("Test initialize Movies: First Movie has expected description")
    public void testInitializeMovies_firstMovieDescription_equals_expected() {
        String expectedDescription = "When an open-minded Jewish librarian and his son become victims of" +
                " the Holocaust, he uses a perfect mixture of will, humor, and imagination to protect his son from " +
                "the dangers around their camp.";
        assertEquals(expectedDescription, this.movies.get(0).getDescription());
    }

    @DisplayName("Test initialize Movies: All Movies have expected description")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4})
    public void testInitializeMovies_allMovieDescription_equal_expected(int index) {
        List<String> expectedDescriptions = new ArrayList<>(List.of("When an open-minded Jewish librarian and his son" +
                        " " +
                        "become victims of" +
                        " the Holocaust, he uses a perfect mixture of will, humor, and imagination to protect his son from " +
                        "the dangers around their camp."
                , "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a CEO."
                , "Greed and class discrimination threaten the newly formed symbiotic relationship between the wealthy Park family and the destitute Kim clan."
                , "An organized crime dynasty's aging patriarch transfers control of his clandestine empire to his reluctant son."
                , "The lives of two mob hitmen, a boxer, a gangster's wife, and a pair of diner bandits intertwine in four tales of violence and redemption."));
        assertEquals(expectedDescriptions.get(index), this.movies.get(index).getDescription());
    }

    @Test
    @DisplayName("Test initialize Movies: First Movie has expected genres")
    public void testInitializeMovies_firstMovieGenres_equals_expected() {
        List<Genre> expectedGenres = List.of(Genre.DRAMA, Genre.ROMANCE);
        assertEquals(expectedGenres, this.movies.get(0).getGenres());
    }

    @DisplayName("Test initialize Movies: All Movies have expected genre")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4})
    public void testInitializeMovies_allMovieGenres_equal_expected(int index) {
        List<List<Genre>> expectedGenreLists = new ArrayList<>(List.of(
                List.of(Genre.DRAMA, Genre.ROMANCE),
                List.of(Genre.SCIENCE_FICTION, Genre.ACTION, Genre.THRILLER),
                List.of(Genre.DRAMA, Genre.THRILLER),
                List.of(Genre.DRAMA, Genre.CRIME),
                List.of(Genre.CRIME, Genre.DRAMA)));
        assertEquals(expectedGenreLists.get(index), this.movies.get(index).getGenres());
    }

    @Test
    @DisplayName("Test 2 different sets of objects(movie) with same movie list")
    public void test2DifferentSetsMovieList_equals_expected() {
        List<Movie> newListOfMovies = Movie.initializeMoviesTestbase();
        Assertions.assertTrue(this.movies.equals(newListOfMovies));
    }

    @Test
    @DisplayName("Test 2 different sets of objects(movie) with not the same movie list")
    public void test2DifferentSetsMovieList_Notequals_expected() {
        List<Movie> newListOfMovies = Movie.initializeMoviesTestbase();
        newListOfMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
        Assertions.assertFalse(this.movies.equals(newListOfMovies));
    }
}
