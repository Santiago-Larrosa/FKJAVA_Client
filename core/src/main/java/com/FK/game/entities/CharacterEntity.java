// Crea un nuevo archivo: CharacterEntity.java
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
import com.FK.game.animations.*;

public abstract class CharacterEntity<T extends CharacterEntity<T>> extends Entity<T> {
    
    protected int maxHealth = 0;
    protected float damageCooldownTime = 0f;
    protected static final float DAMAGE_COOLDOWN_TIME = 0.5f;
    
    private Vector2 lastKnockback;

    public CharacterEntity(float x, float y, float width, float height, float collisionWidth, float collisionHeight) {
    super(x, y, width, height, collisionWidth, collisionHeight);
    this.lastKnockback = new Vector2();
    this.stateMachine = new EntityStateMachine<>((T) this);

}

    public abstract AnimationType getDeathAnimationType();
    public abstract void setCurrentAnimation(AnimationType animType);
    public abstract AnimationType getDamageAnimationType();



public void initStateMachine() {
    this.stateMachine.changeState(getDefaultState());
}

public void receiveDamage(Entity source) {
    if (!(source instanceof CharacterEntity)) return;
    if (damageCooldownTime > 0) return;
    CharacterEntity attacker = (CharacterEntity) source;
    Gdx.app.log("DAMAGE_SYSTEM", 
        "ATACANTE: " + attacker.getClass().getSimpleName() + " (Daño: " + attacker.getDamage() + ")" +
        " | OBJETIVO: " + this.getClass().getSimpleName() + " (Vida antes: " + this.health + ")");
    
    if (isDead()) return;

    this.health -= attacker.getDamage();

    float direction = attacker.isMovingRight() ? 1f : -1f;
    this.lastKnockback.set(attacker.getKnockbackX() * direction, attacker.getKnockbackY());

    if (isDead()) {
        stateMachine.changeState(new DeathState());
    } else {
        stateMachine.changeState(new DamageState());
    }
}

 public void updateDamageCooldown(float delta) {
    if (damageCooldownTime > 0) {
        damageCooldownTime -= delta;
        if (damageCooldownTime < 0) damageCooldownTime = 0;
    }
}

   

    public Vector2 getLastKnockback() {
        return lastKnockback;
    }
    
    public abstract EntityState<T> getDefaultState();
}