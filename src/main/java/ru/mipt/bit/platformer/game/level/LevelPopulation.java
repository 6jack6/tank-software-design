package ru.mipt.bit.platformer.game.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LevelPopulation {

    private final GridPoint2 playerSpawn;
    private final List<GridPoint2> treeCoordinates;

    public LevelPopulation(GridPoint2 playerSpawn, List<GridPoint2> treeCoordinates) {
        this.playerSpawn = new GridPoint2(playerSpawn);
        List<GridPoint2> copy = new ArrayList<>(treeCoordinates.size());
        for (GridPoint2 coordinate : treeCoordinates) {
            copy.add(new GridPoint2(coordinate));
        }
        this.treeCoordinates = Collections.unmodifiableList(copy);
    }

    public GridPoint2 getPlayerSpawn() {
        return new GridPoint2(playerSpawn);
    }

    public List<GridPoint2> getTreeCoordinates() {
        List<GridPoint2> copy = new ArrayList<>(treeCoordinates.size());
        for (GridPoint2 coordinate : treeCoordinates) {
            copy.add(new GridPoint2(coordinate));
        }
        return copy;
    }
}
