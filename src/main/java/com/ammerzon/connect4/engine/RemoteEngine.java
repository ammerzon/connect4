package com.ammerzon.connect4.engine;

import swe4.connect4.api.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class RemoteEngine implements RemoteGameServer, RemoteGameController {
    private static final String PATH = "connect4";
    private static final int RMI_PORT = 1099;
    private static final int ROUND_TIME = 30;
    private static final String CONNECTION_STRING = "rmi://localhost:" + RMI_PORT + "/" + PATH;
    private static final Logger LOGGER = Logger.getLogger(RemoteEngine.class.getName());

    private List<RemoteGameObserver> observers = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private GameState gameState = GameState.STOPPED;
    private PlayerSide currentPlayer = PlayerSide.NONE;
    private PlayerSide[] board;
    private ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
    private int width = 0;
    private int height = 0;

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(RMI_PORT);
            RemoteEngine server = new RemoteEngine();
            Naming.rebind(CONNECTION_STRING, UnicastRemoteObject.exportObject(server, 0));
            System.out.printf("Service available under '%s'\n", CONNECTION_STRING);
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerObserver(RemoteGameObserver remoteGameObserver) throws RemoteException {
        if (remoteGameObserver == null)
            throw new RemoteException("RemoteGameObserver must not be null!");

        observers.add(remoteGameObserver);
    }

    @Override
    public RemoteGameController registerPlayer(RemoteGamePlayer remoteGamePlayer, String name) throws RemoteException {
        if (remoteGamePlayer == null)
            throw new RemoteException("RemoteGamePlayer must not be null!");
        if (players.size() == 2) {
            throw new RemoteException("Only two player can register!");
        }

        players.add(new Player(remoteGamePlayer, name));
        LOGGER.info(name + " connected to the server!");
        if (players.size() == 1) {
            setStatus("Waiting for a second player");
            setPlayer1Name();
        } else if (players.size() == 2) {
            setStatus(name + " connected");
            setPlayer2Name();
        }

        return this;
    }

    @Override
    public ScoreboardEntry[] getScoreboard() throws RemoteException {
        // TODO: Implement scoreboard functionality
        return null;
    }

    @Override
    public void setPreferredGameSize(int width, int height) throws RemoteException {
        if (width > 0 && height > 0 && gameState == GameState.STOPPED) {
            this.width = width;
            this.height = height;
        }
    }

    @Override
    public void startGame() throws RemoteException {
        if (gameState == GameState.STOPPED && players.size() == 2) {
            board = new PlayerSide[width * height];
            Arrays.fill(board, PlayerSide.NONE);
            gameState = GameState.RUNNING;
            currentPlayer = PlayerSide.PLAYER1;
            displayStartTurn();
            setBoard();
            players.get(0).remoteGamePlayer.startTurn(ROUND_TIME);
            scheduleTimer();
            setStatus("Game started");
        }
    }

    private void scheduleTimer() {
        timer = Executors.newSingleThreadScheduledExecutor();
        timer.schedule(() -> {
            try {
                RemoteEngine.this.switchCurrentPlayer();
            } catch (RemoteException e) {
                LOGGER.info("Couldn't switch current player!");
            }
        }, 30, TimeUnit.SECONDS);
    }

    @Override
    public void restartGame() throws RemoteException {
        if (gameState != GameState.STOPPED) {
            gameState = GameState.STOPPED;
            reset();
            startGame();
        }
    }

    @Override
    public void abortGame() throws RemoteException {
        if (gameState != GameState.STOPPED) {
            board = null;
            gameState = GameState.STOPPED;
            currentPlayer = PlayerSide.NONE;
            displayStartTurn();
            setStatus("Game stopped");
        }
    }

    @Override
    public void dropCoin(int column) throws RemoteException {
        boolean hasDropped = false;

        if (currentPlayer != PlayerSide.NONE && gameState == GameState.RUNNING && column <= width) {
            for (int i = (column + 1) * height - 1; i >= column * height && !hasDropped; i--) {
                if (board[i] == PlayerSide.NONE) {
                    hasDropped = true;
                    board[i] = currentPlayer;
                    setBoard();
                    if (!hasPlayerWon()) {
                        switchCurrentPlayer();
                    }
                }
            }
        }
    }

    private void switchCurrentPlayer() throws RemoteException {
        if (!timer.isShutdown()) {
            timer.shutdownNow();
        }
        if (currentPlayer == PlayerSide.PLAYER1) {
            players.get(0).remoteGamePlayer.endTurn();
            currentPlayer = PlayerSide.PLAYER2;
            players.get(1).remoteGamePlayer.startTurn(ROUND_TIME);
        } else if (currentPlayer == PlayerSide.PLAYER2) {
            players.get(1).remoteGamePlayer.endTurn();
            currentPlayer = PlayerSide.PLAYER1;
            players.get(0).remoteGamePlayer.startTurn(ROUND_TIME);
        }
        displayStartTurn();
        scheduleTimer();
    }

    private void reset() {
        for (RemoteGameObserver observer : observers) {
            try {
                observer.reset();
            } catch (RemoteException e) {
                LOGGER.warning("Couldn't reset observer!");
            }
        }
    }

    private void setStatus(String msg) {
        for (RemoteGameObserver observer : observers) {
            try {
                observer.setStatus(msg);
            } catch (RemoteException e) {
                LOGGER.warning("Couldn't send status to observer!");
            }
        }
    }

    private void displayStartTurn() {
        for (RemoteGameObserver observer : observers) {
            try {
                observer.displayStartTurn(currentPlayer, ROUND_TIME);
            } catch (RemoteException e) {
                LOGGER.warning("Couldn't send status to observer!");
            }
        }
    }

    private void setPlayer1Name() {
        for (RemoteGameObserver observer : observers) {
            try {
                observer.setPlayer1Name(players.get(0).name);
            } catch (RemoteException e) {
                LOGGER.warning("Couldn't set player 1 name!");
            }
        }
    }

    private void setPlayer2Name() {
        for (RemoteGameObserver observer : observers) {
            try {
                observer.setPlayer2Name(players.get(1).name);
            } catch (RemoteException e) {
                LOGGER.warning("Couldn't set player 1 name!");
            }
        }
    }

    private void setBoard() {
        for (RemoteGameObserver observer : observers) {
            try {
                observer.setBoard(board, width, height);
            } catch (RemoteException e) {
                LOGGER.warning("Couldn't set player 1 name!");
            }
        }
    }

    private boolean hasPlayerWon() {
        return false;
    }
}
