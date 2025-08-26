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

public class Portal extends Entity <Portal>{
    private EntityStateMachine<Portal> stateMachine;
    protected AnimationHandler[] animations;
    protected ObjectsAnimationType currentAnimationType;

    public Portal(float x, float y) {
        super(x, y, 500f, 250f, 300f, 150f);
        setCollisionBoxOffset(50f, 50f);
        initializeAnimations();
        setCurrentAnimation(ObjectsAnimationType.PORTAL_RISING);
        this.stateMachine = new EntityStateMachine<>(this, new PortalState());
    }

    public EntityStateMachine<Portal> getStateMachine() {
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
