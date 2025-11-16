package com.FK.game.states;

import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.FK.game.entities.Bolb;
import com.FK.game.entities.Player;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.FK.game.network.StateMessage;


public class BolbWalkState implements EntityState<Bolb> {

    private float waitTimer = 0f;
    private boolean waitingToTurn = false;
    private final float waitDuration = 0.5f;
    private boolean edgeDetected = false;
    private int[] airConfirmationCount = new int[1];
    private final Vector2 bolbPos = new Vector2();
    private final Vector2 playerPos = new Vector2();


    @Override
    public void enter(Bolb bolb) {
        
        bolb.setAnimation(bolb.isMovingRight() ? EnemyAnimationType.BOLB : EnemyAnimationType.BOLB_LEFT);
    }

    @Override
    public void update(Bolb bolb, float delta) {
        
        bolb.getCurrentAnimation().update(delta);
        bolb.setAnimation(bolb.isMovingRight() ? EnemyAnimationType.BOLB : EnemyAnimationType.BOLB_LEFT);        
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
    public void handleInput(Bolb bolb) {}

    @Override
    public void exit(Bolb bolb) {}



    @Override
    public StateMessage getNetworkState() {
        return StateMessage.BOLB_WALKING;
    }
}
