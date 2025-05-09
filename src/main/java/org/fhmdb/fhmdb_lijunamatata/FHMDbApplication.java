package org.fhmdb.fhmdb_lijunamatata;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.fhmdb.fhmdb_lijunamatata.controller.FHMDbController;
import org.fhmdb.fhmdb_lijunamatata.database.DatabaseManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class FHMDbApplication extends Application {
    private FHMDbController controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FHMDbApplication.class.getResource("fhmdb-view.fxml"));
        // Load the FXML and get the root node
        Parent root = fxmlLoader.load();

        // Get the controller instance from the FXMLLoader
        this.controller = fxmlLoader.getController();

        //Sets the scene by loading the .fxml element, setting the size and adding
        //elements like stylesheet (for css) and "setting the stage" for the scene
        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add(Objects.requireNonNull(FHMDbApplication.class.getResource("styles.css")).toExternalForm());
        stage.setTitle("FHMDb");
        stage.setScene(scene);
        stage.show();

        //for testing: delete after moving database initialization to actual position
        try {
            DatabaseManager.getDatabaseManager();
        } catch(SQLException e){

        }
    }

    @Override
    public void stop() {
        //Shutting down the scheduler of the controller when application is closing
        //so that the scheduler is not running in the background while the application is not open
        if(this.controller != null) {
            this.controller.shutdownScheduler();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}