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


public class DamageState implements EntityState<Player> {

    private float knockbackTimer;
    private float damage = 0;
    private float knockbackForceX = 0f;
    private float knockbackForceY = 0f;

    public DamageState(Entity entity) {
        this.knockbackTimer = 0.3f;
        this.damage = entity.getDamage();
        this.knockbackForceX = entity.getKnockbackX();
        this.knockbackForceY = entity.getKnockbackY();
    }

    @Override
    public void enter(Player player) {
        knockbackForceX = knockbackForceX * (player.isMovingRight() ? -1f : 1f);
        player.decreaseHealth(this.damage);
        player.getVelocity().x = knockbackForceX;
        player.getVelocity().y = knockbackForceY;

        player.setCurrentAnimation(knockbackForceX > 0 ?
            PlayerAnimationType.JUMPING_RIGHT : PlayerAnimationType.JUMPING_LEFT);
    }

    @Override
    public void handleInput(Player player) {
    }

    @Override
    public void update(Player player, float delta) {
        player.getCurrentAnimation().update(delta);
        player.getVelocity().y += player.getGravity() * delta;
        player.getBounds().x += player.getVelocity().x * delta;
        player.getBounds().y += player.getVelocity().y * delta;

        knockbackTimer -= delta;

        if (player.isOnPlataform() && knockbackTimer <= 0) {
            player.getStateMachine().changeState(new IdleState());
        }
    }

    @Override
    public void render(Player player, Batch batch) {

    }

    @Override
    public void exit(Player player) {
    }
}
