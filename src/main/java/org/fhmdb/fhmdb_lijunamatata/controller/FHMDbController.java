package org.fhmdb.fhmdb_lijunamatata.controller;

import org.fhmdb.fhmdb_lijunamatata.models.Movie;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import java.util.Comparator;


public class FHMDbController implements Initializable {
   /*   @FXML
        private Label welcomeText;
        --> annotation for a element on the GUI
    */
    @FXML
    private Button sortBtn;

    @FXML
    private ListView<String> movieListView;

    private List<Movie> movies;
    private boolean isAscending = true;

    public FHMDbController() {
        //No args constructor for initialization
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        movies = Movie.initializeMovies();
        updateMovieListView();
    }

    @FXML
    private void onSortButtonClick() {
        if (movies == null || movies.isEmpty()) return;

        if (isAscending) {
            movies.sort(Comparator.comparing(Movie::getTitle));
            sortBtn.setText("Sort (desc)");
        } else {
            movies.sort(Comparator.comparing(Movie::getTitle).reversed());
            sortBtn.setText("Sort (asc)");
        }

        isAscending = !isAscending;
        updateMovieListView();
    }

    public void updateMovieListView() {
        if (movieListView == null) return;
        movieListView.getItems().clear();
        for (Movie movie : movies) {
            movieListView.getItems().add(movie.getTitle());
        }
    }

    //TODO: Filtering with query

    //TODO: Filtering with genres

    //TODO: Updating Logic
    //Working with elements from the view has to have the annotation @FXML, please look up JavaFX if you are uncertain

}