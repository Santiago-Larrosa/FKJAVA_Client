package com.FK.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.FK.game.entities.*;
import com.FK.game.screens.*;
import com.FK.game.states.*;
import com.FK.game.sounds.*;
import com.FK.game.network.StateMessage;

public class FallingAttackState implements EntityState<Player> {
    private boolean startedPassAnimation = false;
    private boolean passAnimationFinished = false;
    private float passTimer = 0f;
    private static final float PASS_DURATION = 0.166f; 
    private static final float DIAGONAL_SPEED = 300f;
    private static final float EPSILON = 5f;


    @Override
    public void enter(Player player) {
        player.setDamage(1);
        startedPassAnimation = true;
        passAnimationFinished = false;
        passTimer = 0f;
        player.getDamageBox().set(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        player.setCurrentAnimation(player.isMovingRight()
            ? PlayerAnimationType.FALLING_ATACK_PASS
            : PlayerAnimationType.FALLING_ATACK_PASS_LEFT);
    }

    @Override
    public void update(Player player, float delta) {
       
       // InputHandler input = player.getInputHandler();

        player.getDamageBox().set(player.getX(), player.getY(), player.getCollisionWidth(), player.getCollisionHeight());
        SoundCache.getInstance().playLoop(SoundType.FALLING_ATACK, 0.4f);
        if (startedPassAnimation) {
            passTimer += delta;
            player.getCurrentAnimation().update(delta);

            if (passTimer >= PASS_DURATION) {
                startedPassAnimation = false;
                passAnimationFinished = true;

                player.setCurrentAnimation(player.isMovingRight()
                    ? PlayerAnimationType.FALLING_ATACK
                    : PlayerAnimationType.FALLING_ATACK_LEFT);
            }
        } else if (passAnimationFinished) {

            player.getCurrentAnimation().update(delta);
            ((GameScreen) player.getGame().getScreen()).shakeCamera(0.3f, 5f);
            float diagonalX = player.isMovingRight() ? DIAGONAL_SPEED : -DIAGONAL_SPEED;
            player.getVelocity().x = diagonalX;
            player.getVelocity().y = -DIAGONAL_SPEED;
            float nextX = player.getBounds().x + player.getVelocity().x * delta;
            float nextY = player.getBounds().y + player.getVelocity().y * delta;

            Rectangle nextBounds = new Rectangle(nextX, nextY, player.getBounds().width, player.getBounds().height);
            Array<Rectangle> platforms = player.getCollisionObjects();
            boolean collided = false;

            for (Rectangle platform : platforms) {
                Rectangle slightlyLowerBounds = new Rectangle(
                    nextBounds.x,
                    nextBounds.y - EPSILON,
                    nextBounds.width,
                    nextBounds.height
                );
                if (slightlyLowerBounds.overlaps(platform)) {
                    collided = true;
                    break;
                }
            }   


            if (collided) {
                Gdx.app.log("FallingAttackState", "Colisión detectada. Cambiando a IdleState.");
                player.getVelocity().set(0, 0);
                player.getBounds().x = nextX;
                player.getBounds().y = nextY;
                SoundCache.getInstance().stopLoop(SoundType.FALLING_ATACK);
                SoundCache.getInstance().get(SoundType.FALLING_CLASH).play(0.5f);
                player.getStateMachine().changeState(new FallingState());
            } else {
                player.getBounds().x = nextX;
                player.getBounds().y = nextY;
/*
                if (!input.isAttackPressed()) { 
                    player.getVelocity().set(0, 0);
                    SoundCache.getInstance().stopLoop(SoundType.FALLING_ATACK);
                    player.getStateMachine().changeState(new FallingState());
                }*/
            }


        }
    }

    @Override
    public void handleInput(Player player) {
    }

    @Override
    public void render(Player player, Batch batch) {
        TextureRegion frame = player.getCurrentAnimation().getCurrentFrame();
        batch.draw(
            frame,
            player.getBounds().x,
            player.getBounds().y,
            player.getBounds().width,
            player.getBounds().height
        );
    }

    @Override
    public StateMessage getNetworkState() {
        return StateMessage.PLAYER_FALLING_AND_ATTACKING;
    }

    @Override
    public void exit(Player player) {
        player.setDamage(3);
        SoundCache.getInstance().stopLoop(SoundType.FALLING_ATACK);
        player.setDamageSize(0,0);
    }
}
