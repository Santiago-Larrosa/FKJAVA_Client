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
import com.FK.game.sounds.*;
import com.FK.game.core.MainGame;
import com.FK.game.network.*;
import com.FK.game.core.PlayerData;



public class Player extends CharacterEntity<Player> {

    private static final float WIDTH = 150;
    private static final float HEIGHT = 110;
    public static final float WALK_SPEED = 500f;
    public static final float JUMP_VELOCITY = 10450;
    public static final float FLOOR_Y = 100f; 
    public float damageAmplifier = 1.0f;
    private float maxHealth = 5f;
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
    private InputHandler inputHandler;
    private static final float FIRE_ATTACK_COOLDOWN = 5f;
    private float currentFireCooldown = 0f;
    private boolean isFireCharged = false;
    private final PlayerData playerData;



    public Player(MainGame game, PlayerData playerData) { 
        super(2000, FLOOR_Y, WIDTH, HEIGHT, 100, 100); 
        setHealth(5);
        this.playerData = playerData;  
        this.game = game;
        this.fireCooldown = FIRE_ATTACK_COOLDOWN;
        setDamage(3);
        setKnockbackX(300f);
        setKnockbackY(400f);
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
        applyPlayerData();
    }
   
    @Override
    public void update(float delta) {
        if (!movementLocked) {
            stateMachine.update(delta);
        }
        float newX = lerp(bounds.x, targetX, delta * lerpSpeed);
        float newY = lerp(bounds.y, targetY, delta * lerpSpeed);
        bounds.setPosition(newX, newY);
        collisionBox.setPosition(bounds.x + collisionOffsetX, bounds.y + collisionOffsetY);
        debugPlatformDetection();
    }
    @Override
    public AnimationType getDeathAnimationType() {
        return PlayerAnimationType.SMOKE;
    }


    public void render(Batch batch) {
    if (currentAnimation != null) {
        TextureRegion frame = currentAnimation.getCurrentFrame();
        batch.draw(frame, bounds.x, bounds.y, bounds.width, bounds.height);
    }
}

    @Override
    public AnimationType getDamageAnimationType() {
        return isMovingRight() ? PlayerAnimationType.FALLING_RIGHT : PlayerAnimationType.FALLING_LEFT;
    }


    public void setState(EntityState<Player> newState) {
        currentState.exit(this);
        this.currentState = newState;
        newState.enter(this);
    }

     public EntityStateMachine<Player> getStateMachine() {
        return stateMachine;
    }

 

public void updateFireCooldown(float delta) {
        if (fireCooldown > 0) {
            fireCooldown -= delta;
            if (fireCooldown < 0) {
                fireCooldown = 0;
                isFireCharged = true;
            }
            
            
        }
        fireAttackHUD.updateFire(delta, isFireCharged);
    }

    public void setIsFireCharged(boolean charged) {
        this.isFireCharged = charged;
        if (isFireCharged == false) {
            this.fireCooldown = FIRE_ATTACK_COOLDOWN;
        }
    }

    public boolean isAttackReady() {
        return this.fireAttackHUD.isAttackReady();
    }
    
   @Override
    public EntityState<Player> getDefaultState() {
        return new IdleState();
    }

    public void startFireAttackCooldown() {
        this.fireCooldown = FIRE_ATTACK_COOLDOWN;
        this.isFireCharged = false;
    }


    public PlayerAnimationType getCurrentAnimationType() {
        return currentType;
    }

    @Override
public void setCurrentAnimation(AnimationType animType) {
        PlayerAnimationType type = (PlayerAnimationType) animType;
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

    public InputHandler getInputHandler() {
        return null;
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
    public void setDamage (float newDamage) {
        this.damage = newDamage * this.damageAmplifier;
    }

    @Override 
    public String toString () {
        return "Player";
    }


    public void applyPlayerData() {
        this.maxHealth = playerData.getMaxHealth();
        this.health = playerData.currentHealth;
        this.damage = playerData.getAttackDamage();
    }

    public void updatePlayerData() {
        playerData.currentHealth = this.health;
    }

    public void addCoins(int amount) {
        playerData.coinCount += amount;
    }



    public int getCoinCount() {
        return playerData.coinCount;
    }

    // --- AÑADE ESTE CÓDIGO A TU CLASE Player.java ---

// (Asegúrate de que los enums StateMessage y FacingDirection (o como los llames)
// estén disponibles en el 'core' para que esta clase pueda usarlos)

/**
 * Recibe el estado "real" desde el servidor y fuerza a la máquina de estados
 * VISUAL del cliente a sincronizarse, cambiando la animación.
 */
public void setVisualStateFromServer(String networkState, String networkFacing) {
    
    // 1. Convertir los strings de red a Enums
    StateMessage newState;
    FacingDirection newFacing;

    try {
        // valueOf() convierte un string (ej: "PLAYER_IDLE") en el enum (StateMessage.PLAYER_IDLE)
        newState = StateMessage.valueOf(networkState);
        newFacing = FacingDirection.valueOf(networkFacing); // Asumiendo que tienes un enum para esto
    } catch (IllegalArgumentException e) {
        // El servidor envió un estado/dirección que no existe en nuestro enum
        System.err.println("Estado o dirección de red desconocido: " + networkState + ", " + networkFacing);
        return;
    }

    // 2. Actualizar la dirección (para saber si voltear el sprite)
    // (Ajusta 'isMovingRight' al nombre de tu variable interna para la dirección)
    this.movingRight = (newFacing == FacingDirection.RIGHT);

    // 3. Obtener el estado visual actual
    // (Usamos el método que creamos en el 'core' para esto)
    StateMessage currentStateEnum = stateMachine.getCurrentState().getNetworkState();
    
    // 4. Si el estado ya es el correcto, no hacer nada.
    // (Esto evita reiniciar la animación en cada paquete)
    if (currentStateEnum == newState) {
        return; 
    }

    // 5. El estado es DIFERENTE. Forzar el cambio de estado.
    // (Esto llamará al método 'enter()' del nuevo estado, que
    // es donde se debe configurar la nueva animación).
    switch (newState) {
        case PLAYER_IDLE:
            stateMachine.changeState(new IdleState());
            break;
        case PLAYER_WALKING:
            stateMachine.changeState(new WalkingState());
            break;
        case PLAYER_JUMPING:
            stateMachine.changeState(new JumpingState());
            break;
        case PLAYER_ATTACKING:
            stateMachine.changeState(new AttackingState());
            break;
        case PLAYER_CHARGING_JUMP:
            stateMachine.changeState(new ChargingJumpState());
            break;
        case PLAYER_FIRING:
            stateMachine.changeState(new FireAttackState());
            break;
        case PLAYER_FALLING:
            stateMachine.changeState(new FallingState());
            break;
        case PLAYER_FALLING_AND_ATTACKING:
            stateMachine.changeState(new FallingAttackState());
            break;  
    }
}
}