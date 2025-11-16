package ru.mipt.bit.platformer.game.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import ru.mipt.bit.platformer.game.model.ITankModel;
import ru.mipt.bit.platformer.game.model.ITreeModel;
import ru.mipt.bit.platformer.game.model.TankObstacle;

public class MovementObstacleProvider {

    private final ILevelModel levelModel;
    private final List<ITreeModel> fallbackTrees;
    private final List<ITankModel> fallbackTanks;

    public MovementObstacleProvider(ILevelModel levelModel) {
        this.levelModel = Objects.requireNonNull(levelModel);
        this.fallbackTrees = null;
        this.fallbackTanks = null;
    }

    public MovementObstacleProvider(List<ITreeModel> staticObstacles,
                                    List<ITankModel> tanks) {
        this.levelModel = null;
        this.fallbackTrees = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(staticObstacles)));
        this.fallbackTanks = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(tanks)));
    }

    public Iterable<ITreeModel> getObstaclesFor(ITankModel tank) {
        List<ITreeModel> result = new ArrayList<>();
        List<ITreeModel> trees = fallbackTrees != null ? fallbackTrees : levelModel.getTrees();
        List<ITankModel> tanks = fallbackTanks != null ? fallbackTanks : levelModel.getAllTanks();
        result.addAll(trees);
        for (ITankModel other : tanks) {
            if (other == tank) {
                continue;
            }
            result.add(new TankObstacle(other));
        }
        return result;
    }
}
