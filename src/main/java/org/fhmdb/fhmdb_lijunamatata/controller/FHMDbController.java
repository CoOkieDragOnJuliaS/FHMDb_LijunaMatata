package org.fhmdb.fhmdb_lijunamatata.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.fhmdb.fhmdb_lijunamatata.services.MovieService;
import org.fhmdb.fhmdb_lijunamatata.ui.MovieCell;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class FHMDbController {


    private Genre genre;
    private Integer releaseYear;
    private Double rating;
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
    public ComboBox<Integer> releaseYearComboBox;

    @FXML
    public ComboBox<Double> ratingComboBox;

    @FXML
    private ListView<Movie> movieListView;

    @FXML
    private Button sortBtn;

    @FXML
    public TextField searchField;

    @FXML
    private Label statusLabel;
    //Scheduler for delaying filtering with API after keypress
    private ScheduledExecutorService scheduler;
    //Filtering mechanism for filtering delayed
    private Runnable filterTask;

    /**
     * sets up the logic by initializing movieService
     */
    public FHMDbController() {
        //No args constructor for initialization
        this.movieService = new MovieService();
    }

    /**
     * initializes the Controller by calling methods for initializing the elements of the class.
     *
     * - The `searchText` is automatically updated whenever the user types in the `searchField`.
     * - The `genre` is automatically updated whenever a new genre is selected from the `genreComboBox`.
     */
    @FXML
    public void initialize() {
        //initialize Labels
        initializeStatusLabel();
        //Sets the list of movies
        initializeMovies();
        // Set the ListView items
        initializeMovieListView();
        // Initialize ComboBoxes
        initializeGenreComboBox();
        initializeReleaseYearComboBox();
        initializeRatingComboBox();
        // Add listeners
        initializeListeners();
        initializeSchedulers();
    }

    /**
     * Adding a new instance to statusLabel and set it to not visible by updating it
     */
    private void initializeStatusLabel() {
        updateStatusLabel("", false);
    }

    /**
     * Adding the scheduler to help with delaying input and query search
     */
    private void initializeSchedulers() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Adding listeners to the search field (searchText) and
     * changing the genre in the genreComboBox. Old value is compared with new one.
     */
    private void initializeListeners() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText = newValue;

            //Cancel previously scheduled elements
            if(this.filterTask  != null) {
                scheduler.shutdownNow();
                // Reinitialize the scheduler
                initializeSchedulers();
            }

            //Call to filterTask to filterMovies and set scheduler
            filterTask = () -> {
                Platform.runLater(this::filterMovies);
            };
            scheduler.schedule(filterTask, 2, TimeUnit.SECONDS);
        });

        genreComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            genre = newValue;
        });

        releaseYearComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            releaseYear = newValue;
        });

        ratingComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            rating = newValue;
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
        try {
            updateStatusLabel("Loading movies...", false);
            this.movies = FXCollections.observableArrayList(Movie.initializeMovies());
            this.filteredMovies = FXCollections.observableArrayList(this.movies);
            updateStatusLabel("", false);
        } catch (IOException e) {
            updateStatusLabel("Error loading movies!", true);
            e.printStackTrace();
        }

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
     * Initializes the ComboBox with a "no year" option followed by all distinct years from filtered Movies.
     * The first item, representing "no year", is selected by default.
     */
    private void initializeReleaseYearComboBox() {
        ObservableList<Integer> releaseYearOptions = FXCollections.observableArrayList();
        releaseYearOptions.add(null);
        if (filteredMovies != null && !filteredMovies.isEmpty()) {
            List<Integer> years = filteredMovies.stream() //convert to stream for processing
                    .map(Movie::getReleaseYear) //get only release year of each movie
                    .filter(Objects::nonNull) //safety check to remove any null years
                    .distinct() //keep only unique years
                    .sorted() //ascending
                    .toList(); //convert back to list
            releaseYearOptions.addAll(years);
        }
        this.releaseYearComboBox.setItems(releaseYearOptions);
    }

    /**
     * Initializes the ComboBox with a "no rating" option followed by all distinct ratings from filtered Movies.
     * The first item, representing "no rating", is selected by default.
     */
    private void initializeRatingComboBox() {
        ObservableList<Double> ratingOptions = FXCollections.observableArrayList();
        ratingOptions.add(null);
        if (filteredMovies != null && !filteredMovies.isEmpty()) {
            List<Double> ratings = filteredMovies.stream() //convert to stream for processing
                    .map(Movie::getRating) //get only rating of each movie
                    .filter(Objects::nonNull) //safety check to remove any null ratings
                    .distinct() //keep only unique ratings
                    .sorted() //ascending
                    .toList(); //convert back to list
            ratingOptions.addAll(ratings);
        }
        this.ratingComboBox.setItems(ratingOptions);
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

        try {
            // Filter movies
            this.filteredMovies = FXCollections.observableArrayList();
            this.filteredMovies.addAll(this.movies);
            // Get the filtered results from the movieService
            // and add the filtered results to the filteredMovies list
            this.filteredMovies = FXCollections.observableList(this.movieService.fetchFilteredMovies(
                    this.searchText, this.genre, this.releaseYear, this.rating));
            updateMovieListView();
        } catch (IOException | NullPointerException e) {
            updateStatusLabel("Error loading movies!", true);
            e.printStackTrace();
        }
    }

    /**
     * Updates the movie list view by clearing and repopulating it with sorted movie titles.
     * Ensures the UI list correctly reflects the current order of movies.
     */
    public void updateMovieListView() {
        if (this.movieListView == null) return;
        this.movieListView.getItems().clear();
        this.movieListView.getItems().addAll(this.filteredMovies);
        //if there is no result, because the filtering does not return movies, set the label
        if (this.filteredMovies.isEmpty()) {
            updateStatusLabel("No movies found!", false);
        } else {
            updateStatusLabel("", false);
        }
    }

    //TODO: Testing statusLabel
    /**
     * Updates the status label with a given message.
     * Ensures the update runs on the JavaFX UI thread.
     *
     * @param message The message to display.
     * @param isError If true, the label is made visible; otherwise, it's hidden when empty message and not an error.
     */
    private void updateStatusLabel(String message, boolean isError) {
        if (statusLabel != null) {
            statusLabel.setText(message);
            statusLabel.setVisible(isError || !message.isEmpty());
        }
    }

    /**
     * Shuts down the scheduler which is setup in the Controller.
     * Without this option the scheduler would still run after the Application is closed
     */
    public void shutdownScheduler() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    //Getter, Setter
    public void setMovies(List<Movie> movies) {
        this.movies = FXCollections.observableList(movies);
    }

    public void setFilteredMovies(List<Movie> movies) {
        if (movies != null) {
            this.filteredMovies = FXCollections.observableList(movies);
        }
    }

    //TODO: Discuss if method for testing is relevant?
    public void setFilterElements(String searchText, Genre genre, int releaseYear, double rating) {
        this.searchText = searchText;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.rating = rating;
    }
}