package org.fhmdb.fhmdb_lijunamatata.models;

import org.fhmdb.fhmdb_lijunamatata.api.MovieAPI;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MovieTest {
    private List<Movie> movies;

    @BeforeEach
    public void setUp() {
        //BeforeSetup
        // todo
        MovieAPI movieAPI = new MovieAPI();
        try {
            this.movies = movieAPI.fetchAllMovies();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // this.movies = Movie.initializeMovies();
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
        List<String> expectedTitles = new ArrayList<>(List.of("Life Is Beautiful", "The Usual Suspects", "Puss in " +
                "Boots", "Avatar", "The Wolf of Wall Street"));
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
                , "A sole survivor tells of the twisty events " +
                "leading up to a horrific gun battle on a boat, which begin when five criminals meet at a seemingly " +
                "random police lineup."
                , "An outlaw cat, his childhood egg-friend, and a seductive thief kitty " +
                "set out in search for the eggs of the fabled Golden Goose to clear his name, restore his lost honor," +
                " and regain the trust of his mother and town."
                , "A paraplegic Marine dispatched to the moon Pandora on a unique mission " +
                "becomes torn between following his orders and protecting the world he feels is his home."
                , "Based on the true story of Jordan Belfort, from his rise to " +
                "a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal " +
                "government."));
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
                List.of(Genre.CRIME, Genre.DRAMA, Genre.MYSTERY),
                List.of(Genre.COMEDY, Genre.FAMILY, Genre.ANIMATION),
                List.of(Genre.ANIMATION, Genre.DRAMA, Genre.ACTION),
                List.of(Genre.DRAMA, Genre.ROMANCE, Genre.BIOGRAPHY)));
        assertEquals(expectedGenreLists.get(index), this.movies.get(index).getGenres());
    }
}
