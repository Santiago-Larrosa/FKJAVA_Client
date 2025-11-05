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


public class WalkingState implements EntityState<Player> {
    private static final float FOOTSTEP_SOUND_DELAY = 0.3f;
    private float footstepTimer = 0f;
    private boolean lastDirectionRight = true;
    private int[] airConfirmationCount = new int[1]; 


    @Override
    public void enter(Player player) {
        updateAnimation(player);
        footstepTimer = 0f;
        lastDirectionRight = player.isMovingRight();
        airConfirmationCount[0] = 0;
    }

   @Override
    public void update(Player player, float delta) {
      // InputHandler input = player.getInputHandler();
        //handleInput(player);
        player.getCurrentAnimation().update(delta);
       // player.getBounds().y += player.getVelocity().y * delta;
       /* 
        if (StateUtils.checkFalling(player, delta, airConfirmationCount)) {
            return;
        }

        if (player.getVelocity().y > 0) {
            player.getStateMachine().changeState(new JumpingState());
            return;
        }*/
/*
        if (input.isJumpPressed() && player.isOnPlataform()) {
            player.getStateMachine().changeState(new ChargingJumpState());
            return;
        }

        if (input.isAttackJustPressed()) {
            player.getStateMachine().changeState(new AttackingState());
            SoundCache.getInstance().stopLoop(SoundType.WALK);
            return;
        }
*/
      //  boolean isMoving = input.isMoveLeftPressed() || input.isMoveRightPressed();
/*
        if (!isMoving) {
            player.getStateMachine().changeState(new IdleState());
            return;
        }
*/
        SoundCache.getInstance().playLoop(SoundType.WALK, 0.4f);
    }


    @Override
    public void handleInput(Player player) {
      /*  
        InputHandler input = player.getInputHandler();
        boolean movingLeft = input.isMoveLeftPressed();
        boolean movingRight = input.isMoveRightPressed();

        if (movingLeft) {
            player.setMovingRight(false);
            player.getVelocity().x = -Player.WALK_SPEED;
        } 
        else if (movingRight) {
            player.setMovingRight(true);
            player.getVelocity().x = Player.WALK_SPEED;
        }
        else {
            player.getVelocity().x = 0;
        }
        player.setCurrentAnimation(player.isMovingRight() ? 
            PlayerAnimationType.WALK_RIGHT : PlayerAnimationType.WALK_LEFT);*/
            
    }

    private void updateAnimation(Player player) {
        player.setCurrentAnimation(
            player.isMovingRight() ? 
                PlayerAnimationType.WALK_RIGHT : 
                PlayerAnimationType.WALK_LEFT
        );
    }

    @Override
    public void render(Player player, Batch batch) {
        TextureRegion frame = player.getCurrentAnimation().getCurrentFrame();
        float tiltAngle = (float) Math.sin(footstepTimer * 20f) * 3f;
        
        batch.draw(frame,
                 player.getBounds().x,
                 player.getBounds().y,
                 player.getBounds().width * 0.5f,
                 player.getBounds().height * 0.1f,
                 player.getBounds().width,
                 player.getBounds().height,
                 1f, 1f, tiltAngle);
    }

    @Override
    public StateMessage getNetworkState() {
        return StateMessage.PLAYER_WALKING;
    }

    @Override
    public void exit(Player player) {
        player.getVelocity().x = 0; 
        SoundCache.getInstance().stopLoop(SoundType.WALK);
    }

    @Override
    public String toString() {
        return "WalkingState";

    }
}
