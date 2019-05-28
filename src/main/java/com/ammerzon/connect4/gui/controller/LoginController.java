package com.ammerzon.connect4.gui.controller;

import com.ammerzon.connect4.engine.LocalEngine;
import com.ammerzon.connect4.engine.clients.GUIPlayer;
import com.ammerzon.connect4.engine.clients.Robot;
import com.ammerzon.connect4.engine.contracts.Engine;
import com.ammerzon.connect4.engine.contracts.Player;
import com.ammerzon.connect4.gui.helper.GameMode;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.IntegerValidator;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
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
    @FXML
    public JFXTextField sizeTextField;
    @FXML
    public JFXTextField usernameTextField;
    @FXML
    public JFXTextField ipAddressTextField;
    @FXML
    public JFXTextField portTextField;
    @FXML
    public JFXButton connectButton;
    @FXML
    public Text gameSettingsText;
    private SimpleBooleanProperty sizeTextFieldValidProperty = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty usernameFieldValidProperty = new SimpleBooleanProperty(false);
    private GameMode gameMode;

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
        if (gameMode == GameMode.humanVsHuman || gameMode == GameMode.watch) {
            loginVBox.getChildren().remove(robotSettingsText);
            difficultyHBox.getChildren().remove(difficultyText);
            difficultyHBox.getChildren().remove(difficultySlider);
            timeHBox.getChildren().remove(timeText);
            timeHBox.getChildren().remove(timeSlider);
        }
        if (gameMode == GameMode.watch) {
            loginVBox.getChildren().remove(gameSettingsText);
            loginVBox.getChildren().remove(sizeTextField);
            connectButton.disableProperty().bind(usernameFieldValidProperty.not());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sizeTextField.setText("6");
    }

    private void setUsernameTextFieldValidators() {
        ImageView warnIcon = new ImageView("/images/warning.png");

        RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator("Username required!");
        requiredFieldValidator.setIcon(warnIcon);

        usernameTextField.getValidators().add(requiredFieldValidator);
        usernameTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                usernameFieldValidProperty.set(usernameTextField.validate());
            }
        });
    }

    private void setSizeTextFieldValidators() {
        ImageView warnIcon = new ImageView("/images/warning.png");

        RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator("Size required!");
        requiredFieldValidator.setIcon(warnIcon);

        IntegerValidator integerValidator = new IntegerValidator("Input must be a number!");
        integerValidator.setIcon(warnIcon);

        RegexValidator regexValidator = new RegexValidator("Input must be between 4 and 8!");
        regexValidator.setRegexPattern("[4-8]");
        regexValidator.setIcon(warnIcon);

        sizeTextField.getValidators().addAll(requiredFieldValidator, integerValidator, regexValidator);
        sizeTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                sizeTextFieldValidProperty.set(sizeTextField.validate());
            }
        });
    }

    @Override
    void loaded() {
        super.loaded();
        stage.setTitle("Login");

        setSizeTextFieldValidators();
        setUsernameTextFieldValidators();
        connectButton.disableProperty().bind(sizeTextFieldValidProperty.and(usernameFieldValidProperty).not());
    }

    @FXML
    public void connectClicked(MouseEvent mouseEvent) {
        Engine engine = LocalEngine.getInstance();
        engine.initializeBoard(Integer.parseInt(sizeTextField.getText()));

        if (gameMode == GameMode.humanVsRobot) {
            GUIPlayer guiPlayer = new GUIPlayer(engine, usernameTextField.getText());
            // TODO implement random names
            Robot robot = new Robot(engine, "R2-D2", (int) difficultySlider.getValue(), (int) timeSlider.getValue());
            engine.register(guiPlayer);
            engine.register(robot);

            GameAreaController areaController = (GameAreaController) openWindow(GameAreaController.class);
            areaController.setPlayer(guiPlayer);
        } else {
            openWindow(GameAreaController.class);
        }
    }
}
