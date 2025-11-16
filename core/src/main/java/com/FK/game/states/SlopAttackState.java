package com.FK.game.states;

import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.FK.game.animations.EnemyAnimationType;
import com.FK.game.core.GameContext;
import com.FK.game.entities.Slop;
import com.FK.game.entities.Player;
import com.FK.game.entities.Slop;
import com.FK.game.states.EntityState;
import com.FK.game.network.StateMessage;

public class SlopAttackState implements EntityState<Slop> {
      private float attackTimer;
    private static final float ATTACK_DURATION = 0.913f;

    @Override
    public void enter(Slop slop) {

        slop.setBoundSize(106, 128);
        attackTimer = 0f;
        slop.setAnimation(slop.isMovingRight() == true ? (EnemyAnimationType.SLOP_ATTACK) : (EnemyAnimationType.SLOP_ATTACK_LEFT));
        slop.getVelocity().x = 0;
        slop.getDamageBox().set(slop.getX(), slop.getY(), slop.getWidth(), slop.getHeight());
        slop.setCanAttack(false);
    }

    @Override
    public void update(Slop slop, float delta) {
        if(slop.getCurrentAnimation() != null) {
        slop.getCurrentAnimation().update(delta);
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
    public void render(Slop slop, Batch batch) {
        
        if (slop.getCurrentAnimation() != null && slop.getCurrentAnimation().getCurrentFrame() != null) {
            batch.draw(slop.getCurrentAnimation().getCurrentFrame(),
                slop.getX(), slop.getY(),
                slop.getWidth(), slop.getHeight());
        }
    }

    @Override
    public void exit(Slop slop) {

        slop.setBoundSize(106, 75);
        slop.getDamageBox().set(0, 0, 0, 0);
    }

    @Override
    public StateMessage getNetworkState() {
        return StateMessage.SLOP_ATTACKING;
    }

    @Override
    public void handleInput(Slop slop) {}
}
