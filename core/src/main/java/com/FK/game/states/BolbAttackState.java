package com.FK.game.states;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.FK.game.animations.EnemyAnimationType;
import com.FK.game.core.GameContext;
import com.FK.game.entities.Bolb;
import com.FK.game.entities.Player;
import com.FK.game.states.EntityState;
import com.FK.game.states.BolbWalkState;
import com.FK.game.network.StateMessage;

public class BolbAttackState implements EntityState<Bolb> {

    private float attackTimer;
    private static final float ATTACK_DURATION = 0.1f;

    @Override
    public void enter(Bolb bolb) {
        
        bolb.setAnimation(EnemyAnimationType.BOLB);
    }

    @Override
    public void update(Bolb bolb, float delta) {
    }

    @Override
    public void render(Bolb bolb, Batch batch) {
       
        if (bolb.getCurrentAnimation() != null && bolb.getCurrentAnimation().getCurrentFrame() != null) {
            batch.draw(bolb.getCurrentAnimation().getCurrentFrame(),
                bolb.getX(), bolb.getY(),
                bolb.getWidth(), bolb.getHeight());
        }
    }

    @Override
    public void exit(Bolb bolb) {
    }
    @Override
    public StateMessage getNetworkState() {
        return StateMessage.BOLB_ATTACKING;
    }
    @Override
    public void handleInput(Bolb bolb) {}
}
