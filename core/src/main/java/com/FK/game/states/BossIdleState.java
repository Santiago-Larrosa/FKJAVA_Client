package com.FK.game.states;

import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.FK.game.entities.Enemy;
import com.FK.game.entities.Boss;
import com.FK.game.entities.Player;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BossIdleState implements EntityState<Enemy> {
    private float attackTimer;
    private static final float ATTACK_INTERVAL = 4f; 

    @Override
    public void enter(Enemy enemy) {
        attackTimer = 0f;
    }

    @Override
    public void update(Enemy enemy, float delta) {
        attackTimer += delta;
        if (attackTimer >= ATTACK_INTERVAL) {
            enemy.getStateMachine().changeState(new BossLaserAttackState());
        }
    }

    @Override
    public void render(Enemy enemy, com.badlogic.gdx.graphics.g2d.Batch batch) {
    }

    @Override
    public void exit(Enemy enemy) {
    }

    @Override
    public void handleInput(Enemy enemy) {}
}