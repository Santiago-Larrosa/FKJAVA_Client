package com.FK.game.animations;
import com.FK.game.core.*;
import com.FK.game.entities.*;
import com.FK.game.screens.*;
import com.FK.game.states.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum ObjectsAnimationType implements AnimationType{

    PORTAL_RISING("portal_rising.png", new int[][] {
        {0, 0, 1240, 1450},
        {1240, 0, 1240, 1450},
        {0, 1450, 1240, 1450},
        {1240, 1450, 1240, 1450}
    }, 0.083f),

    PORTAL_LOOP("portal.png", new int[][] {
        {0, 0, 1240, 1450},
        {1240, 0, 1240, 1450},
        {0, 1450, 1240, 1450},
        {1240, 1450, 1240, 1450},
        {0, 2900, 1240, 1450}
    }, 0.083f),

    FIRE_LOOP("fire.png", new int[][] {
        {0, 0, 1500, 1500},
        {1500, 0, 1500, 1500},
        {0, 1500, 1500, 1500},
        {1500, 1500, 1500, 1500},
        {0, 3000, 1500, 1500},
        {1500, 3000, 1500, 1500},
        {0, 4500, 1500, 1500},
        {1500, 4500, 1500, 1500},
        {0, 6000, 1500, 1500}
    }, 0.083f),

    SMOKE("smoke.png", new int[][] {
        {1450, 0, 1450, 1650},
        {0, 0, 1450, 1650},
        {1450, 1650, 1450, 1650},
        {0, 1650, 1450, 1650},
        {1450, 3300, 1450, 1650},
        {0, 3300, 1450, 1650},
        {1450, 4950, 1450, 1650},
        {0, 4950, 1450, 1650},
        {1450, 6600, 1450, 1650},
        {0, 6600, 1450, 1650},
        {1450, 8250, 1450, 1650},
        {0, 8250, 1450, 1650},
    }, 0.083f);

   private final String texturePath;
    private final int[][] frames;
    private final float frameDuration;
    
    ObjectsAnimationType(String texturePath, int[][] frames, float frameDuration) {
        this.texturePath = texturePath;
        this.frames = frames;
        this.frameDuration = frameDuration;
    }

    @Override
    public String getTexturePath() {
        return this.texturePath;
    }
    
    public AnimationHandler create(Texture texture) {
        return new AnimationHandler(texture, frames, frameDuration);
    }
}