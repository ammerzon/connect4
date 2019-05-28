package com.ammerzon.connect4.engine.contracts;

import com.ammerzon.connect4.engine.GameStatus;

public interface Client {
    void receive(GameStatus status);
}