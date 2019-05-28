package com.ammerzon.connect4.engine.contracts;

import com.ammerzon.connect4.engine.Board;
import com.ammerzon.connect4.engine.Draw;

public interface Engine {
    int getSize();

    Board initializeBoard(int size);

    void receive(Player sender, Draw draw);

    void save();

    boolean register(Client client);

    void notifyClients();

    int nextId();

    void start(Player sender);
}
