package org.fhmdb.fhmdb_lijunamatata.repositories;

import com.j256.ormlite.dao.Dao;
import org.fhmdb.fhmdb_lijunamatata.database.DatabaseManager;
import org.fhmdb.fhmdb_lijunamatata.database.MovieEntity;

import java.sql.SQLException;
import java.util.List;

/**
 * Repository class for accessing MovieEntity data in the database.
 */
public class MovieRepository {

    private final Dao<MovieEntity, Long> movieDao; // DAO object for accessing MovieEntity table

    /**
     * Constructor that initializes the MovieRepository by obtaining the DAO instance.
     * @throws SQLException if database access fails
     */
    public MovieRepository() throws SQLException {
        this.movieDao = DatabaseManager.getDatabaseManager().getMovieDao();
    }

    /**
     * Retrieves all movies from the database.
     * @return a list of MovieEntity objects
     * @throws SQLException if database query fails
     */
    public List<MovieEntity> getAllMovies() throws SQLException {
        return movieDao.queryForAll();
    }

    /**
     * Deletes all movies from the database.
     * @throws SQLException if database delete operation fails
     */
    public void removeAll() throws SQLException {
        movieDao.deleteBuilder().delete();
    }

    /**
     * Retrieves a movie by its ID from the database.
     * @param id the ID of the movie
     * @return the MovieEntity object, or null if not found
     * @throws SQLException if database query fails
     */
    public MovieEntity getMovie(long id) throws SQLException {
        return movieDao.queryForId(id);
    }

    /**
     * Adds a list of movies to the database. If a movie already exists, it is updated.
     * @param movies a list of MovieEntity objects to be added
     * @throws SQLException if database insert/update operation fails
     */
    public void addAllMovies(List<MovieEntity> movies) throws SQLException {
        for (MovieEntity movie : movies) {
            movieDao.createOrUpdate(movie);
        }
    }
}
