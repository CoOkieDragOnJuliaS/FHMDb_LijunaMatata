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
     * @throws SQLException if database access fails
     */
    public WatchlistRepository() {
        try {
            this.watchlistDao = DatabaseManager.getDatabaseManager().getWatchlistDao();
        }catch(SQLException e) {
            //TODO: Catching the Exception the earliest?
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Retrieves all movies in the watchlist.
     * @return a list of WatchlistMovieEntity objects
     * @throws SQLException if database query fails
     */
    public List<WatchlistMovieEntity> getWatchlist() throws SQLException {
        return watchlistDao.queryForAll();
    }

    /**
     * Adds a movie to the watchlist if it does not already exist.
     * @param movieEntity the movie to add
     * @throws SQLException if database insert operation fails
     */
    public void addToWatchlist(MovieEntity movieEntity) throws SQLException {
        if (!existsInWatchlist(movieEntity.getApiId())) {
            watchlistDao.create(new WatchlistMovieEntity(movieEntity.getApiId()));
        }
    }

    /**
     * Removes a movie from the watchlist by its API ID.
     * @param apiId the API ID of the movie
     * @throws SQLException if database delete operation fails
     */
    public void removeFromWatchlist(String apiId) throws SQLException {
        QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = watchlistDao.queryBuilder();
        queryBuilder.where().eq("apiId", apiId);
        List<WatchlistMovieEntity> entries = watchlistDao.query(queryBuilder.prepare());

        for (WatchlistMovieEntity entry : entries) {
            watchlistDao.delete(entry);
        }
    }

    /**
     * Checks if a movie with the given API ID already exists in the watchlist.
     * @param apiId the API ID of the movie
     * @return true if the movie exists, false otherwise
     * @throws SQLException if database query fails
     */
    private boolean existsInWatchlist(String apiId) throws SQLException {
        QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = watchlistDao.queryBuilder();
        queryBuilder.setCountOf(true);
        queryBuilder.where().eq("apiId", apiId);
        return watchlistDao.countOf(queryBuilder.prepare()) > 0;
    }
}
