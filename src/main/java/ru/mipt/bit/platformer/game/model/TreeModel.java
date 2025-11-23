package ru.mipt.bit.platformer.game.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.config.TreeConfig;

public class TreeModel implements GameObject, BlockingObject {

    private final GridPoint2 coordinates;
    private final String texturePath;

    public TreeModel(TreeConfig config) {
        this.coordinates = new GridPoint2(config.getCoordinates());
        this.texturePath = config.getTexturePath();
    }

    @Override
    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    @Override
    public String getTexturePath() {
        return texturePath;
    }

    @Override
    public boolean blocks(GridPoint2 tileCoordinates) {
        return coordinates.equals(tileCoordinates);
    }
}
