package com.ammerzon.connect4.engine.contracts;

import com.ammerzon.connect4.engine.Draw;

import java.io.Serializable;

public interface Player extends Serializable {
    /**
     * Returns the unique id.
     *
     * @return The id.
     */
    int getId();

    /**
     * Return the name.
     *
     * @return The name.
     */
    String getName();

    /**
     * Sends a draw and notifies the engine.
     *
     * @param draw The draw.
     */
    void sendDraw(Draw draw);
}
