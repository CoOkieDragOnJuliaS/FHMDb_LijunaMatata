package org.fhmdb.fhmdb_lijunamatata.factory;

import javafx.util.Callback;
import org.fhmdb.fhmdb_lijunamatata.controller.FHMDbController;
import org.fhmdb.fhmdb_lijunamatata.controller.WatchlistController;

/**
 * Factory Class that returns Singleton Controller objects
 */
public class ControllerFactory implements Callback<Class<?>, Object> {
    private final FHMDbController fhmdbController = new FHMDbController(); //instantiated on factory instantiation
    private final WatchlistController watchlistController = new WatchlistController();

    /**
     * gets called by FXMLLoader to know which controller it should pick
     * @param aClass that should be created or returned
     * @return the singleton instance of aClass
     */
    @Override
    public Object call(Class<?> aClass) {
        if (aClass == FHMDbController.class) {
            return fhmdbController;
        } else if (aClass == WatchlistController.class) {
            return watchlistController;
        }

        //if there's another class we haven't covered yet:
        try {
            return aClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            System.err.println("couldn't instantiate " + aClass.getName() + ". ");
        }
        return null;


    }
}
