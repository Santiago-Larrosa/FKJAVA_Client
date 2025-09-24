package com.FK.game.states;

import com.FK.game.animations.PlayerAnimationType; 
import com.FK.game.entities.Player;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class DeathState implements EntityState<Player> {

    @Override
    public void enter(Player player) {
        player.getVelocity().set(0, 0);
        player.setCurrentAnimation(PlayerAnimationType.SMOKE); 
        player.getCurrentAnimation().reset();
    }

    @Override
    public void update(Player player, float delta) {
        player.getCurrentAnimation().update(delta);

        if (player.getCurrentAnimation().isFinished()) {
            player.setReadyForRemoval(true);
        }
    }

    @Override
    public void render(Player player, Batch batch) {
        TextureRegion frame = player.getCurrentAnimation().getCurrentFrame();
        batch.draw(frame, 
                   player.getX(), 
                   player.getY(), 
                   player.getWidth(), 
                   player.getHeight());
    }

    @Override public void exit(Player player) {}
    @Override public void handleInput(Player player) {}
}