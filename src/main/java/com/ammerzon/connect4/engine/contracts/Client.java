package com.ammerzon.connect4.engine.contracts;

import com.ammerzon.connect4.engine.Draw;
import com.ammerzon.connect4.engine.GameStatus;

public interface Client {

    int getId();

    String getName();

    void sendDraw(Draw draw);

    void receive(GameStatus status);
}