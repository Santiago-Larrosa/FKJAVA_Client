package com.FK.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Color;  
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.FK.game.animations.AnimationHandler;
import com.FK.game.states.*;

public abstract class Entity<T extends Entity<T>> {
    protected Rectangle bounds;
    protected Vector2 velocity;
    protected boolean readyForRemoval = false;
    protected boolean onGround = true;
    protected boolean onPlatform = false;
    protected float gravity = -600;
    protected boolean movementLocked = false;
    protected boolean movingRight = true;
    protected Rectangle collisionBox;
    protected float collisionOffsetX = 0;
    protected float collisionOffsetY = 0;
    protected float knockBackForceX = 0;
    protected float knockBackForceY = 0;
    protected int health;
    protected float knockbackTimer = 0f;
    protected EntityStateMachine<T> stateMachine;
    protected AnimationHandler currentAnimation;
    protected AnimationHandler[] animations;
    protected Rectangle DamageBox;
    protected Array<Rectangle> collisionObjects;
    protected float damage = 0;
    private boolean hasWallAhead;
    protected float targetX, targetY;
    protected float lerpSpeed = 30f;

    public Entity(float x, float y, float width, float height, float CollisionBoxWidth, float colisionBoxHeight) {
        bounds = new Rectangle(x, y, width, height);
        targetX = x;
        targetY = y;
        velocity = new Vector2();
        collisionBox = new Rectangle(x, y, CollisionBoxWidth, colisionBoxHeight);
        this.DamageBox = new Rectangle(bounds.x, bounds.y, 0, 0);
    }

    public void update(float delta) {
        applyPhysics(delta);
        bounds.x += velocity.x * delta;
        bounds.y += velocity.y * delta;

        collisionBox.setPosition(bounds.x + collisionOffsetX, bounds.y + collisionOffsetY);
        debugPlatformDetection();
    }

