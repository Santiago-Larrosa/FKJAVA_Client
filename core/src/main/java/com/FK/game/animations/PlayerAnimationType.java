package com.FK.game.animations;
import com.FK.game.core.*;
import com.FK.game.entities.*;
import com.FK.game.screens.*;
import com.FK.game.states.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum PlayerAnimationType implements AnimationType{
    WALK_RIGHT("walk.png", new int[][] {
        {0, 0, 2480, 1754},
        {0, 1755, 2480, 1754},
        {2481, 0, 2480, 1754},
        {2481, 1755, 2480, 1754},
        {0, 3510, 2480, 1754},
        {2481, 3510, 2480, 1754},
        {0, 5265, 2480, 1754}
    }, 0.083f),
    
    WALK_LEFT("walk_left.png", new int[][] {
        {0, 0, 2480, 1754},
        {0, 1755, 2480, 1754},
        {2481, 0, 2480, 1754},
        {2481, 1755, 2480, 1754},
        {0, 3510, 2480, 1754},
        {2481, 3510, 2480, 1754},
        {0, 5265, 2480, 1754}
    }, 0.083f),
    
    IDLE_RIGHT("idle.png", new int[][] {
        {0, 0, 2480, 1754},
        {0, 1755, 2480, 1754},
        {2481, 0, 2480, 1754},
        {2481, 1755, 2480, 1754}
    }, 0.083f),
    
    IDLE_LEFT("idleLeft.png", new int[][] {
        {0, 0, 2480, 1754},
        {0, 1755, 2480, 1754},
        {2481, 0, 2480, 1754},
        {2481, 1755, 2480, 1754}
    }, 0.083f),
    
    JUMPING_RIGHT("jumping.png", new int[][] {
        {0, 0, 2480, 1754},
        {0, 1755, 2480, 1754},
        {2481, 0, 2480, 1754}
    }, 0.083f),
    
    JUMPING_LEFT("jumpingLeft.png", new int[][] {
        {0, 0, 2480, 1754},
        {0, 1755, 2480, 1754},
        {2481, 0, 2480, 1754}
    }, 0.083f),
    
    CHARGE_JUMP_RIGHT("chargejump.png", new int[][] {
        {0, 0, 2480, 1754},
        {0, 1755, 2480, 1754},
        {2481, 0, 2480, 1754}
    }, 0.083f),
    
    CHARGE_JUMP_LEFT("chargeLeftJump.png", new int[][] {
        {0, 0, 2480, 1754},
        {0, 1755, 2480, 1754},
        {2481, 0, 2480, 1754}
    }, 0.083f),
    
    FALLING_RIGHT("falling.png", new int[][] {
        {0, 0, 2480, 1754},
        {0, 1755, 2480, 1754},
        {2481, 0, 2480, 1754}
    }, 0.083f),
    
    FALLING_LEFT("fallingLeft.png", new int[][] {
        {0, 0, 2480, 1754},
        {0, 1755, 2480, 1754},
        {2481, 0, 2480, 1754}
    }, 0.083f),
    
    ATTACKING_RIGHT("attacking.png", new int[][] {
        {0, 0, 2480, 1754},
        {0, 1755, 2480, 1754},
        {2481, 0, 2480, 1754},
        {2481, 1755, 2480, 1754},
        {0, 1755, 2480, 1754}
    }, 0.083f),
    
    ATTACKING_LEFT("attackingLeft.png", new int[][] {
        {0, 0, 2480, 1754},
        {0, 1755, 2480, 1754},
        {2481, 0, 2480, 1754},
        {2481, 1755, 2480, 1754},
        {0, 1755, 2480, 1754}
    }, 0.083f),

    FALLING_ATACK("fallingAttack.png", new int[][] {
        {0, 0, 2480, 1754},
        {0, 1755, 2480, 1754},
        {2481, 0, 2480, 1754}
    }, 0.043f),

    FALLING_ATACK_LEFT("fallingAttackLeft.png", new int[][] {
        {0, 0, 2480, 1754},
        {0, 1755, 2480, 1754},
        {2481, 0, 2480, 1754}
    }, 0.043f),

    FALLING_ATACK_PASS("passAttack.png", new int[][] {
        {0, 0, 2480, 1754},
        {0, 1755, 2480, 1754}
    }, 0.083f),

    FALLING_ATACK_PASS_LEFT("passAttackLeft.png", new int[][] {
        {0, 0, 2480, 1754},
        {0, 1755, 2480, 1754},
        {2481, 0, 2480, 1754}
    }, 0.083f),

    IGNITION("Ignition.png", new int[][] {
    {      0,     0, 3500, 877}, // frame 1 (col 0, row 0)
    {  3500,     0, 3500, 877}, // frame 2 (col 1, row 0)
    {     0,   877, 3500, 877}, // frame 3 (col 0, row 1)
    {  3500,   877, 3500, 877}, // frame 4 (col 1, row 1)
    {     0,  1754, 3500, 877}, // frame 5 (col 0, row 2)
    {  3500,  1754, 3500, 877}, // frame 6 (col 1, row 2)
    {     0,  2631, 3500, 877}, // frame 7 (col 0, row 3)
    {  3500,  2631, 3500, 877}  // frame 8 (col 1, row 3)
}, 0.083f),


    IGNITION_LEFT("ignitionLeft.png", new int[][] {
        {      0,     0, 3500, 877}, // frame 1 (col 0, row 0)
    {  3500,     0, 3500, 877}, // frame 2 (col 1, row 0)
    {     0,   877, 3500, 877}, // frame 3 (col 0, row 1)
    {  3500,   877, 3500, 877}, // frame 4 (col 1, row 1)
    {     0,  1754, 3500, 877}, // frame 5 (col 0, row 2)
    {  3500,  1754, 3500, 877}, // frame 6 (col 1, row 2)
    {     0,  2631, 3500, 877}, // frame 7 (col 0, row 3)
    {  3500,  2631, 3500, 877}  // frame 8 (col 1, row 3)
    }, 0.083f),

    FIRE_ATACK("FireAtack.png", new int[][] {
        {      0,     0, 3500, 877}, // frame 1 (col 0, row 0)
    {  3500,     0, 3500, 877}, // frame 2 (col 1, row 0)
    {     0,   877, 3500, 877}
    }, 0.083f),

    FIRE_ATACK_LEFT("FireAtackLeft.png", new int[][] {
        {      0,     0, 3500, 877}, // frame 1 (col 0, row 0)
    {  3500,     0, 3500, 877}, // frame 2 (col 1, row 0)
    {     0,   877, 3500, 877}
    }, 0.083f),

    LOADED("loaded.png", new int [][] {
        {0, 0, 480, 600}
    }, 0.083f),

    UNLOADED("unloaded.png", new int [][] {
        {0, 0, 480, 600}
    }, 0.083f),

    LOADING("loading.png", new int[][] {
        {0, 0, 480, 600},
        {480, 0, 480, 600},
        {0, 600, 480, 600},
        {480, 600, 480, 600},
        {0, 1200, 480, 600},
        {480, 1200, 480, 600},
        {0, 1800, 480, 600},
        {480, 1800, 480, 600},
        {0, 2400, 480, 600},
        {480, 2400, 480, 600},
        {0, 3000, 480, 600},
        {480, 3000, 480, 600},
        {0, 3600, 480, 600}
}, 0.083f),

    UNLOADING("unloading.png", new int [][] {
        {0, 0, 480, 600},
        {480, 0, 480, 600},
        {0, 600, 480, 600},
        {480, 600, 480, 600},
        {0, 1200, 480, 600},
        {480, 1200, 480, 600},
        {0, 1800, 480, 600},
        {480, 1800, 480, 600},
        {0, 2400, 480, 600},
        {480, 2400, 480, 600},
        {0, 3000, 480, 600},
        {480, 3000, 480, 600},
        {0, 3600, 480, 600}
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