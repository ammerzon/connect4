package com.ammerzon.connect4.gui.controller;

import com.ammerzon.connect4.gui.helper.GameMode;
import com.jfoenix.controls.JFXSlider;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends BaseController implements Initializable {
    @FXML
    public VBox loginVBox;
    @FXML
    public Text robotSettingsText;
    @FXML
    public HBox difficultyHBox;
    @FXML
    public Text difficultyText;
    @FXML
    public JFXSlider difficultySlider;
    @FXML
    public HBox timeHBox;
    @FXML
    public Text timeText;
    @FXML
    public JFXSlider timeSlider;

    private GameMode gameMode;

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
        if (gameMode == GameMode.humanVsHuman || gameMode == GameMode.watch) {
            loginVBox.getChildren().remove(loginVBox);
            loginVBox.getChildren().remove(robotSettingsText);
            difficultyHBox.getChildren().remove(difficultyText);
            difficultyHBox.getChildren().remove(difficultySlider);
            timeHBox.getChildren().remove(timeText);
            timeHBox.getChildren().remove(timeSlider);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @Override
    void loaded() {
        super.loaded();
        stage.setTitle("Login");
    }

    @FXML
    public void connectClicked(MouseEvent mouseEvent) {
        openWindow(GameAreaController.class);
    }
}
