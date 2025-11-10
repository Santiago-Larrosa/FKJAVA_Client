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
public class Bolb extends Enemy {
    
    public Bolb(Array<Rectangle> collisionObjects) {
        super(0, 0, 250, 300, 100, 150, collisionObjects);
        setCollisionBoxOffset(100f, 0f);
        this.maxHealth = 5; 
        this.entityType = EntityTypeMessage.BOLB;
        this.health = this.maxHealth; 
        setDamage(1);
        this.attackRange = 50f;
        setKnockbackX(100f);
        setKnockbackY(200f);
        this.coinValue = 5;
        setCurrentAnimation(EnemyAnimationType.BOLB);
        initStateMachine();
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
    public AnimationType getDamageAnimationType() {
        return isMovingRight() ? EnemyAnimationType.BOLB : EnemyAnimationType.BOLB_LEFT;
    }
    
    @Override
    public void updatePlayerDetection() {
        this.isPlayerInRange = false;

        for (Player player : GameContext.getActivePlayers()) {
            if (player != null && !player.isDead()) {
                // El Bolb usa una detección simple por distancia (radio)
                if (this.getCenter().dst(player.getCenter()) < this.attackRange) {
                    this.isPlayerInRange = true;
                    return;
                }
            }
        }
    }

    @Override
    public EntityState<Enemy> getDefaultState() {
        return new BolbWalkState();
    }
    @Override
    public String toString() {
        return "Bolb";
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
        case BOLB_WALKING:
            stateMachine.changeState(new BolbWalkState());
            break;
        case BOLB_ATTACKING:
            stateMachine.changeState(new BolbAttackState());
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