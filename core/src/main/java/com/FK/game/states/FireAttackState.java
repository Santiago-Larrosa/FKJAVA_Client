package com.FK.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.FK.game.animations.*;
import com.FK.game.entities.*;
import com.FK.game.screens.*;
import com.FK.game.sounds.*;
import com.FK.game.network.StateMessage;


public class FireAttackState implements EntityState<Player> {
    private boolean playingIgnition = true;
    private float ignitionTimer = 0f;
    private float attackTimer = 0f;
    private static final float FIRE_ATTACK_DURATION = 3f;
    private static final float COOLDOWN = 5f;
    private static boolean canUseFireAttack = true; 
    private static float timeSinceLastAttack = 0f;
    private static final float FIRE_ATTACK_LEFT_OFFSET_X = 303f;
    private static float cooldownTimer = 0f;
    private static final float FRAME_WIDTH = 423f;
    private static final float FRAME_HEIGHT = 120f;
    private static final float ORIGINAL_WIDTH = 150f; 
    private static final float ORIGINAL_HEIGHT = 110f;
    private static final float ORIGINAL_COLISION_WIDTH = 100f; 
    private static final float ORIGINAL_COLISION_HEIGHT = 100f;
    public static boolean wasJustUsed = false;


    @Override
    public void enter(Player player) {
        player.setDamage(0.5f);
       /*if (!player.isAttackReady()) {
        player.getStateMachine().changeState(new IdleState());
        return;
    }*/
        SoundCache.getInstance().playLoop(SoundType.FIRE, 0.4f);
        //player.getFireAttackHUD().resetCooldown();

        ignitionTimer = 0f;
        attackTimer = 0f;
        playingIgnition = true;

        //player.startFireAttackCooldown();

        if (player.isMovingRight()) {
            player.setCurrentAnimation(PlayerAnimationType.IGNITION);
            player.setBoundSize(FRAME_WIDTH, FRAME_HEIGHT);
            float offsetX = player.isMovingRight() ? player.getWidth() : 10f;
            float offsetY = 30f; 
            player.getDamageBox().set(player.getX(), player.getY(), FRAME_WIDTH, FRAME_HEIGHT);
        } else {
            player.setCurrentAnimation(PlayerAnimationType.IGNITION_LEFT);
            player.setBoundSize(FRAME_WIDTH, FRAME_HEIGHT);
            player.setCollisionBoxOffset(280,0);
            player.getCollisionBox().set(player.getX() , player.getY(), ORIGINAL_COLISION_WIDTH, ORIGINAL_COLISION_HEIGHT);
            
            player.setX(player.getX() - FIRE_ATTACK_LEFT_OFFSET_X);
            player.getDamageBox().set(player.getX(), player.getY(), FRAME_WIDTH, FRAME_HEIGHT);
        }

    }


    @Override
    public void update(Player player, float delta) {

       /* if (playingIgnition) {
            ignitionTimer += delta;*/
            player.getCurrentAnimation().update(delta);
/*
            if (ignitionTimer >= player.getCurrentAnimation().getTotalDuration()) {
                playingIgnition = false;
                attackTimer = 0f;
               */ player.setCurrentAnimation(player.isMovingRight()
                    ? PlayerAnimationType.FIRE_ATACK
                    : PlayerAnimationType.FIRE_ATACK_LEFT);
            /*}
        } else {
            attackTimer += delta;
            player.getCurrentAnimation().update(delta);
            if (attackTimer >= FIRE_ATTACK_DURATION) {
                player.setBoundSize(player.getWidth(), player.getHeight());
                if (!player.isMovingRight()) {
                    player.setX(player.getBounds().x + FIRE_ATTACK_LEFT_OFFSET_X);
                }
                player.getStateMachine().changeState(new IdleState());
            }
        }*/
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
        return StateMessage.PLAYER_FIRING;
    }

    @Override
    public void exit(Player player) {
        player.setDamage(3);
        SoundCache.getInstance().stopLoop(SoundType.FIRE);
        player.setBoundSize(ORIGINAL_WIDTH, ORIGINAL_HEIGHT);
        player.getCollisionBox().set(player.getX() - FIRE_ATTACK_LEFT_OFFSET_X, player.getY(),ORIGINAL_COLISION_WIDTH, ORIGINAL_COLISION_HEIGHT);
        player.setDamageSize(0, 0);
        if(!player.isMovingRight()) {
            player.setCollisionBoxOffset(20,0);
        }
    }

    public static boolean isOnCooldown() {
        return cooldownTimer > 0f;
    }
}
