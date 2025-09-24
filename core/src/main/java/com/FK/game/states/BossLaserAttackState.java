
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
import com.FK.game.entities.Enemy;
import com.FK.game.entities.Boss; 
import com.FK.game.entities.Player;

public class BossLaserAttackState implements EntityState<Enemy> {

    private enum Phase {
        WARNING,  
        CHARGING,  
        FIRING,   
        COOLDOWN   
    }
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

    @Override
    public void enter(Enemy enemy) {
        currentPhase = Phase.WARNING;
        phaseTimer = 0f;

        Player player = GameContext.getPlayer();
        if (player != null) {
            targetPosition = new Vector2(
                player.getCollisionBox().x + player.getCollisionBox().width / 2,
                player.getCollisionBox().y + player.getCollisionBox().height / 2
            );
        } else {
            targetPosition = new Vector2(enemy.getX(), 0);
        }

        Vector2 bossCenter = new Vector2(enemy.getX() + enemy.getWidth() / 2, enemy.getY() + enemy.getHeight() / 2);
        attackAngle = targetPosition.cpy().sub(bossCenter).angleDeg();
    }

    @Override
    public void update(Enemy enemy, float delta) {
        if (damageCooldown > 0) damageCooldown -= delta;
        phaseTimer += delta;

        switch (currentPhase) {
            case WARNING:
                if (phaseTimer >= WARNING_DURATION) {
                    phaseTimer = 0;
                    currentPhase = Phase.CHARGING;
                }
                break;
            case CHARGING:
                if (phaseTimer >= CHARGING_DURATION) {
                    phaseTimer = 0;
                    currentPhase = Phase.FIRING;
                    createDamagePolygon(enemy); 
                }
                break;
            case FIRING:
                checkCollision(enemy);
                if (phaseTimer >= FIRING_DURATION) {
                    phaseTimer = 0;
                    currentPhase = Phase.COOLDOWN;
                    damagePolygon = null; 
                }
                break;
            case COOLDOWN:
                if (phaseTimer >= COOLDOWN_DURATION) {
                    enemy.getStateMachine().changeState(new BossIdleState());
                }
                break;
        }
    }

    private void createDamagePolygon(Enemy enemy) {
        float beamLength = 2000f;
        float beamWidth = 20f;    

        Vector2 bossCenter = new Vector2(enemy.getX() + enemy.getWidth() / 2, enemy.getY() + enemy.getHeight() / 2);
        damagePolygon = new Polygon(new float[]{
            0, -beamWidth / 2,
            beamLength, -beamWidth / 2,
            beamLength, beamWidth / 2,
            0, beamWidth / 2
        });
        damagePolygon.setOrigin(0, 0);
        damagePolygon.setPosition(bossCenter.x, bossCenter.y);
        damagePolygon.setRotation(attackAngle);
    }

    private void checkCollision(Enemy enemy) {
    Player player = GameContext.getPlayer();
    if (player == null || damagePolygon == null) return;

    Rectangle pRect = player.getCollisionBox();
    Polygon playerPolygon = new Polygon(new float[]{
        pRect.x, pRect.y,
        pRect.x + pRect.width, pRect.y,
        pRect.x + pRect.width, pRect.y + pRect.height,
        pRect.x, pRect.y + pRect.height
    });

    if (Intersector.overlapConvexPolygons(damagePolygon, playerPolygon)) {
        if (damageCooldown <= 0) {
            player.receiveDamage(enemy);
            damageCooldown = 0.2f; 
        }
    }
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

    
    public void renderBeam(Batch batch, TextureRegion whitePixel, Enemy enemy) {
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
    public void exit(Enemy enemy) {
    }

    @Override
    public void render(Enemy enemy, com.badlogic.gdx.graphics.g2d.Batch batch) {
    }

    @Override
    public void handleInput(Enemy enemy) {}

    public float getAttackAngle() {
        return attackAngle;
    }
}