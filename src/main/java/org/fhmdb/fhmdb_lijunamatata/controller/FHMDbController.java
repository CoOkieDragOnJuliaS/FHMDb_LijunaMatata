package org.fhmdb.fhmdb_lijunamatata.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.fhmdb.fhmdb_lijunamatata.database.MovieEntity;
import org.fhmdb.fhmdb_lijunamatata.exceptions.DatabaseException;
import org.fhmdb.fhmdb_lijunamatata.exceptions.MovieApiException;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import org.fhmdb.fhmdb_lijunamatata.observer.WatchlistObserver;
import org.fhmdb.fhmdb_lijunamatata.repositories.MovieRepository;
import org.fhmdb.fhmdb_lijunamatata.repositories.WatchlistRepository;
import org.fhmdb.fhmdb_lijunamatata.services.MovieService;
import org.fhmdb.fhmdb_lijunamatata.state.SortContext;
import org.fhmdb.fhmdb_lijunamatata.ui.MovieCell;
import org.fhmdb.fhmdb_lijunamatata.utils.ClickEventHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * This controller manages the main movie view, including searching, filtering, and sorting movies.
 * It also handles adding movies to the watchlist.
 * This class implements the WatchlistObserver interface,
 * so it gets notified when the watchlist changes. (in constructors)
 * It can then update the UI or display a status message.
 */
public class FHMDbController implements WatchlistObserver {

    private Genre genre;
    private Integer releaseYear;
    private Double rating;
    private final SortContext sortContext = new SortContext();
    private ObservableList<Movie> movies;
    private ObservableList<Movie> filteredMovies;
    private MovieService movieService; //do not make final or can't mock
    private String searchText = "";

    @FXML public Button filterBtn;
    @FXML public ComboBox<Genre> genreComboBox;
    @FXML public ComboBox<Integer> releaseYearComboBox;
    @FXML public ComboBox<String> ratingComboBox;
    @FXML private ListView<Movie> movieListView;
    @FXML private Button sortBtn;
    @FXML public TextField searchField;
    @FXML private Label statusLabel;

    private ClickEventHandler<Movie> onAddToWatchlistClicked;

    //Scheduler for delaying filtering with API after keypress
    private ScheduledExecutorService scheduler;
    //Filtering mechanism for filtering delayed
    private Runnable filterTask;

    private WatchlistRepository watchlistRepository;

    Logger logger = Logger.getLogger(FHMDbController.class.getName());

    // Default constructor
    public FHMDbController() {
        try {
            this.movieService = new MovieService();
            this.watchlistRepository = WatchlistRepository.getInstance();
            this.watchlistRepository.addObserver(this);
        } catch (DatabaseException e) {
            logger.severe("Database initialization failed: " + e.getMessage());
            Platform.runLater(() -> updateStatusLabel("Database initialization failed: " + e.getMessage(), true));
        } catch (Exception e) {
            logger.severe("Initialization failed: " + e.getMessage());
            Platform.runLater(() -> updateStatusLabel("Initialization failed: " + e.getMessage(), true));
        }
    }

    // Constructor with DI for testing
    public FHMDbController(WatchlistRepository watchlistRepository, MovieService movieService) {
        this.movieService = movieService;
        this.watchlistRepository = watchlistRepository;
        this.watchlistRepository.addObserver(this);
    }

    /**
     * initializes the Controller by calling methods for initializing the elements of the class.
     * <p>
     * - The `searchText` is automatically updated whenever the user types in the `searchField`.
     */
    @FXML
    public void initialize() {
        try {
            //Initializing UI components and handlers
            initializeClickHandlers();
            initializeStatusLabel();
            initializeSchedulers();

            //Initializing data and UI components
            initializeMovies();
            initializeGenreComboBox();
            initializeReleaseYearComboBox();
            initializeRatingComboBox();

            //initializing ListView and Listeners
            initializeMovieListView();
            initializeListeners();

            filterMovies();

        } catch (DatabaseException e) {
            logger.severe("Database error during initialization: " + e.getMessage());
            updateStatusLabel("Database error during initialization: " + e.getMessage(), true);
        } catch (MovieApiException e) {
            logger.severe("API error during initialization: " + e.getMessage());
            updateStatusLabel("API error during initialization: " + e.getMessage(), true);
        } catch (IOException e) {
            logger.severe("IO error during initialization: " + e.getMessage());
            updateStatusLabel("IO error during initialization: " + e.getMessage(), true);
        } catch (Exception e) {
            logger.severe("Failed to initialize: " + e.getMessage());
            updateStatusLabel("Failed to initialize application: " + e.getMessage(), true);
        }
    }

