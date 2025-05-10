package org.fhmdb.fhmdb_lijunamatata.database;

import org.fhmdb.fhmdb_lijunamatata.exceptions.DatabaseException;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Nested;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MovieEntityTest {
    private static List<Movie> movies;

    @BeforeAll
    public static void setupAll() {
        movies = Movie.initializeMoviesTestbase();
    }

    @Nested
    class MoviesToMovieEntitiesTests {
        private List<MovieEntity> movieEntities;
        @BeforeEach
        public void setup() {
            movieEntities = MovieEntity.fromMovies(movies);
        }

        @Test
        @DisplayName("Test initialize MovieEntity list from Movie list: MovieEntity list not null")
        public void testMovieEntityListInitialization_notNull() {
            assertNotNull(this.movieEntities);
        }

        @Test
        @DisplayName("Test initialize MovieEntity list from Movie list: MovieEntity list not empty")
        public void testMovieEntityListInitialization_notEmpty() {
            assertFalse(this.movieEntities.isEmpty());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3, 4})
        @DisplayName("Test initialize MovieEntity list from Movie list: apiId equals expected")
        public void testMovieEntityListInitialization_apiID_equals_expected(int index) {
            assertEquals(this.movieEntities.get(index).getApiId(), movies.get(index).getId());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3, 4})
        @DisplayName("Test initialize MovieEntity list from Movie list: title equals expected")
        public void testMovieEntityListInitialization_title_equals_expected(int index) {
            assertEquals(this.movieEntities.get(index).getTitle(), movies.get(index).getTitle());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3, 4})
        @DisplayName("Test initialize MovieEntity list from Movie list: genres equals expected")
        public void testMovieEntityListInitialization_genres_equals_expected(int index) {
            assertEquals(this.movieEntities.get(index).getGenres(),
                    MovieEntity.genresToString(movies.get(index).getGenres()));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3, 4})
        @DisplayName("Test initialize MovieEntity list from Movie list: releaseYear equals expected")
        public void testMovieEntityListInitialization_releaseYear_equals_expected(int index) {
            assertEquals(this.movieEntities.get(index).getReleaseYear(), movies.get(index).getReleaseYear());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3, 4})
        @DisplayName("Test initialize MovieEntity list from Movie list: imgUrl equals expected")
        public void testMovieEntityListInitialization_imgUrl_equals_expected(int index) {
            assertEquals(this.movieEntities.get(index).getImgUrl(), movies.get(index).getImgUrl());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3, 4})
        @DisplayName("Test initialize MovieEntity list from Movie list: lengthInMinutes equals expected")
        public void testMovieEntityListInitialization_lengthInMinutes_equals_expected(int index) {
            assertEquals(this.movieEntities.get(index).getLengthInMinutes(), movies.get(index).getLengthInMinutes());
        }

    }

    @Nested
    class MovieEntitiesToMoviesTests {
        private static List<MovieEntity> movieEntities;
        private List<Movie> moviesFromEntities;
        @BeforeAll
        public static void setupAll() {
            movieEntities = MovieEntity.fromMovies(movies);
        }

        @BeforeEach
        public void setup() {
            moviesFromEntities = MovieEntity.toMovies(movieEntities);
        }
        @Test
        @DisplayName("Test create Movie list from MovieEntity list: Movie list not null")
        public void testMovieListCreation_notNull () {
            assertNotNull(this.moviesFromEntities);
        }

        @Test
        @DisplayName("Test initialize Movie list from Movie list: MovieEntity list not empty")
        public void testMovieListCreation_notEmpty () {
            assertFalse(this.moviesFromEntities.isEmpty());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3, 4})
        @DisplayName("Test create Movie list from MovieEntity list: Id equals expected")
        public void testMovieListCreation_ID_equals_expected (int index){
            assertEquals(this.moviesFromEntities.get(index).getId(), movies.get(index).getId());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3, 4})
        @DisplayName("Test create Movie list from MovieEntity list: Genres equals expected")
        public void testMovieListCreation_Genres_equals_expected (int index){
            assertEquals(this.moviesFromEntities.get(index).getGenres(),
                    movies.get(index).getGenres());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3, 4})
        @DisplayName("Test create Movie list from MovieEntity list: Release Year equals expected")
        public void testMovieListCreation_releaseYear_equals_expected (int index){
            assertEquals(this.moviesFromEntities.get(index).getReleaseYear(), movies.get(index).getReleaseYear());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3, 4})
        @DisplayName("Test create Movie list from MovieEntity list: Img Url equals expected")
        public void testMovieListCreation_imgUrl_equals_expected (int index){
            assertEquals(this.moviesFromEntities.get(index).getImgUrl(), movies.get(index).getImgUrl());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3, 4})
        @DisplayName("Test create Movie list from MovieEntity list: length in minutes equals expected")
        public void testMovieListCreation_lengthInMinutes_equals_expected (int index){
            assertEquals(this.moviesFromEntities.get(index).getLengthInMinutes(), movies.get(index).getLengthInMinutes());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3, 4})
        @DisplayName("Test create Movie list from MovieEntity list: rating equals expected")
        public void testMovieListCreation_rating_equals_expected (int index){
            assertEquals(this.moviesFromEntities.get(index).getRating(), movies.get(index).getRating());
        }
    }

    @Nested
    class UtilsTests {
        @Test
        @DisplayName("Test stringToGenre: all lowercase returns expected genre")
        public void testStringToGenre_all_lowercase_returns_expected() {
            String testString = "action";
            assertEquals(MovieEntity.stringToGenre(testString), Genre.valueOf("ACTION"));
        }

        @Test
        @DisplayName("Test stringToGenre: first letter lowercase returns expected genre")
        public void testStringToGenre_firstLowercase_returns_expected() {
            String testString = "bIOGRAPHY";
            assertEquals(MovieEntity.stringToGenre(testString), Genre.valueOf("BIOGRAPHY"));
        }

        @Test
        @DisplayName("Test stringToGenre: all uppercase returns expected genre")
        public void testStringToGenre_allUppercase_returns_expected() {
            String testString = "FAMILY";
            assertEquals(MovieEntity.stringToGenre(testString), Genre.valueOf("FAMILY"));
        }

        @Test
        @DisplayName("Test stringToGenre: first uppercase returns expected genre")
        public void testStringToGenre_firstUppercase_returns_expected() {
            String testString = "Musical";
            assertEquals(MovieEntity.stringToGenre(testString), Genre.valueOf("MUSICAL"));
        }

        @Test
        @DisplayName("Test stringToGenre: space in the middle returns expected genre")
        public void testStringToGenre_spaceInMiddle_returns_expected() {
            String testString = "science fiction";
            assertEquals(MovieEntity.stringToGenre(testString), Genre.valueOf("SCIENCE_FICTION"));
        }

        @Test
        @DisplayName("Test stringToGenre: whitespace returns expected genre")
        public void testStringToGenre_whitespace_returns_expected() {
            String testString = "   THRILLER   ";
            assertEquals(MovieEntity.stringToGenre(testString), Genre.valueOf("THRILLER"));
        }

        @Test
        @DisplayName("Test stringToGenre: invalid string throws DatabaseException")
        public void testStringToGenre_invalid_throws_exception() {
            String testString = "THILLER";  // Invalid genre
            assertThrows(DatabaseException.class, () -> MovieEntity.stringToGenre(testString));
        }

    }
}
