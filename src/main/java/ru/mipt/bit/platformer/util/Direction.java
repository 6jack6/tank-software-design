package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.GridPoint2;

public enum Direction {
    UP(0, 1, 90f, Input.Keys.UP, Input.Keys.W),
    LEFT(-1, 0, -180f, Input.Keys.LEFT, Input.Keys.A),
    DOWN(0, -1, -90f, Input.Keys.DOWN, Input.Keys.S),
    RIGHT(1, 0, 0f, Input.Keys.RIGHT, Input.Keys.D);

    private final int deltaX;
    private final int deltaY;
    private final float rotation;
    private final int primaryKey;
    private final int secondaryKey;

    Direction(int deltaX, int deltaY, float rotation, int primaryKey, int secondaryKey) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.rotation = rotation;
        this.primaryKey = primaryKey;
        this.secondaryKey = secondaryKey;
    }

    public boolean isPressed() {
        return Gdx.input.isKeyPressed(primaryKey) || Gdx.input.isKeyPressed(secondaryKey);
    }

    public float getRotation() {
        return rotation;
    }

    public GridPoint2 destinationFrom(GridPoint2 origin, GridPoint2 destination) {
        return destination.set(origin).add(deltaX, deltaY);
    }
}
