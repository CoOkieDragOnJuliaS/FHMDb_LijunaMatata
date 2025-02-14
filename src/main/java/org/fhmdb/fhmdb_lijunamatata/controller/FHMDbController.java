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
import java.util.stream.Collectors;

public class FHMDbController {

    private List<Movie> movies;
    private boolean sort = true;
    private String searchText;
    private Genre genre;

    public TextField searchField;
    public Button filterBtn;
    public ComboBox genreComboBox;
    public ListView movieListView;
    /*   @FXML
            private Label welcomeText;
            --> annotation for a element on the GUI
        */


    public FHMDbController() {
        //No args constructor for initialization
        movies = Movie.initializeMovies();
    }

    @FXML
    public void initialize() {
        ObservableList<Genre> genreOptions = FXCollections.observableArrayList(Genre.values());
        genreComboBox.setItems(genreOptions);
    }

    //TODO: Sort Button!
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

    //TODO: Filtering with query
    public void getSearchText() {
        searchText = searchField.getText(); // Получаем текст из TextField
    }

    //TODO: Filtering with genres
    public void getSelectedGenre() {
        genre = (Genre) genreComboBox.getValue(); // Получаем выбранный жанр из ComboBox
    }



    //TODO: Updating Logic
    //Working with elements from the view has to have the annotation @FXML, please look up JavaFX if you are uncertain
    public void filterBtn(ActionEvent actionEvent) {

        getSearchText();
        getSelectedGenre();

        List<Movie> filteredMovies = new ArrayList<>();
        for (Movie movie : movies) {
            // by text in title
            boolean matchesSearchText = movie.getTitle().toLowerCase().contains(searchText.toLowerCase());

            // by gerne
            boolean matchesGenre = (genre == null || movie.getGenres().contains(genre));

            // add to Array
            if (matchesSearchText && matchesGenre) {
                filteredMovies.add(movie);
            }
        }

        // sort
        for (int i = 0; i < filteredMovies.size() - 1; i++) {
            if(sort) {
                // sort asc
                filteredMovies.sort((movie1, movie2) -> movie1.getTitle().compareTo(movie2.getTitle()));
            } else {
                // sort desc
                filteredMovies.sort((movie1, movie2) -> movie2.getTitle().compareTo(movie1.getTitle()));
            }
        }

        for (Movie movie : filteredMovies) {
            System.out.println(movie.getTitle());
        }
        System.out.println("........................................");
    }
}