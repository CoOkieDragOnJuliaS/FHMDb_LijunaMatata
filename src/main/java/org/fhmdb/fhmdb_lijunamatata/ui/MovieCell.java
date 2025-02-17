package org.fhmdb.fhmdb_lijunamatata.ui;

import org.fhmdb.fhmdb_lijunamatata.models.Movie;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * @author Julia Sass
 * @date 13.02.2025
 * This class is the design cell for the Movie object inside the UI
 */
public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label description = new Label();
    private final Label genre = new Label();
    private final VBox layout = new VBox(title, description, genre);

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        // Clear previous styles and graphic
        this.getStyleClass().removeAll("movie-cell");
        setGraphic(null); // Clear the graphic when empty to avoid double entries on MovieCell

        if (empty || movie == null) {
            setText(null);  //sets the text of all labels inside the item null
        } else {
            //Adds the movie-cell element, setting the title, description and genre
            this.getStyleClass().add("movie-cell");
            title.setText(movie.getTitle());
            description.setText(
                    movie.getDescription() != null
                            ? movie.getDescription()
                            : "No description available"
            );
            genre.setText(String.join(", ", movie.getGenres().stream()
                    .map(Enum::name)
                    .toArray(String[]::new)));


            // color scheme
            title.getStyleClass().setAll("text-yellow", "start-title");
            description.getStyleClass().setAll("text-white", "start-description");
            genre.getStyleClass().setAll("text-white", "start-genre");
            layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

            // layout by a template
            double maxWidth = (getScene() != null) ? getScene().getWidth() - 30 : 300; // Fallback width
            description.setMaxWidth(maxWidth);
            description.setWrapText(true);

            layout.setPadding(new Insets(10));
            layout.spacingProperty().set(10);
            layout.alignmentProperty().set(javafx.geometry.Pos.CENTER_LEFT);
            setGraphic(layout);
        }
    }
}
