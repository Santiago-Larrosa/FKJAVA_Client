package com.FK.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.FK.game.entities.*;
import com.FK.game.screens.*;

public class FallingState implements EntityState<Player> {
    private float fallTime = 0f;
    private static final float AIR_CONTROL = 0.5f;
    private static final float MAX_FALL_SPEED = -450f;
    private static final float FAST_FALL_THRESHOLD = -300f;
    private boolean isFastFalling = false;
    private int groundConfirmationCount = 0;
    private static final int REQUIRED_CONFIRMATIONS = 1; 
    private float lastGroundCheckTime = 0f;
    private static final float GROUND_CHECK_INTERVAL = 0.016f; 

    @Override
    public void enter(Player player) {
        player.setCurrentAnimation(player.isMovingRight() ? 
            PlayerAnimationType.FALLING_RIGHT : PlayerAnimationType.FALLING_LEFT);
        fallTime = 0f;
        isFastFalling = false;
        groundConfirmationCount = 0;
    }

    @Override
    public void update(Player player, float delta) {
        fallTime += delta;
        lastGroundCheckTime += delta;
        float fixedDelta = Math.min(delta, 1/60f);

        if (!isFastFalling) {
            player.getVelocity().y += player.getGravity() * fixedDelta;
        }

        player.getVelocity().y = Math.max(player.getVelocity().y, MAX_FALL_SPEED);
        float previousY = player.getBounds().y;
        float movementY = player.getVelocity().y * fixedDelta;
        player.getBounds().y += movementY;

        if (lastGroundCheckTime >= GROUND_CHECK_INTERVAL) {
            lastGroundCheckTime = 0f;
            
            boolean groundDetected = player.isOnPlataform();
            
            if (groundDetected) {
                Gdx.app.log("Jugador", "Toco el suelo");
                groundConfirmationCount++;
                if (groundConfirmationCount >= REQUIRED_CONFIRMATIONS) {
                    handleLanding(player);
                    return;
                }
            } else {
                groundConfirmationCount = Math.max(0, groundConfirmationCount - 1);
            }
        }

        player.setOnGround(false);
        
        
        player.getCurrentAnimation().update(fixedDelta);
        handleAirControl(player, fixedDelta);
        handleInput(player); 
    }

    private void handleLanding(Player player) {
        player.getVelocity().y = 0;
        player.setOnGround(true);
        
        if (isFastFalling) {
            ((GameScreen) player.getGame().getScreen()).shakeCamera(0.2f, 3f);
        }
        
        if (Math.abs(player.getVelocity().x) > 50f) {
            player.getStateMachine().changeState(new WalkingState());
        } else {
            player.getStateMachine().changeState(new IdleState());
        }
    }

    private void handleAirControl(Player player, float delta) {
        // CAMBIO: Obtenemos el handler del jugador
        InputHandler input = player.getInputHandler();
        
        float controlFactor = isFastFalling ? AIR_CONTROL * 0.3f : AIR_CONTROL;
        float targetVelocityX = 0;
        
        // CAMBIO: Usamos el handler abstracto
        if (input.isMoveLeftPressed()) {
            targetVelocityX = -Player.WALK_SPEED * controlFactor;
            player.setMovingRight(false);
        } else if (input.isMoveRightPressed()) {
            targetVelocityX = Player.WALK_SPEED * controlFactor;
            player.setMovingRight(true);
        }
        
        player.getVelocity().x += (targetVelocityX - player.getVelocity().x) * delta * 8f;

        if ((targetVelocityX < 0 && player.isMovingRight()) || 
            (targetVelocityX > 0 && !player.isMovingRight())) {
            player.setCurrentAnimation(player.isMovingRight() ? 
                PlayerAnimationType.FALLING_RIGHT : PlayerAnimationType.FALLING_LEFT);
        }
    }


    @Override
    public void handleInput(Player player) {
        InputHandler input = player.getInputHandler();

        if (input.isAttackJustPressed()) {
            player.getStateMachine().changeState(new FallingAttackState());
        }
        
        if (input.isMoveDownJustPressed() && !isFastFalling) {
            isFastFalling = true;
            player.getVelocity().y = MAX_FALL_SPEED;
        }
    }

    @Override
    public void render(Player player, Batch batch) {
        TextureRegion frame = player.getCurrentAnimation().getCurrentFrame();
        float scaleFactor = isFastFalling ? 
            MathUtils.clamp(-player.getVelocity().y/FAST_FALL_THRESHOLD * 0.1f, 0f, 0.15f) : 0f;
        
        batch.draw(frame,
            player.getBounds().x,
            player.getBounds().y,
            player.getBounds().width * 0.5f,
            player.getBounds().height * 0.8f,
            player.getBounds().width,
            player.getBounds().height,
            1f + scaleFactor,
            1f - scaleFactor,
            0);
    }

    @Override
    public void exit(Player player) {
    }
}