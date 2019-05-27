package com.ammerzon.connect4.engine;

import com.ammerzon.connect4.engine.contracts.Client;

public class GameStatus {
    private Client currentClient;
    private Board board;

    public GameStatus(Client currentClient, Board board) {
        this.currentClient = currentClient;
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    public Client getCurrentClient() {
        return currentClient;
    }
}
