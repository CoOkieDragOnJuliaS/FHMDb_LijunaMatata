package org.fhmdb.fhmdb_lijunamatata;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class FHMDbApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FHMDbApplication.class.getResource("fhmdb-view.fxml"));
        //Sets the scene by loading the .fxml element, setting the size and adding
        //elements like stylesheet (for css) and "setting the stage" for the scene
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        scene.getStylesheets().add(Objects.requireNonNull(FHMDbApplication.class.getResource("styles.css")).toExternalForm());
        stage.setTitle("FHMDb");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}