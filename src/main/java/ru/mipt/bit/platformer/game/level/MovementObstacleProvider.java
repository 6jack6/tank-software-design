package ru.mipt.bit.platformer.game.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import ru.mipt.bit.platformer.game.model.BlockingObject;
import ru.mipt.bit.platformer.game.model.TankModel;
import ru.mipt.bit.platformer.game.model.TankObstacle;
import ru.mipt.bit.platformer.game.model.TreeModel;

public class MovementObstacleProvider {

    private final ILevelModel levelModel;
    private final List<TreeModel> fallbackTrees;
    private final List<TankModel> fallbackTanks;

    public MovementObstacleProvider(ILevelModel levelModel) {
        this.levelModel = Objects.requireNonNull(levelModel);
        this.fallbackTrees = null;
        this.fallbackTanks = null;
    }

    public MovementObstacleProvider(List<? extends TreeModel> staticObstacles,
                                    List<? extends TankModel> tanks) {
        this.levelModel = null;
        this.fallbackTrees = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(staticObstacles)));
        this.fallbackTanks = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(tanks)));
    }

    public Iterable<BlockingObject> getObstaclesFor(TankModel tank) {
        List<BlockingObject> result = new ArrayList<>();
        List<TreeModel> trees = fallbackTrees != null ? fallbackTrees : levelModel.getTrees();
        List<TankModel> tanks = fallbackTanks != null ? fallbackTanks : levelModel.getAllTanks();
        result.addAll(trees);
        for (TankModel other : tanks) {
            if (other == tank) {
                continue;
            }
            result.add(new TankObstacle(other));
        }
        return result;
    }
}
