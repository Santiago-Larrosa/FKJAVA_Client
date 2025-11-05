package com.FK.game.animations;
import com.FK.game.core.*;
import com.FK.game.entities.*;
import com.FK.game.screens.*;
import com.FK.game.states.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum PlayerAnimationType implements AnimationType{
    WALK_RIGHT("walk.png", new int[][] {
    {0, 0, 620, 439},
    {0, 439, 620, 439},
    {620, 0, 620, 439},
    {620, 439, 620, 439},
    {0, 878, 620, 439},
    {620, 878, 620, 439},
    {0, 1316, 620, 439}
}, 0.083f),

WALK_LEFT("walk_left.png", new int[][] {
    {0, 0, 620, 439},
    {0, 439, 620, 439},
    {620, 0, 620, 439},
    {620, 439, 620, 439},
    {0, 878, 620, 439},
    {620, 878, 620, 439},
    {0, 1316, 620, 439}
}, 0.083f),

    
IDLE_RIGHT("idle.png", new int[][] {
    {0, 0, 620, 439},
    {0, 439, 620, 439},
    {620, 0, 620, 439},
    {620, 439, 620, 439}
}, 0.083f),

IDLE_LEFT("idleLeft.png", new int[][] {
    {0, 0, 620, 439},
    {0, 439, 620, 439},
    {620, 0, 620, 439},
    {620, 439, 620, 439}
}, 0.083f),

JUMPING_RIGHT("jumping.png", new int[][] {
    {0, 0, 620, 439},
    {0, 439, 620, 439},
    {620, 0, 620, 439}
}, 0.083f),

JUMPING_LEFT("jumpingLeft.png", new int[][] {
    {0, 0, 620, 439},
    {0, 439, 620, 439},
    {620, 0, 620, 439}
}, 0.083f),

CHARGE_JUMP_RIGHT("chargejump.png", new int[][] {
    {0, 0, 620, 439},
    {0, 439, 620, 439},
    {620, 0, 620, 439}
}, 0.083f),

CHARGE_JUMP_LEFT("chargeLeftJump.png", new int[][] {
    {0, 0, 620, 439},
    {0, 439, 620, 439},
    {620, 0, 620, 439}
}, 0.083f),

FALLING_RIGHT("falling.png", new int[][] {
    {0, 0, 620, 439},
    {0, 439, 620, 439},
    {620, 0, 620, 439}
}, 0.083f),

FALLING_LEFT("fallingLeft.png", new int[][] {
    {0, 0, 620, 439},
    {0, 439, 620, 439},
    {620, 0, 620, 439}
}, 0.083f),

ATTACKING_RIGHT("attacking.png", new int[][] {
    {0, 0, 620, 439},
    {0, 439, 620, 439},
    {620, 0, 620, 439},
    {620, 439, 620, 439},
    {0, 439, 620, 439}
}, 0.083f),

ATTACKING_LEFT("attackingLeft.png", new int[][] {
    {0, 0, 620, 439},
    {0, 439, 620, 439},
    {620, 0, 620, 439},
    {620, 439, 620, 439},
    {0, 439, 620, 439}
}, 0.083f),

FALLING_ATACK("fallingAttack.png", new int[][] {
    {0, 0, 620, 439},
    {0, 439, 620, 439},
    {620, 0, 620, 439}
}, 0.043f),

FALLING_ATACK_LEFT("fallingAttackLeft.png", new int[][] {
    {0, 0, 620, 439},
    {0, 439, 620, 439},
    {620, 0, 620, 439}
}, 0.043f),

FALLING_ATACK_PASS("passAttack.png", new int[][] {
    {0, 0, 620, 439},
    {0, 439, 620, 439}
}, 0.083f),

FALLING_ATACK_PASS_LEFT("passAttackLeft.png", new int[][] {
    {0, 0, 620, 439},
    {0, 439, 620, 439},
    {620, 0, 620, 439}
}, 0.083f),

IGNITION("Ignition.png", new int[][] {
    {0, 0, 875, 219},
    {875, 0, 875, 219},
    {0, 219, 875, 219},
    {875, 219, 875, 219},
    {0, 439, 875, 219},
    {875, 439, 875, 219},
    {0, 658, 875, 219},
    {875, 658, 875, 219}
}, 0.083f),

IGNITION_LEFT("ignitionLeft.png", new int[][] {
    {0, 0, 875, 219},
    {875, 0, 875, 219},
    {0, 219, 875, 219},
    {875, 219, 875, 219},
    {0, 439, 875, 219},
    {875, 439, 875, 219},
    {0, 658, 875, 219},
    {875, 658, 875, 219}
}, 0.083f),

FIRE_ATACK("FireAtack.png", new int[][] {
    {0, 0, 875, 219},
    {875, 0, 875, 219},
    {0, 219, 875, 219}
}, 0.083f),

FIRE_ATACK_LEFT("FireAtackLeft.png", new int[][] {
    {0, 0, 875, 219},
    {875, 0, 875, 219},
    {0, 219, 875, 219}
}, 0.083f),

    LOADED("loaded.png", new int [][] {
        {0, 0, 480, 600}
    }, 0.083f),

    UNLOADED("unloaded.png", new int [][] {
        {0, 0, 480, 600}
    }, 0.083f),

    LOADING("loading.png", new int[][] {
    {0, 0, 120, 150},
    {120, 0, 120, 150},
    {0, 150, 120, 150},
    {120, 150, 120, 150},
    {0, 300, 120, 150},
    {120, 300, 120, 150},
    {0, 450, 120, 150},
    {120, 450, 120, 150},
    {0, 600, 120, 150},
    {120, 600, 120, 150},
    {0, 750, 120, 150},
    {120, 750, 120, 150},
    {0, 900, 120, 150}
}, 0.083f),

UNLOADING("unloading.png", new int[][] {
    {0, 0, 120, 150},
    {120, 0, 120, 150},
    {0, 150, 120, 150},
    {120, 150, 120, 150},
    {0, 300, 120, 150},
    {120, 300, 120, 150},
    {0, 450, 120, 150},
    {120, 450, 120, 150},
    {0, 600, 120, 150},
    {120, 600, 120, 150},
    {0, 750, 120, 150},
    {120, 750, 120, 150},
    {0, 900, 120, 150}
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
    
    PlayerAnimationType(String texturePath, int[][] frames, float frameDuration) {
        this.texturePath = texturePath;
        this.frames = frames;
        this.frameDuration = frameDuration;
    }
    
    public String getTexturePath() {
        return this.texturePath;
    }
    
    public AnimationHandler create(Texture texture) {
        return new AnimationHandler(texture, frames, frameDuration);
    }
}