package com.FK.game.states;

import com.FK.game.animations.PlayerAnimationType; 
import com.FK.game.entities.CharacterEntity;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.FK.game.animations.AnimationHandler;
import com.badlogic.gdx.Gdx;
import com.FK.game.network.StateMessage;

public class DeathState implements EntityState<CharacterEntity> {

    @Override
    public void enter(CharacterEntity character) {

        character.getVelocity().set(0, 0);
        character.setCurrentAnimation(character.getDeathAnimationType());
        if (character.getCurrentAnimation() != null) {
            character.getCurrentAnimation().reset();
        }
    }

    @Override
    public void update(CharacterEntity character, float delta) {
     character.getCurrentAnimation().update(delta);
     Gdx.app.log("DeathState", character + " stateTime=" + character.getCurrentAnimation().getCurrentFrameIndex() + 
             " finished=" + character.getCurrentAnimation().isFinished());


        if (character.getCurrentAnimation().isFinished()) {
            Gdx.app.log("YA SE PUEDE MORIR", "El enemigo ya se puede morir");
            character.setReadyForRemoval(true);
        }
    }

    @Override
    public void render(CharacterEntity character, Batch batch) {
        TextureRegion frame = character.getCurrentAnimation().getCurrentFrame();
        batch.draw(frame, 
                   character.getX(), 
                   character.getY(), 
                   character.getWidth(), 
                   character.getHeight());
    }
    @Override
    public StateMessage getNetworkState() {
        return StateMessage.DYING;
    }

    @Override public void exit(CharacterEntity character) {}
    @Override public void handleInput(CharacterEntity character) {}
}