package com.ammerzon.connect4.engine.contracts;

import com.ammerzon.connect4.engine.Board;
import com.ammerzon.connect4.engine.Draw;
import com.ammerzon.connect4.engine.GameStatus;
import com.ammerzon.connect4.gui.helper.GameMode;

public interface Engine {
    int getSize();

    Board initializeBoard(int size, GameMode gameMode);

    Board initializeBoard(GameStatus gameStatus);

    void receive(Player sender, Draw draw);

    void save();

    GameStatus loadSavepoint();

    boolean register(Client client);

    void notifyClients();

    int nextId();

    void start(Player sender);
}
