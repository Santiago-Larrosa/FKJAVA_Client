package com.FK.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.FK.game.entities.*;

public class DamageState implements EntityState<CharacterEntity> {

    private float knockbackTimer = 0.2f;

    @Override
    public void enter(CharacterEntity character) {
        Gdx.app.log("DAMAGE_STATE", character + " entra en DamageState");
        Gdx.app.log("DAMAGE_STATE", "Velocidad antes del knockback: " + character.getVelocity());
        Gdx.app.log("DAMAGE_STATE", "Último knockback recibido: " + character.getLastKnockback());

        character.getVelocity().set(character.getLastKnockback());
        this.knockbackTimer = 0.2f;

        Gdx.app.log("DAMAGE_STATE", "Velocidad aplicada: " + character.getVelocity());
        Gdx.app.log("DAMAGE_STATE", "Vida actual: " + character.getHealth());
    }

    @Override
    public void update(CharacterEntity character, float delta) {
        knockbackTimer -= delta;
        Gdx.app.log("DAMAGE_STATE", character + " actualizando DamageState | knockbackTimer=" + knockbackTimer);

        if (!character.isOnPlataform()) {
            character.getVelocity().y += character.getGravity() * delta;
            Gdx.app.log("DAMAGE_STATE", "Aplicando gravedad. Nueva velocidad Y: " + character.getVelocity().y);
        }

        character.getBounds().x += character.getVelocity().x * delta;
        character.getBounds().y += character.getVelocity().y * delta;

        if (knockbackTimer <= 0f && character.isOnPlataform()) {
            Gdx.app.log("DAMAGE_STATE", character + " termina daño, cambiando a estado por defecto");
            character.getStateMachine().changeState(character.getDefaultState());
        }
    }

    @Override
    public void render(CharacterEntity character, Batch batch) {
        TextureRegion frame = character.getCurrentAnimation().getCurrentFrame();
        if (frame == null) {
            Gdx.app.log("DAMAGE_STATE", "⚠ " + character + " no tiene frame actual");
            return;
        }

        batch.draw(frame, 
                   character.getX(), 
                   character.getY(), 
                   character.getWidth(), 
                   character.getHeight());

        Gdx.app.log("DAMAGE_STATE", "Renderizando frame de " + character + " en (" + character.getX() + ", " + character.getY() + ")");
    }

    @Override
    public void exit(CharacterEntity character) {
        Gdx.app.log("DAMAGE_STATE", character + " sale de DamageState");
    }

    @Override
    public void handleInput(CharacterEntity character) { }
}
