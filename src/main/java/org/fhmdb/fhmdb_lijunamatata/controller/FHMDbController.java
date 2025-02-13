package org.fhmdb.fhmdb_lijunamatata.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;

import java.net.URL;
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
    private boolean isAscending = true;

    public FHMDbController() {
        //No args constructor for initialization
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        movies = Movie.initializeMovies();
        updateMovieListView();
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
     * Updates the sort button text based on the current sorting order.
     * This method ensures that the UI element is updated only when available.
     */
    private void updateSortButtonText() {
        if (sortBtn != null) {
            sortBtn.setText(isAscending ? "Sort (desc)" : "Sort (asc)");
        }
    }

    //TODO: Filtering with query

    //TODO: Filtering with genres

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