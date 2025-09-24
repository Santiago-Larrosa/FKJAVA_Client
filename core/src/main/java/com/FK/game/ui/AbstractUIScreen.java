// Crea un nuevo archivo: AbstractUIScreen.java
package com.FK.game.ui; // O donde guardes tus pantallas

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.FK.game.core.MainGame;

public abstract class AbstractUIScreen implements Screen {
    protected final MainGame game;
    protected Stage stage;
    protected Skin skin;
    protected Table rootTable; 

    public AbstractUIScreen(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal("ui/glassy-ui.json"));
        stage = new Stage(new ScreenViewport());
        rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);
        buildStage();
        Gdx.input.setInputProcessor(stage);
    }

    protected abstract void buildStage();

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}