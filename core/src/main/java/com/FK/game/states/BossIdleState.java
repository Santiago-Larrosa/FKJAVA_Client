package com.FK.game.states;

import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.FK.game.entities.Enemy;
import com.FK.game.entities.Boss;
import com.FK.game.entities.Player;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.FK.game.network.StateMessage;
import com.FK.game.states.EntityState;

public class BossIdleState implements EntityState<Enemy> {
    private float attackTimer;
    private static final float ATTACK_INTERVAL = 4f; 

    @Override
    public void enter(Enemy enemy) {
        enemy.setAnimation(EnemyAnimationType.BOLB);
        attackTimer = 0f;
    }

    @Override
    public void update(Enemy enemy, float delta) {
      /*  attackTimer += delta;
        if (attackTimer >= ATTACK_INTERVAL && enemy.isPlayerInRange()) {
            ((Boss) enemy).acquireTarget(); 
            // 2. Se inicia el estado de ataque
            enemy.getStateMachine().changeState(new BossLaserAttackState());
        }*/
    }

    @Override
    public void render(Enemy enemy, com.badlogic.gdx.graphics.g2d.Batch batch) {
        Boss boss = (Boss) enemy;
        if (boss.getCurrentAnimation() != null && boss.getCurrentAnimation().getCurrentFrame() != null) {
            batch.draw(boss.getCurrentAnimation().getCurrentFrame(),
                boss.getX(), boss.getY(),
                boss.getWidth(), boss.getHeight());
        }
    }

    @Override
    public void exit(Enemy enemy) {
    }
@Override
    public StateMessage getNetworkState() {
        return StateMessage.BOSS_IDLE;
    }
    @Override
    public void handleInput(Enemy enemy) {}
}