package com.FK.game.animations;
import com.FK.game.core.*;
import com.FK.game.entities.*;
import com.FK.game.screens.*;
import com.FK.game.states.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum EnemyAnimationType implements AnimationType{

    BOLB("bolb.png", new int[][] {
        {0, 0, 2480, 1754},
        {2480, 0, 2480, 1754},
        {0, 1754, 2480, 1754},
        {2480, 1754, 2480, 1754},
        {0, 3508, 2480, 1754},
        {2480, 3508, 2480, 1754},
        {0, 5262, 2480, 1754}
    }, 0.083f),

    BOLB_LEFT("bolbLEFT.png", new int[][] {
        {0, 0, 2480, 1754},
        {2480, 0, 2480, 1754},
        {0, 1754, 2480, 1754},
        {2480, 1754, 2480, 1754},
        {0, 3508, 2480, 1754},
        {2480, 3508, 2480, 1754},
        {0, 5262, 2480, 1754}
    }, 0.083f),

    SLOP("slop.png", new int[][] {
        {0, 0, 2480, 1754},
        {2480, 0, 2480, 1754},
        {0, 1754, 2480, 1754},
        {2480, 1754, 2480, 1754},
        {0, 3508, 2480, 1754},
        {2480, 3508, 2480, 1754},
        {0, 5262, 2480, 1754},
        {2480, 5262, 2480, 1754},
        {0, 7016, 2480, 1754},
        {2480, 7016, 2480, 1754}
    }, 0.083f),

    SLOP_LEFT("slopLEFT.png", new int[][] {
        {0, 0, 2480, 1754},
        {2480, 0, 2480, 1754},
        {0, 1754, 2480, 1754},
        {2480, 1754, 2480, 1754},
        {0, 3508, 2480, 1754},
        {2480, 3508, 2480, 1754},
        {0, 5262, 2480, 1754},
        {2480, 5262, 2480, 1754},
        {0, 7016, 2480, 1754},
        {2480, 7016, 2480, 1754}
    }, 0.083f),

    SLOP_ATTACK("SlopAttack.png", new int[][] {
        {0, 0, 1240, 1500},
        {1240, 0, 1240, 1500},
        {0, 1500, 1240, 1500},
        {1240, 1500, 1240, 1500},
        {0, 3000, 1240, 1500},
        {1240, 3000, 1240, 1500},
        {0, 4500, 1240, 1500},
        {1240, 4500, 1240, 1500},
        {0, 6000, 1240, 1500},
        {1240, 6000, 1240, 1500},
        {0, 7500, 1240, 1500}
    }, 0.083f),

    SLOP_ATTACK_LEFT("SlopAttackLEFT.png", new int[][] {
        {0, 0, 1240, 1500},
        {1240, 0, 1240, 1500},
        {0, 1500, 1240, 1500},
        {1240, 1500, 1240, 1500},
        {0, 3000, 1240, 1500},
        {1240, 3000, 1240, 1500},
        {0, 4500, 1240, 1500},
        {1240, 4500, 1240, 1500},
        {0, 6000, 1240, 1500},
        {1240, 6000, 1240, 1500},
        {0, 7500, 1240, 1500}
    }, 0.083f);

   private final String texturePath;
    private final int[][] frames;
    private final float frameDuration;
    
    EnemyAnimationType(String texturePath, int[][] frames, float frameDuration) {
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