package com.FK.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Color; 
import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.FK.game.entities.*;
import com.FK.game.screens.*;
import com.FK.game.states.*;
import com.FK.game.sounds.*;

public class LoadingScreen implements Screen {
    private final MainGame game;
    private ShapeRenderer shapeRenderer;
    private float progress = 0;
    private float minShowTime = 1.5f;
    private float elapsedTime = 0;
    private Thread loadingThread;
    private volatile boolean loadingFinished = false;

    public LoadingScreen(MainGame game) {
        this.game = game;
    }
   
    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
    AnimationCache.getInstance().loadAssets();
    SoundCache.getInstance().loadAll();
    }

    @Override
    public void render(float delta) {
        boolean loadingComplete = AnimationCache.getInstance().update();
        progress = AnimationCache.getInstance().getProgress();
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(100, 150, 400 * progress, 30);
        shapeRenderer.end();
        if (progress >= 0.99f) {
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void hide() {
        if (loadingThread != null && loadingThread.isAlive()) {
            loadingThread.interrupt();
        }
    }

    @Override
    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
    
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
}