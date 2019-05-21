package com.ammerzon.connect4.gui.controller;

import com.ammerzon.connect4.gui.helper.GameState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class GameAreaController extends Controller implements Initializable {

    @FXML
    public Label currentPlayerLabel;
    @FXML
    public Label remainingTimeLabel;
    @FXML
    public GridPane fieldGridPane;

    private int size = 6;
    private int padding = 5;
    private int circlePadding = 6;
    private Color player1Color = Color.valueOf("#F4C77D");
    private Color player2Color = Color.valueOf("#E44E60");
    private GameState gameState = GameState.TURN_PLAYER_1;
    private int[][] field = new int[size][size];

    private double getCellSize() {
        double width = fieldGridPane.getPrefWidth();
        return width / size - padding;
    }

    private int getFirstPossibleRowIndex(int colIndex) {
        for (int i = size - 1; i >= 0; i--) {
            if (field[i][colIndex] == 0) {
                return i;
            }
        }

        return 0;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (int i = 0; i < size; i++) {
            fieldGridPane.getColumnConstraints().add(new ColumnConstraints(getCellSize()));
            fieldGridPane.getRowConstraints().add(new RowConstraints(getCellSize()));
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Circle circle = new Circle(getCellSize() / 2 - circlePadding);
                circle.setFill(Color.WHITE);
                circle.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.valueOf("#00000024"), 10.0, 0.0, -1, -2));
                int col = j;
                circle.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    gameAreaGridPaneClicked(col);
                });
                fieldGridPane.add(circle, j, i);
            }
        }
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

    public void gameAreaGridPaneClicked(int colIndex) {
        if (field[0][colIndex] == 0) {
            Circle circle = new Circle(getCellSize() / 2 - circlePadding);
            int rowIndex = getFirstPossibleRowIndex(colIndex);
            if (gameState == GameState.TURN_PLAYER_1) {
                circle.setFill(player1Color);
                field[rowIndex][colIndex] = 1;
                gameState = GameState.TURN_PLAYER_2;
            } else {
                circle.setFill(player2Color);
                field[rowIndex][colIndex] = 2;
                gameState = GameState.TURN_PLAYER_1;
            }
            fieldGridPane.add(circle, colIndex, rowIndex);
        }
    }
}
