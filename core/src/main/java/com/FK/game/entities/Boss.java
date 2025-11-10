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
import com.FK.game.network.*;


import java.util.Random;
public class Boss extends Enemy {
    private Player currentTarget;
    private Phase laserState = Phase.IDLE;
    private float laserTimer = 0f;
    private float laserAngle = 0f; 
    public Boss(Array<Rectangle> collisionObjects) {
        super(0, 0, 1250, 1300, 1100, 1150, collisionObjects);
        setCollisionBoxOffset(100f, 0f);
        setDamage(1);
        this.attackRange = 1500f;
        this.entityType = EntityTypeMessage.BOSS;
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
    public void update(float delta) {
        if (!movementLocked) {
            stateMachine.update(delta);
        }
        float newX = lerp(bounds.x, targetX, delta * lerpSpeed);
        float newY = lerp(bounds.y, targetY, delta * lerpSpeed);
        bounds.setPosition(newX, newY);
        collisionBox.setPosition(bounds.x + collisionOffsetX, bounds.y + collisionOffsetY);
        debugPlatformDetection();
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

    public void startLaserCharging(float angle) {
        laserAngle = angle;
    laserState = Phase.WARNING;
    laserTimer = 0f;
}

public void fireLaser(float angle) {
    laserState = Phase.FIRING;
    laserAngle = angle;
    laserTimer = 0f;
}

public void laserCooldown() {
    laserState = Phase.COOLDOWN;
}

public void endLaserAttack() {
    laserState = Phase.IDLE;
}

public Phase getLaserState() {
    return laserState;
}

public float getLaserTimer() {
    return laserTimer;
}

public float getLaserAngle() {
    return laserAngle;
}

    @Override
public void setVisualStateFromServer(String networkState, String networkFacing) {
    
   
    StateMessage newState;
    FacingDirection newFacing;

    try {
        
        newState = StateMessage.valueOf(networkState);
        newFacing = FacingDirection.valueOf(networkFacing); 
    } catch (IllegalArgumentException e) {
        System.err.println("Estado o dirección de red desconocido: " + networkState + ", " + networkFacing);
        return;
    }

    this.movingRight = (newFacing == FacingDirection.RIGHT);

    StateMessage currentStateEnum = stateMachine.getCurrentState().getNetworkState();
    
    if (currentStateEnum == newState) {
        return; 
    }

    switch (newState) {
        case BOSS_IDLE:
            stateMachine.changeState(new BossIdleState());
            break;
        case BOSS_ATTACKING:
            stateMachine.changeState(new BossLaserAttackState());
            break;
        case  DYING:
            stateMachine.changeState(new DeathState());
            break;
        case GETTING_DAMMAGE:
            stateMachine.changeState(new DamageState());;
            break;
    }
}
}