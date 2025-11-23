package ru.mipt.bit.platformer.game.model;

import com.badlogic.gdx.math.GridPoint2;

public interface BlockingObject {

    boolean blocks(GridPoint2 tile);
}
