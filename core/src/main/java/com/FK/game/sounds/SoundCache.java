package com.FK.game.sounds;
import com.FK.game.animations.*;
import com.FK.game.core.*;
import com.FK.game.entities.*;
import com.FK.game.screens.*;
import com.FK.game.states.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;



public class SoundCache {
    private static SoundCache instance;
    private EnumMap<SoundType, Sound> soundMap;
    private Array<ManagedLoop> managedLoops;
    private Map<SoundType, Long> loopingSounds;
    final float MAX_HEARING_DISTANCE = 500f; 
    final float MAX_VOLUME = 0.8f;

    private SoundCache() {
        soundMap = new EnumMap<>(SoundType.class);
        managedLoops = new Array<>();
        loopingSounds = new HashMap<>();
    }

    public static SoundCache getInstance() {
        if (instance == null) {
            instance = new SoundCache();
        }
        return instance;
    }

    public void loadAll() {
        for (SoundType type : SoundType.values()) {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(type.getPath()));
            soundMap.put(type, sound);
        }
    }

    public Sound get(SoundType type) {
        return soundMap.get(type);
    }

    public void playLoop(SoundType type, float volume) {
        if (!loopingSounds.containsKey(type)) {
            long id = soundMap.get(type).loop(volume);
            loopingSounds.put(type, id);
        }
    }

    public void stopLoop(SoundType type) {
        Long id = loopingSounds.remove(type);
        if (id != null) {
            soundMap.get(type).stop(id);
        }
    }
    public void playSpatial(SoundType type, Vector2 sourcePosition, Vector2 listenerPosition) {
        
        float distance = sourcePosition.dst(listenerPosition);

        if (distance > MAX_HEARING_DISTANCE) {
            return;
        }

        float volume = MAX_VOLUME * (1.0f - (distance / MAX_HEARING_DISTANCE));
        float dx = sourcePosition.x - listenerPosition.x;
        float pan = dx / (MAX_HEARING_DISTANCE / 2.0f); 
        pan = MathUtils.clamp(pan, -1.0f, 1.0f);

        Sound sound = soundMap.get(type);
        if (sound != null) {
            sound.play(volume, 1.0f, pan); 
        }
    }

    public void stopAllSounds() {
        for (Map.Entry<SoundType, Long> entry : loopingSounds.entrySet()) {
            Sound sound = soundMap.get(entry.getKey());
            sound.stop(entry.getValue());
        }
        loopingSounds.clear();

        for (Sound sound : soundMap.values()) {
            sound.stop();
        }
    }

     public void startSpatialLoop(SoundType type, Entity source) {
        for (ManagedLoop loop : managedLoops) {
            if (loop.type == type) {
                return; 
            }
        }
        
        Sound sound = soundMap.get(type);
        if (sound != null) {
            long id = sound.loop(0); 
            managedLoops.add(new ManagedLoop(type, id, source));
        }
    }

    public void stopSpatialLoop(SoundType type) {
        ManagedLoop loopToRemove = null;
        for (ManagedLoop loop : managedLoops) {
            if (loop.type == type) {
                soundMap.get(type).stop(loop.soundId);
                loopToRemove = loop;
                break;
            }
        }
        if (loopToRemove != null) {
            managedLoops.removeValue(loopToRemove, true);
        }
    }

    public void updateSpatialLoops(Player listener) {
        if (listener == null) return;
        Vector2 listenerPosition = new Vector2(listener.getX(), listener.getY());

        final float MAX_HEARING_DISTANCE = 500f;
        final float MAX_VOLUME = 0.8f;

        for (ManagedLoop loop : managedLoops) {
            Vector2 sourcePosition = new Vector2(loop.source.getX(), loop.source.getY());
            
            float distance = sourcePosition.dst(listenerPosition);
            float volume = (distance > MAX_HEARING_DISTANCE) ? 0 : MAX_VOLUME * (1.0f - (distance / MAX_HEARING_DISTANCE));
            
            float dx = sourcePosition.x - listenerPosition.x;
            float pan = MathUtils.clamp(dx / (MAX_HEARING_DISTANCE / 2.0f), -1.0f, 1.0f);

            soundMap.get(loop.type).setVolume(loop.soundId, volume);
            soundMap.get(loop.type).setPan(loop.soundId, pan, volume);
        }
    }

    private static class ManagedLoop {
    public SoundType type;
    public long soundId;
    public Entity source;

    public ManagedLoop(SoundType type, long soundId, Entity source) {
        this.type = type;
        this.soundId = soundId;
        this.source = source;
    }
}


    public void dispose() {
        for (Sound sound : soundMap.values()) {
            sound.dispose();
        }
        soundMap.clear();
        loopingSounds.clear();
    }
}