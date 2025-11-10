package com.FK.game.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.FK.game.core.*;
import com.FK.game.network.*;
import com.FK.game.animations.UIAssets;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class ClientConnectionScreen implements Screen {
    private final MainGame game;
    private Stage stage;
    private Skin skin;
    private Label statusLabel;
    private TextButton connectButton;
    private boolean connecting = false;
    private boolean connected = false;
    private float retryTimer = 0f;

    public ClientConnectionScreen(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/glassy-ui.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label titleLabel = new Label("Conexión al Servidor", skin);
        titleLabel.setAlignment(Align.center);

        statusLabel = new Label("Presiona 'Conectarse' para iniciar conexión.", skin);
        connectButton = new TextButton("Conectarse", skin);

        connectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!connecting) {
                    attemptConnection();
                }
            }
        });

        table.add(titleLabel).padBottom(40).row();
        table.add(connectButton).size(200, 60).padBottom(20).row();
        table.add(statusLabel).padTop(20);
    }

    private void attemptConnection() {
        connecting = true;
        statusLabel.setText("Intentando conectar al servidor...");

        new Thread(() -> {
            try {
                game.client = new ClientThread("255.255.255.255"); // broadcast o localhost
                game.client.start();

                // Esperar hasta recibir ID (puedes modificar ClientThread para exponer connected)
                int waitCount = 0;
                while (!game.client.isConnected() && waitCount < 200) { // 200 * 50ms = 10s
                    Thread.sleep(50);
                    waitCount++;
                }

                if (game.client.isConnected()) {
                    connected = true;
                    Gdx.app.postRunnable(() -> {
                        statusLabel.setText("Conectado al servidor. Iniciando juego...");
                        game.setScreen(new LoadingScreen(game)); // o GameScreen directamente
                    });
                } else {
                    Gdx.app.postRunnable(() -> {
                        statusLabel.setText("No se pudo conectar. Intenta nuevamente.");
                        connecting = false;
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
                Gdx.app.postRunnable(() -> {
                    statusLabel.setText("Error de conexión. Reintenta.");
                    connecting = false;
                });
            }
        }).start();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    @Override
    public void dispose() { stage.dispose(); if (skin != null) skin.dispose(); }
}
