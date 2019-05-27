package com.ammerzon.connect4.gui.controller;

import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController extends BaseController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    void loaded() {
        super.loaded();
        stage.setTitle("Settings");
    }

    public void saveClicked(MouseEvent mouseEvent) {
        stage.close();
    }
}
