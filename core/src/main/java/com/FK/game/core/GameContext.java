package com.FK.game.core;


import com.FK.game.entities.Player;

import com.FK.game.screens.*;


public class GameContext {
    private static Player player;

    private static GameScreen currentScreen;

    public static void setPlayer(Player p) {
        player = p;
    }

    public static Player getPlayer() {
        return player;
    }

    public static void setScreen(GameScreen screen) {
        currentScreen = screen;
    }

    public static GameScreen getScreen() {
        return currentScreen;
    }
}
