
package com.FK.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.FK.game.core.MainGame;
import com.FK.game.screens.*;

public class UpgradeScreen extends AbstractUIScreen {

    private final GameScreen gameScreen;

    public UpgradeScreen(MainGame game, GameScreen gameScreen) {
        super(game);
        this.gameScreen = gameScreen;
    }

    @Override
    protected void buildStage() {
        Label titleLabel = new Label("MEJORAR PERSONAJE", skin); 

        rootTable.add(titleLabel).padBottom(50);
        rootTable.row(); 

        int currentDamage = 5; 
        int upgradeCost = 100; 
        
        TextButton damageButton = new TextButton("Mejorar Dano (" + currentDamage + ") - " + upgradeCost + " oro", skin);
        damageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("¡Botón de mejorar daño presionado!");
            }
        });
        rootTable.add(damageButton).width(400).pad(10);
        rootTable.row();
        TextButton healthButton = new TextButton("Mejorar Vida", skin);
        rootTable.add(healthButton).width(400).pad(10);
        rootTable.row();
        TextButton backButton = new TextButton("Volver al Juego", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(gameScreen);
            }
        });
        rootTable.add(backButton).width(200).padTop(50);
        rootTable.setDebug(true);

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}