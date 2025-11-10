

package com.FK.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.FK.game.network.EntityTypeMessage;
import com.FK.game.animations.Assets;

public class Coin extends Entity<Coin> {

    private static final float COIN_PICKUP_RADIUS = 80f;    
    private static final float SPEED = 350f;
    private static final float TURN_SPEED = 15f; 
    private Player target = null;

    public Coin(float x, float y) {
        super(x, y, 16, 16, 16, 16);
        this.entityType = EntityTypeMessage.COIN;
    }
    
    public void setTarget(Player player) {
        if (this.target == null) { 
            this.target = player;
        }
    }

    public Player getTarget() {
        return this.target;
    }

    @Override
    public void update(float delta) {
        if (target == null) {
            return;
        }
        Vector2 targetPosition = target.getCenter();
        Vector2 directionToTarget = targetPosition.sub(this.getCenter()).nor();
        velocity.lerp(directionToTarget.scl(SPEED), delta * TURN_SPEED);
        bounds.x += velocity.x * delta;
        bounds.y += velocity.y * delta; 
        if (getCenter().dst(target.getCenter()) < 15f) {
            target.addCoins(1); 
            setReadyForRemoval(true); 
        }
    }

    @Override
    public void render(Batch batch) {
        batch.draw(Assets.coinTexture, getX(), getY(), getWidth(), getHeight());
    }
    
    @Override
    public float getGravity() {
        return 0f; 
    }

    public float getCanPickupRadius() {
        return COIN_PICKUP_RADIUS;
    }   


    
    @Override
    public void setVisualStateFromServer(String networkState, String networkFacing) {
    }
}