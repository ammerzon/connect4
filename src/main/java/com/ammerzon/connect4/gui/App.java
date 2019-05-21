package com.ammerzon.connect4.gui;

import com.ammerzon.connect4.gui.controller.GameModeController;
import com.ammerzon.connect4.gui.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("/GameModeView.fxml"));
        Parent root = loader.load();
        GameModeController controller = loader.getController();
        controller.setStage(primaryStage);
        Scene scene = new Scene(root);

        primaryStage.setTitle("Game Mode Selection");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
