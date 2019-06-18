package com.ammerzon.connect4.business.game.boundary;

import com.ammerzon.connect4.business.game.entity.GameMode;
import javafx.beans.property.*;

public class GameService {
    private ObjectProperty<GameMode> gameMode = new SimpleObjectProperty<>();
    private IntegerProperty width = new SimpleIntegerProperty();
    private IntegerProperty height = new SimpleIntegerProperty();
    private IntegerProperty port = new SimpleIntegerProperty();
    private StringProperty host = new SimpleStringProperty();
    private StringProperty username = new SimpleStringProperty();
    private IntegerProperty time = new SimpleIntegerProperty();
    private IntegerProperty difficulty = new SimpleIntegerProperty();

    public GameMode getGameMode() {
        return gameMode.get();
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode.set(gameMode);
    }

    public ReadOnlyObjectProperty<GameMode> gameModeProperty() {
        return gameMode;
    }

    public int getWidth() {
        return width.get();
    }

    public void setWidth(int width) {
        this.width.set(width);
    }

    public IntegerProperty widthProperty() {
        return width;
    }

    public int getHeight() {
        return height.get();
    }

    public void setHeight(int height) {
        this.height.set(height);
    }

    public IntegerProperty heightProperty() {
        return height;
    }

    public int getPort() {
        return port.get();
    }

    public void setPort(int port) {
        this.port.set(port);
    }

    public IntegerProperty portProperty() {
        return port;
    }

    public String getHost() {
        return host.get();
    }

    public void setHost(String host) {
        this.host.set(host);
    }

    public StringProperty hostProperty() {
        return host;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public int getTime() {
        return time.get();
    }

    public void setTime(int time) {
        this.time.set(time);
    }

    public IntegerProperty timeProperty() {
        return time;
    }

    public int getDifficulty() {
        return difficulty.get();
    }

    public void setDifficulty(int difficulty) {
        this.difficulty.set(difficulty);
    }

    public IntegerProperty difficultyProperty() {
        return difficulty;
    }
}
