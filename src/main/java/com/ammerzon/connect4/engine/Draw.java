package com.ammerzon.connect4.engine;

public class Draw {
    private int name;
    private int column;
    private int row;
    private boolean timeout;

    public Draw(int name, int column, int row) {
        this(name, column, row, true);
    }

    public Draw(int name, int column, int row, boolean timeout) {
        this.name = name;
        this.column = column;
        this.row = row;
        this.timeout = timeout;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
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
