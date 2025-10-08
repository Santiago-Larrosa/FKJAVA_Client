
package com.FK.game.states;

import com.FK.game.animations.ObjectsAnimationType;
import com.FK.game.entities.Fire; 
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FireBasicState implements EntityState<Fire> {

    @Override
    public void enter(Fire fire) {
        fire.setCurrentAnimation(ObjectsAnimationType.FIRE_LOOP);
    }

    @Override
    public void update(Fire fire, float delta) {
        if (fire.getCurrentAnimation() != null) {
            fire.getCurrentAnimation().update(delta);
        }
    }

    @Override
    public void render(Fire fire, Batch batch) {
        if (fire.getCurrentAnimation() != null) {
            TextureRegion frame = fire.getCurrentAnimation().getCurrentFrame();
            batch.draw(frame, fire.getX(), fire.getY(), fire.getWidth(), fire.getHeight());
        }
    }

    @Override
    public void exit(Fire fire) {
    }

    @Override
    public void handleInput(Fire fire) {
    }
}