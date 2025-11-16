package com.FK.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.FK.game.entities.*;
import com.FK.game.screens.*;
import com.FK.game.states.*;
import com.FK.game.sounds.*;
import com.FK.game.network.StateMessage;

public class ChargingJumpState implements EntityState<Player> {
    private float chargeTime = 0f;
    private static final float MAX_CHARGE_TIME = 0.5f;
    private static final float MIN_JUMP_FORCE = 250f;
    private static final float MAX_JUMP_FORCE = 1200f;

     @Override
    public void enter(Player player) {
        chargeTime = 0;
        player.setCurrentAnimation(player.isMovingRight() ? 
            PlayerAnimationType.CHARGE_JUMP_RIGHT : 
            PlayerAnimationType.CHARGE_JUMP_LEFT);
    }

    @Override
    public void update(Player player, float delta) {
        chargeTime = Math.min(chargeTime + delta, MAX_CHARGE_TIME);
        
        player.getCurrentAnimation().update(delta);
        SoundCache.getInstance().stopLoop(SoundType.WALK);
    }

    @Override
    public void handleInput(Player player) {
    }

    @Override
    public void render(Player player, Batch batch) {
        TextureRegion frame = player.getCurrentAnimation().getCurrentFrame();
        float chargeRatio = chargeTime / MAX_CHARGE_TIME;
        
        
        float scaleY = 1.0f - chargeRatio * 0.3f; 
        float scaleX = 1.0f + chargeRatio * 0.1f;
        
        batch.draw(frame,
                player.getBounds().x,
                player.getBounds().y,
                player.getBounds().width * 0.5f,
                0, 
                player.getBounds().width,
                player.getBounds().height,
                scaleX,
                scaleY,
                0);
    }

    @Override
    public StateMessage getNetworkState() {
        return StateMessage.PLAYER_CHARGING_JUMP;
    }

    @Override
    public void exit(Player player) {
    }
}