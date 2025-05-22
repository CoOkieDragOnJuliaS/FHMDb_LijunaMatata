package org.fhmdb.fhmdb_lijunamatata.repositories;

import org.fhmdb.fhmdb_lijunamatata.database.MovieEntity;
import org.fhmdb.fhmdb_lijunamatata.exceptions.DatabaseException;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MovieRepositoryTest {

    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() throws SQLException, DatabaseException {
        movieRepository = new MovieRepository();
        clearMovies();
    }

    @AfterEach
    void tearDown() throws SQLException, DatabaseException {
        clearMovies();
    }

    private void clearMovies() throws SQLException, DatabaseException {
        movieRepository.removeAll();
    }

    @Test
    @Order(1)
    @DisplayName("Add multiple movies and retrieve all movies")
    void shouldAddAndRetrieveAllMovies() throws SQLException, DatabaseException {
        MovieEntity movie1 = new MovieEntity();
        movie1.setApiId("api1");
        movie1.setTitle("Movie 1");

        MovieEntity movie2 = new MovieEntity();
        movie2.setApiId("api2");
        movie2.setTitle("Movie 2");

        movieRepository.addAllMovies(List.of(movie1, movie2));

        List<MovieEntity> movies = movieRepository.getAllMovies();

        assertEquals(2, movies.size());
    }

    @Test
    @Order(2)
    @DisplayName("Remove all movies from the database")
    void shouldRemoveAllMovies() throws SQLException, DatabaseException {
        MovieEntity movie = new MovieEntity();
        movie.setApiId("api3");
        movie.setTitle("Movie 3");

        movieRepository.addAllMovies(List.of(movie));
        movieRepository.removeAll();

        List<MovieEntity> movies = movieRepository.getAllMovies();
        assertTrue(movies.isEmpty());
    }

    @Test
    @Order(3)
    @DisplayName("Retrieve a movie by its ID")
    void shouldRetrieveMovieById() throws SQLException, DatabaseException {
        MovieEntity movie = new MovieEntity();
        movie.setApiId("api4");
        movie.setTitle("Movie 4");

        movieRepository.addAllMovies(List.of(movie));

        List<MovieEntity> movies = movieRepository.getAllMovies();
        assertFalse(movies.isEmpty());

        long id = movies.get(0).getId(); // get ID of the movie

        MovieEntity foundMovie = movieRepository.getMovie(id);

        assertNotNull(foundMovie);
        assertEquals("Movie 4", foundMovie.getTitle());
    }
}