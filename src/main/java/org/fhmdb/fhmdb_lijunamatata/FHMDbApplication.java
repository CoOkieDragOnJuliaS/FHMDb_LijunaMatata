package org.fhmdb.fhmdb_lijunamatata;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.fhmdb.fhmdb_lijunamatata.controller.FHMDbController;
import org.fhmdb.fhmdb_lijunamatata.controller.WatchlistController;
import org.fhmdb.fhmdb_lijunamatata.database.DatabaseManager;
import org.fhmdb.fhmdb_lijunamatata.exceptions.DatabaseException;
import org.fhmdb.fhmdb_lijunamatata.factory.ControllerFactory;
import org.fhmdb.fhmdb_lijunamatata.ui.SceneRoot;

import java.io.IOException;
import java.util.Objects;

public class FHMDbApplication extends Application {

    private BorderPane root;
    private FHMDbController fhmdbController;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            // Initialize database first
            DatabaseManager.getDatabaseManager();
            //Initialize factory
            ControllerFactory controllerFactory = new ControllerFactory();

            // Load FXML views and assign factory
            FXMLLoader fxmlLoaderMoviesView = new FXMLLoader(FHMDbApplication.class.getResource("fhmdb-view.fxml"));
            fxmlLoaderMoviesView.setControllerFactory(controllerFactory);
            // Get the controller instance from the FXMLLoader
            this.fhmdbController = fxmlLoaderMoviesView.getController();

            FXMLLoader fxmlLoaderWatchlistView = new FXMLLoader(FHMDbApplication.class.getResource("watchlist-view.fxml"));
            fxmlLoaderWatchlistView.setControllerFactory(controllerFactory);

            //create and style singleton instance of scene root
            root = SceneRoot.getInstance(fxmlLoaderMoviesView, fxmlLoaderWatchlistView);

            //Sets the scene by loading the .fxml element, setting the size and adding
            //elements like stylesheet (for css) and "setting the stage" for the scene
            Scene scene = new Scene(root, 1280, 720);
            scene.getStylesheets().add(Objects.requireNonNull(FHMDbApplication.class.getResource("styles.css")).toExternalForm());
            stage.setTitle("FHMDb");
            stage.setScene(scene);
            stage.show();

        } catch (DatabaseException e) {
            System.err.println("Failed to initialize DB: " + e.getMessage());
            // Show error dialog to user
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Failed to initialize database");
            alert.setContentText("The application could not start because the database initialization failed: " + e.getMessage());
            alert.showAndWait();
            // Exit application
            javafx.application.Platform.exit();
        }
    }

    @Override
    public void stop() {
        //Shutting down the scheduler of the controller when application is closing
        //so that the scheduler is not running in the background while the application is not open

        if(this.fhmdbController != null) {
            this.fhmdbController.shutdownScheduler();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}