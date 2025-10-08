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
    
    public Boss(Array<Rectangle> collisionObjects) {
        super(0, 0, 1250, 1300, 1100, 1150, collisionObjects);
        setCollisionBoxOffset(100f, 0f);
        setDamage(1);
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
    protected EnemyDamageState createDamageState(Entity source) {
        return new EnemyDamageState(source);
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