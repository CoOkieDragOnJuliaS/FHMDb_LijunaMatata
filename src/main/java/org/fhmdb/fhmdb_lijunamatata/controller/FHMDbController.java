package org.fhmdb.fhmdb_lijunamatata.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.fhmdb.fhmdb_lijunamatata.services.MovieService;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;


public class FHMDbController {

    private List<Movie> movies;
    private boolean sort = true;
    private String searchText = "";
    private Genre genre;
    private MovieService movieService;

    @FXML
    public TextField searchField;
    @FXML
    public Button filterBtn;
    @FXML
    public ComboBox genreComboBox;
    @FXML
    public ListView movieListView;

    public FHMDbController() {
        //No args constructor for initialization
        movies = Movie.initializeMovies();
        movieService = new MovieService();
    }

    /**
     * Initializes the ComboBox with a "no genre" option followed by all genres from the {@link Genre} enum.
     * The first item, representing "no genre", is selected by default.
     * Additionally, sets up listeners to automatically update the search text and selected genre.

     * - The `searchText` is automatically updated whenever the user types in the `searchField`.
     * - The `genre` is automatically updated whenever a new genre is selected from the `genreComboBox`.
     */
    @FXML
    public void initialize() {
        // Create an ObservableList to store genre options, including a "no genre" option.
        ObservableList<Genre> genreOptions = FXCollections.observableArrayList();
        genreOptions.add(null);
        genreOptions.addAll(Genre.values());
        genreComboBox.setItems(genreOptions);
        /* Alternative
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText = newValue;
        });
        genreComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            genre = (Genre) newValue;
        });
         */
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchText = newValue);
        genreComboBox.valueProperty().addListener((observable, oldValue, newValue) -> genre = (Genre) newValue);
    }


    /**
     * Toggles the sorting order between ascending and descending when the button is clicked.
     * Updates the button text to reflect the current sort order ("Sort (asc)" or "Sort (desc)").
     *
     * @param actionEvent The action event triggered by the button click.
     */
    public void sortBtn(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        if (button.getText().equals("Sort (asc)")) {
            button.setText("Sort (desc)");
            sort = false;
        } else {
            button.setText("Sort (asc)");
            sort = true;
        }
    }

    //TODO: Updating Logic
    //Working with elements from the view has to have the annotation @FXML, please look up JavaFX if you are uncertain
    public void filterBtn() {
        // Filter movies
        List<Movie> filteredMovies = movieService.filterMovies(movies, searchText, genre);
        // Sort movies
        List<Movie> sortedMovies = movieService.sortMovies(filteredMovies, sort);

        // Display sorted movie titles
        for (Movie movie : sortedMovies) {
            System.out.println(movie.getTitle());
        }
    }
}