package com.ammerzon.connect4.engine.contracts;

import com.ammerzon.connect4.engine.Board;
import com.ammerzon.connect4.engine.Draw;

public interface Engine {
    int getSize();

    Board initializeBoard(int size);

    void receive(Client sender, Draw draw);

    void save();

    boolean register(Client client);

    void notifyClients();
}
