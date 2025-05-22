package org.fhmdb.fhmdb_lijunamatata.ui;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import org.fhmdb.fhmdb_lijunamatata.controller.FHMDbController;
import org.fhmdb.fhmdb_lijunamatata.controller.WatchlistController;

import java.io.IOException;

public class SceneRoot extends BorderPane {
    private static SceneRoot instance;
    private FHMDbController fhmdbController;
    private WatchlistController watchlistController;


    private SceneRoot(FXMLLoader fxmlLoaderMoviesView, FXMLLoader fxmlLoaderWatchlistView) throws IOException {
        Parent moviesRoot = fxmlLoaderMoviesView.load();
        Parent watchlistRoot = fxmlLoaderWatchlistView.load();

        //Toolbar/Navbar
        Label navigationLabel = new Label();
        navigationLabel.setText("Navigation-Menu");
        navigationLabel.getStyleClass().add("nav-label");
        Separator separator = new Separator();
        Button buttonMovieView = new Button("Home");
        Button buttonWatchlistView = new Button("Watchlist");

        ToolBar toolBar = new ToolBar(navigationLabel, separator, buttonMovieView, buttonWatchlistView);
        toolBar.getStyleClass().add("nav-bar");
        buttonMovieView.maxWidthProperty().bind(this.widthProperty());
        buttonMovieView.getStyleClass().add("nav-button");
        buttonWatchlistView.maxWidthProperty().bind(this.widthProperty());
        buttonWatchlistView.getStyleClass().add("nav-button");
        toolBar.setOrientation(Orientation.VERTICAL);

        this.setLeft(toolBar);

        //MovieView als default
        this.setCenter(moviesRoot);

        // Get the controller instance from the FXMLLoader
        this.fhmdbController = fxmlLoaderMoviesView.getController();
        this.watchlistController = fxmlLoaderWatchlistView.getController();

        //Button to View --> ClickAction
        buttonMovieView.setOnAction(e -> {this.setCenter(moviesRoot); fhmdbController.initialize();});
        buttonWatchlistView.setOnAction(e -> {this.setCenter(watchlistRoot); watchlistController.initialize();});

    }

    public static SceneRoot getInstance (FXMLLoader fxmlLoaderMoviesView, FXMLLoader fxmlLoaderWatchlistView) throws IOException {
        if (instance == null) {
            instance = new SceneRoot(fxmlLoaderMoviesView, fxmlLoaderWatchlistView);
        }
        return instance;
    }
}
