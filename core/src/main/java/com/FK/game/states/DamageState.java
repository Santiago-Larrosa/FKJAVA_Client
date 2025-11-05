package com.FK.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.FK.game.entities.*;
import com.FK.game.network.StateMessage;

public class DamageState implements EntityState<CharacterEntity> {

    private float knockbackTimer = 0.2f;

    @Override
    public void enter(CharacterEntity character) {
        character.getVelocity().set(character.getLastKnockback());
        this.knockbackTimer = 0.2f;
        character.setCurrentAnimation(character.getDamageAnimationType());
        if (character.getCurrentAnimation() != null) {
            character.getCurrentAnimation().reset();
        }
    }

    @Override
    public void update(CharacterEntity character, float delta) {
        knockbackTimer -= delta;

        if (character.getCurrentAnimation() != null) {
            character.getCurrentAnimation().update(delta);
        }

        if (!character.isOnPlataform()) {
            character.getVelocity().y += character.getGravity() * delta;
        }

        character.getBounds().x += character.getVelocity().x * delta;
        character.getBounds().y += character.getVelocity().y * delta;

        if (knockbackTimer <= 0f && character.isOnPlataform()) {
            character.getStateMachine().changeState(character.getDefaultState());
        }
    }

    @Override
    public void render(CharacterEntity character, Batch batch) {
        TextureRegion frame = character.getCurrentAnimation().getCurrentFrame();
        if (frame == null) {
            return;
        }

        batch.draw(frame, 
                   character.getX(), 
                   character.getY(), 
                   character.getWidth(), 
                   character.getHeight());

    }

    @Override
    public void exit(CharacterEntity character) {
    }

    @Override
    public StateMessage getNetworkState() {
        return StateMessage.GETTING_DAMMAGE;
    }

    @Override
    public void handleInput(CharacterEntity character) { }
}
