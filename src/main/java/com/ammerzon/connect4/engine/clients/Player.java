package com.ammerzon.connect4.engine.clients;

import com.ammerzon.connect4.engine.Draw;
import com.ammerzon.connect4.engine.GameStatus;
import com.ammerzon.connect4.engine.contracts.Client;

public class Player implements Client {
    private String name;
    private int id;

    public Player(String name, int id) {
        this.name = name;
        this.id = id;
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

    }

    @Override
    public void receive(GameStatus status) {

    }
}
