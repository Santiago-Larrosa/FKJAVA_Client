// Crea un nuevo archivo: UpgradeWindow.java
package com.FK.game.ui; // O donde guardes tus clases de UI

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class UpgradeWindow extends Window {

    private final Runnable closeAction;

    public UpgradeWindow(Skin skin, Runnable closeAction) {
        super("Mejorar Personaje", skin);
        
        this.closeAction = closeAction;

        setModal(true);     
        setMovable(true);     
        padTop(40);           
        
        int currentDamage = 5;
        int upgradeCost = 100;
        TextButton damageButton = new TextButton("Mejorar Dano (" + currentDamage + ") - " + upgradeCost + " oro", skin);
        damageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("¡Botón de mejorar daño presionado!");
            }
        });
        
        TextButton healthButton = new TextButton("Mejorar Vida", skin);
        
        TextButton backButton = new TextButton("Volver al Juego", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closeAction.run();
            }
        });

        add(damageButton).width(400).pad(10);
        row();
        add(healthButton).width(400).pad(10);
        row();
        add(backButton).width(200).padTop(30);

        pack();

    }

    public void centerWindow() {
        if (getStage() == null) return;

        setPosition(
            (getStage().getWidth() / 2) - (getWidth() / 2), 
            (getStage().getHeight() / 2) - (getHeight() / 2)
        );
    }
}