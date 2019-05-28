package com.ammerzon.connect4.engine.contracts;

import com.ammerzon.connect4.engine.GameStatus;

public interface Client {
    /**
     * When a Client is subscribed to an engine this method will be called to receive the current status.
     *
     * @param status The current status.
     */
    void receive(GameStatus status);
}