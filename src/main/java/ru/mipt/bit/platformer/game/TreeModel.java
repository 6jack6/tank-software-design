package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.config.TreeConfig;

public class TreeModel implements ITreeModel {

    private final GridPoint2 coordinates;
    private final String texturePath;

    public TreeModel(TreeConfig config) {
        GridPoint2 configCoordinates = config.getCoordinates();
        this.coordinates = new GridPoint2(configCoordinates);
        this.texturePath = config.getTexturePath();
    }

    @Override
    public boolean blocks(GridPoint2 tileCoordinates) {
        return coordinates.equals(tileCoordinates);
    }

    @Override
    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    @Override
    public String getTexturePath() {
        return texturePath;
    }
}
