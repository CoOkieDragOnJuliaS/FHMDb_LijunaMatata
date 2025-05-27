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

    private ScheduledExecutorService scheduler;
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

    @FXML
    public void initialize() {
        try {
            initializeClickHandlers();
            initializeStatusLabel();
            initializeSchedulers();

            initializeMovies();
            initializeGenreComboBox();
            initializeReleaseYearComboBox();
            initializeRatingComboBox();

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

    protected void initializeClickHandlers() {
        onAddToWatchlistClicked = (clickedMovie) -> {
            if (watchlistRepository == null) {
                logger.severe("WatchlistRepository is not initialized");
                updateStatusLabel("Cannot add to watchlist: Database not initialized", true);
                return;
            }
            try {
                MovieEntity movieEntity = new MovieEntity(clickedMovie);
                watchlistRepository.addToWatchlist(movieEntity);
                logger.info("Adding movie to watchlist: " + clickedMovie.getTitle());
                updateStatusLabel("Added " + clickedMovie.getTitle() + " to Watchlist!", false);
            } catch (DatabaseException dbException) {
                logger.severe("Database error: " + dbException.getMessage());
                updateStatusLabel("Movie " + clickedMovie.getTitle() + " could not be added to the watchlist: " + dbException.getMessage(), true);
            } catch (Exception e) {
                logger.severe("Unexpected error: " + e.getMessage());
                updateStatusLabel("Unexpected error adding movie to watchlist: " + e.getMessage(), true);
            }
        };
    }

    private void initializeStatusLabel() {
        updateStatusLabel("", false);
    }

    private void initializeSchedulers() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

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

    private void initializeMovieListView() {
        movieListView.setItems(this.filteredMovies);
        movieListView.setCellFactory(movieListView -> new MovieCell(onAddToWatchlistClicked));
    }

    private void initializeMovies() throws MovieApiException, DatabaseException, IOException {
        updateStatusLabel("Loading movies...", false);
        this.movies = FXCollections.observableArrayList(Movie.initializeMovies());

        initializeMovieRepository();

        this.filteredMovies = FXCollections.observableArrayList(this.movies);
        updateStatusLabel("", false);
    }

    private void initializeMovieRepository() throws DatabaseException {
        MovieRepository movieRepository = MovieRepository.getInstance();
        if (movieRepository.getAllMovies().isEmpty()) {
            movieRepository.addAllMovies(MovieEntity.fromMovies(movies));
        }
    }

    private void initializeGenreComboBox() {
        ObservableList<Genre> genreOptions = FXCollections.observableArrayList();
        genreOptions.add(null);
        genreOptions.addAll(Genre.values());
        this.genreComboBox.setItems(genreOptions);
    }

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

    private void updateRatingFilter(String selectedRatingString) {
        if (selectedRatingString == null || selectedRatingString.equals("All Ratings")) {
            this.rating = null;
        } else if (selectedRatingString.startsWith("> ")) {
            try {
                this.rating = Double.parseDouble(selectedRatingString.substring(2));
            } catch (NumberFormatException e) {
                System.err.println("Error parsing rating threshold: " + selectedRatingString);
                this.rating = null;
            }
        } else {
            this.rating = null;
        }
    }

    @FXML
    public void onSortButtonClick() {
        sortMovies();
    }

    void sortMovies() {
        if (this.filteredMovies == null || this.filteredMovies.isEmpty()) {
            return;
        }

        sortContext.nextState();

        List<Movie> sortedMovies = sortContext.sort(new ArrayList<>(this.filteredMovies));
        this.filteredMovies = FXCollections.observableList(sortedMovies);

        updateSortButtonText();
        updateMovieListView(this.searchText, 
                          this.genre != null ? this.genre.name() : "", 
                          this.releaseYear != null ? this.releaseYear : 0, 
                          this.rating != null ? this.rating : 0.0);
    }

    void updateSortButtonText() {
        if (this.sortBtn != null) {
            this.sortBtn.setText(sortContext.getButtonText());
        }
    }

    @FXML
    public void onFilterButtonClick() {
        filterMovies();
    }

    void filterMovies() {
        if (this.movies == null) {
            return;
        }
        
        try {
            List<Movie> filtered;
            
            // Try to fetch filtered movies from API first
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

    public void updateStatusLabel(String message, boolean isError) {
        if (statusLabel != null) {
            logger.info("Updating status label...");
            statusLabel.setText(message);
            statusLabel.setVisible(isError || !message.isEmpty());
        }
    }

    public void shutdownScheduler() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    public void setMovies(List<Movie> movies) {
        this.movies = FXCollections.observableList(movies);
    }

    public void setFilteredMovies(List<Movie> movies) {
        if (movies != null) {
            this.filteredMovies = FXCollections.observableList(movies);
        }
    }

    @Override
    public void onWatchlistChanged(List<Movie> updatedWatchlist) {
        logger.info("Watchlist updated: " + updatedWatchlist.size() + " movies");
        updateStatusLabel("Watchlist updated: " + updatedWatchlist.size() + " movies", false);
    }

    public SortContext getSortContext() {
        return sortContext;
    }

    public ObservableList<Movie> getFilteredMovies() {
        return filteredMovies;
    }
}
