package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import ru.mipt.bit.platformer.config.LevelConfig;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class LevelModel implements ILevelModel {

    private final TiledMap map;
    private final TiledMapTileLayer groundLayer;
    private final TileMovement tileMovement;

    public LevelModel(LevelConfig config) {
        map = new TmxMapLoader().load(config.getMapPath());
        groundLayer = getSingleLayer(map);
        tileMovement = new TileMovement(groundLayer, config.getInterpolation());
    }

    @Override
    public TiledMap getMap() {
        return map;
    }

    @Override
    public TiledMapTileLayer getGroundLayer() {
        return groundLayer;
    }

    @Override
    public TileMovement getTileMovement() {
        return tileMovement;
    }

    @Override
    public void dispose() {
        map.dispose();
    }
}
