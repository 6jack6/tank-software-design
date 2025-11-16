package ru.mipt.bit.platformer.game.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.config.TankConfig;
import ru.mipt.bit.platformer.util.Direction;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static com.badlogic.gdx.math.MathUtils.random;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class TankModel implements ITankModel {

    private final GridPoint2 coordinates;
    private final GridPoint2 destination;
    private final GridPoint2 movementCandidate = new GridPoint2();
    private final float movementSpeed;
    private final String texturePath;
    private final int healthPoints;

    private float movementProgress = 1f;
    private float rotation = 0f;

    public TankModel(TankConfig config) {
        GridPoint2 initialCoordinates = config.getInitialCoordinates();
        this.coordinates = new GridPoint2(initialCoordinates);
        this.destination = new GridPoint2(initialCoordinates);
        this.movementSpeed = config.getMovementSpeed();
        this.texturePath = config.getTexturePath();
        this.healthPoints = random(80, 100);
    }

    @Override
    public void attemptMove(Direction direction, Iterable<? extends ITreeModel> obstacles) {
        if (!isReadyForNextMove()) {
            return;
        }

        direction.destinationFrom(coordinates, movementCandidate);
        if (!isBlocked(movementCandidate, obstacles)) {
            destination.set(movementCandidate);
            movementProgress = 0f;
        }
        rotation = direction.getRotation();
    }

    private boolean isBlocked(GridPoint2 candidate, Iterable<? extends ITreeModel> obstacles) {
        for (ITreeModel obstacle : obstacles) {
            if (obstacle.blocks(candidate)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
        if (isEqual(movementProgress, 1f)) {
            coordinates.set(destination);
        }
    }

    @Override
    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    @Override
    public GridPoint2 getDestination() {
        return destination;
    }

    @Override
    public float getMovementProgress() {
        return movementProgress;
    }

    @Override
    public float getRotation() {
        return rotation;
    }

    @Override
    public String getTexturePath() {
        return texturePath;
    }

    @Override
    public int getHealthPoints() {
        return healthPoints;
    }

    private boolean isReadyForNextMove() {
        return isEqual(movementProgress, 1f);
    }
}
