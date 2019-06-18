package com.ammerzon.connect4.presentation.login;

import com.ammerzon.connect4.business.game.boundary.GameService;
import com.ammerzon.connect4.business.game.entity.GameMode;
import com.ammerzon.connect4.presentation.board.BoardView;
import com.ammerzon.connect4.presentation.helper.BasePresenter;
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

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginPresenter extends BasePresenter implements Initializable {
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
    public JFXTextField widthTextField;
    @FXML
    public JFXTextField heightTextField;
    @FXML
    public JFXTextField usernameTextField;
    @FXML
    public JFXTextField hostTextField;
    @FXML
    public JFXTextField portTextField;
    @FXML
    public JFXButton connectButton;
    @FXML
    public Text gameSettingsText;
    @Inject
    private GameService gameService;
    private SimpleBooleanProperty sizeTextFieldValidProperty = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty usernameFieldValidProperty = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        widthTextField.setText("7");
        heightTextField.setText("6");
        hostTextField.setText("localhost");
        portTextField.setText("1099");

        GameMode gameMode = gameService.getGameMode();
        if (gameMode == GameMode.humanVsHuman || gameMode == GameMode.watch) {
            loginVBox.getChildren().remove(robotSettingsText);
            difficultyHBox.getChildren().remove(difficultyText);
            difficultyHBox.getChildren().remove(difficultySlider);
            timeHBox.getChildren().remove(timeText);
            timeHBox.getChildren().remove(timeSlider);
        }
        if (gameMode == GameMode.watch) {
            loginVBox.getChildren().remove(gameSettingsText);
            loginVBox.getChildren().remove(widthTextField);
            connectButton.disableProperty().bind(usernameFieldValidProperty.not());
        }

        setSizeTextFieldValidators();
        setUsernameTextFieldValidators();
        connectButton.disableProperty().bind(sizeTextFieldValidProperty.and(usernameFieldValidProperty).not());
        gameService.hostProperty().bind(hostTextField.textProperty());
        gameService.usernameProperty().bind(usernameTextField.textProperty());
    }

    @FXML
    public void connectClicked(MouseEvent mouseEvent) {
        gameService.setWidth(Integer.parseInt(widthTextField.getText()));
        gameService.setHeight(Integer.parseInt(heightTextField.getText()));
        gameService.setPort(Integer.parseInt(portTextField.getText()));
        GameMode gameMode = gameService.getGameMode();
        if (gameMode == GameMode.robotVsRobot || gameMode == GameMode.humanVsRobot) {
            gameService.setTime((int) timeSlider.getValue());
            gameService.setTime((int) difficultySlider.getValue());
        }
        show(BoardView.class, "Connect4");
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

        widthTextField.getValidators().addAll(requiredFieldValidator, integerValidator, regexValidator);
        widthTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                sizeTextFieldValidProperty.set(widthTextField.validate());
            }
        });

        heightTextField.getValidators().addAll(requiredFieldValidator, integerValidator, regexValidator);
        heightTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                sizeTextFieldValidProperty.set(heightTextField.validate());
            }
        });
    }
}
