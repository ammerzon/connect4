package com.ammerzon.connect4.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

abstract class Controller {
    Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    Object openWindow(Class<?> clazz) {
        Controller controller = null;

        try {
            FXMLLoader loader = new FXMLLoader(clazz.getResource("/" + clazz.getSimpleName().replaceAll("Controller", "View") + ".fxml"));
            Parent pane = loader.load();

            controller = loader.getController();
            controller.setStage(stage);
            controller.loaded();

            Scene scene = new Scene(pane);
            stage.setScene(scene);
            maximize();
        } catch (IOException e) {
            System.err.println("Couldn't open the controller!");
            e.printStackTrace();
        }

        return controller;
    }

    void loaded() {
    }

    Object openNewWindow(Class<?> clazz, int width, int heigth) {
        Controller controller = null;

        try {
            FXMLLoader loader = new FXMLLoader(clazz.getResource("/" + clazz.getSimpleName().replaceAll("Controller", "View") + ".fxml"));
            Parent pane = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Settings");
            stage.setScene(new Scene(pane, width, heigth));

            controller = loader.getController();
            controller.setStage(stage);
            controller.loaded();

            stage.show();
        } catch (IOException e) {
            System.err.println("Couldn't open the controller!");
            e.printStackTrace();
        }

        return controller;
    }

    void maximize() {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
    }
}
