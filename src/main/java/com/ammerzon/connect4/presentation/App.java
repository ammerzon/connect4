package com.ammerzon.connect4.presentation;

import com.airhacks.afterburner.injection.Injector;
import com.ammerzon.connect4.presentation.gameMode.GameModeView;
import com.ammerzon.connect4.presentation.helper.BasePresenter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        GameModeView gameModeView = new GameModeView();
        BasePresenter presenter = (BasePresenter) gameModeView.getPresenter();
        presenter.setStage(stage);
        Scene scene = new Scene(gameModeView.getView());
        stage.setTitle("Game Mode Selection");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        Injector.forgetAll();
    }
}
