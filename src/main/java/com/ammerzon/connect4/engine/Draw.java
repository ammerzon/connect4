package com.ammerzon.connect4.engine;

public class Draw {
    private int column;
    private int row;
    private boolean timeout;

    public Draw(int column, int row) {
        this(column, row, false);
    }

    public Draw(int column, int row, boolean timeout) {
        this.column = column;
        this.row = row;
        this.timeout = timeout;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public boolean isTimeout() {
        return timeout;
    }

    public void setTimeout(boolean timeout) {
        this.timeout = timeout;
    }
}
