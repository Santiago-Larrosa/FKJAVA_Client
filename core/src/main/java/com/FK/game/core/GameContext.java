package com.FK.game.core;


import com.FK.game.entities.Player;

import com.FK.game.screens.*;
import com.badlogic.gdx.utils.Array;


public class GameContext {
    private static volatile Player player;
    private static volatile GameScreen currentScreen;
    private static volatile int myPlayerId = -1;
    private static final Array<Player> activePlayers = new Array<>();

    public static Array<Player> getActivePlayers() {
        return activePlayers;
    }

    public static int getMyPlayerId() {
        return myPlayerId;
    }

    public static void setMyPlayerId(int id) {
        myPlayerId = id;
    }

    public static void addPlayer(Player player) {
        if (!activePlayers.contains(player, true)) {
            activePlayers.add(player);
        }
    }

    public static void removePlayer(Player player) {
        activePlayers.removeValue(player, true);
    }
    
    public static void clearPlayers() {
        activePlayers.clear();
    }

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
