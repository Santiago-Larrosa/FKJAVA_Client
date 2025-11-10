package com.FK.game.animations;
import com.FK.game.core.*;
import com.FK.game.entities.*;
import com.FK.game.screens.*;
import com.FK.game.states.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
    public static final AssetManager manager = new AssetManager();
    public static Texture playerIdle;
    public static Texture playerPass;
    public static Texture playerPassLeft;
    public static Texture coinTexture;
    public static Texture whitePixel;

    public static void load() {
        manager.load("idle1.png", Texture.class);
        manager.load("Pass.png", Texture.class);
        manager.load("PassLeft.png", Texture.class);
        manager.load("coin.png", Texture.class);
        manager.load("white_pixel.jpg", Texture.class);
        
    }
    
    public static void assignTextures() {
        playerIdle = manager.get("idle1.png", Texture.class);
        playerPass = manager.get("Pass.png", Texture.class);
        playerPassLeft = manager.get("PassLeft.png", Texture.class);
        coinTexture = manager.get("coin.png", Texture.class);
        whitePixel = manager.get("white_pixel.jpg", Texture.class);
    }
    
    public static void dispose() {
        manager.dispose();
    }
}