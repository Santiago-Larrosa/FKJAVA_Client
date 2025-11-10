package com.FK.game.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.FK.game.core.PlayerData;
import com.FK.game.core.UpgradeManager;
import com.FK.game.core.GameContext;
import com.FK.game.core.MainGame;
import com.FK.game.screens.GameScreen;

public class UpgradeWindow extends Window {
    private final Runnable closeAction;
    private final PlayerData playerData;
    private final UpgradeManager upgradeManager;

    private final TextButton damageButton;
    private final TextButton healthButton;
    private final Label coinLabel;
    private final MainGame game;

    public UpgradeWindow(Skin skin, Runnable closeAction, PlayerData playerData, UpgradeManager upgradeManager) {
        super("Hoguera de Mejoras", skin);
        this.game = GameContext.getScreen().getGame();
        this.closeAction = closeAction;
        this.playerData = playerData;
        this.upgradeManager = upgradeManager;
        setModal(true);
        setMovable(false);
        pad(40);
        getTitleLabel().setAlignment(Align.center);
        getTitleTable().padBottom(20);
        defaults().pad(10).growX();
        coinLabel = new Label("", skin);
        coinLabel.setAlignment(Align.center);
        
        damageButton = new TextButton("", skin);
        healthButton = new TextButton("", skin);
        TextButton backButton = new TextButton("Volver", skin);
        for (TextButton btn : new TextButton[]{damageButton, healthButton, backButton}) {
            btn.getLabel().setWrap(true);
            btn.getLabel().setAlignment(Align.center);
            btn.pad(10);
        }
        damageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.client.sendMessage("UPGRADE_DAMAGE");
            }
        });

        healthButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.client.sendMessage("UPGRADE_HEALTH");
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.client.sendMessage("CLOSE_UPGRADE_WINDOW");
                closeAction.run();
            }
        });

        game.client.sendMessage("REQUEST_UPGRADE_DATA");
        add(coinLabel).padBottom(20);
        row();
        add(damageButton);
        row();
        add(healthButton);
        row();
        add(backButton).width(200).padTop(30).center();

        updateButtons();
        pack();

        getColor().a = 0f;
        addAction(Actions.fadeIn(0.25f));
    }

    public void updateButtons() {
        coinLabel.setText("Monedas: " + playerData.coinCount);

        String damageText = "Mejorar Daño (" + playerData.getAttackDamage() +
                            ") - Costo: " + upgradeManager.getDamageUpgradeCost(playerData);
        damageButton.setText(damageText);
        damageButton.setDisabled(!upgradeManager.canAffordDamageUpgrade(playerData));

        String healthText = "Mejorar Vida (" + playerData.getMaxHealth() +
                            ") - Costo: " + upgradeManager.getHealthUpgradeCost(playerData);
        healthButton.setText(healthText);
        healthButton.setDisabled(!upgradeManager.canAffordHealthUpgrade(playerData));
    }

    public void centerWindow() {
        if (getStage() == null) return;

        float stageWidth = getStage().getWidth();
        float stageHeight = getStage().getHeight();

        float desiredWidth = Math.min(stageWidth * 0.6f, 600f);
        setSize(desiredWidth, getPrefHeight());
        invalidateHierarchy();

        setPosition(
            (stageWidth - getWidth()) / 2f,
            (stageHeight - getHeight()) / 2f
        );
    }

    public void updateUIFromNetwork(int coins, int dmgLvl, int hpLvl, int dmgCost, int hpCost) {
    coinLabel.setText("Monedas: " + coins);

    String damageText = "Mejorar Daño (Nivel " + dmgLvl +
                        ") - Costo: " + dmgCost;
    damageButton.setText(damageText);
    damageButton.setDisabled(coins < dmgCost);

    String healthText = "Mejorar Vida (Nivel " + hpLvl +
                        ") - Costo: " + hpCost;
    healthButton.setText(healthText);
    healthButton.setDisabled(coins < hpCost);
}

public void resize(int width, int height) {
    // Recentrar la ventana al cambiar la resolución
    setPosition(
        (width - getWidth()) / 2f,
        (height - getHeight()) / 2f
    );
}



}
