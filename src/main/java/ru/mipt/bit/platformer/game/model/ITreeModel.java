package ru.mipt.bit.platformer.game.model;

import com.badlogic.gdx.math.GridPoint2;

public interface ITreeModel {

    boolean blocks(GridPoint2 tileCoordinates);

    GridPoint2 getCoordinates();

    String getTexturePath();
}
