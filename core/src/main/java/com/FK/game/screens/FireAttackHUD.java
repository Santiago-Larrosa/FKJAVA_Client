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

public class FireAttackHUD {
    private AnimationHandler unloadedAnim;
    private AnimationHandler loadingAnim;
    private AnimationHandler loadedAnim;
    private AnimationHandler currentAnim;
    private AnimationHandler unloadingAnim;
    
    private enum HUDState { UNLOADED, TRANSITION_TO_LOADED, LOADED, TRANSITION_TO_UNLOADED }
    private HUDState state = HUDState.UNLOADED;

    private float stateTime = 0f;
    private float cooldown = 5f;
    private float cooldownTimer = 0f;

    public FireAttackHUD() {
    AnimationCache cache = AnimationCache.getInstance();

    // CAMBIO: Se utiliza 'createAnimation' para obtener una instancia nueva para cada animación.
    unloadedAnim = cache.createAnimation(PlayerAnimationType.UNLOADED);
    loadingAnim = cache.createAnimation(PlayerAnimationType.LOADING);
    loadedAnim = cache.createAnimation(PlayerAnimationType.LOADED);
    unloadingAnim = cache.createAnimation(PlayerAnimationType.UNLOADING);
    
    if (loadingAnim == null) {
        Gdx.app.error("FireAttackHUD", "Loading animation is null!");
    }
    
    currentAnim = unloadedAnim;
}

    public void update(float delta) {
        stateTime += delta;

        switch (state) {
            case UNLOADED:
                cooldownTimer += delta;
                if (cooldownTimer >= cooldown) {
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
                    cooldownTimer = 0f;
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

   public void render(SpriteBatch batch, OrthographicCamera camera) {
        if (currentAnim == null) {
            Gdx.app.error("FireAttackHUD", "Current animation is null! State: " + state);
            return;
        }

        TextureRegion frame = currentAnim.getCurrentFrame();
        if (frame == null) {
            Gdx.app.error("FireAttackHUD", "Current frame is null! State: " + state + 
                          ", Animation: " + currentAnim);
            return;
        }

        if (frame.getTexture() == null) {
            Gdx.app.error("FireAttackHUD", "Texture is null for frame!");
            return;
        }

        batch.enableBlending();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        float scale = 0.25f;
        float hudWidth = frame.getRegionWidth() * scale;
        float hudHeight = frame.getRegionHeight() * scale;

        float margin = 20f;
        float x = camera.position.x + (camera.viewportWidth / 2f) - hudWidth - margin;
        float y = camera.position.y + (camera.viewportHeight / 2f) - hudHeight - margin;

        batch.setColor(Color.WHITE);
        batch.draw(frame, x, y, hudWidth, hudHeight);
    }
    public void resetCooldown() {
        changeState(HUDState.TRANSITION_TO_UNLOADED, unloadingAnim);
    }
}