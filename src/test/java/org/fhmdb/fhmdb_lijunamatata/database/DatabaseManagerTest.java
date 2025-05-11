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
         * This test ensures that attempting to start the H2 console a second time
         * will throw a DatabaseException.
         */
        @Test
        @DisplayName("startH2Console should throw DatabaseException on second call")
        void test_startH2Console_secondCallAlwaysFails() {
            // First call might succeed or fail depending on port availability
            try {
                DatabaseManager.startH2Console();
            } catch (DatabaseException e) {
                // Print actual error message for debugging
                System.out.println("Actual error message: " + e.getMessage());
                // Either message is acceptable for the first call
                boolean isValidMessage = 
                    e.getMessage().contains("Failed to start H2 console on any port") ||
                    e.getMessage().contains("H2 Console is already running");
                assertTrue(isValidMessage,
                        "Expected either 'Failed to start H2 console on any port' or 'H2 Console is already running' but got: '" + e.getMessage() + "'");
                return;
            }

            // If first call succeeded, second call must fail with "already running"
            DatabaseException e = assertThrows(DatabaseException.class, 
                DatabaseManager::startH2Console,
                "Expected DatabaseException on second call");
            
            assertTrue(e.getMessage().contains("already running"),
                "Expected exception to indicate console is already running");
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