    public void render(Batch batch) {
        if (currentAnimation != null) {
            TextureRegion frame = currentAnimation.getCurrentFrame();
            batch.draw(frame, bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    

    protected void applyPhysics(float delta) {
        if (!onGround) {
            velocity.y += getGravity() * delta;
        } 
    }
    public boolean isMovingRight() {
        return movingRight;
    }

    public void setTargetPosition(float x, float y) {
        //System.out.println("Entidad objetivo posicionada en: (" + x + ", " + y + ")");
        this.targetX = x;
        this.targetY = y;
    }

    protected float lerp(float a, float b, float t) {
        return a + (b - a) * Math.min(t, 1f);
    }

     public void debugPlatformDetection() {      
        if (collisionObjects == null || collisionObjects.isEmpty()) {
            return;
        }

        onPlatform = false;
        
        float detectionWidth = collisionBox.width * 0.8f;
        float detectionHeight = 15f;
        float xMargin = (collisionBox.width - detectionWidth) / 2;
        
        Rectangle feetArea = new Rectangle(
            collisionBox.x + xMargin,
            collisionBox.y - detectionHeight,
            detectionWidth,
            detectionHeight
        );


        for (Rectangle platform : collisionObjects) {

            boolean xCollision = (feetArea.x + feetArea.width > platform.x) && 
                            (feetArea.x < platform.x + platform.width);
            
            boolean yCollision = (feetArea.y <= platform.y + platform.height) && 
                            (feetArea.y + feetArea.height >= platform.y);
            
            if (xCollision && yCollision) {
                onPlatform = true;
                break;
            }
        }
    }

    

    public void renderDebug(ShapeRenderer renderer) {
        renderer.setColor(Color.BLUE);
        renderer.begin(ShapeType.Line);
        renderer.rect(getCollisionBox().x, getCollisionBox().y, getCollisionBox().width, getCollisionBox().height);
        renderer.end();
    }

    public void renderDebugDamage(ShapeRenderer renderer) {
        renderer.setColor(Color.RED);
        renderer.begin(ShapeType.Line);
        renderer.rect(DamageBox.x, DamageBox.y, DamageBox.width, DamageBox.height);
        renderer.end();
    }


    public EntityStateMachine<T> getStateMachine() {
        return stateMachine;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public float getGravity() {
        return gravity;
    }

    public float setGravity(float gravity) {
        return this.gravity = gravity;
    }


    public void setOnGround(boolean value) {
        onGround = value;
    }

    public boolean isOnPlataform() {
        return onPlatform;
    }

    public void setOnPlatform(boolean value) {
        onPlatform = value;
    }

    public void setVelocityX (float velX) {
        this.velocity.x = velX;
    }

    public void setVelocityY (float velY) {
        this.velocity.y = velY;
    }

    public void setCollisionObjects(Array<Rectangle> objects) {
        this.collisionObjects = objects;
    }

    public void setDamage (float newDamage) {
        this.damage = newDamage;
    }

    public float getDamage () {
        return this.damage;
    }

    public Array<Rectangle> getCollisionObjects() {        
        return collisionObjects;
    }

    public float getKnockbackX () {
        return this.knockBackForceX;
    }

    public void setKnockbackX (float forceX) {
        this.knockBackForceX = forceX;
    }
    
    public float getKnockbackY () {
        return this.knockBackForceY;
    }

    public void setKnockbackY (float forceY) {
        this.knockBackForceY = forceY;
    }

    public Rectangle getCollisionBox() {
        return new Rectangle(getX() + collisionOffsetX, getY() + collisionOffsetY, collisionBox.width, collisionBox.height);
    }

    public boolean hasWallAhead() { return hasWallAhead; }
    public void setHasWallAhead(boolean value) { hasWallAhead = value; }

    public void setCollisionBoxOffset(float offsetX, float offsetY) {
        this.collisionOffsetX = offsetX;
        this.collisionOffsetY = offsetY;
    }

    public float getCollisionBoxOffsetX () {
        return this.collisionOffsetX;
    }

    public float getCollisionBoxOffsetY () {
        return this.collisionOffsetY;
    }

    public void setAnimation(AnimationHandler animation) {
        this.currentAnimation = animation;
    }

    public AnimationHandler getCurrentAnimation() {
        return currentAnimation;
    }

    public void setAnimations(AnimationHandler[] animations) {
        this.animations = animations;
    }

    public AnimationHandler getAnimation(int ordinal) {
        return animations[ordinal];
    }

    public void setCollisionBoxSize(float width, float height) {
        this.collisionBox.setSize(width, height);
    }

    public void setDamageSize(float width, float height) {
        DamageBox.setSize(width, height);
    }

    public void setBoundSize(float width, float height) {
        bounds.setSize(width, height);
    }

    public void setPosition(float x, float y) {
        bounds.setPosition(x, y);
        
    }

    public float getX() {
        return bounds.x;
    }
    public void decreaseHealth (float damage) {
        this.health -= damage;
         
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = Math.max(0, health);
    }

    public boolean isDead() {
        return health <= 0;
    }


    public float getY() {
        return bounds.y;
    }
    public float getCollisionWidth () {
        return this.collisionBox.width;
    }

    public float getCollisionHeight () {
        return this.collisionBox.height;
    }

    public float getWidth() {
        return bounds.width;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public float getHeight() {
        return bounds.height;
    }

    public boolean isReadyForRemoval() {
        return readyForRemoval;
    }

    public void setReadyForRemoval(boolean ready) {
        this.readyForRemoval = ready;
    }

    public Rectangle getDamageBox() {
        return DamageBox;
    }

    public Vector2 getCenter() {
        return new Vector2(
            bounds.x + bounds.width / 2,
            bounds.y + bounds.height / 2
        );
    }   

    public void dispose() {
        for (AnimationHandler animation : animations) {
            if (animation != null) animation.dispose();
        }
    }
}
