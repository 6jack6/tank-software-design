package ru.mipt.bit.platformer.game;

import java.util.ArrayList;
import java.util.List;
import ru.mipt.bit.platformer.util.Direction;

public class TankInputHandler implements ITankInputHandler {

    private final ITankModel tank;
    private final List<TankInputAction> actions = new ArrayList<>();

    public TankInputHandler(ITankModel tank) {
        this.tank = tank;
        registerDirectionalMovement();
    }

    @Override
    public void handleInput(Iterable<? extends ITreeModel> obstacles) {
        for (TankInputAction action : actions) {
            if (action.isPressed()) {
                action.execute(tank, obstacles);
            }
        }
    }

    @Override
    public void registerAction(TankInputAction action) {
        actions.add(action);
    }

    private void registerDirectionalMovement() {
        for (Direction direction : Direction.values()) {
            actions.add(new DirectionMovementAction(direction));
        }
    }

    private static final class DirectionMovementAction implements TankInputAction {

        private final Direction direction;

        private DirectionMovementAction(Direction direction) {
            this.direction = direction;
        }

        @Override
        public boolean isPressed() {
            return direction.isPressed();
        }

        @Override
        public void execute(ITankModel tank, Iterable<? extends ITreeModel> obstacles) {
            tank.attemptMove(direction, obstacles);
        }
    }
}
