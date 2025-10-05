package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.util.Direction;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class TankModel {

    private final GridPoint2 coordinates;
    private final GridPoint2 destination;
    private final GridPoint2 movementCandidate = new GridPoint2();
    private final float movementSpeed;

    private float movementProgress = 1f;
    private float rotation = 0f;

    public TankModel(GridPoint2 startCoordinates, float movementSpeed) {
        this.coordinates = new GridPoint2(startCoordinates);
        this.destination = new GridPoint2(startCoordinates);
        this.movementSpeed = movementSpeed;
    }

    public void attemptMove(Direction direction, Iterable<TreeModel> obstacles) {
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

    private boolean isBlocked(GridPoint2 candidate, Iterable<TreeModel> obstacles) {
        for (TreeModel obstacle : obstacles) {
            if (obstacle.blocks(candidate)) {
                return true;
            }
        }
        return false;
    }

    public void update(float deltaTime) {
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
        if (isEqual(movementProgress, 1f)) {
            coordinates.set(destination);
        }
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    public GridPoint2 getDestination() {
        return destination;
    }

    public float getMovementProgress() {
        return movementProgress;
    }

    public float getRotation() {
        return rotation;
    }

    private boolean isReadyForNextMove() {
        return isEqual(movementProgress, 1f);
    }
}
