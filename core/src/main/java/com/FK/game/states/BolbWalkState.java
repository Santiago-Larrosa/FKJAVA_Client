package com.FK.game.states;

import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.FK.game.entities.Enemy;
import com.FK.game.entities.Bolb;
import com.FK.game.entities.Player;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.FK.game.network.StateMessage;


public class BolbWalkState implements EntityState<Enemy> {

    private float waitTimer = 0f;
    private boolean waitingToTurn = false;
    private final float waitDuration = 0.5f;
    private boolean edgeDetected = false;
    private int[] airConfirmationCount = new int[1];
    private final Vector2 bolbPos = new Vector2();
    private final Vector2 playerPos = new Vector2();


    @Override
    public void enter(Enemy enemy) {
        Bolb bolb = (Bolb) enemy;
        bolb.setAnimation(bolb.isMovingRight() ? EnemyAnimationType.BOLB : EnemyAnimationType.BOLB_LEFT);
        airConfirmationCount[0] = 0;
    }

    @Override
    public void update(Enemy enemy, float delta) {
        Bolb bolb = (Bolb) enemy;
        bolb.getCurrentAnimation().update(delta);

        if (waitingToTurn) {
            waitTimer += delta;
            if (waitTimer >= waitDuration) {
                waitingToTurn = false;
                waitTimer = 0f;
                bolb.setMovingRight(!bolb.isMovingRight());
                edgeDetected = true;

                bolb.setAnimation(bolb.isMovingRight() ? EnemyAnimationType.BOLB : EnemyAnimationType.BOLB_LEFT);
            }
            return;
        }

        bolb.getVelocity().x = bolb.isMovingRight() ? bolb.getSpeed() : -bolb.getSpeed();
        bolb.getBounds().x += bolb.getVelocity().x * delta;

        if (!waitingToTurn && (bolb.hasWallAhead() || ( !hasGroundAhead(bolb) && !edgeDetected ))) {
                waitingToTurn = true;
                waitTimer = 0f; 
            }

        if (hasGroundAhead(bolb)) {
            edgeDetected = false;
        }

        if (bolb.getKnockbackTimer() > 0f) {
            bolb.setKnockbackTimer(bolb.getKnockbackTimer() - delta);
        } else {
            if (StateUtils.checkFalling(bolb, delta, airConfirmationCount)) {
            return;
        }
        }

        bolb.getBounds().y += bolb.getVelocity().y * delta;
        bolb.getCollisionBox().setPosition(
            bolb.getBounds().x + bolb.getCollisionBoxOffsetX(),
            bolb.getBounds().y + bolb.getCollisionBoxOffsetY()
        );

        if (enemy.isPlayerInRange() && enemy.canAttack()) {
            enemy.getStateMachine().changeState(new BolbAttackState());
            return; // Cambiamos de estado, no necesitamos hacer más nada aquí
        }


        
    }

    @Override
    public void render(Enemy enemy, Batch batch) {
        Bolb bolb = (Bolb) enemy;
        if (bolb.getCurrentAnimation() != null && bolb.getCurrentAnimation().getCurrentFrame() != null) {
            batch.draw(bolb.getCurrentAnimation().getCurrentFrame(),
                bolb.getX(), bolb.getY(),
                bolb.getWidth(), bolb.getHeight());
        }
    }

    @Override
    public void handleInput(Enemy enemy) {}

    @Override
    public void exit(Enemy enemy) {}

    private boolean isWallAhead(Bolb bolb) {
        float checkX = bolb.isMovingRight()
            ? bolb.getCollisionBox().x + bolb.getCollisionBox().width + 1
            : bolb.getCollisionBox().x - 1;

        float checkY = bolb.getCollisionBox().y;
        float checkHeight = bolb.getCollisionBox().height;

        Rectangle checkArea = new Rectangle(checkX, checkY, 1, checkHeight);

        for (Rectangle platform : bolb.getCollisionObjects()) {
            if (checkArea.overlaps(platform)) return true;
        }

        return false;
    }


    private boolean hasGroundAhead(Bolb bolb) {
    float checkX = bolb.isMovingRight() 
        ? bolb.getCollisionBox().x + bolb.getCollisionBox().width + 5 
        : bolb.getCollisionBox().x - 5;
    
    Rectangle checkArea = new Rectangle(
        checkX,
        bolb.getCollisionBox().y - 15, 
        10, 
        15
    );
    
    for (Rectangle platform : bolb.getCollisionObjects()) {
        if (checkArea.overlaps(platform)) return true;
    }
    return false;
}

    @Override
    public StateMessage getNetworkState() {
        return StateMessage.BOLB_WALKING;
    }
}
