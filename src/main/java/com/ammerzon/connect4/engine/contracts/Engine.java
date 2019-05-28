package com.ammerzon.connect4.engine.contracts;

import com.ammerzon.connect4.engine.Board;
import com.ammerzon.connect4.engine.Draw;
import com.ammerzon.connect4.engine.GameStatus;
import com.ammerzon.connect4.gui.helper.GameMode;

public interface Engine {
    int getSize();

    /**
     * Initializes the board with a size and a game mode.
     *
     * @param size The field size.
     * @param gameMode The game mode.
     * @return The new board.
     */
    Board initializeBoard(int size, GameMode gameMode);

    /**
     * Initializes the board with a game status.
     *
     * @param gameStatus The game status.
     * @return The new board.
     */
    Board initializeBoard(GameStatus gameStatus);

    /**
     * This method will be called when players want to notify the engine.
     *
     * @param sender The sender.
     * @param draw The draw.
     */
    void receive(Player sender, Draw draw);

    /**
     * Persists the current game state.
     */
    void save();

    /**
     * Load the savepoint.
     *
     * @return The loaded game status.
     */
    GameStatus loadSavepoint();

    /**
     * Register a client which will be called when a new draw is received.
     *
     * @param client The client to register.
     * @return True if the client could be registered. Otherwise false.
     */
    boolean register(Client client);

    /**
     * Notify all clients.
     */
    void notifyClients();

    /**
     * Returns the next unique id.
     *
     * @return An unique id.
     */
    int nextId();

    /**
     * Starts the game engine and notifies the clients.
     *
     * @param sender The sender.
     */
    void start(Player sender);

    /**
     * Resets all values and restarts the engine.
     */
    void restart();
}
