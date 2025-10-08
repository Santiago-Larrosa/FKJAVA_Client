
package com.FK.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.FK.game.core.MainGame;

public class InterlevelLoadingScreen implements Screen {
    private final MainGame game;
    private final GameScreen gameScreen; 
    private ShapeRenderer shapeRenderer;
    private final float DURATION = 2.0f; 
    private float elapsedTime = 0;
    public InterlevelLoadingScreen(MainGame game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
    }
   
    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        elapsedTime += delta;
        float progress = Math.min(1f, elapsedTime / DURATION);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(Gdx.graphics.getWidth() / 2 - 200, Gdx.graphics.getHeight() / 2 - 15, 400 * progress, 30);
        shapeRenderer.end();
        
        if (elapsedTime >= DURATION) {
            gameScreen.loadRandomGameMap();
            game.setScreen(gameScreen);
        }
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
    
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}