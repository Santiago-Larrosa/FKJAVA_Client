package com.FK.game.states;

import com.badlogic.gdx.Gdx;
import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.FK.game.entities.Enemy;
import com.FK.game.entities.Slop;
import com.FK.game.entities.Slop;
import com.FK.game.entities.Player;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.FK.game.network.StateMessage;


public class SlopWalkState implements EntityState<Enemy> {
    
        private float waitTimer = 0f;
    private boolean waitingToTurn = false;
    private final float waitDuration = 0.5f;
    private boolean edgeDetected = false;
    private int[] airConfirmationCount = new int[1];
    private final Vector2 slopPos = new Vector2();
    private final Vector2 playerPos = new Vector2();

    @Override
    public void enter(Enemy enemy) {
        Slop slop = (Slop) enemy;
        slop.setCurrentAnimation(slop.isMovingRight() ? EnemyAnimationType.SLOP : EnemyAnimationType.SLOP_LEFT);
        airConfirmationCount[0] = 0;
    }

     @Override
    public void update(Enemy enemy, float delta) {
        Slop slop = (Slop) enemy;
        slop.getCurrentAnimation().update(delta);
        slop.setCurrentAnimation(slop.isMovingRight() ? EnemyAnimationType.SLOP : EnemyAnimationType.SLOP_LEFT);
        /*
        // --- NUEVA LÓGICA DE ATAQUE (SIMPLE Y LIMPIA) ---
        // Primero, comprobamos si debemos atacar.
        if (enemy.isPlayerInRange() && enemy.canAttack()) {
            enemy.getStateMachine().changeState(new SlopAttackState());
            return; // Si atacamos, no necesitamos movernos en este frame.
        }

        // --- LÓGICA DE MOVIMIENTO (SIN CAMBIOS, SOLO REORDENADA) ---
        if (waitingToTurn) {
            waitTimer += delta;
            if (waitTimer >= waitDuration) {
                waitingToTurn = false;
                waitTimer = 0f;
                slop.setMovingRight(!slop.isMovingRight());
                edgeDetected = true;
                
            }
            return; // Mientras espera para girar, no hace nada más.
        }

        slop.getVelocity().x = slop.isMovingRight() ? slop.getSpeed() : -slop.getSpeed();
        slop.getBounds().x += slop.getVelocity().x * delta;

        if (!waitingToTurn && (slop.hasWallAhead() || (!hasGroundAhead(slop) && !edgeDetected))) {
            waitingToTurn = true;
            waitTimer = 0f;
        }

        if (hasGroundAhead(slop)) {
            edgeDetected = false;
        }

        if (StateUtils.checkFalling(slop, delta, airConfirmationCount)) {
            return;
        }

        slop.getBounds().y += slop.getVelocity().y * delta;
        slop.getCollisionBox().setPosition(
            slop.getBounds().x + slop.getCollisionBoxOffsetX(),
            slop.getBounds().y + slop.getCollisionBoxOffsetY()
        );*/
    }

    @Override
    public void render(Enemy enemy, Batch batch) {
        Slop slop = (Slop) enemy;
        if (slop.getCurrentAnimation() != null && slop.getCurrentAnimation().getCurrentFrame() != null) {
            batch.draw(slop.getCurrentAnimation().getCurrentFrame(),
                slop.getX(), slop.getY(),
                slop.getWidth(), slop.getHeight());
        }
    }

    @Override
    public void handleInput(Enemy enemy) {}

    @Override
    public void exit(Enemy enemy) {}

    private boolean isWallAhead(Slop slop) {
      /*  float checkX = slop.isMovingRight()
            ? slop.getCollisionBox().x + slop.getCollisionBox().width + 1
            : slop.getCollisionBox().x - 1;

        float checkY = slop.getCollisionBox().y;
        float checkHeight = slop.getCollisionBox().height;

        Rectangle checkArea = new Rectangle(checkX, checkY, 1, checkHeight);

        for (Rectangle platform : slop.getCollisionObjects()) {
            if (checkArea.overlaps(platform)) return true;
        }*/

        return false;
    }

    @Override
    public StateMessage getNetworkState() {
        return StateMessage.SLOP_WALKING;
    }

    private boolean hasGroundAhead(Slop slop) {
        /*
    float checkX = slop.isMovingRight() 
        ? slop.getCollisionBox().x + slop.getCollisionBox().width + 5 
        : slop.getCollisionBox().x - 5;
    
    Rectangle checkArea = new Rectangle(
        checkX,
        slop.getCollisionBox().y - 15, 
        10, 
        15
    );
    
    for (Rectangle platform : slop.getCollisionObjects()) {
        if (checkArea.overlaps(platform)) return true;
    }*/
        return false;
        
}
}
