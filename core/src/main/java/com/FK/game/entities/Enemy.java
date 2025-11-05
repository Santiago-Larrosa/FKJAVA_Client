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

public abstract class Enemy extends CharacterEntity<Enemy> {
    protected AnimationHandler[] animations;
    protected EnemyAnimationType currentAnimationType;
    protected AnimationHandler currentAnimation;
    protected float speed = 100f;
    protected float knockbackTimer = 0f;
    protected int coinValue = 1;
    protected boolean canAttack = true;
    protected float attackCooldownTimer = 0f;   
    protected float attackRange = 0f; 
    protected boolean isPlayerInRange = false;
    
    public Enemy(float x, float y, float width, float height, float collisionWidth, float collisionHeight, 
                Array<Rectangle> collisionObjects) {
        super(x, y, width, height, collisionWidth, collisionHeight);
        setHealth(3);
        
        this.collisionObjects = collisionObjects;
        initializeAnimations();
    }


    @Override
    public AnimationType getDeathAnimationType() {
        return EnemyAnimationType.SMOKE; 
    }

        private void initializeAnimations() {
        animations = new AnimationHandler[EnemyAnimationType.values().length];
        AnimationCache cache = AnimationCache.getInstance();
        
        for (EnemyAnimationType type : EnemyAnimationType.values()) {
            animations[type.ordinal()] = cache.createAnimation(type);
        }
    }
    
    @Override
    public void update(float delta) {
        updatePlayerDetection(); 
        stateMachine.update(delta);
    }

    protected abstract void updatePlayerDetection();

    public boolean isPlayerInRange() {
        return isPlayerInRange;
    }

    @Override
    public void render(Batch batch) {
        stateMachine.render(batch);
    }
    
    

    @Override
    public EntityState<Enemy> getDefaultState() {
        return new BolbWalkState();
    }

  @Override
    public void setCurrentAnimation(AnimationType animType) {
        EnemyAnimationType type = (EnemyAnimationType) animType;
        if (type == null || type.ordinal() >= animations.length) {
                throw new IllegalArgumentException("Tipo de animación inválido");
            }
            this.currentAnimation = animations[type.ordinal()];
            if (currentAnimation == null) {
                throw new IllegalStateException("Animación no cargada para: " + type);
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

public int getCoinValue() {
        return coinValue;
    }
    
@Override
    public String toString() {
        return "Enemy";
    }

}