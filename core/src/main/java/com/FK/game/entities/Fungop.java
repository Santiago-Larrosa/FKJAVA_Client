// Crea un nuevo archivo: Fungo.java
package com.FK.game.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.FK.game.animations.EnemyAnimationType;
import com.FK.game.entities.*;
import com.FK.game.states.*;
import com.FK.game.animations.*;
import com.FK.game.core.GameContext;
import com.FK.game.network.*;

public class Fungop extends Enemy {
   
    public Fungop(Array<Rectangle> collisionObjects) {
        super(0, 0, 200, 269, 120, 150, collisionObjects);
        setCollisionBoxOffset(35f, 35f); 
        setDamage(2);
        setKnockbackX(200f);
        this.attackRange = 200f;
        this.entityType = EntityTypeMessage.FUNGOP;
        setKnockbackY(400f);
        this.maxHealth = 10; 
        this.health = this.maxHealth; 
        this.attackCooldownTimer = 5f; 
        this.setCanAttack(true);
        this.coinValue = 10;
        setCurrentAnimation(EnemyAnimationType.FUNGOP);
        initStateMachine();
        setGravity(-250f);
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

        // Definimos las dimensiones del área de ataque del Fungop
        final float ATTACK_DETECTION_WIDTH = 150f;
        final float ATTACK_DETECTION_HEIGHT = 400f;

        // Creamos la caja de detección rectangular debajo del Fungop
        Rectangle detectionBox = new Rectangle(
            this.getCollisionBox().x - (ATTACK_DETECTION_WIDTH / 2f) + (this.getCollisionBox().width / 2f),
            this.getCollisionBox().y - ATTACK_DETECTION_HEIGHT,
            ATTACK_DETECTION_WIDTH,
            ATTACK_DETECTION_HEIGHT
        );

        // Comprobamos si algún jugador está dentro de esa caja
        for (Player player : GameContext.getActivePlayers()) {
            if (player != null && !player.isDead()) {
                if (detectionBox.overlaps(player.getCollisionBox())) {
                    this.isPlayerInRange = true;
                    return; // Encontramos un objetivo, no necesitamos seguir buscando
                }
            }
        }
    }

    @Override
public AnimationType getDamageAnimationType() {
    // La misma lógica, pero con los tipos de animación del enemigo.
    return isMovingRight() ? EnemyAnimationType.FUNGOP : EnemyAnimationType.FUNGOP;
}

    @Override
    public EntityState<Enemy> getDefaultState() {
        return new FungoFlyingState();
    }

    @Override
    public String toString() {
        return "Fungop";
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
        case FUNGOP_FLIYING:
            stateMachine.changeState(new FungoFlyingState());
            break;
        case FUNGOP_ATTACKING:
            stateMachine.changeState(new FungopDiveAttackState());
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