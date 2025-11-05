package com.FK.game.states;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.FK.game.animations.*;
import com.FK.game.core.*; 
import com.FK.game.entities.*;
import com.FK.game.screens.*;
import com.FK.game.states.*;
import com.FK.game.network.StateMessage;

public class JumpingState implements EntityState<Player> {
    private float airTime = 0f;
    private static final float AIR_CONTROL = 0.7f;
    private boolean hitCeiling = false;

    @Override
    public void enter(Player player) {
        player.setCurrentAnimation(player.isMovingRight() ? 
            PlayerAnimationType.JUMPING_RIGHT : PlayerAnimationType.JUMPING_LEFT);
        if (player.isOnGround()) {
            player.getVelocity().y = Player.JUMP_VELOCITY;
            player.setOnGround(false);
        }

        airTime = 0f;
        hitCeiling = false;
    }

    @Override
    public void update(Player player, float delta) {
       // InputHandler input = player.getInputHandler();

        airTime += delta;
        player.getCurrentAnimation().update(delta);
        /*if (input.isMoveLeftPressed()) {
            player.getVelocity().x = -Player.WALK_SPEED * AIR_CONTROL;
            player.setCurrentAnimation(PlayerAnimationType.JUMPING_LEFT);
        } else if (input.isMoveRightPressed()) {
            player.getVelocity().x = Player.WALK_SPEED * AIR_CONTROL;
            player.setCurrentAnimation(PlayerAnimationType.JUMPING_RIGHT);
        }*/
/*
        if (checkCeilingCollision(player)) {
            player.getVelocity().y = 0;
            hitCeiling = true;
        }

        if (hitCeiling || player.getVelocity().y < 0) {
            player.getStateMachine().changeState(new FallingState());
        }*/
    }

    private boolean checkCeilingCollision(Player player) {
       /* Rectangle collisionBox = player.getCollisionBox();
        Rectangle ceilingSensor = new Rectangle(
            collisionBox.x + collisionBox.width * 0.2f,
            collisionBox.y + collisionBox.height,
            collisionBox.width * 0.6f,
            6f
        );
        for (Rectangle rect : player.getCollisionObjects()) {
            if (ceilingSensor.overlaps(rect)) {
                player.setCollisionY(rect.y - collisionBox.height - 1);
                return true;
            }
        }*/
        return false;
    }

    @Override
    public void handleInput(Player player) {
     /*   InputHandler input = player.getInputHandler();
        if (input.isAttackJustPressed()) {
            player.getStateMachine().changeState(new FallingAttackState());
        }
        if (input.isMoveLeftPressed()) {
            player.setMovingRight(false);
        } else if (input.isMoveRightPressed()) {
            player.setMovingRight(true);
        }*/
    }

    @Override
    public void render(Player player, Batch batch) {
        TextureRegion frame = player.getCurrentAnimation().getCurrentFrame();
        float scale = 1.0f + (float)Math.sin(airTime * 10) * 0.05f;
        batch.draw(frame,
            player.getBounds().x,
            player.getBounds().y,
            player.getBounds().width * 0.5f,
            player.getBounds().height * 0.5f,
            player.getBounds().width,
            player.getBounds().height,
            scale,
            scale,
            0);
    }

    @Override
    public StateMessage getNetworkState() {
        return StateMessage.PLAYER_JUMPING;
    }

    @Override
    public void exit(Player player) {
    }
}