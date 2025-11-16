package com.FK.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.FK.game.entities.*;
import com.FK.game.screens.*;
import com.FK.game.states.*;
import com.FK.game.sounds.*;
import com.FK.game.network.StateMessage;

public class AttackingState implements EntityState<Player> {
    private float attackTimer = 0f;
    private static final float ATTACK_DURATION = 0.415f;
    private static final float HITBOX_ACTIVATION_FRAME = 0.15f;
    private boolean hitboxActive = false;
    private Rectangle attackHitbox;
    private static final boolean DEBUG_MODE = false;

    @Override
    public void enter(Player player) {
        player.setCurrentAnimation(player.isMovingRight() ? PlayerAnimationType.ATTACKING_RIGHT : PlayerAnimationType.ATTACKING_LEFT);
        SoundCache.getInstance().get(SoundType.SWORD).play(0.5f);
    }

   @Override
    public void update(Player player, float delta) {
        player.getCurrentAnimation().update(delta);
    }

    

    @Override
    public void handleInput(Player player) {
    }

    @Override
    public void render(Player player, Batch batch) {
        TextureRegion frame = player.getCurrentAnimation().getCurrentFrame();
        batch.draw(frame, player.getBounds().x, player.getBounds().y,  player.getBounds().width, player.getBounds().height);
    }

    @Override
    public StateMessage getNetworkState() {
        return StateMessage.PLAYER_ATTACKING;
    }

    @Override
    public void exit(Player player) {
    }
}