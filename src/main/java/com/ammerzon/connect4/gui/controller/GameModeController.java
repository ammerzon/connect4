package com.ammerzon.connect4.gui.controller;

import com.ammerzon.connect4.gui.helper.GameMode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class GameModeController extends Controller implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void humanVsHumanClicked(MouseEvent mouseEvent) {
        openLoginController(GameMode.humanVsHuman);
    }

    @FXML
    public void humanVsRobotClicked(MouseEvent mouseEvent) {
        openLoginController(GameMode.humanVsRobot);
    }

    @FXML
    public void robotVsRobotClicked(MouseEvent mouseEvent) {
        openLoginController(GameMode.robotVsRobot);
    }

    @FXML
    public void watchClicked(MouseEvent mouseEvent) {
        openLoginController(GameMode.watch);
    }

    private void openLoginController(GameMode gameMode) {
        Object controller = openWindow(LoginController.class);
        LoginController loginController = (LoginController) controller;
        loginController.setGameMode(gameMode);
    }
}
