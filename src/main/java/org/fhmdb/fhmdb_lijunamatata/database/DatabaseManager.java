package org.fhmdb.fhmdb_lijunamatata.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
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
     * start web-based admin console on port 8082
     * @throws SQLException
     */
    protected static void startH2Console() throws SQLException {
        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
        System.out.println("H2 Console: http://localhost:8082");
    }

    private DatabaseManager() throws SQLException{
        try {
            createConnectionSource();
            startH2Console();
            movieDao = DaoManager.createDao(connectionSource, MovieEntity.class);
            watchlistDao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
            createTables();
        } catch (SQLException e) {
            throw new SQLException();
            //throw new DatabaseException("Error while constructing DatabaseManager", e);
        }
    }

    public void testDB() {
        MovieEntity movie = new MovieEntity();
    }

    public static DatabaseManager getDatabaseManager() throws SQLException{
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    protected static void createTables() throws SQLException{
        TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, WatchlistMovieEntity.class);
    }

    protected static void createConnectionSource() throws SQLException{
        try {
            connectionSource = new JdbcConnectionSource(DB_URL, username, password);
        } catch (SQLException e) {
            throw new SQLException("Error while creating Connection source", e);
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
