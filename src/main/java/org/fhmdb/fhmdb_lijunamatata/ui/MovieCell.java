package org.fhmdb.fhmdb_lijunamatata.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;

import java.util.List;

/**
 * @author Julia Sass
 * @date 13.02.2025
 * This class is the design cell for the Movie object inside the UI
 */
public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label description = new Label();
    private final Label genre = new Label();
    private final Label releaseYear = new Label();
    private final Label rating = new Label();
    private final VBox layout = new VBox(title, description, genre, releaseYear, rating);
    private final HBox releaseRatingLayout = new HBox(releaseYear, new Label(" | "), rating);

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
            releaseYear.setText("Release year: " + movie.getReleaseYear());
            rating.setText("Movie Rating: " + movie.getRating());

            // color scheme
            title.getStyleClass().setAll(List.of("text-yellow", "title"));
            description.getStyleClass().setAll(List.of("text-white", "description"));
            genre.getStyleClass().setAll(List.of("text-white", "genre"));
            releaseYear.getStyleClass().setAll(List.of("text-white", "releaseYear"));
            rating.getStyleClass().setAll(List.of("text-yellow", "rating"));
            layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

            // layout by a template
            double maxWidth = (getScene() != null) ? getScene().getWidth() - 30 : 300; // Fallback width
            description.setMaxWidth(maxWidth);
            description.setWrapText(true);

            layout.setPadding(new Insets(10));
            layout.spacingProperty().set(10);
            layout.alignmentProperty().set(javafx.geometry.Pos.CENTER_LEFT);

            // Clear previous children from the layout before adding new ones
            layout.getChildren().removeIf(node -> node instanceof HBox); // Remove any existing HBox

            // Add the releaseRatingLayout to the main layout
            releaseRatingLayout.setPadding(new Insets(0, 0, 0, 0)); // Optional: adjust padding as needed
            releaseRatingLayout.setSpacing(5); // Optional: adjust spacing as needed

            // Add the releaseRatingLayout to the main layout
            layout.getChildren().add(releaseRatingLayout);
            setGraphic(layout);
        }
    }
}
