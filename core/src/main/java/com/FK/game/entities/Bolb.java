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
public class Bolb extends Enemy {
    
    public Bolb(Array<Rectangle> collisionObjects) {
        super(0, 0, 250, 300, 100, 150, collisionObjects);
        setCollisionBoxOffset(100f, 0f);
        this.maxHealth = 5; 
        this.health = this.maxHealth; 
        setDamage(1);
        setKnockbackX(100f);
        setKnockbackY(200f);
        this.coinValue = 5;
        setCurrentAnimation(EnemyAnimationType.BOLB);
        initStateMachine();
        spawnOnRandomPlatform();
    }
    
    @Override
    protected EnemyDamageState createDamageState(Entity source) {
        return new EnemyDamageState(source);
    }
    @Override
    public EntityState<Enemy> getDefaultState() {
        return new BolbWalkState();
    }
    @Override
    public String toString() {
        return "Bolb";
    }
}