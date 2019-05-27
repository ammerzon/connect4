package com.ammerzon.connect4.engine;

import com.ammerzon.connect4.engine.contracts.Client;
import com.ammerzon.connect4.engine.contracts.Engine;
import com.ammerzon.connect4.engine.exceptions.BoardException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LocalEngine implements Engine {

    private static final int INITIAL_SIZE = 6;
    private static final Logger LOGGER = Logger.getLogger(LocalEngine.class.getName());
    private static volatile LocalEngine instance = null;

    private int size = INITIAL_SIZE;
    private List<Client> clients = new ArrayList<>();
    private Board board = null;
    private Client currentClient;

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
    public Board initializeBoard(int size) {
        if (board == null) {
            this.size = size;
            board = new Board(size);
        }

        return board;
    }

    @Override
    public void receive(Client sender, Draw draw) {
        try {
            board.setTile(draw.getRow(), draw.getColumn(), sender.getId());
        } catch (BoardException e) {
            LOGGER.severe("Wrong client id sent!");
            e.printStackTrace();
        }
        currentClient = sender;
        notifyClients();
    }

    @Override
    public void save() {
        // TODO: serialize
    }

    @Override
    public boolean register(Client client) {
        return clients.add(client);
    }

    @Override
    public void notifyClients() {
        GameStatus state = new GameStatus(currentClient, board);
        clients.stream().filter(i -> i.getId() != currentClient.getId()).forEach(i -> i.receive(state));
    }
}
