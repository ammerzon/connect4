package com.ammerzon.connect4.gui.controller;

import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController extends Controller implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void saveClicked(MouseEvent mouseEvent) {
        stage.close();
    }
}
