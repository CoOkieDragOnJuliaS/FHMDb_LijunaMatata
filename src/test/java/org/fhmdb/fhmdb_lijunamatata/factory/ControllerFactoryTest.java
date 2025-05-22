package org.fhmdb.fhmdb_lijunamatata.factory;

import org.fhmdb.fhmdb_lijunamatata.controller.FHMDbController;
import org.fhmdb.fhmdb_lijunamatata.controller.WatchlistController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerFactoryTest {
    private ControllerFactory factory;

    @BeforeEach
    public void setUp() {
        factory = new ControllerFactory();
    }

    @Test
    public void returnsSameFhmdbControllerInstance() {
        FHMDbController first = (FHMDbController) factory.call(FHMDbController.class);
        FHMDbController second = (FHMDbController) factory.call(FHMDbController.class);

        assertSame(first, second, "Factory should return the same FHMDbController instance");
    }

    @Test
    public void returnsSameWatchlistControllerInstance() {
        WatchlistController first = (WatchlistController) factory.call(WatchlistController.class);
        WatchlistController second = (WatchlistController) factory.call(WatchlistController.class);

        assertSame(first, second, "Factory should return the same WatchlistController instance");
    }

    @Test
    public void createsNewInstanceForUnknownClass() {
        Object obj1 = factory.call(DummyClass.class);
        Object obj2 = factory.call(DummyClass.class);

        assertNotNull(obj1, "Should return a new instance");
        assertNotSame(obj1, obj2, "Should return a different instance each time for unknown classes");
    }

    // Dummy class for testing fallback
    static class DummyClass {
        public DummyClass() {}
    }
}
