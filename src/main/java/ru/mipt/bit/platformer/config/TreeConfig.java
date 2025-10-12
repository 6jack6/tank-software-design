package ru.mipt.bit.platformer.config;

import com.badlogic.gdx.math.GridPoint2;

public class TreeConfig {

    private final String texturePath;
    private final GridPoint2 coordinates;

    public TreeConfig(String texturePath, GridPoint2 coordinates) {
        this.texturePath = texturePath;
        this.coordinates = new GridPoint2(coordinates);
    }

    public String getTexturePath() {
        return texturePath;
    }

    public GridPoint2 getCoordinates() {
        return new GridPoint2(coordinates);
    }
}
