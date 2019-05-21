package com.ammerzon.connect4.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class GameAreaController extends Controller implements Initializable {

    @FXML
    public Label currentPlayerLabel;
    @FXML
    public Label remainingTimeLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @Override
    void loaded() {
        stage.setTitle("Connect4");
    }

    @FXML
    public void preferencesMenuItemClicked(ActionEvent actionEvent) {
        openNewWindow(SettingsController.class, 400, 430);
    }

    @FXML
    public void playButtonClicked(ActionEvent actionEvent) {
    }

    @FXML
    public void stopButtonClicked(ActionEvent actionEvent) {
    }

    @FXML
    public void restartButtonClicked(ActionEvent actionEvent) {
    }

    @FXML
    public void statsButtonClicked(ActionEvent actionEvent) {
        openNewWindow(HighScoreController.class, 900, 600);
    }

    @FXML
    public void gameAreaGridPaneClicked(MouseEvent mouseEvent) {
    }
}
