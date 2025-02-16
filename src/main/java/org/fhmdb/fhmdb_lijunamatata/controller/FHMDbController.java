package org.fhmdb.fhmdb_lijunamatata.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.fhmdb.fhmdb_lijunamatata.services.MovieService;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.Comparator;


public class FHMDbController implements Initializable {
    @FXML
    private Button sortBtn;

    @FXML
    private ListView<String> movieListView;

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

    private boolean isAscending = true;


    public FHMDbController() {
        //No args constructor for initialization
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
        movies = Movie.initializeMovies();
        movieService = new MovieService();
        updateMovieListView();
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
     * Handles the sorting button click event.
     * Calls the sortMovies() method to toggle the sorting order.
     */
    @FXML
    private void onSortButtonClick() {
        sortMovies();
    }

    /**
     * Sorts the list of movies based on the current sorting order.
     * Updates the button text and refreshes the movie list view.
     */
    public void sortMovies() {
        if (movies == null || movies.isEmpty())
            return;

        sortMovieAlgorithm(movies, isAscending);

        updateSortButtonText();

        isAscending = !isAscending;
        updateMovieListView();
    }

    /**
     * Updates the sort button text based on the current sorting order.
     * This method ensures that the UI element is updated only when available.
     */
    private void updateSortButtonText() {
        if (sortBtn != null) {
            sortBtn.setText(isAscending ? "Sort (desc)" : "Sort (asc)");
        }
    }

    /**
     * Sorts the given list of movies either in ascending or descending order by title.
     *
     * @param movies The list of movies to be sorted.
     * @param isAscending Determines the sorting order:
     *                    true for ascending, false for descending.
     */
    public void sortMovieAlgorithm(List<Movie> movies, boolean isAscending) {
        if (isAscending) {
            movies.sort(Comparator.comparing(Movie::getTitle));
        } else {
            movies.sort(Comparator.comparing(Movie::getTitle).reversed());
        }
    }
    /**
     * Updates the movie list view by clearing and repopulating it with sorted movie titles.
     * Ensures the UI list correctly reflects the current order of movies.
     */
    public void updateMovieListView() {
        if (movieListView == null) return;
        movieListView.getItems().clear();
        for (Movie movie : movies) {
            movieListView.getItems().add(movie.getTitle());
        }
    }
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


    //Getter, Setter
    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public boolean isAscending() {
        return isAscending;
    }

    public void setAscending(boolean ascending) {
        isAscending = ascending;
    }
}