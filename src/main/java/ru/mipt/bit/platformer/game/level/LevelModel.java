package ru.mipt.bit.platformer.game.level;

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
    private final LevelBounds bounds;

    public LevelModel(LevelConfig config) {
        map = new TmxMapLoader().load(config.getMapPath());
        groundLayer = getSingleLayer(map);
        tileMovement = new TileMovement(groundLayer, config.getInterpolation());
        bounds = new LevelBounds(groundLayer.getWidth(), groundLayer.getHeight());
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

    public LevelBounds getBounds() {
        return bounds;
    }

    @Override
    public void dispose() {
        map.dispose();
    }

    public static class LevelBounds {

        private final int width;
        private final int height;

        public LevelBounds(int width, int height) {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("Width and height must be positive");
            }
            this.width = width;
            this.height = height;
        }

        public boolean contains(com.badlogic.gdx.math.GridPoint2 coordinates) {
            return coordinates.x >= 0 && coordinates.x < width
                    && coordinates.y >= 0 && coordinates.y < height;
        }
    }
}
