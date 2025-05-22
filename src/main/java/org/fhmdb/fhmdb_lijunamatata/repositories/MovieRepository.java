package org.fhmdb.fhmdb_lijunamatata.repositories;

import com.j256.ormlite.dao.Dao;
import org.fhmdb.fhmdb_lijunamatata.database.DatabaseManager;
import org.fhmdb.fhmdb_lijunamatata.database.MovieEntity;
import org.fhmdb.fhmdb_lijunamatata.exceptions.DatabaseException;

import java.sql.SQLException;
import java.util.List;

/**
 * Repository class for accessing MovieEntity data in the database.
 * All checked SQLExceptions are caught and rethrown as DatabaseException to enforce explicit handling.
 */
public class MovieRepository {

    private final Dao<MovieEntity, Long> movieDao; // DAO object for accessing MovieEntity table

    /**
     * Constructor that initializes the MovieRepository by obtaining the DAO instance.
     * Throws DatabaseException if the database manager or DAO is not initialized.
     *
     * @throws DatabaseException if DatabaseManager or movieDao is not properly initialized
     */
    public MovieRepository() throws DatabaseException {
        DatabaseManager dbManager = DatabaseManager.getDatabaseManager();
        if (dbManager == null) {
            throw new DatabaseException("DatabaseManager is not initialized");
        }
        this.movieDao = dbManager.getMovieDao();
        if (this.movieDao == null) {
            throw new DatabaseException("MovieDao is null after initialization");
        }
    }

    /**
     * Retrieves all movies from the database.
     *
     * @return List of MovieEntity objects representing all movies in the database
     * @throws DatabaseException if an SQL exception occurs during query
     */
    public List<MovieEntity> getAllMovies() throws DatabaseException {
        try {
            return movieDao.queryForAll();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch movies from the database", e);
        }
    }

    /**
     * Deletes all movies from the database.
     *
     * @throws DatabaseException if an SQL exception occurs during delete operation
     */
    public void removeAll() throws DatabaseException {
        try {
            movieDao.deleteBuilder().delete();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete all movies", e);
        }
    }

    /**
     * Retrieves a movie by its ID from the database.
     *
     * @param id The ID of the movie
     * @return The MovieEntity object if found, or null if no movie matches the ID
     * @throws DatabaseException if an SQL exception occurs during query
     */
    public MovieEntity getMovie(long id) throws DatabaseException {
        try {
            return movieDao.queryForId(id);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch movie with ID: " + id, e);
        }
    }

    /**
     * Adds or updates a list of movies in the database.
     * Each movie is either created or updated based on whether it already exists.
     *
     * @param movies List of MovieEntity objects to be added or updated
     * @throws DatabaseException if an SQL exception occurs during create or update operations
     */
    public void addAllMovies(List<MovieEntity> movies) throws DatabaseException {
        try {
            for (MovieEntity movie : movies) {
                movieDao.createOrUpdate(movie);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to save movies to the database", e);
        }
    }
}

