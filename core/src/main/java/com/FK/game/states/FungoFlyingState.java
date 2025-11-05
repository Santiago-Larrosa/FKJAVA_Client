package com.FK.game.states;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.FK.game.animations.AnimationHandler;
import com.badlogic.gdx.math.Rectangle;
import com.FK.game.animations.EnemyAnimationType;
import com.FK.game.core.GameContext;
import com.FK.game.entities.Enemy;
import com.FK.game.entities.Player;
import com.FK.game.sounds.*;
import com.FK.game.network.StateMessage;

public class FungoFlyingState implements EntityState<Enemy> {

    private final float HORIZONTAL_SPEED = 70f;
    private final float FLAP_FORCE = 200f;
    private final float TILT_ANGLE = 15f;
    private final float PLAYER_DETECTION_RANGE_X = 50f;
    private final float ATTACK_DETECTION_WIDTH = 150f;
    private final float ATTACK_DETECTION_HEIGHT = 400f;
    private boolean waitingToTurn = false;
    private float waitTimer = 0f;
    private final float TURN_DELAY = 0.2f; 

    private boolean isMovingRight;
    private int lastFrameIndex = -1;

    @Override
    public void enter(Enemy fungo) {
        fungo.setCurrentAnimation(EnemyAnimationType.FUNGOP);
        SoundCache.getInstance().startSpatialLoop(SoundType.FLAP, fungo);
        this.isMovingRight = true;
        fungo.getVelocity().x = HORIZONTAL_SPEED;
        this.lastFrameIndex = -1;
        this.waitingToTurn = false;
        this.waitTimer = 0;
    }

    @Override
    public void update(Enemy fungo, float delta) {
        AnimationHandler animation = fungo.getCurrentAnimation();
        int currentFrameIndex = animation.getCurrentFrameIndex();
        fungo.getVelocity().y += fungo.getGravity() * delta;
        if (currentFrameIndex < lastFrameIndex) {
            fungo.getVelocity().y += FLAP_FORCE;
        }
        this.lastFrameIndex = currentFrameIndex;
        
        if (waitingToTurn) {
            waitTimer += delta;
            fungo.getVelocity().x = 0;
            if (waitTimer >= TURN_DELAY) {
                waitingToTurn = false;
                waitTimer = 0f;
                isMovingRight = !isMovingRight; 
                fungo.setHasWallAhead(false); 
            }
        } else {
            fungo.getVelocity().x = isMovingRight ? HORIZONTAL_SPEED : -HORIZONTAL_SPEED;
            if (fungo.hasWallAhead()) {
                waitingToTurn = true;
            }
        }
        fungo.setPosition(fungo.getX() + fungo.getVelocity().x * delta, fungo.getY() + fungo.getVelocity().y * delta);
        animation.update(delta);
        if (fungo.isPlayerInRange() && fungo.canAttack()) {
        fungo.getStateMachine().changeState(new FungopDiveAttackState());
    }
    }

    @Override
    public void render(Enemy fungo, Batch batch) {
        TextureRegion frame = fungo.getCurrentAnimation().getCurrentFrame();
        float rotation = isMovingRight ? -TILT_ANGLE : TILT_ANGLE;
        batch.draw(frame, fungo.getX(), fungo.getY(), fungo.getWidth() / 2f, fungo.getHeight() / 2f,
                   fungo.getWidth(), fungo.getHeight(), 1f, 1f, rotation);
    }

    @Override
    public StateMessage getNetworkState() {
        return StateMessage.FUNGOP_FLIYING;
    }
    
    @Override public void exit(Enemy fungo) {
        SoundCache.getInstance().stopSpatialLoop(SoundType.FLAP);
    }
    @Override public void handleInput(Enemy entity) {}
}