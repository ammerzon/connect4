package com.ammerzon.connect4.engine.clients;

import com.ammerzon.connect4.engine.Draw;
import com.ammerzon.connect4.engine.GameStatus;
import com.ammerzon.connect4.engine.contracts.Client;
import com.ammerzon.connect4.engine.contracts.Engine;
import com.ammerzon.connect4.engine.contracts.Player;

public class GUIPlayer implements Client, Player {

    private static final long serialVersionUID = -5676636618076899125L;
    private String name;
    private int id;
    private transient Engine engine;

    public GUIPlayer(Engine engine, String name) {
        this.engine = engine;
        this.name = name;
        this.id = engine.nextId();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void sendDraw(Draw draw) {
        engine.receive(this, draw);
    }

    @Override
    public void receive(GameStatus status) {
    }
}
