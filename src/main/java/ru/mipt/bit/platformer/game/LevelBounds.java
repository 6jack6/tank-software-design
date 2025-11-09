package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;

public class LevelBounds {

    private final int width;
    private final int height;

    public LevelBounds(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive");
        }
        this.width = width;
        this.height = height;
    }

    public boolean contains(GridPoint2 coordinates) {
        return coordinates.x >= 0 && coordinates.x < width
                && coordinates.y >= 0 && coordinates.y < height;
    }
}
