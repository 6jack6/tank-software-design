package ru.mipt.bit.platformer.game.model;

import com.badlogic.gdx.math.GridPoint2;

public class TankObstacle implements BlockingObject {

    private final TankModel tank;

    public TankObstacle(TankModel tank) {
        this.tank = tank;
    }

    public boolean blocks(GridPoint2 tileCoordinates) {
        return tank.getCoordinates().equals(tileCoordinates)
                || tank.getDestination().equals(tileCoordinates);
    }
}
