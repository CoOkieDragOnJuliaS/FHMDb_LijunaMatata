package org.fhmdb.fhmdb_lijunamatata.database;

import com.j256.ormlite.support.ConnectionSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTest {
    private static String DB_URL ="jdbc:h2:file:./db/moviesdb"; //Path for embedded version of H2
    private static String username = "user";
    private static String password = "password";
    private static ConnectionSource connectionSource;

    @Nested
    class CreateConnectionSourceTests {
        @Test
        @DisplayName("Test createConnectionSouce: correct credentials don't throw exception")
        public void test_createConnectionSouce_correct_credentials_dont_throw_excpetion() throws SQLException {
            assertDoesNotThrow(DatabaseManager::createConnectionSource);
        }

        @Test
        @DisplayName("Test createConnectionSouce: correct credentials initialize connectionSource")
        public void test_createConnectionSouce_correct_credentials_initialize_connectionSource() throws SQLException {
            DatabaseManager.createConnectionSource();
            assertNotNull(connectionSource);
        }

        @Test
        @DisplayName("Test createConnectionSouce: incorrect username throws exception")
        public void test_createConnectionSouce_incorrect_username_throws_excpetion() throws SQLException {
            username = "wrong username";
            assertThrows(SQLException.class, DatabaseManager::createConnectionSource);
        }

        @Test
        @DisplayName("Test createConnectionSouce: incorrect password throws exception")
        public void test_createConnectionSouce_incorrect_password_throws_excpetion() throws SQLException {
            password = "wrong password";
            assertThrows(SQLException.class, DatabaseManager::createConnectionSource);
        }

        @Test
        @DisplayName("Test createConnectionSouce: incorrect DB url throws exception")
        public void test_createConnectionSouce_incorrect_db_url_throws_excpetion() throws SQLException {
            DB_URL = "wrong db url";
            assertThrows(SQLException.class, DatabaseManager::createConnectionSource);
        }

        @Nested
        class CreateTablesTests {
            @BeforeEach
            void setup() throws SQLException{
                DatabaseManager.createConnectionSource();
            }

            @Test
            @DisplayName("Test createTables: valid connectionSource does not throw exception")
            public void test_createTables_valid_connectionSource_doesnt_throw_exception() throws SQLException{
                assertDoesNotThrow(DatabaseManager::createTables);
            }

        }

        @Nested
        class H2ConsoleTests {
            @Test
            @DisplayName("Test startH2Console: does not throw exception")
            public void test_startH2Console_does_not_throw_exception() {
                assertDoesNotThrow(DatabaseManager::startH2Console);
            }
        }
    }
}
