package com.ammerzon.connect4.engine;

import com.ammerzon.connect4.engine.contracts.Client;
import com.ammerzon.connect4.engine.contracts.Engine;
import com.ammerzon.connect4.engine.contracts.Player;
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
    private List<Player> players = new ArrayList<>(2);
    private Board board = null;
    private Player currentPlayer;
    private int assignedIds = 0;

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
    public void receive(Player sender, Draw draw) {
        if (!draw.isTimeout()) {
            try {
                board.setTile(draw.getRow(), draw.getColumn(), sender.getId());
            } catch (BoardException e) {
                LOGGER.severe("Wrong client id sent!");
                e.printStackTrace();
            }
        }
        currentPlayer = players.stream().filter(i -> i.getId() != sender.getId()).findFirst().get();
        notifyClients();
    }

    @Override
    public void save() {
        // TODO: serialize
    }

    @Override
    public boolean register(Client client) {
        if (client instanceof Player) {
            players.add((Player) client);
        }
        return clients.add(client);
    }

    @Override
    public void notifyClients() {
        for (Client client : clients) {
            GameStatus state = new GameStatus(currentPlayer, board, System.currentTimeMillis());
            client.receive(state);
        }
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
        currentPlayer = sender;
        notifyClients();
    }
}
