package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.util.Direction;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class Tank implements Disposable {

    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle bounds;
    private final GridPoint2 coordinates;
    private final GridPoint2 destination;
    private final GridPoint2 movementCandidate = new GridPoint2();
    private final TileMovement tileMovement;
    private final float movementSpeed;

    private float movementProgress = 1f;
    private float rotation = 0f;

    public Tank(String texturePath, GridPoint2 startCoordinates, TileMovement tileMovement,
                TiledMapTileLayer tileLayer, float movementSpeed) {
        this.texture = new Texture(texturePath);
        this.graphics = new TextureRegion(texture);
        this.bounds = createBoundingRectangle(graphics);
        this.coordinates = new GridPoint2(startCoordinates);
        this.destination = new GridPoint2(startCoordinates);
        this.tileMovement = tileMovement;
        this.movementSpeed = movementSpeed;
        moveRectangleAtTileCenter(tileLayer, bounds, this.coordinates);
    }

    public void handleInput(Iterable<Tree> obstacles) {
        for (Direction direction : Direction.values()) {
            attemptMovement(direction, obstacles);
        }
    }

    private void attemptMovement(Direction direction, Iterable<Tree> obstacles) {
        if (!direction.isPressed()) {
            return;
        }
        if (!isEqual(movementProgress, 1f)) {
            return;
        }

        direction.destinationFrom(coordinates, movementCandidate);
        if (!isBlocked(movementCandidate, obstacles)) {
            destination.set(movementCandidate);
            movementProgress = 0f;
        }
        rotation = direction.getRotation();
    }

    private boolean isBlocked(GridPoint2 candidate, Iterable<Tree> obstacles) {
        for (Tree obstacle : obstacles) {
            if (obstacle.blocks(candidate)) {
                return true;
            }
        }
        return false;
    }

    public void update(float deltaTime) {
        tileMovement.moveRectangleBetweenTileCenters(bounds, coordinates, destination, movementProgress);
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
        if (isEqual(movementProgress, 1f)) {
            coordinates.set(destination);
        }
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, bounds, rotation);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
