package com.ammerzon.connect4.engine.contracts;

import com.ammerzon.connect4.engine.Draw;

import java.io.Serializable;

public interface Player extends Serializable {
    int getId();

    String getName();

    void sendDraw(Draw draw);
}
