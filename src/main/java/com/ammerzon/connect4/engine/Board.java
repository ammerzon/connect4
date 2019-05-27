package com.ammerzon.connect4.engine;

import com.ammerzon.connect4.engine.exceptions.BoardException;

public class Board {
    public static final int PLAYER1_KEY = 1;
    public static final int PLAYER2_KEY = 2;

    private int[][] field;

    public Board(int size) {
        field = new int[size][size];
    }

    public int getPoints() {
        // TODO calculate points
        return -1;
    }

    public void setTile(int row, int col, int player) throws BoardException {
        if (player != PLAYER1_KEY && player != PLAYER2_KEY) {
            throw new BoardException(String.format("Val must be either %d or %d", PLAYER1_KEY, PLAYER2_KEY));
        }
        if (row < 0 || col < 0 || row > field.length || col > field[0].length) {
            throw new BoardException(String.format("Row and col must be greater equal zero and less than %d!", field.length));
        }

        field[row][col] = player;
    }

    public BoardStatus getStatus() {
        // TODO check status
        return BoardStatus.UNKNOWN;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int[] y : field) {
            for (int x : y) {
                sb.append(x).append(" | ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
