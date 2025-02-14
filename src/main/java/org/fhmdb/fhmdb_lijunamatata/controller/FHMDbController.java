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
    private String searchText;
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
     * Retrieves the text entered in the search field and stores it in the {@code searchText} variable.
     */
    public void getSearchText() {
        searchText = searchField.getText(); // Получаем текст из TextField
    }

    /**
     * Retrieves the selected genre from the ComboBox and stores it in the {@code genre} variable.
     * The selected genre is cast to the {@link Genre} type and can be {@code null} if no genre is selected.
     */
    public void getSelectedGenre() {
        genre = (Genre) genreComboBox.getValue(); // Получаем выбранный жанр из ComboBox
    }


    //TODO: Updating Logic
    //Working with elements from the view has to have the annotation @FXML, please look up JavaFX if you are uncertain
    public void filterBtn(ActionEvent actionEvent) {
        getSearchText();
        getSelectedGenre();
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