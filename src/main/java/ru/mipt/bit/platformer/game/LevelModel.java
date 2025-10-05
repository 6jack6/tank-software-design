package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class LevelModel implements Disposable {

    private final TiledMap map;
    private final TiledMapTileLayer groundLayer;
    private final TileMovement tileMovement;

    public LevelModel(String mapPath, Interpolation interpolation) {
        map = new TmxMapLoader().load(mapPath);
        groundLayer = getSingleLayer(map);
        tileMovement = new TileMovement(groundLayer, interpolation);
    }

    public TiledMap getMap() {
        return map;
    }

    public TiledMapTileLayer getGroundLayer() {
        return groundLayer;
    }

    public TileMovement getTileMovement() {
        return tileMovement;
    }

    @Override
    public void dispose() {
        map.dispose();
    }
}
