// Crea un nuevo archivo: Fungo.java
package com.FK.game.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.FK.game.animations.EnemyAnimationType;
import com.FK.game.entities.*;
import com.FK.game.states.*;

public class Fungop extends Enemy {
   
    public Fungop(Array<Rectangle> collisionObjects) {
        super(0, 0, 200, 269, 120, 150, collisionObjects);
        setCollisionBoxOffset(35f, 35f); 
        setDamage(2);
        setKnockbackX(200f);
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
    protected EnemyDamageState createDamageState(Entity source) {
        return new EnemyDamageState(source);
    }

    @Override
    public EntityState<Enemy> getDefaultState() {
        return new FungoFlyingState();
    }

    @Override
    public String toString() {
        return "Fungop";
    }
}