package com.FK.game.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import java.util.List;
import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.FK.game.entities.*;
import com.FK.game.screens.*;
import com.FK.game.states.*;
import com.FK.game.sounds.*;
import com.FK.game.network.*;

public class Fire extends Entity <Fire>{
    protected AnimationHandler[] animations;
    protected ObjectsAnimationType currentAnimationType;

    public Fire(float x, float y) {
        super(x, y, 400, 400f, 200f, 200f);
        setDamageSize(150, 150);
        setDamage(0);
        this.entityType = EntityTypeMessage.FIRE;
        setKnockbackX(100f);
        setKnockbackY(200f);
        initializeAnimations();
        setCurrentAnimation(ObjectsAnimationType.PORTAL_RISING);
        this.stateMachine = new EntityStateMachine<>(this, new FireBasicState());
    }

    public EntityStateMachine<Fire> getStateMachine() {
        return stateMachine;
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

     public void setAnimation(ObjectsAnimationType type) {
        this.currentAnimation = animations[type.ordinal()];
    }
    @Override
    public float getGravity() {
        return 0f;
    }

    public void setCurrentAnimation(ObjectsAnimationType type) {
        if (type == null || animations == null || type.ordinal() >= animations.length) return;
        
        AnimationHandler newAnimation = animations[type.ordinal()];
        if (newAnimation != null && newAnimation != currentAnimation) {
            currentAnimationType = type;
            currentAnimation = newAnimation;
            currentAnimation.reset();
        }
    }

    private void initializeAnimations() {
        animations = new AnimationHandler[ObjectsAnimationType.values().length];
        AnimationCache cache = AnimationCache.getInstance();
        
        for (ObjectsAnimationType type : ObjectsAnimationType.values()) {
            animations[type.ordinal()] = cache.createAnimation(type);
        }
    }

    @Override
    public void render(com.badlogic.gdx.graphics.g2d.Batch batch) {
        stateMachine.render(batch);
    }

        @Override
public void setVisualStateFromServer(String networkState, String networkFacing) {
    
   
    StateMessage newState;
    FacingDirection newFacing;

    try {
        
        newState = StateMessage.valueOf(networkState);
        newFacing = FacingDirection.valueOf(networkFacing); 
    } catch (IllegalArgumentException e) {
        System.err.println("Estado o dirección de red desconocido: " + networkState + ", " + networkFacing);
        return;
    }

    this.movingRight = (newFacing == FacingDirection.RIGHT);

    StateMessage currentStateEnum = stateMachine.getCurrentState().getNetworkState();
    
    if (currentStateEnum == newState) {
        return; 
    }

    switch (newState) {
        case FIRE:
            stateMachine.changeState(new FireBasicState());
            break;
    }
}
}
