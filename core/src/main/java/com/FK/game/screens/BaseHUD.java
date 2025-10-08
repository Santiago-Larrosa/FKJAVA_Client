
package com.FK.game.screens; 

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class BaseHUD {
    protected float x, y, width, height;
    protected float scale = 1f;

    public abstract void update(float delta);

    public abstract void render(SpriteBatch batch, OrthographicCamera camera);


    protected void calculatePosition(OrthographicCamera camera, float marginX, float marginY, boolean alignRight, boolean alignTop) {
        float viewWidth = camera.viewportWidth;
        float viewHeight = camera.viewportHeight;
        float camX = camera.position.x - viewWidth / 2f;
        float camY = camera.position.y - viewHeight / 2f;

        if (alignRight) {
            this.x = camX + viewWidth - this.width - marginX;
        } else {
            this.x = camX + marginX;
        }

        if (alignTop) {
            this.y = camY + viewHeight - this.height - marginY;
        } else {
            this.y = camY + marginY;
        }
    }
}