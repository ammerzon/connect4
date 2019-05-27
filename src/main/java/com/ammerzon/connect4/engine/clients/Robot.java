package com.ammerzon.connect4.engine.clients;

import com.ammerzon.connect4.engine.Draw;
import com.ammerzon.connect4.engine.GameStatus;
import com.ammerzon.connect4.engine.contracts.Client;

public class Robot implements Client {
    private String name;
    private int difficulty;
    private int time;
    private int id;

    public Robot(String name, int id) {
        this(name, id, 1);
    }

    public Robot(String name, int id, int difficulty) {
        this(name, id, difficulty, 15);
    }

    public Robot(String name, int id, int difficulty, int time) {
        this.name = name;
        this.id = id;
        this.difficulty = difficulty;
        this.time = time;
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
