package com.FK.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.FK.game.entities.*;
import com.FK.game.screens.*;
import com.FK.game.states.*;
import com.FK.game.sounds.*;
import com.FK.game.network.StateMessage;

public class AttackingState implements EntityState<Player> {
    private float attackTimer = 0f;
    private static final float ATTACK_DURATION = 0.415f;
    private static final float HITBOX_ACTIVATION_FRAME = 0.15f;
    private boolean hitboxActive = false;
    private Rectangle attackHitbox;
    private static final boolean DEBUG_MODE = false;

    @Override
    public void enter(Player player) {
        player.getDamageBox().set(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        player.setCurrentAnimation(player.isMovingRight() ? PlayerAnimationType.ATTACKING_RIGHT : PlayerAnimationType.ATTACKING_LEFT);
         SoundCache.getInstance().get(SoundType.SWORD).play(0.5f);
         player.setDamage(0f);
        attackTimer = 0f;
        hitboxActive = false;
        attackHitbox = new Rectangle(
            player.getBounds().x + (player.isMovingRight() ? player.getBounds().width * 0.7f : -player.getBounds().width * 0.2f),
            player.getBounds().y + player.getBounds().height * 0.2f,
            player.getBounds().width * 0.5f,
            player.getBounds().height * 0.6f
        );
        
        player.getVelocity().x = 0; 
    }

   @Override
    public void update(Player player, float delta) {
        attackTimer += delta;

        player.getCurrentAnimation().update(delta);
        if (!hitboxActive && attackTimer >= HITBOX_ACTIVATION_FRAME) {
            hitboxActive = true;
        }
        
        updateHitboxPosition(player);
        
        if (attackTimer >= ATTACK_DURATION * 0.7f) {
            handleEarlyMovement(player);
        }
        
        if (attackTimer >= ATTACK_DURATION) {
            transitionToNextState(player);
        }
    }

    private void handleEarlyMovement(Player player) {
/*
        InputHandler input = player.getInputHandler();

        if (input.isMoveLeftPressed()) {
            player.setMovingRight(false);
            player.getVelocity().x = -Player.WALK_SPEED * 0.5f; 
        } 
        else if (input.isMoveRightPressed()) {
            player.setMovingRight(true);
            player.getVelocity().x = Player.WALK_SPEED * 0.5f;
        }*/
    }

    private void transitionToNextState(Player player) {
     /*   InputHandler input = player.getInputHandler();
    
        boolean wantsToMove = input.isMoveLeftPressed() || input.isMoveRightPressed();
        
        if (player.isOnGround()) {
            player.getStateMachine().changeState(
                wantsToMove ? new WalkingState() : new IdleState()
            );
        } else {
            player.getStateMachine().changeState(new FallingState());
        }
        
        if (wantsToMove) {
            player.getVelocity().x = player.isMovingRight() ? 
                Player.WALK_SPEED : -Player.WALK_SPEED;
        }*/
    }
    private void updateHitboxPosition(Player player) {
        attackHitbox.setPosition(
            player.getBounds().x + (player.isMovingRight() ? player.getBounds().width * 0.7f : -player.getBounds().width * 0.2f),
            player.getBounds().y + player.getBounds().height * 0.2f
        );
    }

    

    @Override
    public void handleInput(Player player) {
        //InputHandler input = player.getInputHandler();
       // if (attackTimer >= ATTACK_DURATION * 0.7f && input.isAttackJustPressed()) {
        //}
    }

    @Override
    public void render(Player player, Batch batch) {
        TextureRegion frame = player.getCurrentAnimation().getCurrentFrame();
        batch.draw(frame, player.getBounds().x, player.getBounds().y,  player.getBounds().width, player.getBounds().height);
        if (DEBUG_MODE && hitboxActive) {
            ShapeRenderer renderer = new ShapeRenderer();
            renderer.setProjectionMatrix(batch.getProjectionMatrix());
            renderer.begin(ShapeRenderer.ShapeType.Line);
            renderer.setColor(Color.RED);
            renderer.rect(attackHitbox.x, attackHitbox.y, attackHitbox.width, attackHitbox.height);
            renderer.end();
        }
    }

    @Override
    public StateMessage getNetworkState() {
        return StateMessage.PLAYER_ATTACKING;
    }

    @Override
    public void exit(Player player) {
        player.setDamage(3f);
        player.setDamageSize(0,0);
    }
}