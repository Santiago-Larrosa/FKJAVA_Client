package com.FK.game.states;

import com.badlogic.gdx.Gdx;
import com.FK.game.animations.EnemyAnimationType; 
import com.FK.game.entities.Enemy;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EnemyDeathState implements EntityState<Enemy> {

    @Override
    public void enter(Enemy enemy) {
        enemy.getVelocity().set(0, 0);
        enemy.setCurrentAnimation(EnemyAnimationType.SMOKE); 
        enemy.getCurrentAnimation().reset();
    }

    @Override
    public void update(Enemy enemy, float delta) {
        enemy.getCurrentAnimation().update(delta);

        if (enemy.getCurrentAnimation().isFinished()) {
            Gdx.app.log("YA SE PUEDE MORIR", "El enemigo ya se puede morir");
            enemy.setReadyForRemoval(true);
        }
    }

    @Override
    public void render(Enemy enemy, Batch batch) {
        TextureRegion frame = enemy.getCurrentAnimation().getCurrentFrame();
        batch.draw(frame, 
                   enemy.getX(), 
                   enemy.getY(), 
                   enemy.getWidth(), 
                   enemy.getHeight());
    }

    @Override public void exit(Enemy enemy) {}
    @Override public void handleInput(Enemy enemy) {}
}