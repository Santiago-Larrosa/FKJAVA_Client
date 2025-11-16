
package com.FK.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.FK.game.core.GameContext;
import com.FK.game.entities.Boss; 
import com.FK.game.entities.Player;
import com.FK.game.network.StateMessage;
import com.FK.game.animations.EnemyAnimationType;

public class BossLaserAttackState implements EntityState<Boss> {

    
    private Phase currentPhase;
    private float damageCooldown = 0f;
    private static final float WARNING_DURATION = 1.5f;
    private static final float CHARGING_DURATION = 0.3f;
    private static final float FIRING_DURATION = 0.5f;
    private static final float COOLDOWN_DURATION = 1.0f;
    private float phaseTimer;
    private Vector2 targetPosition; 
    private float attackAngle;
    private Polygon damagePolygon;  
    private Boss boss;  

@Override
public void enter(Boss enemy) {
    enemy.setAnimation(EnemyAnimationType.BOLB);
    currentPhase = Phase.WARNING;
    phaseTimer = 0f;

    boss = enemy;


    /*if (target != null) {
        targetPosition = target.getCenter();
    } else {
        targetPosition = new Vector2(enemy.getX(), 0);
    }*/

    Vector2 bossCenter = new Vector2(enemy.getX() + enemy.getWidth() / 2, enemy.getY() + enemy.getHeight() / 2);
    
}

    @Override
    public void update(Boss enemy, float delta) {
        phaseTimer += delta;
        currentPhase = boss.getLaserState();
        attackAngle = boss.getLaserAngle();

    }


    public void renderWarning(ShapeRenderer renderer) {
    if (currentPhase != Phase.WARNING) return;

    float alpha = 0.3f + 0.3f * (1 + (float)Math.sin(phaseTimer * 8f)) / 2f; 
    renderer.begin(ShapeRenderer.ShapeType.Filled);
    renderer.setColor(1, 0, 0, alpha);

    float grow = 2 + 6 * (phaseTimer / WARNING_DURATION); 
    renderer.rect(0, -grow / 2, 2000, grow);

    renderer.end();
}

    
    public void renderBeam(Batch batch, TextureRegion whitePixel, Boss enemy) {
    if (currentPhase != Phase.FIRING) return;
    Vector2 bossCenter = new Vector2(enemy.getX() + enemy.getWidth() / 2, enemy.getY() + enemy.getHeight() / 2);

    batch.setColor(Color.WHITE);
    batch.draw(whitePixel, bossCenter.x, bossCenter.y, 0, 0, 2000, 8, 1, 1, attackAngle);

    batch.setColor(Color.YELLOW);
    batch.draw(whitePixel, bossCenter.x, bossCenter.y, 0, 0, 2000, 20, 1, 1, attackAngle);

    batch.setColor(0, 0.6f, 1, 0.3f);
    batch.draw(whitePixel, bossCenter.x, bossCenter.y, 0, 0, 2000, 30, 1, 1, attackAngle);

    batch.setColor(Color.WHITE);
}


    @Override
    public void exit(Boss enemy) {
    }

    @Override
    public void render(Boss enemy, Batch batch) {
        Boss boss = enemy;
        if (boss.getCurrentAnimation() != null && boss.getCurrentAnimation().getCurrentFrame() != null) {
            batch.draw(boss.getCurrentAnimation().getCurrentFrame(),
                boss.getX(), boss.getY(),
                boss.getWidth(), boss.getHeight());
        }
    }

    @Override
    public void handleInput(Boss enemy) {}

    @Override
    public StateMessage getNetworkState() {
        return StateMessage.BOSS_ATTACKING;
    }

    public float getAttackAngle() {
        return attackAngle;
    }
}
