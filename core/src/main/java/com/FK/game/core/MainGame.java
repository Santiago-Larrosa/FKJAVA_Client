package com.FK.game.core;
import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.FK.game.entities.*;
import com.FK.game.screens.*;
import com.FK.game.states.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.FK.game.network.ClientThread;
import com.FK.game.network.NetworkMessage;

public class MainGame extends Game {

    public PlayerData playerData;
    public PlayerData playerData2;
    public int roomsClearedCount = 0;
    public ClientThread client;
    @Override
    public void create() {
        String serverIp = "127.0.0.1"; // O la IP de la máquina del servidor
        this.client = new ClientThread(serverIp);
        client.start();
        Assets.load(); 
        Assets.manager.finishLoading();
        Assets.assignTextures();
        playerData = new PlayerData();
        playerData2 = new PlayerData();
        setScreen(new LoadingScreen(this));
    }
}

//./gradlew build
//./gradlew :lwjgl3:run