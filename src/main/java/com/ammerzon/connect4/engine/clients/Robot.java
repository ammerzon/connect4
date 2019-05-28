package com.ammerzon.connect4.engine.clients;

import com.ammerzon.connect4.engine.Draw;
import com.ammerzon.connect4.engine.GameStatus;
import com.ammerzon.connect4.engine.contracts.Client;
import com.ammerzon.connect4.engine.contracts.Engine;
import com.ammerzon.connect4.engine.contracts.Player;

public class Robot implements Client, Player {

    private static final long serialVersionUID = 2925446995481765691L;
    private transient Engine engine;
    private String name;
    private int difficulty;
    private int time;
    private int id;

    public Robot(Engine engine, String name) {
        this(engine, name, 1);
    }

    public Robot(Engine engine, String name, int difficulty) {
        this(engine, name, difficulty, 15);
    }

    public Robot(Engine engine, String name, int difficulty, int time) {
        this.engine = engine;
        this.name = name;
        this.id = engine.nextId();
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
        engine.receive(this, draw);
    }

    @Override
    public void receive(GameStatus status) {
        if (status.getCurrentPlayer().getId() == id) {
            Runnable drawCalculation = new Runnable() {
                @Override
                public void run() {
                    Draw draw = calculateNewDraw(status.getBoard().getFieldCopy());
                    sendDraw(draw);
                }
            };
            Thread t = new Thread(drawCalculation);
            t.start();
        }
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getTime() {
        return time;
    }

    private Draw calculateNewDraw(int[][] field) {

        for (int row = 0; row < field.length; row++) {
            for (int col = 0; col < field[row].length; col++) {
                if (field[row][col] != 0 && row - 1 >= 0) {
                    return new Draw(col, row - 1);
                }
            }
        }

        return new Draw(0, 0);
    }
}
