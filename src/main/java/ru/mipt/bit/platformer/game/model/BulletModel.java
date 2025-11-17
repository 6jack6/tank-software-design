package ru.mipt.bit.platformer.game.model;

import com.badlogic.gdx.math.GridPoint2;
import java.util.Objects;
import ru.mipt.bit.platformer.game.level.LevelModel;
import ru.mipt.bit.platformer.util.Direction;

public class BulletModel {

    private static final float DEFAULT_SPEED = 5f;
    private static final int DEFAULT_DAMAGE = 25;

    private final LevelModel levelModel;
    private final ITankModel owner;
    private final Direction direction;
    private final GridPoint2 coordinates;
    private final float tilesPerSecond;
    private final int damage;

    private float progress;
    private boolean active = true;

    public BulletModel(LevelModel levelModel,
                       ITankModel owner,
                       GridPoint2 coordinates,
                       Direction direction) {
        this(levelModel, owner, coordinates, direction, DEFAULT_DAMAGE, DEFAULT_SPEED);
    }

    public BulletModel(LevelModel levelModel,
                       ITankModel owner,
                       GridPoint2 coordinates,
                       Direction direction,
                       int damage,
                       float tilesPerSecond) {
        this.levelModel = Objects.requireNonNull(levelModel);
        this.owner = Objects.requireNonNull(owner);
        this.coordinates = new GridPoint2(Objects.requireNonNull(coordinates));
        this.direction = Objects.requireNonNull(direction);
        this.damage = damage;
        this.tilesPerSecond = tilesPerSecond;
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isActive() {
        return active;
    }

    public void markRemoved() {
        active = false;
    }

    public void update(float deltaTime) {
        if (!active) {
            return;
        }
        progress += deltaTime * tilesPerSecond;
        while (progress >= 1f && active) {
            progress -= 1f;
            travelOneTile();
        }
    }

    private void travelOneTile() {
        GridPoint2 next = direction.destinationFrom(coordinates, new GridPoint2());
        if (!levelModel.isInsideBounds(next)) {
            markRemoved();
            return;
        }
        if (levelModel.findTree(next) != null) {
            markRemoved();
            return;
        }
        ITankModel tank = levelModel.findTank(next);
        if (tank != null && tank != owner) {
            tank.applyDamage(damage);
            if (tank.isDestroyed()) {
                levelModel.removeTank(tank);
            }
            markRemoved();
            return;
        }
        BulletModel otherBullet = levelModel.findBullet(next, this);
        if (otherBullet != null) {
            otherBullet.markRemoved();
            markRemoved();
            return;
        }
        coordinates.set(next);
    }
}
