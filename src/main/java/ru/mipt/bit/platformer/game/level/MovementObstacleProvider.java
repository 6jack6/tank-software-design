package ru.mipt.bit.platformer.game.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import ru.mipt.bit.platformer.game.model.ITankModel;
import ru.mipt.bit.platformer.game.model.ITreeModel;
import ru.mipt.bit.platformer.game.model.TankObstacle;

public class MovementObstacleProvider {

    private final List<ITreeModel> staticObstacles;
    private final List<ITankModel> tanks;

    public MovementObstacleProvider(List<ITreeModel> staticObstacles,
                                    List<ITankModel> tanks) {
        this.staticObstacles = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(staticObstacles)));
        this.tanks = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(tanks)));
    }

    public Iterable<ITreeModel> getObstaclesFor(ITankModel tank) {
        List<ITreeModel> result = new ArrayList<>(staticObstacles.size() + Math.max(tanks.size() - 1, 0));
        result.addAll(staticObstacles);
        for (ITankModel other : tanks) {
            if (other == tank) {
                continue;
            }
            result.add(new TankObstacle(other));
        }
        return result;
    }
}
