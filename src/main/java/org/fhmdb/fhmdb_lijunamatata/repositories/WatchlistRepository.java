package org.fhmdb.fhmdb_lijunamatata.repositories;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import org.fhmdb.fhmdb_lijunamatata.database.DatabaseManager;
import org.fhmdb.fhmdb_lijunamatata.database.MovieEntity;
import org.fhmdb.fhmdb_lijunamatata.database.WatchlistMovieEntity;
import org.fhmdb.fhmdb_lijunamatata.exceptions.DatabaseException;
import org.fhmdb.fhmdb_lijunamatata.observer.WatchlistObserver;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

    /**
    * Repository class for accessing WatchlistMovieEntity data in the database.
    * Implements Observable pattern for notifying watchers on data changes.
    */
    public class WatchlistRepository {

    private static WatchlistRepository instance;
    private final Dao<WatchlistMovieEntity, Long> watchlistDao;
    private final List<WatchlistObserver> observers = new ArrayList<>();

    /**
     * Returns the singleton instance of WatchlistRepository.
     * Initializes the instance if it has not been created yet.
     *
     * @return the single instance of WatchlistRepository
     * @throws DatabaseException if the repository fails to initialize
     */
    public static WatchlistRepository getInstance() throws DatabaseException {
        if (instance == null) {
            instance = new WatchlistRepository();
        }
        return instance;
    }
    /**
     * Registers an observer to be notified when the watchlist changes.
     * @param observer the observer to add
     */
    public void addObserver(WatchlistObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from notifications.
     * @param observer the observer to remove
     */
    public void removeObserver(WatchlistObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers with the current watchlist movies.
     * @throws DatabaseException if fetching watchlist movies fails
     */
    private void notifyObservers() throws DatabaseException {
        List<Movie> updatedWatchlist = MovieEntity.toMovies(getWatchlistMovies());
        for (WatchlistObserver observer : observers) {
            observer.onWatchlistChanged(updatedWatchlist);
        }
    }

    /**
     * Constructs the repository, initializing the DAO.
     * @throws DatabaseException if DatabaseManager or DAO are not properly initialized
     */
    private WatchlistRepository() throws DatabaseException {
        DatabaseManager dbManager = DatabaseManager.getDatabaseManager();
        if (dbManager == null) {
            throw new DatabaseException("DatabaseManager is not initialized");
        }
        this.watchlistDao = dbManager.getWatchlistDao();
        if (this.watchlistDao == null) {
            throw new DatabaseException("WatchlistDao is null after initialization");
        }
    }

    /**
     * Retrieves all watchlist entries from the database.
     * @return List of WatchlistMovieEntity
     * @throws DatabaseException if query fails
     */
    public List<WatchlistMovieEntity> getWatchlist() throws DatabaseException {
        try {
            return watchlistDao.queryForAll();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to load watchlist", e);
        }
    }

    /**
     * Adds a movie to the watchlist if not already present.
     * Notifies observers upon success.
     * @param movieEntity movie to add
     * @throws DatabaseException if DB error occurs during add or notify
     */
    public void addToWatchlist(MovieEntity movieEntity) throws DatabaseException {
        try {
            if (!existsInWatchlist(movieEntity.getApiId())) {
                watchlistDao.create(new WatchlistMovieEntity(movieEntity.getApiId()));
                notifyObservers();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to add movie to watchlist", e);
        }
    }

    /**
     * Removes a movie from watchlist by API ID.
     * Notifies observers upon success.
     * @param apiId API ID of movie to remove
     * @throws DatabaseException if DB error occurs during removal or notify
     */
    public void removeFromWatchlist(String apiId) throws DatabaseException {
        try {
            QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = watchlistDao.queryBuilder();
            queryBuilder.where().eq("apiId", apiId);
            List<WatchlistMovieEntity> entries = watchlistDao.query(queryBuilder.prepare());

            for (WatchlistMovieEntity entry : entries) {
                watchlistDao.delete(entry);
            }

            notifyObservers();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to remove movie from watchlist", e);
        }
    }

    /**
     * Checks whether a movie is already in the watchlist by API ID.
     * @param apiId API ID to check
     * @return true if movie exists, false otherwise
     * @throws DatabaseException if DB query fails
     */
    private boolean existsInWatchlist(String apiId) throws DatabaseException {
        try {
            QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = watchlistDao.queryBuilder();
            queryBuilder.setCountOf(true);
            queryBuilder.where().eq("apiId", apiId);
            return watchlistDao.countOf(queryBuilder.prepare()) > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to check if movie exists in watchlist", e);
        }
    }

    /**
     * Retrieves list of MovieEntity objects corresponding to the movies in the watchlist.
     * @return list of MovieEntity
     * @throws DatabaseException if DB errors occur during retrieval
     */
    public List<MovieEntity> getWatchlistMovies() throws DatabaseException {
        List<WatchlistMovieEntity> watchlist = getWatchlist();
        try {
            MovieRepository movieRepository = MovieRepository.getInstance();
            List<MovieEntity> allMovies = movieRepository.getAllMovies();

            return allMovies.stream()
                    .filter(movieEntity -> watchlist.stream()
                            .anyMatch(watchlistMovieEntity ->
                                    watchlistMovieEntity.getApiId().equals(movieEntity.getApiId())))
                    .toList();

        } catch (DatabaseException e) {
            throw new DatabaseException("Failed to get movies from watchlist", e);
        }
    }
}
