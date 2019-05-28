package com.ammerzon.connect4.engine;

import com.ammerzon.connect4.engine.contracts.Client;
import com.ammerzon.connect4.engine.contracts.Engine;
import com.ammerzon.connect4.engine.contracts.Player;
import com.ammerzon.connect4.engine.exceptions.BoardException;
import com.ammerzon.connect4.gui.helper.GameMode;
import com.ammerzon.connect4.gui.helper.GameState;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LocalEngine implements Engine {

    private static final String PATH = "bin/savepoint.ser";
    private static final int INITIAL_SIZE = 6;
    private static final Logger LOGGER = Logger.getLogger(LocalEngine.class.getName());
    private static volatile LocalEngine instance = null;

    private int size = INITIAL_SIZE;
    private List<Client> clients = new ArrayList<>();
    private GameStatus status;
    private int assignedIds = 0;
    private boolean hasEnded;

    private LocalEngine() {
    }

    public static LocalEngine getInstance() {
        if (instance == null) {
            instance = new LocalEngine();
        }

        return instance;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public Board initializeBoard(int size, GameMode gameMode) {
        this.size = size;
        status = new GameStatus(gameMode, new Board(size), System.currentTimeMillis());

        return status.getBoard();
    }

    @Override
    public Board initializeBoard(GameStatus gameStatus) {
        this.size = gameStatus.getBoard().getSize();
        status = new GameStatus(gameStatus.getGameMode(), gameStatus.getBoard(), gameStatus.getTimeoutStart());

        return status.getBoard();
    }

    @Override
    public void receive(Player sender, Draw draw) {
        if (!draw.isTimeout() && status.getBoard().getStatus() == BoardStatus.RUNNING) {
            try {
                status.getBoard().setTile(draw.getRow(), draw.getColumn(), sender.getId());
            } catch (BoardException e) {
                LOGGER.severe("Wrong client id sent!");
                e.printStackTrace();
            }
        }
        status.setCurrentPlayer(status.getPlayers().stream().filter(i -> i.getId() != sender.getId()).findFirst().get());
        notifyClients();
    }

    @Override
    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PATH))) {
            oos.writeObject(status);
        } catch (IOException e) {
            System.err.println("Couldn't save the current state!");
            e.printStackTrace();
        }
    }

    @Override
    public GameStatus loadSavepoint() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PATH))) {
            return (GameStatus) ois.readObject();
        } catch (IOException e) {
            System.err.println("Couldn't load the current savepoint!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean register(Client client) {
        if (client instanceof Player) {
            status.getPlayers().add((Player) client);
        }
        return clients.add(client);
    }

    @Override
    public void notifyClients() {
        for (int i = 0; i < clients.size() && !hasEnded; i++) {
            status.setTimeoutStart(System.currentTimeMillis());
            clients.get(i).receive(status);
        }
        if (status.getBoard().getStatus() != BoardStatus.RUNNING) {
            hasEnded = true;
        }
        save();
    }

    @Override
    public int nextId() {
        if (assignedIds == 0) {
            assignedIds++;
            return Board.PLAYER1_KEY;
        } else if (assignedIds == 1) {
            assignedIds++;
            return Board.PLAYER2_KEY;
        } else {
            return -1;
        }
    }

    @Override
    public void start(Player sender) {
        status.setCurrentPlayer(sender);
        notifyClients();
    }
}
