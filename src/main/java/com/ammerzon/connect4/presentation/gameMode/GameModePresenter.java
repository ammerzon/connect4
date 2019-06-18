package com.ammerzon.connect4.presentation.gameMode;

import com.ammerzon.connect4.business.game.boundary.GameService;
import com.ammerzon.connect4.business.game.entity.GameMode;
import com.ammerzon.connect4.presentation.helper.BasePresenter;
import com.ammerzon.connect4.presentation.login.LoginView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class GameModePresenter extends BasePresenter implements Initializable {
    @Inject
    private GameService gameService;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    private void openLoginController(GameMode gameMode) {
        gameService.setGameMode(gameMode);
        show(LoginView.class, "Login");
    }
}
