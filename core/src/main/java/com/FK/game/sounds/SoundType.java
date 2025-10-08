package com.FK.game.sounds;
import com.FK.game.core.*;
import com.FK.game.entities.*;
import com.FK.game.screens.*;
import com.FK.game.states.*;

public enum SoundType {
    WALK("footSteps.wav"),
    BACKGROUND("Background.wav"),
    SWORD("sword.wav"),
    FALLING_ATACK("FallingAttackSound.wav"),
    FALLING_CLASH("fallingClash.wav"),
    FIRE("fire.wav"),
    FLAP("flap.wav");

    private final String path;

    SoundType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
