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
    private static boolean h2ConsoleStarted = false; // Track if H2 console is running

    /**
     * starts web-based H2 Console on specified port
     * @throws SQLException
     */
    protected static synchronized void startH2Console() {
        // If console is already started, don't try again
        if (h2ConsoleStarted) {
            throw new DatabaseException("H2 Console is already running");
        }

        System.out.println("Starting H2 Console...");
        
        // Try ports in range 8082-8092
        for (int port = 8082; port <= 8092; port++) {
            try {
                Server server = Server.createWebServer(
                    "-web",
                    "-webAllowOthers",
                    "-webPort", String.valueOf(port)
                );
                server.start();
                h2ConsoleStarted = true;
                System.out.println("H2 Console started at: " + server.getURL());
                return;
            } catch (SQLException e) {
                System.out.println("Port " + port + " is in use, trying next port...");
            }
        }
        
        // If we get here, no ports were available
        System.err.println("Could not start H2 console on any port. Continuing without web console.");
        throw new DatabaseException("Failed to start H2 console on any port");
    }

    /**
     * initializes connectionSource, H2console, DAOs and tables
     * @throws SQLException
     */
    private DatabaseManager() {
        try {
            // Initialize in proper order
            createConnectionSource();
            if (connectionSource == null) {
                throw new DatabaseException("Connection source is null after initialization");
            }
            
            // Try to start H2 console, but continue if it fails
            try {
                startH2Console();
            } catch (Exception e) {
                System.err.println("H2 console could not be started. Continuing without web console.");
            }
            
            // Create DAOs
            movieDao = DaoManager.createDao(connectionSource, MovieEntity.class);
            if (movieDao == null) {
                throw new DatabaseException("MovieDao is null after initialization");
            }
            
            watchlistDao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
            if (watchlistDao == null) {
                throw new DatabaseException("WatchlistDao is null after initialization");
            }
            
            // Create tables last
            createTables();
            
            System.out.println("DatabaseManager initialized successfully");
            
        } catch (SQLException e) {
            String msg = "Failed to initialize DatabaseManager: " + e.getMessage();
            System.err.println(msg);
            throw new DatabaseException(msg, e);
        } catch (Exception e) {
            String msg = "Unexpected error initializing DatabaseManager: " + e.getMessage();
            System.err.println(msg);
            throw new DatabaseException(msg, e);
        }
    }

    /**
     * Initializes the DatabaseManager singleton instance.
     * @return DatabaseManager instance
     */
    public static synchronized DatabaseManager getDatabaseManager() {
        if (instance == null) {
            try {
                instance = new DatabaseManager();
            } catch (Exception e) {
                System.err.println("Failed to create DatabaseManager instance: " + e.getMessage());
                throw e; // Re-throw to ensure caller knows about the failure
            }
        }
        return instance;
    }

    /**
     * creates the SQL tables for MovieEntity and WatchlistMovieEntity classes
     * @throws SQLException
     */
    protected static void createTables() throws SQLException{
        try {
            TableUtils.dropTable(connectionSource, MovieEntity.class, true);
            TableUtils.dropTable(connectionSource, WatchlistMovieEntity.class, true);
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
