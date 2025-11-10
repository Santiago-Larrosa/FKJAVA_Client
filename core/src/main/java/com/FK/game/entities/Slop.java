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
public class Slop extends Enemy {
    
    public Slop(Array<Rectangle> collisionObjects) {
        super(0, 0, 106, 75, 106, 75, collisionObjects);
        setCollisionBoxOffset(0f, 0f);
        setCurrentAnimation(EnemyAnimationType.SLOP);
        this.maxHealth = 3; 
        this.attackRange = 50f;
        this.entityType = EntityTypeMessage.SLOP;   
        this.health = this.maxHealth; 
        setKnockbackX(100f);
        setKnockbackY(200f);
        setDamage(1);
        this.coinValue = 3;
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
    public void updatePlayerDetection() {
        // Por defecto, asumimos que no hay nadie en rango
        this.isPlayerInRange = false;

        // Recorremos la lista de jugadores activos
        for (Player player : GameContext.getActivePlayers()) {
            if (player != null && !player.isDead()) {
                // El Slop usa una detección simple por distancia (radio)
                if (this.getCenter().dst(player.getCenter()) < this.attackRange) {
                    this.isPlayerInRange = true;
                    // Encontramos un objetivo, no necesitamos seguir buscando
                    return;
                }
            }
        }
    }
    @Override
public AnimationType getDamageAnimationType() {
    // La misma lógica, pero con los tipos de animación del enemigo.
    return isMovingRight() ? EnemyAnimationType.SLOP : EnemyAnimationType.SLOP_LEFT;
}

    @Override
    public EntityState<Enemy> getDefaultState() {
        return (EntityState<Enemy>) new SlopWalkState();
    }
    @Override
    public String toString() {
        return "Slop";
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
        case SLOP_WALKING:
            stateMachine.changeState(new SlopWalkState());
            break;
        case SLOP_ATTACKING:
            stateMachine.changeState(new SlopAttackState());
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