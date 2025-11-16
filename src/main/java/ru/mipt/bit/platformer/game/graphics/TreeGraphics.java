package ru.mipt.bit.platformer.game.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.game.model.ITreeModel;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class TreeGraphics implements ITreeGraphics {

    private final ITreeModel model;
    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle bounds;

    public TreeGraphics(ITreeModel model, TiledMapTileLayer tileLayer) {
        this.model = model;
        this.texture = new Texture(model.getTexturePath());
        this.graphics = new TextureRegion(texture);
        this.bounds = createBoundingRectangle(graphics);
        moveRectangleAtTileCenter(tileLayer, bounds, model.getCoordinates());
    }

    @Override
    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, bounds, 0f);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
