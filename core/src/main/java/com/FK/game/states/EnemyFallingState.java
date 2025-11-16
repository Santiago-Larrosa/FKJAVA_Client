package com.FK.game.states;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.FK.game.entities.Enemy;
import com.FK.game.network.StateMessage;

public class EnemyFallingState<E extends Enemy<E>> implements EntityState<E> {  
    private int groundConfirmationCount = 0;
    private static final int REQUIRED_CONFIRMATIONS = 1;

    @Override
    public void enter(E enemy) {
        groundConfirmationCount = 0;
    }

    @Override
    public void update(E enemy, float delta) {
        
    }

    @Override
    public void handleInput(E enemy) {
    }

    @Override
    public void render(E enemy, Batch batch) {
        TextureRegion frame = enemy.getCurrentAnimation().getCurrentFrame();
        batch.draw(frame,
                enemy.getBounds().x,
                enemy.getBounds().y,
                enemy.getBounds().width,
                enemy.getBounds().height);
    }

    @Override
    public StateMessage getNetworkState() {
        return StateMessage.ENEMY_FALLING;
    }

    @Override
    public void exit(E enemy) {
    }
}
