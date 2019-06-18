package com.ammerzon.connect4.engine;

import swe4.connect4.api.RemoteGamePlayer;

public class Player {
    RemoteGamePlayer remoteGamePlayer;
    String name;

    public Player(RemoteGamePlayer remoteGamePlayer, String name) {
        this.remoteGamePlayer = remoteGamePlayer;
        this.name = name;
    }
}
