package ru.mipt.bit.platformer.game;

import java.util.Objects;
import java.util.function.Supplier;
import ru.mipt.bit.platformer.util.Direction;

public class MoveTankCommand implements TankCommand {

    private final ITankModel tank;
    private final Direction direction;
    private final Supplier<Iterable<? extends ITreeModel>> obstaclesSupplier;

    public MoveTankCommand(ITankModel tank,
                           Direction direction,
                           Supplier<Iterable<? extends ITreeModel>> obstaclesSupplier) {
        this.tank = Objects.requireNonNull(tank);
        this.direction = Objects.requireNonNull(direction);
        this.obstaclesSupplier = Objects.requireNonNull(obstaclesSupplier);
    }

    @Override
    public void execute() {
        tank.attemptMove(direction, obstaclesSupplier.get());
    }
}
