package com.FK.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import java.util.List;
import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.FK.game.entities.*;
import com.FK.game.screens.*;
import com.FK.game.states.*;
import com.FK.game.sounds.*;

import java.util.Random;
public class Boss extends Enemy {
    private Player currentTarget; 
    public Boss(Array<Rectangle> collisionObjects) {
        super(0, 0, 1250, 1300, 1100, 1150, collisionObjects);
        setCollisionBoxOffset(100f, 0f);
        setDamage(1);
        this.attackRange = 1500f;
        setHealth(50);
        setKnockbackX(100f);
        setKnockbackY(200f);
        this.coinValue = 100;
        setCurrentAnimation(EnemyAnimationType.BOLB);
        initStateMachine();
        this.stateMachine = new EntityStateMachine<>(this, new BossIdleState());
        spawnOnRandomPlatform();
    }
    @Override
    public void updatePlayerDetection() {
        this.isPlayerInRange = false;

        for (Player player : GameContext.getActivePlayers()) {
            if (player != null && !player.isDead()) {
                if (this.getCenter().dst(player.getCenter()) < this.attackRange) {
                    this.isPlayerInRange = true;
                }
            }
        }
    }

public void acquireTarget() {
        this.currentTarget = null;
        float closestDistance = Float.MAX_VALUE;

        for (Player player : GameContext.getActivePlayers()) {
            if (player != null && !player.isDead()) {
                float distance = this.getCenter().dst(player.getCenter());
                if (distance < closestDistance) {
                    closestDistance = distance;
                    this.currentTarget = player;
                }
            }
        }
    }

    public Player getCurrentTarget() {
        return this.currentTarget;
    }
    @Override
    public AnimationType getDamageAnimationType() {
        return isMovingRight() ? EnemyAnimationType.BOLB : EnemyAnimationType.BOLB_LEFT;
    }

    @Override
    public EntityState<Enemy> getDefaultState() {
        return new BossIdleState();
    }
    @Override
    public String toString() {
        return "Boss";
    }
}