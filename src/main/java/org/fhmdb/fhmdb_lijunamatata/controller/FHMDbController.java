package org.fhmdb.fhmdb_lijunamatata.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class FHMDbController implements Initializable {
    @FXML
    private Label welcomeText;

    public FHMDbController(Label welcomeText) {
        this.welcomeText = welcomeText;
    }

    public FHMDbController() {
        //No args constructor for initialization
    }

    @FXML
    public void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public Label getWelcomeText() {
        return welcomeText;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Do Stuff
    }
}