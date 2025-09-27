package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class Tree implements Disposable {

    private final Texture texture;
    private final TextureRegion graphics;
    private final GridPoint2 coordinates;
    private final Rectangle bounds;

    public Tree(String texturePath, GridPoint2 coordinates, TiledMapTileLayer tileLayer) {
        this.texture = new Texture(texturePath);
        this.graphics = new TextureRegion(texture);
        this.bounds = createBoundingRectangle(graphics);
        this.coordinates = new GridPoint2(coordinates);
        moveRectangleAtTileCenter(tileLayer, bounds, this.coordinates);
    }

    public boolean blocks(GridPoint2 tileCoordinates) {
        return coordinates.equals(tileCoordinates);
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, bounds, 0f);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }
}
