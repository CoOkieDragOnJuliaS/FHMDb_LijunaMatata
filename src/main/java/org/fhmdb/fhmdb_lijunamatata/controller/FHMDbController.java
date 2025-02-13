package org.fhmdb.fhmdb_lijunamatata.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.fhmdb.fhmdb_lijunamatata.ui.MovieCell;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FHMDbController implements Initializable {

    private List<Movie> movies;
    @FXML
    ListView<Movie> movieListView;

    public FHMDbController() {
        //No args constructor for initialization
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movies = Movie.initializeMovies();
        movieListView.setItems(FXCollections.observableArrayList(movies));
        movieListView.setCellFactory(movieListView -> new MovieCell());
    }

    //TODO: Sort Button!

    //TODO: Filtering with query

    //TODO: Filtering with genres

    //TODO: Updating Logic
    //Working with elements from the view has to have the annotation @FXML, please look up JavaFX if you are uncertain

}