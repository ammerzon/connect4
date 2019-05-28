package com.ammerzon.connect4.gui.controller;

import com.ammerzon.connect4.engine.*;
import com.ammerzon.connect4.engine.clients.GUIPlayer;
import com.ammerzon.connect4.engine.contracts.Client;
import com.ammerzon.connect4.engine.contracts.Engine;
import com.ammerzon.connect4.gui.helper.GameState;
import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.stage.Modality;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GameAreaController extends BaseController implements Initializable, Client {

    private static final int PADDING = 5;
    private static final int CIRCLE_PADDING = 6;
    private static final long TIMEOUT = 30;
    private static final Color PLAYER_1_COLOR = Color.valueOf("#F4C77D");
    private static final Color PLAYER_2_COLOR = Color.valueOf("#E44E60");
    @FXML
    public Label currentPlayerLabel;
    @FXML
    public Label remainingTimeLabel;
    @FXML
    public GridPane fieldGridPane;
    @FXML
    public JFXButton playButton;
    @FXML
    public JFXButton stopButton;
    @FXML
    public JFXButton restartButton;
    private Engine engine = LocalEngine.getInstance();
    private int size = engine.getSize();
    private GameState gameState = GameState.TURN_PLAYER_1;
    private int[][] board = null;
    private GUIPlayer player = null;
    private SimpleBooleanProperty gameRunning = new SimpleBooleanProperty(false);
    private Timeline timeline = new Timeline();
    private long timeoutStart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame timerKeyframe = new KeyFrame(Duration.seconds(1), event -> {
            long elapsedSeconds = TIMEOUT - (System.currentTimeMillis() - timeoutStart) / 1000;
            if (elapsedSeconds == 0) {
                player.sendDraw(new Draw(-1, -1, true));
            }
            remainingTimeLabel.setText(String.format("00:%02d", elapsedSeconds));
        });
        timeline.getKeyFrames().add(timerKeyframe);

        LocalEngine.getInstance().register(this);

        for (int i = 0; i < size; i++) {
            fieldGridPane.getColumnConstraints().add(new ColumnConstraints(getCellSize()));
            fieldGridPane.getRowConstraints().add(new RowConstraints(getCellSize()));
        }

        renderBoardHoles();
    }

    @Override
    void loaded() {
        stage.setTitle("Connect4");

        playButton.disableProperty().bind(gameRunning.not());
        stopButton.disableProperty().bind(gameRunning.not());
        restartButton.disableProperty().bind(gameRunning.not());
    }

    @FXML
    public void preferencesMenuItemClicked(ActionEvent actionEvent) {
        openNewWindow(SettingsController.class, 400, 430);
    }

    @FXML
    public void playButtonClicked(ActionEvent actionEvent) {
        engine.start(player);
        timeline.play();
        gameRunning.set(true);
    }

    @FXML
    public void stopButtonClicked(ActionEvent actionEvent) {
        gameRunning.set(false);
        timeline.stop();
    }

    @FXML
    public void restartButtonClicked(ActionEvent actionEvent) {
    }

    @FXML
    public void statsButtonClicked(ActionEvent actionEvent) {
        openNewWindow(HighScoreController.class, 900, 600);
    }

    @Override
    public void receive(GameStatus status) {
        if (status.getBoard().getStatus() == BoardStatus.RUNNING) {
            currentPlayerLabel.setText(status.getCurrentPlayer().getName());

            if (status.getCurrentPlayer().getId() == player.getId()) {
                fieldGridPane.disableProperty().setValue(false);
            }

            board = status.getBoard().getFieldCopy();
            timeoutStart = status.getTimeoutStart();
            renderBoard();
        } else {
            showWinnerDialog(status);
        }
    }

    public void setPlayer(GUIPlayer player) {
        this.player = player;
        playButton.disableProperty().bind(gameRunning);
    }

    private void gameAreaGridPaneClicked(int colIndex) {
        if (board[0][colIndex] == Board.DEFAULT_VAL && gameRunning.getValue()) {
            Circle circle = new Circle(getCellSize() / 2 - CIRCLE_PADDING);
            int rowIndex = getFirstPossibleRowIndex(colIndex);
            if (player == null) {
                // TODO: will be implemented in other game modes
                if (gameState == GameState.TURN_PLAYER_1) {
                    circle.setFill(PLAYER_1_COLOR);
                    board[rowIndex][colIndex] = 1;
                    gameState = GameState.TURN_PLAYER_2;
                } else {
                    circle.setFill(PLAYER_2_COLOR);
                    board[rowIndex][colIndex] = 2;
                    gameState = GameState.TURN_PLAYER_1;
                }

                fieldGridPane.add(circle, colIndex, rowIndex);
            } else {
                circle.setFill(PLAYER_1_COLOR);
                board[rowIndex][colIndex] = player.getId();
                fieldGridPane.add(circle, colIndex, rowIndex);
                fieldGridPane.disableProperty().setValue(true);
                player.sendDraw(new Draw(colIndex, rowIndex));
            }
        }
    }

    private double getCellSize() {
        double width = fieldGridPane.getPrefWidth();
        return width / size - PADDING;
    }

    private int getFirstPossibleRowIndex(int colIndex) {
        for (int i = size - 1; i >= 0; i--) {
            if (board[i][colIndex] == 0) {
                return i;
            }
        }

        return 0;
    }

    private void showWinnerDialog(GameStatus status) {
        JFXDialogLayout layout = new JFXDialogLayout();
        StringBuilder sb = new StringBuilder();
        sb.append("Player ");
        if (status.getBoard().getStatus() == BoardStatus.PLAYER1_WON) {
            sb.append("1");
        } else if (status.getBoard().getStatus() == BoardStatus.PLAYER2_WON) {
            sb.append("2");
        }
        sb.append(" has won!");

        layout.setBody(new Label(sb.toString()));
        JFXAlert<Void> alert = new JFXAlert<>(stage);
        alert.setOverlayClose(true);
        alert.setAnimation(JFXAlertAnimation.CENTER_ANIMATION);
        alert.setContent(layout);
        alert.initModality(Modality.NONE);
        alert.show();
    }

    private void renderBoard() {
        fieldGridPane.getChildren().clear();
        renderBoardHoles();

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] != Board.DEFAULT_VAL) {
                    Circle circle = new Circle(getCellSize() / 2 - CIRCLE_PADDING);

                    if (board[row][col] == Board.PLAYER1_KEY) {
                        circle.setFill(PLAYER_1_COLOR);
                    } else if (board[row][col] == Board.PLAYER2_KEY) {
                        circle.setFill(PLAYER_2_COLOR);
                    }

                    fieldGridPane.add(circle, col, row);
                }
            }
        }
    }

    private void renderBoardHoles() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Circle circle = new Circle(getCellSize() / 2 - CIRCLE_PADDING);
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
}
