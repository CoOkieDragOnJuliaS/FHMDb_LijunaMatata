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

    private Genre genre;
    private boolean isAscending = true;
    private ObservableList<Movie> movies;
    private ObservableList<Movie> filteredMovies;
    private MovieService movieService; //do not make final or can't mock
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

    /**
     * sets up the logic by initializing movieService
     */
    public FHMDbController() {
        //No args constructor for initialization
        this.movieService = new MovieService();
    }

    /**
     * initializes the Controller by calling methods for initializing the elements of the class.
     * <p>
     * - The `searchText` is automatically updated whenever the user types in the `searchField`.
     * - The `genre` is automatically updated whenever a new genre is selected from the `genreComboBox`.
     */
    @FXML
    public void initialize() {
        //Sets the list of movies
        initializeMovies();
        // Set the ListView items
        initializeMovieListView();
        // Initialize genreComboBox
        initializeGenreComboBox();
        // Add listeners
        initializeListeners();
    }

    /**
     * Adding listeners to the search field (searchText) and
     * changing the genre in the genreComboBox. Old value is compared with new one.
     */
    private void initializeListeners() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText = newValue;
            filterMovies();
        });

        genreComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            genre = newValue;
        });
    }

    /**
     * Initializes movieListView element and setting the MovieCell onto the view
     */
    private void initializeMovieListView() {
        movieListView.setItems(this.filteredMovies);
        movieListView.setCellFactory(movieListView -> new MovieCell());
    }

    /**
     * Initializes the ObservableArrayList() of movies and filteredMovies
     */
    private void initializeMovies() {
        this.movies = FXCollections.observableArrayList(Movie.initializeMovies());
        this.filteredMovies = FXCollections.observableArrayList(this.movies);
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
    public void onSortButtonClick() {
        sortMovies();
    }

    /**
     * Sorts the list of movies based on the current sorting order.
     * Updates the button text and refreshes the movie list view.
     */
    void sortMovies() {
        if (this.filteredMovies == null || this.filteredMovies.isEmpty()) {
            return;
        }

        List<Movie> sortedMovies = this.movieService.sortMovies(this.filteredMovies, this.isAscending);
        this.filteredMovies = FXCollections.observableList(sortedMovies);
        updateSortButtonText();

        this.isAscending = !this.isAscending;
        updateMovieListView();
    }

    /**
     * Updates the sort button text based on the current sorting order.
     * This method ensures that the UI element is updated only when available.
     */
    void updateSortButtonText() {
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
    void filterMovies() {
        // Filter movies
        this.filteredMovies = FXCollections.observableArrayList();
        this.filteredMovies.addAll(this.movies);
        // Get the filtered results from the movieService
        // and add the filtered results to the filteredMovies list
        this.filteredMovies = FXCollections.observableList(this.movieService.filterMovies(this.movies, this.searchText, this.genre));
        updateMovieListView();
    }

    /**
     * Updates the movie list view by clearing and repopulating it with sorted movie titles.
     * Ensures the UI list correctly reflects the current order of movies.
     */
    public void updateMovieListView() {
        if (this.movieListView == null) return;
        this.movieListView.getItems().clear();
        this.movieListView.getItems().addAll(this.filteredMovies);
    }

    //Getter, Setter
    public void setMovies(List<Movie> movies) {
        this.movies = FXCollections.observableList(movies);
    }

    void setFilteredMovies(List<Movie> movies) {
        if (movies != null) {
            this.filteredMovies = FXCollections.observableList(movies);
        }
    }
}