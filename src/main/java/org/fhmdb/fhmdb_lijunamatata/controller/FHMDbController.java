package org.fhmdb.fhmdb_lijunamatata.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.fhmdb.fhmdb_lijunamatata.models.Genre;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;


public class FHMDbController {

    private List<Movie> movies;
    private boolean sort = true;
    private String searchText;
    private Genre genre;

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
    }

    /**
     * Initializes the ComboBox with a "no genre" option followed by all genres from the {@link Genre} enum.
     * The first item, representing "no genre", is selected by default.
     */
    @FXML
    public void initialize() {
        ObservableList<Genre> genreOptions = FXCollections.observableArrayList();
        genreOptions.add(null);
        genreOptions.addAll(Genre.values());
        genreComboBox.setItems(genreOptions);
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

    /**
     * Sorts a list of movies based on their titles in either ascending or descending order,
     * depending on the value of the {@code sort} variable.
     *
     * @param sortedMovies The list of movies to be sorted.
     * @return The sorted list of movies.
     */
    private List<Movie> sortMovies(List<Movie> sortedMovies) {
        for (int i = 0; i < sortedMovies.size() - 1; i++) {
            if(sort) {
                // sort asc
                sortedMovies.sort((movie1, movie2) -> movie1.getTitle().compareTo(movie2.getTitle()));
            } else {
                // sort desc
                sortedMovies.sort((movie1, movie2) -> movie2.getTitle().compareTo(movie1.getTitle()));
            }
        }
        return sortedMovies;
    }

    /**
     * Checks if the movie title contains the search text, ignoring case sensitivity.
     *
     * @param movie The movie to be checked.
     * @return {@code true} if the movie title contains the search text; {@code false} otherwise.
     */
    private boolean isMatchesSearchText(Movie movie) {
        return movie.getTitle().toLowerCase().contains(searchText.toLowerCase());
    }

    /**
     * Retrieves the text entered in the search field and stores it in the {@code searchText} variable.
     */
    public void getSearchText() {
        searchText = searchField.getText(); // Получаем текст из TextField
    }

    /**
     * Checks if the movie's genres contain the selected genre.
     * If no genre is selected (i.e., {@code genre} is {@code null}), it returns {@code true} for any movie.
     *
     * @param movie The movie to be checked.
     * @return {@code true} if the movie's genres contain the selected genre, or if no genre is selected; {@code false} otherwise.
     */
    private boolean isMatchesGenre(Movie movie) {
        return (genre == null || movie.getGenres().contains(genre));
    }

    /**
     * Retrieves the selected genre from the ComboBox and stores it in the {@code genre} variable.
     * The selected genre is cast to the {@link Genre} type and can be {@code null} if no genre is selected.
     */
    public void getSelectedGenre() {
        genre = (Genre) genreComboBox.getValue(); // Получаем выбранный жанр из ComboBox
    }

    /**
     * Filters the list of movies based on the current search text and selected genre.
     * The method checks each movie's title against the search text and its genres against the selected genre.
     * Only movies that match both criteria are included in the returned list.
     *
     * @return A list of movies that match the search text and selected genre.
     */
    private List<Movie> filterMovies() {
        List<Movie> filteredMovies = new ArrayList<>();
        for (Movie movie : movies) {
            // by text in title
            boolean matchesSearchText = isMatchesSearchText(movie);
            // by gerne
            boolean matchesGenre = isMatchesGenre(movie);
            // add to Array
            if (matchesSearchText && matchesGenre) {
                filteredMovies.add(movie);
            }
        }
        return filteredMovies;
    }


    //TODO: Updating Logic
    //Working with elements from the view has to have the annotation @FXML, please look up JavaFX if you are uncertain
    public void filterBtn(ActionEvent actionEvent) {
        getSearchText();
        getSelectedGenre();
        // filter
        List<Movie> filteredMovies = filterMovies();
        // sort
        List<Movie> sortedMovies = sortMovies(filteredMovies);

        for (Movie movie : sortedMovies) {
            System.out.println(movie.getTitle());
        }
    }
}