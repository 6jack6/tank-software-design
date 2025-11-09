package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.util.Direction;

public interface ITankModel {

    void attemptMove(Direction direction, Iterable<? extends ITreeModel> obstacles);

    void update(float deltaTime);

    GridPoint2 getCoordinates();

    GridPoint2 getDestination();

    float getMovementProgress();

    float getRotation();

    String getTexturePath();

    int getHealthPoints();
}
