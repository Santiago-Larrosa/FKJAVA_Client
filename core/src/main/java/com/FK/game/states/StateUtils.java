package com.FK.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.FK.game.entities.*;
import com.FK.game.screens.*;
import com.FK.game.states.*;
import com.FK.game.sounds.*;
import java.util.function.Supplier;

public class StateUtils {
    private static final int REQUIRED_CONFIRMATIONS = 5;

    public static boolean checkFalling(Player player, float delta, int[] airConfirmationCount) {
        return checkFallingInternal(player, delta, airConfirmationCount, FallingState::new);
    }

    public static <E extends Enemy<E>> boolean checkFalling(E enemy, float delta, int[] airConfirmationCount) {
        return checkFallingInternal(enemy, delta, airConfirmationCount, EnemyFallingState<E>::new);
    }

    private static <E extends Entity<E>> boolean checkFallingInternal(
            E entity,
            float delta,
            int[] airConfirmationCount,
            Supplier<EntityState<? super E>> fallingStateSupplier) {
        if (!entity.isOnPlataform()) {
            entity.getVelocity().y += entity.getGravity() * delta;
            airConfirmationCount[0]++;

            if (airConfirmationCount[0] >= REQUIRED_CONFIRMATIONS) {

                entity.getStateMachine().changeState(fallingStateSupplier.get());
                return true;
            }
        } else {
            entity.getVelocity().y = 0;
            airConfirmationCount[0] = 0;
        }
        return false;
    }
}
