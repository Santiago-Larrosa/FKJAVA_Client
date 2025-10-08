package com.FK.game.states;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.FK.game.entities.*;
import com.FK.game.screens.*;
import com.FK.game.states.*;
import com.badlogic.gdx.Gdx;

public class EntityStateMachine<T extends Entity> {
    private T entity;
    private EntityState<T> currentState;
    private EntityState<T> previousState;

    public EntityStateMachine(T entity, EntityState<T> initialState) {
        this.entity = entity;
        this.currentState = initialState;
        this.currentState.enter(entity);
    }

    public EntityStateMachine(T entity) {
        this.entity = entity;
        this.currentState = null;
    }

   public void changeState(EntityState<? super T> newState) {
    Gdx.app.log("STATE_MACHINE", "Changing from " +
    (currentState == null ? "null" : currentState.getClass().getSimpleName()) +
    " to " + newState.getClass().getSimpleName());

    if (currentState != null) {
        currentState.exit(entity);
    }
    currentState = (EntityState<T>) newState; 
    currentState.enter(entity);
}

    public void update(float delta) {
        if (currentState != null) {
            currentState.handleInput(entity); 
            currentState.update(entity, delta);
        }
    }

    public void render(Batch batch) {
        if (currentState != null) {
            currentState.render(entity, batch);
        }
    }

    public EntityState<T> getCurrentState() {
        return currentState;
    }

    public EntityState<T> getPreviousState() {
        return previousState;
    }

    public void revertToPreviousState() {
        if (previousState != null) {
            changeState(previousState);
        }
    }
        public boolean isInState(Class<?> stateClass) {
    return currentState != null && currentState.getClass() == stateClass;
}

}
