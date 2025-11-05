package com.FK.game.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;


public class MapManager {

    private final Array<TiledMap> maps = new Array<>();
    private int currentMapIndex = 0;
    private final float mapScale;

    public MapManager(float mapScale) {
        this.mapScale = mapScale;
    }

    public void loadMaps(String... mapFiles) {
        TmxMapLoader loader = new TmxMapLoader();
        for (String file : mapFiles) {
            TiledMap map = loader.load(file);
            map.getProperties().put("fileName", file);
            maps.add(map);
        }
    }

    public TiledMap getCurrentMap() {
        return maps.get(currentMapIndex);
    }

    public void setMap(int index) {
        if (index >= 0 && index < maps.size) {
            currentMapIndex = index;
        }
    }
    public void setMapByName(String mapName) {
    for (int i = 0; i < maps.size; i++) {
        TiledMap map = maps.get(i);
        String fileName = map.getProperties().get("fileName", String.class);
        if (fileName != null && fileName.equals(mapName)) {
            currentMapIndex = i;
            return;
        }
    }
}

    public void setRandomMap() {
        currentMapIndex = MathUtils.random(0, maps.size - 1);
    }

    public float getScale() {
        return mapScale;
    }

    public Array<TiledMap> getMaps() {
        return this.maps;
    }
}
