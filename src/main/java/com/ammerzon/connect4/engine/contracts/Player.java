package com.ammerzon.connect4.engine.contracts;

import com.ammerzon.connect4.engine.Draw;

public interface Player {
    int getId();

    String getName();

    void sendDraw(Draw draw);
}