    /**
     * Initializes the click handler for adding movies to the watchlist.
     * Prevents duplicate entries and shows feedback using popup alerts.
     */
    protected void initializeClickHandlers() {
        onAddToWatchlistClicked = (clickedMovie) -> {
            if (watchlistRepository == null) {
                logger.severe("WatchlistRepository is not initialized");
                showPopup("Error", "Database not initialized.");
                return;
            }

            try {
                // Convert Movie to MovieEntity to access the API ID
                MovieEntity movieEntity = new MovieEntity(clickedMovie);
                String apiId = movieEntity.getApiId();

                // Add the movie to the watchlist
                watchlistRepository.addToWatchlist(movieEntity);
                logger.info("Added to watchlist: " + clickedMovie.getTitle());
                showPopup("Watchlist", "✅ " + clickedMovie.getTitle() + " added to watchlist!");

            } catch (DatabaseException dbException) {
                logger.severe("Database error: " + dbException.getMessage());
                showPopup("Database Error", "Could not add movie: " + dbException.getMessage());
            } catch (Exception e) {
                logger.severe("Unexpected error: " + e.getMessage());
                showPopup("Error", "Unexpected error: " + e.getMessage());
            }
        };
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
            if (filterTask != null) {
                scheduler.shutdownNow();
                initializeSchedulers();
            }
            filterTask = () -> Platform.runLater(() -> {
                try {
                    filterMovies();
                } catch (Exception e) {
                    logger.severe("Error filtering movies: " + e.getMessage());
                    updateStatusLabel("Error filtering movies: " + e.getMessage(), true);
                }
            });
            scheduler.schedule(filterTask, 500, TimeUnit.MILLISECONDS);
        });

        genreComboBox.valueProperty().addListener((observable, oldValue, newValue) -> genre = newValue);
        releaseYearComboBox.valueProperty().addListener((observable, oldValue, newValue) -> releaseYear = newValue);
        ratingComboBox.valueProperty().addListener((observable, oldValue, newValue) -> updateRatingFilter(newValue));
    }

    /**
     * Initializes movieListView element and setting the MovieCell onto the view
     */
    private void initializeMovieListView() {
        movieListView.setItems(this.filteredMovies);
        movieListView.setCellFactory(movieListView -> new MovieCell(onAddToWatchlistClicked));
    }

    /**
     * Initializes the ObservableArrayList() of movies and filteredMovies
     */
    private void initializeMovies() throws MovieApiException, DatabaseException, IOException {
        updateStatusLabel("Loading movies...", false);
        this.movies = FXCollections.observableArrayList(Movie.initializeMovies());

        initializeMovieRepository();

        this.filteredMovies = FXCollections.observableArrayList(this.movies);
        updateStatusLabel("", false);
    }

    /**
     * initializes the repository with the movies from the database
     */
    private void initializeMovieRepository() throws DatabaseException {
        MovieRepository movieRepository = MovieRepository.getInstance();
        if (movieRepository.getAllMovies().isEmpty()) {
            movieRepository.addAllMovies(MovieEntity.fromMovies(movies));
        }
    }

    /**
     * Initializes the genre ComboBox with a "no genre" option followed by all genres from the {@link Genre} enum.
     * The first item, representing "no genre", is selected by default.
     */
    private void initializeGenreComboBox() {
        ObservableList<Genre> genreOptions = FXCollections.observableArrayList();
        genreOptions.add(null);
        genreOptions.addAll(Genre.values());
        this.genreComboBox.setItems(genreOptions);
    }

    /**
     * Initializes a releaseYear ComboBox with a "no year" option followed by all distinct years from filtered Movies.
     * The first item, representing "no year", is selected by default.
     */
    private void initializeReleaseYearComboBox() {
        ObservableList<Integer> releaseYearOptions = FXCollections.observableArrayList();
        releaseYearOptions.add(null);
        if (filteredMovies != null && !filteredMovies.isEmpty()) {
            List<Integer> years = filteredMovies.stream()
                    .filter(Objects::nonNull)
                    .map(Movie::getReleaseYear)
                    .distinct()
                    .sorted()
                    .toList();
            releaseYearOptions.addAll(years);
        }
        this.releaseYearComboBox.setItems(releaseYearOptions);
    }

    /**
     * Initializes the rating ComboBox with a "no rating" option followed by all distinct ratings from filtered Movies.
     * The first item, representing "no rating", is selected by default.
     */
    private void initializeRatingComboBox() {
        ObservableList<String> ratingOptions = FXCollections.observableArrayList();
        ratingOptions.add("All Ratings");
        ratingOptions.add("> 1");
        ratingOptions.add("> 2");
        ratingOptions.add("> 3");
        ratingOptions.add("> 4");
        ratingOptions.add("> 5");
        ratingOptions.add("> 6");
        ratingOptions.add("> 7");
        ratingOptions.add("> 8");
        ratingOptions.add("> 9");
        this.ratingComboBox.setItems(ratingOptions);
    }

    /**
     * @param selectedRatingString This method updates the rating variable which is used for filtering movies
     *                             according to the value picked in the ratingComboBox (e.g. > 2)
     */
    private void updateRatingFilter(String selectedRatingString) {
        if (selectedRatingString == null || selectedRatingString.equals("All Ratings")) {
            this.rating = null;
        } else if (selectedRatingString.startsWith("> ")) {
            try {
                this.rating = Double.parseDouble(selectedRatingString.substring(2));
            } catch (NumberFormatException e) {
                System.err.println("Error parsing rating threshold: " + selectedRatingString);
                this.rating = null; // Reset rating if parsing fails
            }
        } else {
            this.rating = null;
        }
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
     * Update: The sorting order and sorting mechanism is now done by the State Pattern SortContext()
     * Updates the button text and refreshes the movie list view.
     */
    void sortMovies() {
        if (this.filteredMovies == null || this.filteredMovies.isEmpty()) {
            return;
        }

        //Iterating to the next state, e.g. unsorted, asc or desc
        sortContext.nextState();

        List<Movie> sortedMovies = sortContext.sort(new ArrayList<>(this.filteredMovies));
        this.filteredMovies = FXCollections.observableList(sortedMovies);

        updateSortButtonText();
        updateMovieListView(this.searchText, 
                          this.genre != null ? this.genre.name() : "", 
                          this.releaseYear != null ? this.releaseYear : 0, 
                          this.rating != null ? this.rating : 0.0);
    }

    /**
     * Updates the sort button text based on the current sorting order.
     * This method ensures that the UI element is updated only when available.
     */
    void updateSortButtonText() {
        if (this.sortBtn != null) {
            this.sortBtn.setText(sortContext.getButtonText());
        }
    }

    /**
     * Handles the filter button click event.
     * Calls the filterMovies() method to trigger filtering
     */
    @FXML
    public void onFilterButtonClick() {
        filterMovies();
    }

    /**
     * Calls the fetchFilteredMovies() method from movieService if API is accessible,
     * otherwise referring to local filtering mechanism in movieService
     * Updating the movieListView and the sortingState during the process
     */
    void filterMovies() {
        if (this.movies == null) {
            return;
        }
        
        try {
            List<Movie> filtered;
            
            // Trying to fetch filtered movies from API first
            try {
                filtered = this.movieService.fetchFilteredMovies(
                    this.searchText.isEmpty() ? null : this.searchText, this.genre,
                    this.releaseYear, this.rating
                );
                // Update the local movies list with the fresh data from API
                if (filtered != null && !filtered.isEmpty()) {
                    this.movies = FXCollections.observableArrayList(filtered);
                }
            } catch (Exception e) {
                // If API call fails, fall back to local filtering
                logger.warning("API call failed, using local filtering: " + e.getMessage());
                filtered = this.movieService.filterMovies(
                    this.movies, this.searchText, this.genre, this.releaseYear, this.rating
                );
            }
            
            // Apply the current sort state if not in unsorted state
            if (!sortContext.isUnsorted() && filtered != null) {
                filtered = sortContext.sort(filtered);
            }
            
            this.filteredMovies = filtered != null ? 
                FXCollections.observableArrayList(filtered) : 
                FXCollections.observableArrayList();
            
            // Update the UI
            updateMovieListView(
                this.searchText, this.genre != null ? this.genre.name() : "",
                this.releaseYear != null ? this.releaseYear : 0, this.rating != null ? this.rating : 0.0
            );
            
        } catch (Exception e) {
            logger.severe("Error filtering movies: " + e.getMessage());
            updateStatusLabel("Error filtering movies: " + e.getMessage(), true);
        }
    }

    /**
     * Updates the movie list view by clearing and repopulating it with sorted movie titles.
     * Ensures the UI list correctly reflects the current order of movies.
     */
    void updateMovieListView(String searchText, String genre, int releaseYear, double rating) {
        if (this.movieListView == null) {
            return;
        }
        
        this.movieListView.getItems().clear();
        
        if (this.filteredMovies != null) {
            this.movieListView.getItems().addAll(this.filteredMovies);
            
            if (this.filteredMovies.isEmpty()) {
                logger.info("No movies found!");
                updateStatusLabel("No movies found!", false);
            } else {
                logger.info("Movies found, updating status label");
                updateStatusLabel(String.format(
                    "Movies found with Query = %s / Genre = %s / ReleaseYear = %d and Rating from %.1f", searchText,
                    genre != null && !genre.isEmpty() ? genre : "Any", releaseYear, rating
                ), false);
            }
        }
    }

    /**
     * Updates the status label with a given message.
     * Ensures the update runs on the JavaFX UI thread.
     *
     * @param message The message to display.
     * @param isError If true, the label is made visible; otherwise, it's hidden when empty message and not an error.
     */
    public void updateStatusLabel(String message, boolean isError) {
        if (statusLabel != null) {
            logger.info("Updating status label...");
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

    /**
     * overridden method to call the updateStatusLabel to set the new information
     * @param updatedWatchlist the current state of the watchlist
     */
    @Override
    public void onWatchlistChanged(List<Movie> updatedWatchlist) {
        logger.info("Watchlist updated: " + updatedWatchlist.size() + " movies");
        updateStatusLabel("Watchlist updated: " + updatedWatchlist.size() + " movies", false);
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

    public SortContext getSortContext() {
        return sortContext;
    }

    public ObservableList<Movie> getFilteredMovies() {
        return filteredMovies;
    }

    void showPopup(String title, String content) {
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
        });
    }



}
