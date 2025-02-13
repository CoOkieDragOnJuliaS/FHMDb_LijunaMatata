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
     * JavaDoc which tells what the method has to do - in this case sortMovies or sortListOfMovies
     */
    @FXML
    private void onSortButtonClick() {
        sortMovies();
    }

    //TODO: separate sortingLogic from FXML button elements for better testing?!
    //maybe update of sortButtonText in another method?
    /**
     * JavaDoc which tells what the method has to do - in this case sortMovies or sortListOfMovies
     */
    public void sortMovies() {
        if (movies == null || movies.isEmpty())
            return; //return and check is necessary to avoid NullPointerException when checking

        //other way of testing is to ask if it is not null and notEmpty and therefore work the logic below
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



    //TODO: Filtering with query

    //TODO: Filtering with genres

    /**
     * What does this method do, short explanation and what happens, BUT: was not part of the issue
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