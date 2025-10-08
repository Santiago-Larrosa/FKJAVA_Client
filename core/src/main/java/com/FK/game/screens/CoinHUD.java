// Crea un nuevo archivo: CoinHUD.java
package com.FK.game.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.FK.game.core.PlayerData;
import com.FK.game.entities.Player;

public class CoinHUD extends BaseHUD {
    private final PlayerData playerData;
    private final Texture coinTexture;
    private final BitmapFont font;
    private final GlyphLayout layout; 

    public CoinHUD(PlayerData playerData, Texture coinTexture, BitmapFont font) {
        this.playerData = playerData;
        this.coinTexture = coinTexture;
        this.font = font;
        this.layout = new GlyphLayout();
        this.scale = 0.8f; 
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        if (playerData == null) return;

        float iconSize = 48 * scale; 
        this.width = iconSize;
        this.height = iconSize;
        calculatePosition(camera, 20f, 20f, false, true);
        batch.draw(coinTexture, this.x, this.y, this.width, this.height);
        String coinText = "" + playerData.coinCount;
        layout.setText(font, coinText);

        float textX = this.x + this.width + 10 * scale; 
        float textY = this.y + this.height / 2 + layout.height / 2;

        font.draw(batch, layout, textX, textY);
    }
}