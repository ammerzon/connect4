package com.ammerzon.connect4.gui.controller;

import com.ammerzon.connect4.gui.entity.HighScoreEntry;
import com.ammerzon.connect4.gui.view.HighScoreCell;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class HighScoreController extends BaseController implements Initializable {
    @FXML
    public JFXListView<HighScoreEntry> highScoreList;

    private ObservableList<HighScoreEntry> highScoreEntries = FXCollections.observableArrayList();

    public HighScoreController() {
        HighScoreEntry highScoreEntry1 = new HighScoreEntry("cooldude21", 4, "cooldude45", 6);
        HighScoreEntry highScoreEntry2 = new HighScoreEntry("cooldude56", 4, "cooldude23", 11);
        HighScoreEntry highScoreEntry3 = new HighScoreEntry("cooldude43", 9, "cooldude12", 2);
        HighScoreEntry highScoreEntry4 = new HighScoreEntry("cooldude76", 3, "cooldude45", 6);
        highScoreEntries.addAll(highScoreEntry1, highScoreEntry2, highScoreEntry3, highScoreEntry4);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        highScoreList.setItems(highScoreEntries);
        highScoreList.depthProperty().set(1);
        highScoreList.setExpanded(true);
        highScoreList.setCellFactory(param -> new HighScoreCell());
    }

    @Override
    void loaded() {
        super.loaded();
        stage.setTitle("High score");
    }
}
