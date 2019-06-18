package com.ammerzon.connect4.robot;

import swe4.connect4.api.PlayerSide;
import swe4.connect4.api.RemoteGameController;
import swe4.connect4.api.RemoteGamePlayer;
import swe4.connect4.api.RemoteGameServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.logging.Logger;

public class GameRobot extends UnicastRemoteObject implements RemoteGamePlayer {
    private static final Logger LOGGER = Logger.getLogger(GameRobot.class.getName());

    private PlayerSide[] playerSides;
    private int time;
    private int difficulty;
    private int width;
    private int height;
    private RemoteGameController gameController;

    public GameRobot(String name, String serviceURL, int time, int difficulty) throws RemoteException {
        this.time = time;
        this.difficulty = difficulty;

        try {
            RemoteGameServer gameServer = (RemoteGameServer) Naming.lookup(serviceURL);
            gameServer.registerObserver(this);
            gameController = gameServer.registerPlayer(this, name);
            gameController.setPreferredGameSize(6, 6);
            gameController.startGame();
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            LOGGER.info("Couldn't connect robot to server!");
        }
    }

    @Override
    public void startTurn(int roundTimeInSeconds) throws RemoteException {
        Runnable drawCalculation = () -> {
            try {
                Thread.sleep(time * 1000);
                gameController.dropCoin(calculateNewDraw());
            } catch (InterruptedException | RemoteException e) {
                e.printStackTrace();
            }
        };
        Thread t = new Thread(drawCalculation);
        t.start();
    }

    @Override
    public void setBoard(PlayerSide[] playerSides, int width, int height) throws RemoteException {
        this.width = width;
        this.height = height;
        this.playerSides = playerSides;
    }

    @Override
    public void endTurn() throws RemoteException {

    }

    @Override
    public void displayStartTurn(PlayerSide playerSide, int roundTimeInSeconds) throws RemoteException {

    }

    @Override
    public void setStatus(String status) throws RemoteException {

    }

    @Override
    public void setPlayer1Name(String name) throws RemoteException {

    }

    @Override
    public void setPlayer2Name(String name) throws RemoteException {

    }

    @Override
    public void reset() throws RemoteException {

    }

    @Override
    public void setWinner(PlayerSide playerSide) throws RemoteException {

    }

    private int calculateNewDraw() {
        Random random = new Random();
        boolean hasSet = false;
        int col = 0;

        while (!hasSet) {
            col = random.nextInt(width);
            if (playerSides[col * height] == PlayerSide.NONE) {
                hasSet = true;
            }
        }
        return col;
    }
}
