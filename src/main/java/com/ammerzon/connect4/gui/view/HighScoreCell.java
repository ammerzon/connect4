package com.ammerzon.connect4.gui.view;

import com.ammerzon.connect4.gui.entity.HighScoreEntry;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class HighScoreCell extends ListCell<HighScoreEntry> {

    @FXML
    private Label player1Username;

    @FXML
    private Label player1Wins;

    @FXML
    private Label player2Username;

    @FXML
    private Label player2Wins;

    @FXML
    private Label currentLeader;

    public HighScoreCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HighScoreCellView.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(HighScoreEntry item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            player1Username.setText(item.getPlayer1Username());
            player1Wins.setText(String.valueOf(item.getPlayer1Wins()));
            player2Username.setText(item.getPlayer2Username());
            player2Wins.setText(String.valueOf(item.getPlayer2Wins()));
            currentLeader.setText(item.getCurrentLeader());

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}


