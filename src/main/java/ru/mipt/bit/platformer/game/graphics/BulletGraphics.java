package ru.mipt.bit.platformer.game.graphics;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.game.model.BulletModel;

public class BulletGraphics implements IBulletGraphics {

    private final BulletModel model;
    private final TiledMapTileLayer tileLayer;
    private final Rectangle bounds = new Rectangle();
    private final Texture texture;

    public BulletGraphics(BulletModel model, TiledMapTileLayer tileLayer) {
        this.model = model;
        this.tileLayer = tileLayer;
        this.texture = createTexture();
        moveToCurrentTile();
    }

    @Override
    public void update() {
        moveToCurrentTile();
    }

    @Override
    public void render(Batch batch) {
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

    private void moveToCurrentTile() {
        float tileWidth = tileLayer.getTileWidth();
        float tileHeight = tileLayer.getTileHeight();
        float width = tileWidth * 0.25f;
        float height = tileHeight * 0.25f;
        bounds.setSize(width, height);
        float centerX = model.getCoordinates().x * tileWidth + tileWidth / 2f;
        float centerY = model.getCoordinates().y * tileHeight + tileHeight / 2f;
        bounds.setPosition(centerX - width / 2f, centerY - height / 2f);
    }

    private static Texture createTexture() {
        Pixmap pixmap = new Pixmap(4, 4, Pixmap.Format.RGBA8888);
        pixmap.setColor(1f, 1f, 0f, 1f);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }
}
