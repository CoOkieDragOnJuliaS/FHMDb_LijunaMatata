package org.fhmdb.fhmdb_lijunamatata.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.fhmdb.fhmdb_lijunamatata.exceptions.DatabaseException;
import org.h2.tools.Server;

import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_URL ="jdbc:h2:file:./db/moviesdb"; //Path for embedded version of H2
    private static final String username = "user";
    private static final String password = "password";
    private static ConnectionSource connectionSource;
    Dao<MovieEntity, Long> movieDao; //DAO = Data Access Object
    Dao<WatchlistMovieEntity, Long> watchlistDao;
    private static DatabaseManager instance; //Singleton Pattern: we only want a single instance of DatabaseManager

    /**
     * starts web-based H2 Console on specified port
     * @throws SQLException
     */
    protected static void startH2Console() throws SQLException {
        try {
            Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
            System.out.println("H2 Console: http://localhost:8082");
        } catch (SQLException e) {
            // Wrap H2 startup issues in a higher-level custom exception
            throw new DatabaseException("Failed to start H2 console", e);
        }
    }

    /**
     * initializes connectionSource, H2console, DAOs and tables
     * @throws SQLException
     */
    private DatabaseManager() {
        try {
            createConnectionSource();
            startH2Console();
            movieDao = DaoManager.createDao(connectionSource, MovieEntity.class);
            watchlistDao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
            createTables();
        } catch (SQLException e) {
            // Wrap low-level SQLException into a custom unchecked DatabaseException
            throw new DatabaseException("Failed to initialize DatabaseManager", e);
        }
    }

    /**
     * Initializes the DatabaseManager singleton instance.
     * @return DatabaseManager instance
     */
    public static DatabaseManager getDatabaseManager() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * creates the SQL tables for MovieEntity and WatchlistMovieEntity classes
     * @throws SQLException
     */
    protected static void createTables() throws SQLException{
        try {
            TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, WatchlistMovieEntity.class);
        } catch (SQLException e) {
            // Ensure table creation failures are not silently ignored
            throw new DatabaseException("Failed to create tables", e);
        }
    }

    /**
     * initializes the connection to the database
     * @throws SQLException
     */
    protected static void createConnectionSource() throws SQLException{
        try {
            connectionSource = new JdbcConnectionSource(DB_URL, username, password);
        } catch (SQLException e) {
            // Clearly signals that DB connection failed
            throw new DatabaseException("Error while creating connection source", e);
        }

    }

    public Dao<WatchlistMovieEntity, Long> getWatchlistDao() {
        return watchlistDao;
    }

    public Dao<MovieEntity, Long> getMovieDao() {
        return movieDao;
    }

    public static ConnectionSource getConnectionSource() {
        return connectionSource;
    }
}
