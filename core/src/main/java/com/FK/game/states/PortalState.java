package com.FK.game.states;

import com.FK.game.animations.AnimationHandler;
import com.FK.game.animations.ObjectsAnimationType;
import com.FK.game.entities.Portal;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;
import com.FK.game.network.StateMessage;

public class PortalState implements EntityState<Portal> {
    private boolean rising = true;
    private float risingTimer = 0f;
    private static final float RISING_DURATION = 1.0f; 

    @Override
    public void enter(Portal portal) {
        portal.setAnimation(ObjectsAnimationType.PORTAL_RISING);
        rising = true;
        risingTimer = 0f;
    }

    @Override
    public void update(Portal portal, float delta) {
        portal.getCurrentAnimation().update(delta);

        if (rising) {
            risingTimer += delta;
            if (risingTimer >= RISING_DURATION) {
                rising = false;
                portal.setAnimation(ObjectsAnimationType.PORTAL_LOOP);
                Gdx.app.log("PortalState", "El portal terminó de aparecer y ahora está activo.");
            }
        }
    }

    @Override
    public void handleInput(Portal portal) {
    }

    @Override
    public void render(Portal portal, Batch batch) {
        TextureRegion frame = portal.getCurrentAnimation().getCurrentFrame();
        batch.draw(frame, portal.getX(), portal.getY(), portal.getWidth(), portal.getHeight());
    }

    @Override
    public StateMessage getNetworkState() {
        return StateMessage.PORTAL;
    }

    @Override
    public void exit(Portal portal) {
    }
}
