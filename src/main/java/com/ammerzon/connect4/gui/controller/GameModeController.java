package com.ammerzon.connect4.gui.controller;

import com.ammerzon.connect4.engine.GameStatus;
import com.ammerzon.connect4.engine.LocalEngine;
import com.ammerzon.connect4.engine.clients.GUIPlayer;
import com.ammerzon.connect4.engine.clients.Robot;
import com.ammerzon.connect4.engine.contracts.Engine;
import com.ammerzon.connect4.gui.helper.GameMode;
import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class GameModeController extends BaseController implements Initializable {

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

    private void openGameAreaController(GameStatus gameStatus) {
        Engine engine = LocalEngine.getInstance();
        Object controller = openWindow(GameAreaController.class);
        GameAreaController gameAreaController = (GameAreaController) controller;

        if (gameStatus.getGameMode() == GameMode.humanVsRobot) {
            GUIPlayer guiPlayer = new GUIPlayer(engine, gameStatus.getPlayers().get(0).getName());
            Robot savedRobot = (Robot) gameStatus.getPlayers().get(1);
            Robot robot = new Robot(engine, "R2-D2", savedRobot.getDifficulty(), savedRobot.getTime());
            engine.register(guiPlayer);
            engine.register(robot);

            gameAreaController.setPlayer(guiPlayer);
            gameAreaController.startEngine();
        } else {
            // TODO support other game modes
        }
    }

    public void checkSaveGame() {
        Engine engine = LocalEngine.getInstance();
        GameStatus status = engine.loadSavepoint();

        if (status != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy HH:mm");
            Date resultDate = new Date(status.getTimeoutStart());

            JFXDialogLayout layout = new JFXDialogLayout();
            JFXAlert<Void> alert = new JFXAlert<>(stage);
            JFXButton cancelButton = new JFXButton("Cancel");
            JFXButton loadButton = new JFXButton("Load");

            layout.setHeading(new Label("Savepoint found!"));
            layout.setBody(new Label(String.format("Do you want to load the saved game from %s?", sdf.format(resultDate))));
            layout.setActions(cancelButton, loadButton);

            cancelButton.setOnMouseClicked(event -> alert.close());
            loadButton.setOnMouseClicked(event -> {
                alert.close();
                engine.initializeBoard(status);
                openGameAreaController(status);
            });

            alert.setAnimation(JFXAlertAnimation.CENTER_ANIMATION);
            alert.setContent(layout);
            alert.initModality(Modality.NONE);
            alert.show();
        }
    }
}
