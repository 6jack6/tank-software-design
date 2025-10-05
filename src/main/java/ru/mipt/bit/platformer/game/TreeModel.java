package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;

public class TreeModel {

    private final GridPoint2 coordinates;

    public TreeModel(GridPoint2 coordinates) {
        this.coordinates = new GridPoint2(coordinates);
    }

    public boolean blocks(GridPoint2 tileCoordinates) {
        return coordinates.equals(tileCoordinates);
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }
}
