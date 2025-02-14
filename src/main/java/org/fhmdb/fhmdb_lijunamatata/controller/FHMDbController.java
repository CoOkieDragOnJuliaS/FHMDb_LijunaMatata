package org.fhmdb.fhmdb_lijunamatata.controller;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;

import java.util.List;

public class FHMDbController {

    public Button filterBtn;
    public ComboBox genreComboBox;
    public Button sortBtn;
    public TextField searchField;
    /*   @FXML
            private Label welcomeText;
            --> annotation for a element on the GUI
        */
    private List<Movie> movies;

    public FHMDbController() {
        //No args constructor for initialization
        movies = Movie.initializeMovies();

    }

    //TODO: Sort Button!

    //TODO: Filtering with query

    //TODO: Filtering with genres
    

    //TODO: Updating Logic
    //Working with elements from the view has to have the annotation @FXML, please look up JavaFX if you are uncertain

}