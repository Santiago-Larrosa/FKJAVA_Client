// Crea un nuevo archivo: FireBasicState.java
package com.FK.game.states;

import com.FK.game.animations.ObjectsAnimationType;
import com.FK.game.entities.Fire; // Importamos la nueva clase Fire que crearemos
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FireBasicState implements EntityState<Fire> {

    @Override
    public void enter(Fire fire) {
        // Al entrar en este estado, inmediatamente establecemos la animación de bucle.
        fire.setCurrentAnimation(ObjectsAnimationType.FIRE_LOOP);
    }

    @Override
    public void update(Fire fire, float delta) {
        // En cada frame, solo necesitamos actualizar la animación para que avance.
        if (fire.getCurrentAnimation() != null) {
            fire.getCurrentAnimation().update(delta);
        }
    }

    @Override
    public void render(Fire fire, Batch batch) {
        // Dibujamos el frame actual de la animación.
        if (fire.getCurrentAnimation() != null) {
            TextureRegion frame = fire.getCurrentAnimation().getCurrentFrame();
            batch.draw(frame, fire.getX(), fire.getY(), fire.getWidth(), fire.getHeight());
        }
    }

    @Override
    public void exit(Fire fire) {
        // No se necesita ninguna limpieza especial al salir de este estado.
    }

    @Override
    public void handleInput(Fire fire) {
        // El fuego es un objeto decorativo, no responde a la entrada del jugador.
    }
}