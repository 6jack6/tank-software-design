package ru.mipt.bit.platformer.config;

import com.badlogic.gdx.math.GridPoint2;

public class TankConfig {

    private final String texturePath;
    private final GridPoint2 initialCoordinates;
    private final float movementSpeed;

    public TankConfig(String texturePath, GridPoint2 initialCoordinates, float movementSpeed) {
        this.texturePath = texturePath;
        this.initialCoordinates = new GridPoint2(initialCoordinates);
        this.movementSpeed = movementSpeed;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public GridPoint2 getInitialCoordinates() {
        return new GridPoint2(initialCoordinates);
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }
}
