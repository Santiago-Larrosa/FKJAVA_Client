package com.FK.game.states;

import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.FK.game.entities.Boss;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.FK.game.network.StateMessage;
import com.FK.game.states.EntityState;

public class BossIdleState implements EntityState<Boss> {
    private float attackTimer;
    private static final float ATTACK_INTERVAL = 4f; 

    @Override
    public void enter(Boss enemy) {
        enemy.setAnimation(EnemyAnimationType.BOLB);
        attackTimer = 0f;
    }

    @Override
    public void update(Boss enemy, float delta) {
    }

    @Override
    public void render(Boss enemy, Batch batch) {
        Boss boss = enemy;
        if (boss.getCurrentAnimation() != null && boss.getCurrentAnimation().getCurrentFrame() != null) {
            batch.draw(boss.getCurrentAnimation().getCurrentFrame(),
                boss.getX(), boss.getY(),
                boss.getWidth(), boss.getHeight());
        }
    }

    @Override
    public void exit(Boss enemy) {
    }
@Override
    public StateMessage getNetworkState() {
        return StateMessage.BOSS_IDLE;
    }
    @Override
    public void handleInput(Boss enemy) {}
}
