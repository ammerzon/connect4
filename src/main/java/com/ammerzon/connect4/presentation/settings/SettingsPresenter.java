package com.ammerzon.connect4.presentation.settings;

import com.ammerzon.connect4.business.game.boundary.GameService;
import com.ammerzon.connect4.business.game.entity.GameMode;
import com.ammerzon.connect4.presentation.helper.BasePresenter;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsPresenter extends BasePresenter implements Initializable {
    @FXML
    public JFXTextField hostTextField;
    @FXML
    public JFXTextField portTextField;
    @FXML
    public JFXSlider difficultySlider;
    @FXML
    public HBox difficultyHBox;
    @FXML
    public JFXSlider timeSlider;
    @FXML
    public HBox timeHBox;
    @FXML
    public Label timeLabel;
    @FXML
    public Label difficultyLabel;
    @Inject
    private GameService gameService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hostTextField.setText(gameService.getHost());
        portTextField.setText(String.valueOf(gameService.getPort()));

        if (gameService.getGameMode() == GameMode.humanVsHuman || gameService.getGameMode() == GameMode.watch) {
            difficultyHBox.getChildren().remove(difficultyLabel);
            difficultyHBox.getChildren().remove(difficultySlider);
            timeHBox.getChildren().remove(timeLabel);
            timeHBox.getChildren().remove(timeSlider);
        }
    }

    public void saveClicked(MouseEvent mouseEvent) {
        gameService.setHost(hostTextField.getText());
        gameService.setPort(Integer.parseInt(portTextField.getText()));
        stage.close();
    }
}
