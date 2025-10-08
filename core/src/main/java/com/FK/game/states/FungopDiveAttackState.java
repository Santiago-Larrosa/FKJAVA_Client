

package com.FK.game.states;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.FK.game.animations.AnimationHandler;
import com.FK.game.animations.EnemyAnimationType;
import com.FK.game.core.GameContext;
import com.FK.game.entities.Enemy;
import com.FK.game.entities.Player;
import com.FK.game.screens.GameScreen;

public class FungopDiveAttackState implements EntityState<Enemy> {

    private final float ATTACK_SPEED = 900f;
    private final float PASS_ANIMATION_DURATION = 0.3f; 
    private final float TILT_ANGLE_FLYING = 15f; 
    private Vector2 trajectory = new Vector2();
    private float passTimer = 0f;
    private boolean passAnimationFinished = false;
    private float startAngle;
    private float targetAngle;
    private float currentRotation;
    Player player = GameContext.getPlayer();

    @Override
    public void enter(Enemy fungo) {
        fungo.setCanAttack(false);
        passTimer = 0f;
        passAnimationFinished = false;

        fungo.setCurrentAnimation(EnemyAnimationType.FUNGOP_PASS_ATTACK);
        fungo.getCurrentAnimation().reset();
        fungo.setHasWallAhead(false);
        fungo.setOnPlatform(false);
        this.startAngle = fungo.isMovingRight() ? -TILT_ANGLE_FLYING : TILT_ANGLE_FLYING;
        this.currentRotation = this.startAngle; 
        if (player != null) {
            Vector2 fungoCenter = new Vector2(fungo.getCollisionBox().x + fungo.getCollisionBox().width / 2, fungo.getCollisionBox().y + fungo.getCollisionBox().height / 2);
            Vector2 playerCenter = new Vector2(player.getCollisionBox().x + player.getCollisionBox().width / 2, player.getCollisionBox().y + player.getCollisionBox().height / 2);
            trajectory.set(playerCenter).sub(fungoCenter).nor();
        } else {
            trajectory.set(0, -1);
        }
        this.targetAngle = trajectory.angleDeg() + 90;

        fungo.getDamageBox().set(fungo.getX(), fungo.getY(), 120, 150);
    }

    @Override
    public void update(Enemy fungo, float delta) {
        fungo.getCurrentAnimation().update(delta);

        if (!passAnimationFinished) {
            passTimer += delta;
            float progress = Math.min(1f, passTimer / PASS_ANIMATION_DURATION);
            float diff = targetAngle - startAngle;
            if (diff > 180) diff -= 360;
            if (diff < -180) diff += 360;
            
            currentRotation = startAngle + diff * progress;

            if (passTimer >= PASS_ANIMATION_DURATION) {
                passAnimationFinished = true;
                currentRotation = targetAngle; 
                fungo.setCurrentAnimation(EnemyAnimationType.FUNGOP_ATTACK);
            }
        } else {
            fungo.getVelocity().set(trajectory).scl(ATTACK_SPEED);
            fungo.getBounds().x += fungo.getVelocity().x * delta;
            fungo.getBounds().y += fungo.getVelocity().y * delta;
            fungo.getCollisionBox().setPosition(
                fungo.getBounds().x + fungo.getCollisionBoxOffsetX(),
                fungo.getBounds().y + fungo.getCollisionBoxOffsetY()
            );

            if (fungo.isOnPlataform() || fungo.hasWallAhead()) {
                GameContext.getScreen().createImpactEffect(
                    fungo.getCollisionBox().x + fungo.getCollisionBox().width / 2,
                    fungo.getCollisionBox().y
                );
                fungo.getStateMachine().changeState(new FungoFlyingState());
            }else {
                fungo.getDamageBox().set(fungo.getX(), fungo.getY(), 120, 150);
            }
        }
        
    }

    @Override
    public void render(Enemy fungo, Batch batch) {
        TextureRegion frame = fungo.getCurrentAnimation().getCurrentFrame();
        batch.draw(frame, fungo.getX(), fungo.getY(), fungo.getWidth() / 2f, fungo.getHeight() / 2f,
                   fungo.getWidth(), fungo.getHeight(), 1f, 1f, currentRotation);
    }
    
    @Override public void exit(Enemy fungo) {
        ((GameScreen) player.getGame().getScreen()).shakeCamera(1f, 20f);
        fungo.getVelocity().set(0, 0);
        fungo.getDamageBox().set(0, 0, 0, 0);
    }
    @Override public void handleInput(Enemy entity) {}
}