package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class Level implements Disposable {

    private final TiledMap map;
    private final MapRenderer renderer;
    private final TiledMapTileLayer groundLayer;
    private final TileMovement tileMovement;

    public Level(String mapPath, Batch batch, Interpolation interpolation) {
        map = new TmxMapLoader().load(mapPath);
        renderer = createSingleLayerMapRenderer(map, batch);
        groundLayer = getSingleLayer(map);
        tileMovement = new TileMovement(groundLayer, interpolation);
    }

    public TiledMapTileLayer getGroundLayer() {
        return groundLayer;
    }

    public TileMovement getTileMovement() {
        return tileMovement;
    }

    public void render() {
        renderer.render();
    }

    @Override
    public void dispose() {
        map.dispose();
        if (renderer instanceof Disposable) {
            ((Disposable) renderer).dispose();
        }
    }
}
