package com.FK.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.FK.game.states.*;

public abstract class Enemy extends Entity <Enemy> {
    protected static final float KNOCKBACK_FORCE_X = 300f;
    protected static final float KNOCKBACK_FORCE_Y = 500f;
    
    protected AnimationHandler[] animations;
    protected EnemyAnimationType currentAnimationType;
    protected AnimationHandler currentAnimation;
    
    protected float speed = 100f;
    protected float gravity = -900f;
    protected float knockbackTimer = 0f;
    protected boolean canAttack = true;
    protected float attackCooldownTimer = 0f;   
    
    protected EntityStateMachine<Enemy> stateMachine;
    
    public Enemy(float x, float y, float width, float height, float collisionWidth, float collisionHeight, 
                Array<Rectangle> collisionObjects) {
        super(x, y, width, height, collisionWidth, collisionHeight);
        setHealth(3);
        this.collisionObjects = collisionObjects;
        initializeAnimations();
    }
    
    public abstract EntityState<Enemy> getDefaultState();   

    private void initializeAnimations() {
    animations = new AnimationHandler[EnemyAnimationType.values().length];
    AnimationCache cache = AnimationCache.getInstance();
    
    for (EnemyAnimationType type : EnemyAnimationType.values()) {
        animations[type.ordinal()] = cache.createAnimation(type);
    }
}
    
    @Override
public void update(float delta) {

    stateMachine.update(delta);
}



   

    @Override
    public void render(Batch batch) {
        stateMachine.render(batch);
    }

    public void receiveDamage(Entity source) {
        if (stateMachine.getCurrentState() instanceof EnemyDamageState) return;
        
        float centerTarget = this.getX() + this.getWidth() / 2f;
        float centerSource = source.getX() + source.getWidth() / 2f;
        float knockbackX = (centerTarget > centerSource) ? KNOCKBACK_FORCE_X : -KNOCKBACK_FORCE_X;
        
        this.velocity.x = knockbackX;
        this.velocity.y = KNOCKBACK_FORCE_Y;
        
        this.getStateMachine().changeState(createDamageState(source));
    }
    
    protected abstract EnemyDamageState createDamageState(Entity source);
    
    public void setCurrentAnimation(EnemyAnimationType type) {
        if (type == null || animations == null || type.ordinal() >= animations.length) return;
        
        AnimationHandler newAnimation = animations[type.ordinal()];
        if (newAnimation != null && newAnimation != currentAnimation) {
            currentAnimationType = type;
            currentAnimation = newAnimation;
            currentAnimation.reset();
        }
    }
    
  
    public AnimationHandler getCurrentAnimation() {
        return currentAnimation;
    }
    
    public EnemyAnimationType getCurrentAnimationType() {
        return currentAnimationType;
    }
    
    public float getSpeed() {
        return speed;
    }
    
    public float getGravity() {
        return gravity;
    }
    
    public float getKnockbackTimer() {
        return knockbackTimer;
    }
    
    public void setKnockbackTimer(float knockbackTimer) {
        this.knockbackTimer = knockbackTimer;
    }
    
    public Vector2 getVelocity() {
        return velocity;
    }
    
    public boolean isOnSolidGround() {
        for (Rectangle platform : collisionObjects) {
            if (collisionBox.overlaps(platform)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean canAttack() {
        return canAttack;
    }
    
    public void setCanAttack(boolean value) {
        this.canAttack = value;
    }
    
    public void updateAttackCooldown(float delta) {
        if (!canAttack) {
            attackCooldownTimer += delta;
            if (attackCooldownTimer >= 5f) {
                canAttack = true;
                attackCooldownTimer = 0f;
            }
        }
    }
    
    public EntityStateMachine<Enemy> getStateMachine() {
        return stateMachine;
    }
    
    
    public void setAnimation(EnemyAnimationType type) {
        this.currentAnimation = animations[type.ordinal()];
    }
protected void spawnOnRandomPlatform() {
    if (collisionObjects.size == 0) return;

    boolean valid = false;
    int attempts = 0;
    int maxAttempts = 50; 
    final int triesPerPlatform = 3; 

    do {
        Rectangle platform = collisionObjects.random();

        if (platform.width >= getWidth() * 1.1f) {
            for (int i = 0; i < triesPerPlatform; i++) {
                float margin = getWidth() * 0.1f;
                float minX = platform.x + margin;
                float maxX = platform.x + platform.width - getWidth() - margin;

                if (maxX <= minX) {
                    minX = platform.x;
                    maxX = platform.x + platform.width - getWidth();
                }

                float x = MathUtils.random(minX, maxX);
                float y = platform.y + platform.height;

          
                Rectangle enemyBounds = new Rectangle(x, y, getWidth(), getHeight());

    
                boolean overlapsPlatform = false;
                for (Rectangle obj : collisionObjects) {
                    if (obj != platform && enemyBounds.overlaps(obj)) {
                        overlapsPlatform = true;
                        break;
                    }
                }

                if (!overlapsPlatform) {
                    setPosition(x, y);
                    valid = true;
                    break; 
                }
            }
        }

        attempts++;
    } while (!valid && attempts < maxAttempts);

    if (!valid) {
        Gdx.app.log("DEBUG", "No se encontró plataforma adecuada para spawn");
    }
}
@Override
    public String toString() {
        return "Enemy";
    }

}