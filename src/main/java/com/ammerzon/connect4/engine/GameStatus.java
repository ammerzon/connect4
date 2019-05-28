package com.ammerzon.connect4.engine;

import com.ammerzon.connect4.engine.contracts.Player;
import com.ammerzon.connect4.gui.helper.GameMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameStatus implements Serializable {
    private static final long serialVersionUID = 5167730551526714361L;
    private Player currentPlayer;
    private List<Player> players = new ArrayList<>(2);
    private Board board;
    private long timeoutStart;
    private GameMode gameMode;

    public GameStatus(GameMode gameMode, Board board, long timeoutStart) {
        this.gameMode = gameMode;
        this.board = board;
        this.timeoutStart = timeoutStart;
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public long getTimeoutStart() {
        return timeoutStart;
    }

    public void setTimeoutStart(long timeoutStart) {
        this.timeoutStart = timeoutStart;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
