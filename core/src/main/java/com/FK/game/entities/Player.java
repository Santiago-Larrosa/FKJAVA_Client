package com.FK.game.entities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Color;  
import com.badlogic.gdx.utils.Array;
import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.FK.game.entities.*;
import com.FK.game.screens.*;
import com.FK.game.states.*;


public class Player extends Entity<Player> {

    private static final float WIDTH = 150;
    private static final float HEIGHT = 110;
    public static final float WALK_SPEED = 500f;
    public static final float GRAVITY = -600;
    public static final float JUMP_VELOCITY = 10450;
    public static final float FLOOR_Y = 100f; 
    private boolean chargingJump = false;
    private final Texture texture = Assets.playerIdle;
    private final Texture pass = Assets.playerPass;
    private final Texture passLeft = Assets.playerPassLeft;
    private boolean isAttacking = false;
    private float attackTimeLeft = 0f;
    public static final float ATTACK_DURATION = 0.332f; 
    private EntityState<Player> currentState;
    private PlayerAnimationType currentType;
    private MainGame game;
    private FireAttackHUD fireAttackHUD;
    private float fireCooldown = 0f;
    private static final float FIRE_ATTACK_COOLDOWN = 5f;

    public Player(MainGame game) { 
        super(2000, FLOOR_Y, WIDTH, HEIGHT, 100, 100); 
        setHealth(5);
        
        this.game = game;
        setDamage(3);
        setCollisionBoxOffset(10f, 0f);
        TextureLoader loader = new BasicTextureLoader(); 
        AnimationCache cache = AnimationCache.getInstance();
        this.animations = new AnimationHandler[PlayerAnimationType.values().length];
        for (PlayerAnimationType type : PlayerAnimationType.values()) {
            animations[type.ordinal()] = cache.createAnimation(type);
        }

        this.stateMachine = new EntityStateMachine<>(this, new IdleState());
        this.currentState = new IdleState();
        this.currentState.enter(this);
    }

   
    @Override
    public void update(float delta) {
        if (!movementLocked) {
            stateMachine.update(delta);
        }
        super.update(delta); 
        debugPlatformDetection();
    }


    public void render(Batch batch) {
    if (currentAnimation != null) {
        TextureRegion frame = currentAnimation.getCurrentFrame();
        batch.draw(frame, bounds.x, bounds.y, bounds.width, bounds.height);
    }
}


    public void setState(EntityState<Player> newState) {
        currentState.exit(this);
        this.currentState = newState;
        newState.enter(this);
    }

     public EntityStateMachine<Player> getStateMachine() {
        return stateMachine;
    }



    public boolean isAttackReady() {
        return this.fireAttackHUD.isAttackReady();
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

    public void startFireAttackCooldown() {
        this.fireCooldown = FIRE_ATTACK_COOLDOWN;
        fireAttackHUD.resetCooldown(); 
    }

    public void receiveDamage(Entity source) {
        float centerTarget = this.getX() + this.getWidth() / 2f;
        float centerSource = source.getX() + source.getWidth() / 2f;
        float knockbackX = (centerTarget > centerSource) ? KNOCKBACK_FORCE_X : -KNOCKBACK_FORCE_X;

        this.velocity.x = knockbackX;
        this.velocity.y = KNOCKBACK_FORCE_Y;

        this.getStateMachine().changeState(new DamageState(source));
    }

    public PlayerAnimationType getCurrentAnimationType() {
        return currentType;
    }

    public void setCurrentAnimation(PlayerAnimationType type) {
        if (type == null || type.ordinal() >= animations.length) {
            throw new IllegalArgumentException("Tipo de animación inválido");
        }
        this.currentAnimation = animations[type.ordinal()];
        if (currentAnimation == null) {
            throw new IllegalStateException("Animación no cargada para: " + type);
        }
    }

    

    public MainGame getGame() {
        return game;
    }
    @Override
    public float getGravity() {
        return GRAVITY;
    }



    @Override
    public void dispose() {
        for (AnimationHandler animation : animations) {
            if (animation != null) animation.dispose();
        }
        texture.dispose();
    }

    public void setX(float x) {
        this.bounds.x = x;
    }
    
    public void setY(float y) {
        this.bounds.y = y;
    }

    public void setCollisionX(float x) {
        this.collisionBox.x = x;
    }
    
    public void setCollisionY(float y) {
        this.collisionBox.y = y;
    }

    public boolean isOnSolidGround() {
        Rectangle sensor = new Rectangle(
            collisionBox.x,
            collisionBox.y - 2f,   
            collisionBox.width,
            4f
        );

        for (Rectangle platform : collisionObjects) {
            if (sensor.overlaps(platform)) {
                return true;
            }
        }

        return false;
    }




    public Rectangle getDamageBox() {
        return DamageBox;
    }
    public void setFireAttackHUD(FireAttackHUD hud) {
        this.fireAttackHUD = hud;
    }

    public FireAttackHUD getFireAttackHUD() {
        return fireAttackHUD;
    }

    @Override 
    public String toString () {
        return "Player";
    }
}