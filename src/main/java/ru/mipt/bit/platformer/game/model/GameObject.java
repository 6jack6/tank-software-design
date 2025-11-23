package ru.mipt.bit.platformer.game.model;

import com.badlogic.gdx.math.GridPoint2;

public interface GameObject {

    GridPoint2 getCoordinates();

    String getTexturePath();

    default void update(float deltaTime) {
    }
}
