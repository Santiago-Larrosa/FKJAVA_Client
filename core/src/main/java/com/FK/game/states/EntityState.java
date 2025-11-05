package com.FK.game.states;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.FK.game.entities.*;
import com.FK.game.screens.*;
import com.FK.game.states.*;
import com.FK.game.network.StateMessage;

public interface EntityState<T extends Entity> {
    void enter(T entity);
    void update(T entity, float delta);
    void handleInput(T entity); 
    void render(T entity, Batch batch);
    void exit(T entity);

    StateMessage getNetworkState();
}
