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

import java.io.IOException;
import java.util.Objects;

public class FHMDbApplication extends Application {
    private FHMDbController fhmdbController;
    private WatchlistController watchlistController;
    private BorderPane root;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            // Initialize database first
            DatabaseManager.getDatabaseManager();

            // Load FXML views
            FXMLLoader fxmlLoaderMoviesView = new FXMLLoader(FHMDbApplication.class.getResource("fhmdb-view.fxml"));
            Parent moviesRoot = fxmlLoaderMoviesView.load();
            FXMLLoader fxmlLoaderWatchlistView = new FXMLLoader(FHMDbApplication.class.getResource("watchlist-view.fxml"));
            Parent watchlistRoot = fxmlLoaderWatchlistView.load();

            root = new BorderPane();

            //Toolbar/Navbar
            Label navigationLabel = new Label();
            navigationLabel.setText("Navigation-Menu");
            Separator separator = new Separator();
            Button buttonMovieView = new Button("Home");
            Button buttonWatchlistView = new Button("Watchlist");

            ToolBar toolBar = new ToolBar(navigationLabel, separator, buttonMovieView, buttonWatchlistView);
            buttonMovieView.maxWidthProperty().bind(root.widthProperty());
            buttonWatchlistView.maxWidthProperty().bind(root.widthProperty());
            toolBar.setOrientation(Orientation.VERTICAL);

            root.setLeft(toolBar);

            //MovieView als default
            root.setCenter(moviesRoot);

            // Get the controller instance from the FXMLLoader
            this.fhmdbController = fxmlLoaderMoviesView.getController();
            this.watchlistController = fxmlLoaderWatchlistView.getController();

            //Button to View --> ClickAction
            buttonMovieView.setOnAction(e -> {root.setCenter(moviesRoot); fhmdbController.initialize();});
            buttonWatchlistView.setOnAction(e -> {root.setCenter(watchlistRoot); watchlistController.initialize();});

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