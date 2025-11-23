package ru.mipt.bit.platformer.game.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import ru.mipt.bit.platformer.game.model.BlockingObject;
import ru.mipt.bit.platformer.game.model.GameObject;
import ru.mipt.bit.platformer.game.model.TankModel;
import ru.mipt.bit.platformer.game.model.TankObstacle;

public class MovementObstacleProvider {

    private final ILevelModel levelModel;
    private final List<GameObject> fallbackTrees;
    private final List<GameObject> fallbackTanks;

    public MovementObstacleProvider(ILevelModel levelModel) {
        this.levelModel = Objects.requireNonNull(levelModel);
        this.fallbackTrees = null;
        this.fallbackTanks = null;
    }

    public MovementObstacleProvider(List<? extends GameObject> staticObstacles,
                                    List<? extends GameObject> tanks) {
        this.levelModel = null;
        this.fallbackTrees = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(staticObstacles)));
        this.fallbackTanks = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(tanks)));
    }

    public Iterable<BlockingObject> getObstaclesFor(TankModel tank) {
        List<BlockingObject> result = new ArrayList<>();
        List<GameObject> trees = fallbackTrees != null ? fallbackTrees : levelModel.getTrees();
        List<GameObject> tanks = fallbackTanks != null ? fallbackTanks : levelModel.getAllTanks();
        for (GameObject tree : trees) {
            if (!(tree instanceof BlockingObject)) {
                continue;
            }
            result.add((BlockingObject) tree);
        }
        for (GameObject other : tanks) {
            if (!(other instanceof TankModel)) {
                continue;
            }
            TankModel otherTank = (TankModel) other;
            if (otherTank == tank) {
                continue;
            }
            result.add(new TankObstacle(otherTank));
        }
        return result;
    }
}
