package org.fhmdb.fhmdb_lijunamatata.repositories;

import org.fhmdb.fhmdb_lijunamatata.database.MovieEntity;
import org.fhmdb.fhmdb_lijunamatata.database.WatchlistMovieEntity;
import org.fhmdb.fhmdb_lijunamatata.exceptions.DatabaseException;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WatchlistRepositoryTest {

    private WatchlistRepository watchlistRepository;

    @BeforeEach
    void setUp() throws SQLException, DatabaseException {
        watchlistRepository = WatchlistRepository.getInstance();
        clearWatchlist();
    }

    @AfterEach
    void tearDown() throws SQLException, DatabaseException {
        clearWatchlist();
    }

    private void clearWatchlist() throws SQLException, DatabaseException {
        for (WatchlistMovieEntity movie : watchlistRepository.getWatchlist()) {
            watchlistRepository.removeFromWatchlist(movie.getApiId());
        }
    }

    @Test
    @Order(1)
    @DisplayName("Add a movie to the watchlist successfully")
    void shouldAddMovieToWatchlist() throws SQLException, DatabaseException {
        MovieEntity movie = new MovieEntity();
        movie.setApiId("api1");

        watchlistRepository.addToWatchlist(movie);
        List<WatchlistMovieEntity> watchlist = watchlistRepository.getWatchlist();

        assertEquals(1, watchlist.size());
        assertEquals("api1", watchlist.get(0).getApiId());
    }

    @Test
    @Order(2)
    @DisplayName("Prevent adding the same movie twice")
    void shouldNotAddDuplicateMovieToWatchlist() throws SQLException, DatabaseException {
        MovieEntity movie = new MovieEntity();
        movie.setApiId("api2");

        watchlistRepository.addToWatchlist(movie);
        watchlistRepository.addToWatchlist(movie); // try to add again

        List<WatchlistMovieEntity> watchlist = watchlistRepository.getWatchlist();

        assertEquals(1, watchlist.size());
    }

    @Test
    @Order(3)
    @DisplayName("Remove a movie from the watchlist by API ID")
    void shouldRemoveMovieFromWatchlistByApiId() throws SQLException, DatabaseException {
        MovieEntity movie = new MovieEntity();
        movie.setApiId("api3");

        watchlistRepository.addToWatchlist(movie);
        assertEquals(1, watchlistRepository.getWatchlist().size());

        watchlistRepository.removeFromWatchlist("api3");

        assertEquals(0, watchlistRepository.getWatchlist().size());
    }
}
