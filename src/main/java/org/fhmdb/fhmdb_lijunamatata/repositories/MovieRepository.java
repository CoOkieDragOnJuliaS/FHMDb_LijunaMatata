package org.fhmdb.fhmdb_lijunamatata.repositories;

import com.j256.ormlite.dao.Dao;
import org.fhmdb.fhmdb_lijunamatata.database.DatabaseManager;
import org.fhmdb.fhmdb_lijunamatata.database.MovieEntity;
import org.fhmdb.fhmdb_lijunamatata.exceptions.DatabaseException;

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
    public MovieRepository() {
        DatabaseManager dbManager = DatabaseManager.getDatabaseManager();
        if (dbManager != null) {
            this.movieDao = dbManager.getMovieDao();
            if (this.movieDao == null) {
                throw new DatabaseException("MovieDao is null after initialization");
            }
        } else {
            throw new DatabaseException("DatabaseManager is not initialized");
        }
    }

    /**
     * Retrieves all movies from the database.
     * @return a list of MovieEntity objects
     * @throws DatabaseException if the query fails
     */
    public List<MovieEntity> getAllMovies() {
        try {
            return movieDao.queryForAll();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch movies from the database", e);
        }
    }


    /**
     * Deletes all movies from the database.
     * @throws DatabaseException if database delete operation fails
     */
    public void removeAll() {
        try {
            movieDao.deleteBuilder().delete();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete all movies", e);
        }
    }


    /**
     * Retrieves a movie by its ID from the database.
     * @param id the ID of the movie
     * @return the MovieEntity object, or null if not found
     * @throws DatabaseException database query fails
     */
    public MovieEntity getMovie(long id) {
        try {
            return movieDao.queryForId(id);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch movie with ID: " + id, e);
        }
    }

    /**
     * Adds a list of movies to the database. If a movie already exists, it is updated.
     * @param movies a list of MovieEntity objects to be added
     * @throws DatabaseException if database insert/update operation fails
     */
    public void addAllMovies(List<MovieEntity> movies) {
        try {
            for (MovieEntity movie : movies) {
                movieDao.createOrUpdate(movie);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to save movies to the database", e);
        }
    }
}
