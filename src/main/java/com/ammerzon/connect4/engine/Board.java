package com.ammerzon.connect4.engine;

import com.ammerzon.connect4.engine.exceptions.BoardException;

import java.io.Serializable;

public class Board implements Serializable {
    public static final int DEFAULT_VAL = 0;
    public static final int PLAYER1_KEY = 1;
    public static final int PLAYER2_KEY = 2;
    private static final long serialVersionUID = 5456870289367765153L;

    private int[][] field;
    private BoardStatus currentBoardStatus = BoardStatus.RUNNING;

    public Board(int size) {
        field = new int[size][size];
    }

    public int getPoints() {
        // TODO calculate points
        return -1;
    }

    public int[][] getFieldCopy() {
        return field.clone();
    }

    public void setTile(int row, int col, int player) throws BoardException {
        if (player != PLAYER1_KEY && player != PLAYER2_KEY) {
            throw new BoardException(String.format("Val must be either %d or %d", PLAYER1_KEY, PLAYER2_KEY));
        }
        if (row < 0 || col < 0 || row > field.length || col > field[0].length) {
            throw new BoardException(String.format("Row and col must be greater equal zero and less than %d!", field.length));
        }

        field[row][col] = player;
        updateBoardStatus();
    }

    public BoardStatus getStatus() {
        return currentBoardStatus;
    }

    public int getFirstPossibleRowIndex(int colIndex) {
        for (int i = field.length - 1; i >= 0; i--) {
            if (field[i][colIndex] == 0) {
                return i;
            }
        }

        return 0;
    }

    private void updateBoardStatus() {
        for (int row = field.length - 1; row >= 0; row--) {
            for (int col = 0; col < field[row].length; col++) {
                int tileId = field[row][col];
                if (tileId != DEFAULT_VAL) {
                    // Check horizontal line
                    if (col + 3 < field.length &&
                            tileId == field[row][col + 1] &&
                            tileId == field[row][col + 2] &&
                            tileId == field[row][col + 3]) {
                        currentBoardStatus = getWinner(tileId);
                        break;
                    }
                    if (row + 3 < field.length) {
                        // Check vertical line
                        if (tileId == field[row + 1][col] &&
                                tileId == field[row + 2][col] &&
                                tileId == field[row + 3][col]) {
                            currentBoardStatus = getWinner(tileId);
                            break;
                        }
                        // Check backslash (up and left) line
                        if (col - 3 >= 0 &&
                                tileId == field[row + 1][col - 1] &&
                                tileId == field[row + 2][col - 2] &&
                                tileId == field[row + 3][col - 3]) {
                            currentBoardStatus = getWinner(tileId);
                            break;
                        }
                        // Check slash (up and right) line
                        if (col + 3 < field.length &&
                                tileId == field[row + 1][col + 1] &&
                                tileId == field[row + 2][col + 2] &&
                                tileId == field[row + 3][col + 3]) {
                            currentBoardStatus = getWinner(tileId);
                            break;
                        }
                    }
                }
            }
        }

        if (hasNoWinner()) {
            currentBoardStatus = BoardStatus.TIE;
        }
    }

    public int getSize() {
        return field.length;
    }

    private boolean hasNoWinner() {
        for (int[] row : field) {
            for (int col = 0; col < field.length; col++) {
                if (row[col] == Board.DEFAULT_VAL) {
                    return false;
                }
            }
        }


        return true;
    }

    private BoardStatus getWinner(int id) {
        if (id == Board.PLAYER1_KEY) {
            return BoardStatus.PLAYER1_WON;
        } else if (id == Board.PLAYER2_KEY) {
            return BoardStatus.PLAYER2_WON;
        }

        return BoardStatus.RUNNING;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int[] y : field) {
            sb.append("| ");
            for (int x : y) {
                sb.append(x).append(" | ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
