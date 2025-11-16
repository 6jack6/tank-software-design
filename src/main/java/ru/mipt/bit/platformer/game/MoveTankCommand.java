package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;
import java.util.Objects;
import java.util.function.Supplier;
import ru.mipt.bit.platformer.game.level.LevelModel.LevelBounds;
import ru.mipt.bit.platformer.game.model.ITankModel;
import ru.mipt.bit.platformer.game.model.ITreeModel;
import ru.mipt.bit.platformer.util.Direction;

public class MoveTankCommand implements ITankCommand {

    private final ITankModel tank;
    private final Direction direction;
    private final Supplier<Iterable<? extends ITreeModel>> obstaclesSupplier;
    private final LevelBounds levelBounds;
    private final GridPoint2 candidate = new GridPoint2();

    public MoveTankCommand(ITankModel tank,
                           Direction direction,
                           Supplier<Iterable<? extends ITreeModel>> obstaclesSupplier,
                           LevelBounds levelBounds) {
        this.tank = Objects.requireNonNull(tank);
        this.direction = Objects.requireNonNull(direction);
        this.obstaclesSupplier = Objects.requireNonNull(obstaclesSupplier);
        this.levelBounds = Objects.requireNonNull(levelBounds);
    }

    @Override
    public void execute() {
        direction.destinationFrom(tank.getCoordinates(), candidate);
        if (!levelBounds.contains(candidate)) {
            return;
        }
        tank.attemptMove(direction, obstaclesSupplier.get());
    }
}
