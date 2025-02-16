package org.fhmdb.fhmdb_lijunamatata.controller;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.fhmdb.fhmdb_lijunamatata.services.MovieService;
import org.fhmdb.fhmdb_lijunamatata.ui.MovieCell;

import java.util.List;


public class FHMDbController {

    //private List<Movie> filteredMovies;
    private Genre genre;
    private boolean isAscending = true;
    private ObservableList<Movie> movies;
    private ObservableList<Movie> filteredMovies;
    private MovieService movieService;
    private String searchText = "";

    @FXML
    public Button filterBtn;

    @FXML
    public ComboBox<Genre> genreComboBox;

    @FXML
    private ListView<Movie> movieListView;

    @FXML
    private Button sortBtn;

    @FXML
    public TextField searchField;

    public FHMDbController() {
        //No args constructor for initialization
    }

    /**
     * Initializes the movie database and initializes the listview, combobox and movies
     * Additionally, sets up listeners to automatically update the search text and selected genre.
     * <p>
     * - The `searchText` is automatically updated whenever the user types in the `searchField`.
     * - The `genre` is automatically updated whenever a new genre is selected from the `genreComboBox`.
     */
    @FXML
    public void initialize() {
        this.movies = FXCollections.observableList(Movie.initializeMovies());
        this.filteredMovies = FXCollections.observableList(this.movies);
        this.movieService = new MovieService();

        //Initial update of the movie list on the UI
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data
        updateMovieListView();

        //Initialization of genreComboBox
        initializeGenreComboBox();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText = newValue;
            filterMovies();
        });

        genreComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            genre = newValue;
        });
    }

    /**
     * Initializes the ComboBox with a "no genre" option followed by all genres from the {@link Genre} enum.
     * The first item, representing "no genre", is selected by default.
     */
    private void initializeGenreComboBox() {
        // Create an ObservableList to store genre options, including a "no genre" option.
        ObservableList<Genre> genreOptions = FXCollections.observableArrayList();
        genreOptions.add(null);
        genreOptions.addAll(Genre.values());
        this.genreComboBox.setItems(genreOptions);
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
        if (this.filteredMovies == null || this.filteredMovies.isEmpty())
            return;

        this.filteredMovies = FXCollections.observableList(this.movieService.sortMovies(this.filteredMovies, this.isAscending));
        updateSortButtonText();

        this.isAscending = !this.isAscending;
    }

    /**
     * Updates the sort button text based on the current sorting order.
     * This method ensures that the UI element is updated only when available.
     */
    private void updateSortButtonText() {
        if (this.sortBtn != null) {
            this.sortBtn.setText(this.isAscending ? "Sort (desc)" : "Sort (asc)");
        }
    }

    /**
     * Handles the filter button click event.
     * Calls the filterMovies() method to trigger filtering
     */
    @FXML
    public void onFilterButtonClick() {
        //Calling the filterMovies() method to separate FXMl of logic
        filterMovies();
    }

    /**
     * Calls the filterMovies method inside the movieService class and updates the movieListView
     */
    private void filterMovies(){
        // Filter movies
        //TODO: Bugfix Filtering movies with new MovieCell method of UI change
        this.filteredMovies.clear();
        // Get the filtered results from the movieService
        List<Movie> filteredResults = this.movieService.filterMovies(this.movies, this.searchText, this.genre);

        // Add the filtered results to the filteredMovies list
        this.filteredMovies.addAll(filteredResults);}

    /**
     * Updates the movie list view by clearing and repopulating it with sorted movie titles.
     * Ensures the UI list correctly reflects the current order of movies.
     */
    public void updateMovieListView() {
        //TODO: Bugfix updating the listview according to the changes
        if (this.movieListView == null) return;
        this.movieListView.getItems().clear();
        this.movieListView.getItems().addAll(this.filteredMovies);
    }

    //Getter, Setter
    public void setMovies(List<Movie> movies) {
        this.movies = FXCollections.observableList(movies);
    }
}