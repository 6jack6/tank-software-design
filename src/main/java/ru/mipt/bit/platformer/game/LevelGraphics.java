package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.utils.Disposable;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;

public class LevelGraphics implements Disposable {

    private final MapRenderer renderer;

    public LevelGraphics(LevelModel model, Batch batch) {
        renderer = createSingleLayerMapRenderer(model.getMap(), batch);
    }

    public void render() {
        renderer.render();
    }

    @Override
    public void dispose() {
        if (renderer instanceof Disposable) {
            ((Disposable) renderer).dispose();
        }
    }
}
