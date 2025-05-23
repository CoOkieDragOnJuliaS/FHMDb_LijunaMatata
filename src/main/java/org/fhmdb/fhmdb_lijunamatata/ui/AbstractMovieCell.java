package org.fhmdb.fhmdb_lijunamatata.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.fhmdb.fhmdb_lijunamatata.models.Movie;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Julia, Lilie
 * @date 11.05.2025
 * Abstract base class for movie cells in both main list and watchlist
 */
public abstract class AbstractMovieCell extends ListCell<Movie> {
    protected final Label title = new Label();
    protected final Label description = new Label();
    protected final Label genre = new Label();
    protected final Button detailButton = new Button("Show Details");
    protected final VBox detailsBox = new VBox();
    protected final VBox movieLayout = new VBox();
    protected final HBox buttonLayout;
    protected final VBox rightLayout = new VBox();
    protected final HBox cellLayout;
    protected boolean detailsVisible = false;

    protected AbstractMovieCell() {
        buttonLayout = new HBox(detailButton);
        cellLayout = new HBox(movieLayout, rightLayout);
        styleLayout();
        initButtonEvent();
    }

    protected void setActionButton(Button actionButton) {
        buttonLayout.getChildren().add(0, actionButton);
    }

    /**
     * Method to style the main layout, the buttons
     * as well as the details box where additional information is stored!
     */
    protected void styleLayout() {
        // Add CSS classes
        detailButton.getStyleClass().add("details-button");
        detailsBox.getStyleClass().add("details-box");

        // Add CSS styles
        title.getStyleClass().setAll(List.of("text-yellow", "title"));
        description.getStyleClass().setAll(List.of("text-white", "description"));
        genre.getStyleClass().setAll(List.of("text-white", "genre"));

        // Enable text wrapping
        title.setWrapText(true);
        description.setWrapText(true);
        genre.setWrapText(true);

        // Layout configuration
        movieLayout.setPadding(new Insets(10));
        movieLayout.setSpacing(10);
        movieLayout.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(movieLayout, Priority.ALWAYS);

        // Set up the main content
        movieLayout.getChildren().setAll(title, description, genre);

        // Style the details box
        detailsBox.setSpacing(5);
        detailsBox.getStyleClass().add("details-box");

        // Style button layout
        buttonLayout.setSpacing(10);
        buttonLayout.setPadding(new Insets(10));
        buttonLayout.setAlignment(Pos.CENTER_RIGHT);

        // Setup right layout with buttons at bottom
        rightLayout.setAlignment(Pos.BOTTOM_RIGHT);
        rightLayout.getChildren().add(buttonLayout);
        VBox.setVgrow(rightLayout, Priority.ALWAYS);
    }

    /**
     * Method to initialize the mouseClickEvent to
     * show and hide details for the detailsbox
     */
    protected void initButtonEvent() {
        detailButton.setOnMouseClicked(mouseEvent -> {
            if (!detailsVisible) {
                // Show details
                detailsBox.getChildren().setAll(getDetails());
                movieLayout.getChildren().add(detailsBox);
                detailButton.setText("Hide Details");
                detailsVisible = true;
            } else {
                // Hide details
                movieLayout.getChildren().remove(detailsBox);
                detailButton.setText("Show Details");
                detailsVisible = false;
            }
        });
    }

    /**
     * Method to structure the detailed information inside the details box.
     * Additional non-null check if the information (like in WatchlistCell) is empty, so the information
     * is not shown in the detail-box!
     * @return VBox with all the detailed information of the movie
     */
    protected VBox getDetails() {
        VBox details = new VBox(5); // 5px spacing between elements

        // Create a TextFlow for the details to be added to the detailsbox as children
        TextFlow releaseYear = createStyledText("Release Year: ", String.valueOf(getItem().getReleaseYear()));
        TextFlow length = createStyledText("Length: ", getItem().getLengthInMinutes() + " minutes");
        TextFlow rating = createStyledText("Rating: ", getItem().getRating() + "/10");

        List<String> directorsList = getItem().getDirectors();
        TextFlow directors = directorsList.isEmpty() ? null : createStyledText("Directors: ", String.join(", ", directorsList));

        List<String> writersList = getItem().getWriters();
        TextFlow writers = writersList.isEmpty() ? null : createStyledText("Writers: ", String.join(", ", writersList));

        List<String> mainCastList = getItem().getMainCast();
        TextFlow mainCast = mainCastList.isEmpty() ? null : createStyledText("Main Cast: ", String.join(", ", mainCastList));

        // Add the non-null TextFlows
        // Add all non-null elements
        details.getChildren().addAll(releaseYear, rating, length);
        Stream.of(directors, writers, mainCast)
                .filter(Objects::nonNull)
                .forEach(details.getChildren()::add);

        return details;
    }

    /**
     * Method that formats the parts of the detail box to make it more readable
     * @param labelText
     * @param valueText
     * @return a combined TextFlow element with formatted label(bold) and text for the detailbox
     */
    protected TextFlow createStyledText(String labelText, String valueText) {
        Text labelPart = new Text(labelText);
        Text valuePart = new Text(valueText);

        // Apply styles directly using inline styles for Text nodes
        labelPart.setFill(Color.WHITE);
        valuePart.setFill(Color.WHITE);
        labelPart.setStyle("-fx-font-weight: bold;");

        TextFlow textFlow = new TextFlow(labelPart, valuePart);
        textFlow.getStyleClass().add("text-white");
        return textFlow;
    }

    /**
     * Method to setup responsive layout for the movieLayout-box, title, description and genre to be
     * bound with the widthProperty, respondent to width, padding and listView element
     */
    protected void setupResponsiveLayout() {
        // Set up responsive layout if ListView is available
        if (getListView() != null) {
            // Bind main layout width (80% of list view width / the parent)
            movieLayout.prefWidthProperty().bind(getListView().widthProperty().multiply(0.8));
            title.prefWidthProperty().bind(movieLayout.prefWidthProperty());
            description.prefWidthProperty().bind(movieLayout.prefWidthProperty());
            genre.prefWidthProperty().bind(movieLayout.prefWidthProperty());

            // Bind cell layout width (full width minus padding)
            cellLayout.prefWidthProperty().bind(getListView().widthProperty().subtract(20));

            // Bind right layout width (20% of list view width) --> for the buttons
            rightLayout.prefWidthProperty().bind(getListView().widthProperty().multiply(0.2));
        } else {
            // Fallback fixed widths if ListView isn't available
            movieLayout.setPrefWidth(800);
            cellLayout.setPrefWidth(1000);
            rightLayout.setPrefWidth(200);
        }
    }

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        // Clear previous styles and graphic
        this.getStyleClass().removeAll("movie-cell");
        setGraphic(null);

        if (empty || movie == null) {
            setText(null);
        } else {
            // Adds the movie-cell element, setting the title, description and genre
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

            // Style the cell
            cellLayout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

            setupResponsiveLayout();

            // Style buttons
            //Control.USE_PREF_SIZE is a constant in JavaFX that tells the layout to use the control's preferred size for the minimum width.
            buttonLayout.setMinWidth(Control.USE_PREF_SIZE);

            // Ensure only basic info is shown initially
            movieLayout.getChildren().setAll(title, description, genre);

            //Set everything in place
            setGraphic(cellLayout);
        }
    }
}
