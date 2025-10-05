package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class TreeGraphics implements Disposable {

    private final TreeModel model;
    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle bounds;

    public TreeGraphics(String texturePath, TreeModel model, TiledMapTileLayer tileLayer) {
        this.model = model;
        this.texture = new Texture(texturePath);
        this.graphics = new TextureRegion(texture);
        this.bounds = createBoundingRectangle(graphics);
        moveRectangleAtTileCenter(tileLayer, bounds, model.getCoordinates());
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, bounds, 0f);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
