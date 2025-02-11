package org.fhmdb.fhmdb_lijunamatata.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FHMDbController {
    @FXML
    private Label welcomeText;

    public FHMDbController(Label welcomeText) {
        this.welcomeText = welcomeText;
    }

    @FXML
    public void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public Label getWelcomeText() {
        return welcomeText;
    }
}