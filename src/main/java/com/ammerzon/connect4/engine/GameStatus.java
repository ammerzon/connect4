package com.ammerzon.connect4.engine;

import com.ammerzon.connect4.engine.contracts.Player;

public class GameStatus {
    private Player currentPlayer;
    private Board board;
    private long timeoutStart;

    public GameStatus(Player currentPlayer, Board board, long timeoutStart) {
        this.currentPlayer = currentPlayer;
        this.board = board;
        this.timeoutStart = timeoutStart;
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public long getTimeoutStart() {
        return timeoutStart;
    }
}
