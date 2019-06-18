package com.ammerzon.connect4.presentation.board;

import com.ammerzon.connect4.business.game.boundary.GameService;
import com.ammerzon.connect4.business.game.entity.GameMode;
import com.ammerzon.connect4.presentation.helper.BasePresenter;
import com.ammerzon.connect4.presentation.settings.SettingsView;
import com.ammerzon.connect4.robot.GameRobot;
import com.jfoenix.controls.JFXButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
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
import javafx.util.Duration;
import swe4.connect4.api.PlayerSide;
import swe4.connect4.api.RemoteGameController;
import swe4.connect4.api.RemoteGamePlayer;
import swe4.connect4.api.RemoteGameServer;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class BoardPresenter extends BasePresenter implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(BoardPresenter.class.getName());
    private static final int PADDING = 5;
    private static final int CIRCLE_PADDING = 3;
    private static final Color PLAYER_1_COLOR = Color.valueOf("#F4C77D");
    private static final Color PLAYER_2_COLOR = Color.valueOf("#E44E60");
    private static final String NONE_PLAYERSIDE = "n/a";
    private static final String PATH = "connect4";
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
    @FXML
    public Label statusLabel;
    @Inject
    private GameService gameService;

    private Timeline timeline = new Timeline();
    private BooleanProperty hasTurn = new SimpleBooleanProperty(true);
    private GameCommunicator gameCommunicator;
    private String player1Name;
    private String player2Name;
    private long timeoutStart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String serviceUrl = "rmi://" + gameService.getHost() + ":" + gameService.getPort() + "/" + PATH;

        try {
            gameCommunicator = new GameCommunicator();

            if (gameService.getGameMode() == GameMode.humanVsRobot || gameService.getGameMode() == GameMode.robotVsRobot) {
                new GameRobot("R2-D2", serviceUrl, gameService.getTime(), gameService.getDifficulty());
            }
        } catch (RemoteException e) {
            LOGGER.info("Couldn't connect to game server!");
        }

        fieldGridPane.disableProperty().bind(hasTurn.not());
        stopButton.disableProperty().setValue(true);
        restartButton.disableProperty().setValue(true);
    }

    @FXML
    public void preferencesMenuItemClicked(ActionEvent actionEvent) {
        presentModally(SettingsView.class, "Settings");
    }

    @FXML
    public void playButtonClicked(ActionEvent actionEvent) {
        try {
            gameCommunicator.gameController.setPreferredGameSize(gameService.getWidth(), gameService.getHeight());
            gameCommunicator.gameController.startGame();
        } catch (RemoteException e) {
            LOGGER.info("Couldn't start the game!");
        }
    }

    @FXML
    public void stopButtonClicked(ActionEvent actionEvent) {
        try {
            gameCommunicator.gameController.abortGame();
        } catch (RemoteException e) {
            LOGGER.info("Couldn't start the game!");
        }
    }

    @FXML
    public void restartButtonClicked(ActionEvent actionEvent) {
        try {
            gameCommunicator.gameController.restartGame();
        } catch (RemoteException e) {
            LOGGER.info("Couldn't restart the game!");
        }
    }

    @FXML
    public void statsButtonClicked(ActionEvent actionEvent) {
        showDialog("Not implemented!", "This feature is not implemented!");
    }

    private void renderBoardHoles() {
        for (int col = 0; col < gameService.getWidth(); col++) {
            for (int row = 0; row < gameService.getHeight(); row++) {
                Circle circle = new Circle(getCellSize() / 2 - CIRCLE_PADDING);
                circle.setFill(Color.WHITE);
                circle.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.valueOf("#00000024"), 10.0, 0.0, -1, -2));
                int finalCol = col;
                circle.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    gameAreaGridPaneClicked(finalCol);
                });
                fieldGridPane.add(circle, col, row);
            }
        }
    }

    private double getCellSize() {
        double width = fieldGridPane.getPrefWidth();
        return width / gameService.getWidth() - PADDING;
    }

    private void gameAreaGridPaneClicked(int col) {
        Task dropCoin = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    gameCommunicator.gameController.dropCoin(col);
                } catch (RemoteException e) {
                    LOGGER.info("Couldn't drop coin!");
                }
                return null;
            }
        };
        new Thread(dropCoin).start();
    }

    private void startCountdown(int countdownInSeconds) {
        timeoutStart = System.currentTimeMillis();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame timerKeyframe = new KeyFrame(Duration.seconds(1), event -> {
            long elapsedSeconds = countdownInSeconds - (System.currentTimeMillis() - timeoutStart) / 1000;
            remainingTimeLabel.setText(String.format("00:%02d", elapsedSeconds));
        });
        timeline.getKeyFrames().add(timerKeyframe);
        timeline.play();
    }

    class GameCommunicator extends UnicastRemoteObject implements RemoteGamePlayer {
        RemoteGameServer gameServer;
        RemoteGameController gameController;

        protected GameCommunicator() throws RemoteException {
            String serviceUrl = "rmi://" + gameService.getHost() + ":" + gameService.getPort() + "/" + PATH;

            try {
                gameServer = (RemoteGameServer) Naming.lookup(serviceUrl);
                gameServer.registerObserver(this);

                if (gameService.getGameMode() == GameMode.humanVsHuman || gameService.getGameMode() == GameMode.humanVsRobot) {
                    gameController = gameServer.registerPlayer(this, gameService.getUsername());
                }
            } catch (NotBoundException | MalformedURLException e) {
                LOGGER.info("Couldn't connect to game server!");
            }
        }

        @Override
        public void displayStartTurn(PlayerSide playerSide, int countdownInSeconds) throws RemoteException {
            playButton.disableProperty().setValue(true);
            stopButton.disableProperty().setValue(false);
            restartButton.disableProperty().setValue(false);
            switch (playerSide) {
                case NONE:
                    Platform.runLater(() -> currentPlayerLabel.setText(NONE_PLAYERSIDE));
                    timeline.stop();
                    break;
                case PLAYER1:
                    Platform.runLater(() -> currentPlayerLabel.setText(player1Name));
                    startCountdown(countdownInSeconds);
                    break;
                case PLAYER2:
                    Platform.runLater(() -> currentPlayerLabel.setText(player2Name));
                    startCountdown(countdownInSeconds);
                    break;
            }
        }

        @Override
        public void setStatus(String status) throws RemoteException {
            Platform.runLater(() -> statusLabel.setText(status));
        }

        @Override
        public void setPlayer1Name(String name) throws RemoteException {
            player1Name = name;
        }

        @Override
        public void setPlayer2Name(String name) throws RemoteException {
            player2Name = name;
        }

        @Override
        public void reset() throws RemoteException {
            player1Name = null;
            player2Name = null;
            playButton.disableProperty().setValue(false);
            stopButton.disableProperty().setValue(true);
            restartButton.disableProperty().setValue(true);
        }

        @Override
        public void setBoard(PlayerSide[] playerSides, int width, int height) throws RemoteException {
            Platform.runLater(() -> {
                gameService.setWidth(width);
                gameService.setHeight(height);
                for (int i = fieldGridPane.getColumnConstraints().size(); i < width; i++) {
                    fieldGridPane.getColumnConstraints().add(new ColumnConstraints(getCellSize()));
                }

                for (int i = fieldGridPane.getRowConstraints().size(); i < height; i++) {
                    fieldGridPane.getRowConstraints().add(new RowConstraints(getCellSize()));
                }

                fieldGridPane.getChildren().clear();
                renderBoardHoles();

                for (int i = 0; i < playerSides.length; i++) {
                    if (playerSides[i] != PlayerSide.NONE) {
                        Circle circle = new Circle(getCellSize() / 2 - CIRCLE_PADDING);

                        if (playerSides[i] == PlayerSide.PLAYER1) {
                            circle.setFill(PLAYER_1_COLOR);
                        } else if (playerSides[i] == PlayerSide.PLAYER2) {
                            circle.setFill(PLAYER_2_COLOR);
                        }

                        int row = i % height;
                        int col = i / height;
                        fieldGridPane.add(circle, col, row);
                    }
                }
            });
        }

        @Override
        public void setWinner(PlayerSide playerSide) throws RemoteException {
            String message = "";

            switch (playerSide) {
                case NONE:
                    message = "No player has won! Tie!";
                    break;
                case PLAYER1:
                    message = player1Name + " has won!";
                    break;
                case PLAYER2:
                    message = player2Name + " has won!";
                    break;
            }

            String finalMessage = message;
            Platform.runLater(() -> showDialog("Game ended!", finalMessage));
            timeline.stop();
        }

        @Override
        public void startTurn(int roundTimeInSeconds) throws RemoteException {
            hasTurn.setValue(true);
        }

        @Override
        public void endTurn() throws RemoteException {
            hasTurn.setValue(false);
        }
    }
}
