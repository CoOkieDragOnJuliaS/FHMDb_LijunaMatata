package org.fhmdb.fhmdb_lijunamatata.utils;

import javafx.application.Platform;

/**
 * Utility class to safely initialize the JavaFX Toolkit once.
 * <p>
 * This class is primarily intended for use in unit and integration tests
 * where JavaFX components (e.g., Platform.runLater, GUI controllers) need to be tested
 * without launching a full JavaFX Application.
 * <p>
 * Calling {@code JavaFxToolkitInitializer.initialize()} ensures that the JavaFX runtime
 * is available and avoids IllegalStateException due to multiple initializations.
 * <p>
 * It is safe to call from multiple test classes or threads.
 */
public class JavaFxToolkitInitializer {
    private static boolean initialized = false;

    /**
     * Ensures JavaFX Toolkit is started exactly once.
     * Safe to call from multiple test classes.
     */
    public static synchronized void initialize() {
        if (!initialized) {
            try {
                Platform.startup(() -> {});
            } catch (IllegalStateException ignored) {
                // Toolkit already initialized — ignore
            }
            initialized = true;
        }
    }
}
