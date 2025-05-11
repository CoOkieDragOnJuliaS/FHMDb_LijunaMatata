package org.fhmdb.fhmdb_lijunamatata.repositories;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import org.fhmdb.fhmdb_lijunamatata.database.DatabaseManager;
import org.fhmdb.fhmdb_lijunamatata.database.MovieEntity;
import org.fhmdb.fhmdb_lijunamatata.database.WatchlistMovieEntity;
import org.fhmdb.fhmdb_lijunamatata.exceptions.DatabaseException;

import java.sql.SQLException;
import java.util.List;

/**
 * Repository class for accessing WatchlistMovieEntity data in the database.
 */
public class WatchlistRepository {

    private final Dao<WatchlistMovieEntity, Long> watchlistDao;

    /**
     * Constructor that initializes the WatchlistRepository by obtaining the DAO instance.
     * @throws DatabaseException if database access fails
     */
    public WatchlistRepository() {
        DatabaseManager dbManager = DatabaseManager.getDatabaseManager();
        if (dbManager != null) {
            this.watchlistDao = dbManager.getWatchlistDao();
            if (this.watchlistDao == null) {
                throw new DatabaseException("WatchlistDao is null after initialization");
            }
        } else {
            throw new DatabaseException("DatabaseManager is not initialized");
        }
    }

    /**
     * Retrieves all movies in the watchlist.
     * @return a list of WatchlistMovieEntity objects
     * @throws DatabaseException if database query fails
     */
    public List<WatchlistMovieEntity> getWatchlist() {
        try {
            return watchlistDao.queryForAll();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to load watchlist", e);
        }
    }

    /**
     * Adds a movie to the watchlist if it does not already exist.
     * @param movieEntity the movie to add
     * @throws DatabaseException if database insert operation fails
     */
    public void addToWatchlist(MovieEntity movieEntity) {
        try {
            if (!existsInWatchlist(movieEntity.getApiId())) {
                watchlistDao.create(new WatchlistMovieEntity(movieEntity.getApiId()));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to add movie to watchlist", e);
        }
    }


    /**
     * Removes a movie from the watchlist by its API ID.
     * @param apiId the API ID of the movie
     * @throws DatabaseException if database delete operation fails
     */
    public void removeFromWatchlist(String apiId) {
        try {
            QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = watchlistDao.queryBuilder();
            queryBuilder.where().eq("apiId", apiId);
            List<WatchlistMovieEntity> entries = watchlistDao.query(queryBuilder.prepare());

            for (WatchlistMovieEntity entry : entries) {
                watchlistDao.delete(entry);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to remove movie from watchlist", e);
        }
    }

    /**
     * Checks if a movie with the given API ID already exists in the watchlist.
     * @param apiId the API ID of the movie
     * @return true if the movie exists, false otherwise
     * @throws DatabaseException if database query fails
     */
    private boolean existsInWatchlist(String apiId) {
        try {
            QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = watchlistDao.queryBuilder();
            queryBuilder.setCountOf(true);
            queryBuilder.where().eq("apiId", apiId);
            return watchlistDao.countOf(queryBuilder.prepare()) > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to check if movie exists in watchlist", e);
        }
    }

    public List<MovieEntity> getWatchlistMovies() {
        List<WatchlistMovieEntity> watchlist = getWatchlist();

        try {
            MovieRepository movieRepository = new MovieRepository();
            List<MovieEntity> allMovies = movieRepository.getAllMovies();

            return allMovies.stream()
                    .filter(movieEntity -> watchlist.stream()
                            .anyMatch(watchlistMovieEntity  ->
                                    watchlistMovieEntity.getApiId().equals(movieEntity.getApiId())))
                    .toList();
        } catch (Exception e) {
            throw new DatabaseException("Failed to get movies from watchlist", e);
        }
    }
}
