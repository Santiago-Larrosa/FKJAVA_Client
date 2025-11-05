package com.FK.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Color; 
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.FK.game.entities.*;
import com.FK.game.screens.*;
import com.FK.game.states.*;
import com.FK.game.sounds.*;

public class FireAttackHUD extends BaseHUD{
    private AnimationHandler unloadedAnim;
    private AnimationHandler loadingAnim;
    private AnimationHandler loadedAnim;
    private AnimationHandler currentAnim;
    private AnimationHandler unloadingAnim;
    
    private enum HUDState { UNLOADED, TRANSITION_TO_LOADED, LOADED, TRANSITION_TO_UNLOADED }
    private HUDState state = HUDState.UNLOADED;
    private float stateTime = 0f;

    public FireAttackHUD() {
    AnimationCache cache = AnimationCache.getInstance();

    unloadedAnim = cache.createAnimation(PlayerAnimationType.UNLOADED);
    loadingAnim = cache.createAnimation(PlayerAnimationType.LOADING);
    loadedAnim = cache.createAnimation(PlayerAnimationType.LOADED);
    unloadingAnim = cache.createAnimation(PlayerAnimationType.UNLOADING);
    
    if (loadingAnim == null) {
        Gdx.app.error("FireAttackHUD", "Loading animation is null!");
    }
    
    currentAnim = unloadedAnim;
    this.scale = 0.25f;
}

    @Override
    public void update(float delta) {

    }

    public void updateFire(float delta, boolean isCharged) {
        stateTime += delta;

        switch (state) {
            case UNLOADED:
                if (isCharged) {
                    changeState(HUDState.TRANSITION_TO_LOADED, loadingAnim);
                }
                break;

            case TRANSITION_TO_LOADED:
                if (stateTime >= currentAnim.getTotalDuration()) {
                    changeState(HUDState.LOADED, loadedAnim);
                }
                break;

            case LOADED:
                break;

            case TRANSITION_TO_UNLOADED:
                if (stateTime >= currentAnim.getTotalDuration()) {
                    changeState(HUDState.UNLOADED, unloadedAnim);
                }
                break;
        }

        if (currentAnim != null) {
            currentAnim.update(delta);
        }
    }


    private void changeState(HUDState newState, AnimationHandler newAnim) {
        state = newState;
        currentAnim = newAnim;
        if (currentAnim != null) {
            currentAnim.reset();
        }
        stateTime = 0f;
    }

    public boolean isAttackReady() {
        return state == HUDState.LOADED;
    }

   @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        if (currentAnim == null) return;
        TextureRegion frame = currentAnim.getCurrentFrame();
        if (frame == null || frame.getTexture() == null) return;

        this.width = frame.getRegionWidth() * scale;
        this.height = frame.getRegionHeight() * scale;

        calculatePosition(camera, 20f, 20f, true, true);

        batch.draw(frame, this.x, this.y, this.width, this.height);
    }
    public void resetCooldown() {
        changeState(HUDState.TRANSITION_TO_UNLOADED, unloadingAnim);
    }
}