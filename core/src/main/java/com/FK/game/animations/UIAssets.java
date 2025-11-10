package com.FK.game.animations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.FK.game.core.*;


public class UIAssets {
    public static Skin glassySkin;

    public static void load() {
        glassySkin = new Skin(Gdx.files.internal("ui/glassy-ui.json"));
    }

    public static void dispose() {
        glassySkin.dispose();
    }
}
