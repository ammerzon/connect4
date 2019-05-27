package com.ammerzon.connect4.gui.controller;

import com.ammerzon.connect4.gui.helper.GameMode;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.IntegerValidator;
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
    private SimpleBooleanProperty sizeTextFieldValidProperty = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty usernameFieldValidProperty = new SimpleBooleanProperty(false);
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

        sizeTextField.getValidators().addAll(requiredFieldValidator, integerValidator);
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
        openWindow(GameAreaController.class);
    }
}
