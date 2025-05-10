package org.fhmdb.fhmdb_lijunamatata.database;

import com.j256.ormlite.support.ConnectionSource;
import org.fhmdb.fhmdb_lijunamatata.exceptions.DatabaseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTest {

    @Nested
    class ConnectionSourceTests {

        @Test
        @DisplayName("createConnectionSource should not throw with correct credentials")
        void test_createConnectionSource_validCredentials_doesNotThrow() {
            assertDoesNotThrow(() -> DatabaseManager.createConnectionSource());
        }

        @Test
        @DisplayName("getConnectionSource should return a non-null object after initialization")
        void test_getConnectionSource_returns_notNull_afterCreate() throws SQLException {
            DatabaseManager.createConnectionSource();
            ConnectionSource source = DatabaseManager.getConnectionSource();
            assertNotNull(source, "ConnectionSource should not be null after initialization");
        }
    }

    @Nested
    class CreateTablesTests {

        @Test
        @DisplayName("createTables should not throw exception after valid connection")
        void test_createTables_doesNotThrow() throws SQLException {
            DatabaseManager.createConnectionSource();
            assertDoesNotThrow(() -> DatabaseManager.createTables());
        }
    }

    @Nested
    class H2ConsoleTests {

        /**
         * This test ensures that the H2 console can either start successfully
         * or fail gracefully with a DatabaseException when the port (8082) is already in use.
         */
        @Test
        @DisplayName("startH2Console should either succeed or throw DatabaseException if port is in use")
        void test_startH2Console_shouldStartOrFailGracefully() {
            try {
                DatabaseManager.startH2Console();
            } catch (DatabaseException e) {
                // Acceptable outcome if port 8082 is already in use
                assertTrue(e.getMessage().contains("Failed to start H2 console"),
                        "Expected exception message to indicate console startup failure");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * This test ensures that attempting to start the H2 console a second time
         * will throw a DatabaseException if the port is already taken.
         * The first call may fail or succeed depending on test environment, but the second must fail.
         */
        @Test
        @DisplayName("startH2Console should throw DatabaseException on second call if port already bound")
        void test_startH2Console_secondCallAlwaysFails() {
            // Try once: either succeed or already in use
            try {
                DatabaseManager.startH2Console();
            } catch (DatabaseException ignored) {
                // Port might already be in use; ignore for first call
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // Second call must throw DatabaseException
            assertThrows(DatabaseException.class, DatabaseManager::startH2Console,
                    "Expected DatabaseException because the port is already bound");
        }
    }

    @Nested
    class SingletonTests {

        @Test
        @DisplayName("getDatabaseManager should return a non-null singleton instance")
        void test_getDatabaseManager_returnsInstance() {
            DatabaseManager instance = DatabaseManager.getDatabaseManager();
            assertNotNull(instance, "DatabaseManager instance should not be null");
        }

        @Test
        @DisplayName("getWatchlistDao and getMovieDao should return DAOs")
        void test_getDaos_areNotNull() {
            DatabaseManager instance = DatabaseManager.getDatabaseManager();
            assertNotNull(instance.getMovieDao(), "MovieDao should not be null");
            assertNotNull(instance.getWatchlistDao(), "WatchlistDao should not be null");
        }
    }

}
