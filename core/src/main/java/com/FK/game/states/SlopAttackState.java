package com.FK.game.states;

import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.FK.game.animations.EnemyAnimationType;
import com.FK.game.core.GameContext;
import com.FK.game.entities.Enemy;
import com.FK.game.entities.Slop;
import com.FK.game.entities.Player;
import com.FK.game.entities.Slop;
import com.FK.game.states.EntityState;
import com.FK.game.network.StateMessage;

public class SlopAttackState implements EntityState<Enemy> {
      private float attackTimer;
    private static final float ATTACK_DURATION = 0.913f;

    @Override
    public void enter(Enemy enemy) {
        Slop Slop = (Slop) enemy;
        Slop.setBoundSize(106, 128);
        attackTimer = 0f;
        Slop.setAnimation(Slop.isMovingRight() == true ? (EnemyAnimationType.SLOP_ATTACK) : (EnemyAnimationType.SLOP_ATTACK_LEFT));
        Slop.getVelocity().x = 0;
        Slop.getDamageBox().set(Slop.getX(), Slop.getY(), Slop.getWidth(), Slop.getHeight());
        Slop.setCanAttack(false);
    }

    @Override
    public void update(Enemy enemy, float delta) {
        Slop Slop = (Slop) enemy;
       // attackTimer += delta;

        if(Slop.getCurrentAnimation() != null) {
        Slop.getCurrentAnimation().update(delta);
    }/*

        Player player = GameContext.getPlayer();

        if (player != null) {
            if (player.getBounds().x < Slop.getBounds().x) {
                Slop.setMovingRight(false);
            } else {
                Slop.setMovingRight(true);
            }
        }

        if (attackTimer >= ATTACK_DURATION) {
            Slop.getDamageBox().set(0, 0, 0, 0);
            Slop.getStateMachine().changeState(new SlopWalkState());
        }*/
    }

    @Override
    public void render(Enemy enemy, Batch batch) {
        Slop Slop = (Slop) enemy;
        if (Slop.getCurrentAnimation() != null && Slop.getCurrentAnimation().getCurrentFrame() != null) {
            batch.draw(Slop.getCurrentAnimation().getCurrentFrame(),
                Slop.getX(), Slop.getY(),
                Slop.getWidth(), Slop.getHeight());
        }
    }

    @Override
    public void exit(Enemy enemy) {
        Slop Slop = (Slop) enemy;
        Slop.setBoundSize(106, 75);
        Slop.getDamageBox().set(0, 0, 0, 0);
    }

    @Override
    public StateMessage getNetworkState() {
        return StateMessage.SLOP_ATTACKING;
    }

    @Override
    public void handleInput(Enemy enemy) {}
}