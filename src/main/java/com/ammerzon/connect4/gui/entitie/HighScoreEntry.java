package com.ammerzon.connect4.gui.entitie;

public class HighScoreEntry {
    private String player1Username;
    private Integer player1Wins;
    private String player2Username;
    private Integer player2Wins;

    public HighScoreEntry(String player1Username, Integer player1Wins, String player2Username, Integer player2Wins) {
        this.player1Username = player1Username;
        this.player1Wins = player1Wins;
        this.player2Username = player2Username;
        this.player2Wins = player2Wins;
    }

    public String getPlayer1Username() {
        return player1Username;
    }

    public void setPlayer1Username(String player1Username) {
        this.player1Username = player1Username;
    }

    public Integer getPlayer1Wins() {
        return player1Wins;
    }

    public void setPlayer1Wins(Integer player1Wins) {
        this.player1Wins = player1Wins;
    }

    public String getPlayer2Username() {
        return player2Username;
    }

    public void setPlayer2Username(String player2Username) {
        this.player2Username = player2Username;
    }

    public Integer getPlayer2Wins() {
        return player2Wins;
    }

    public void setPlayer2Wins(Integer player2Wins) {
        this.player2Wins = player2Wins;
    }

    public String getCurrentLeader() {
        if (player1Wins > player2Wins) {
            return player1Username;
        } else {
            return player2Username;
        }
    }
}
