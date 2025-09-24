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

public class Fire extends Entity <Fire>{
    protected AnimationHandler[] animations;
    protected ObjectsAnimationType currentAnimationType;

    public Fire(float x, float y) {
        super(x, y, 400, 400f, 200f, 200f);
        setDamageSize(150, 150);
        setDamage(0);
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
        stateMachine.update(delta);
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
}
