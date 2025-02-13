package org.fhmdb.fhmdb_lijunamatata.controller;

import org.fhmdb.fhmdb_lijunamatata.models.Movie;

import java.util.List;

public class FHMDbController {
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