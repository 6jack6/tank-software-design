package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;

public class TankObstacle implements ITreeModel {

    private final ITankModel tank;

    public TankObstacle(ITankModel tank) {
        this.tank = tank;
    }

    @Override
    public boolean blocks(GridPoint2 tileCoordinates) {
        return tank.getCoordinates().equals(tileCoordinates) || tank.getDestination().equals(tileCoordinates);
    }

    @Override
    public GridPoint2 getCoordinates() {
        return tank.getCoordinates();
    }

    @Override
    public String getTexturePath() {
        return tank.getTexturePath();
    }
}
