package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class TankGraphics implements Disposable {

    private final TankModel model;
    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle bounds;
    private final TileMovement tileMovement;

    public TankGraphics(String texturePath, TankModel model, TileMovement tileMovement,
                        TiledMapTileLayer tileLayer) {
        this.model = model;
        this.texture = new Texture(texturePath);
        this.graphics = new TextureRegion(texture);
        this.bounds = createBoundingRectangle(graphics);
        this.tileMovement = tileMovement;
        moveRectangleAtTileCenter(tileLayer, bounds, model.getCoordinates());
    }

    public void update() {
        tileMovement.moveRectangleBetweenTileCenters(bounds, model.getCoordinates(),
                model.getDestination(), model.getMovementProgress());
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, bounds, model.getRotation());
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
