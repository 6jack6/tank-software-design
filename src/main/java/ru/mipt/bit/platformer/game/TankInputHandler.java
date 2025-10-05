package ru.mipt.bit.platformer.game;

import java.util.ArrayList;
import java.util.List;
import ru.mipt.bit.platformer.util.Direction;

public class TankInputHandler {

    private final TankModel tank;
    private final List<TankInputAction> actions = new ArrayList<>();

    public TankInputHandler(TankModel tank) {
        this.tank = tank;
        registerDirectionalMovement();
    }

    public void handleInput(Iterable<TreeModel> obstacles) {
        for (TankInputAction action : actions) {
            if (action.isPressed()) {
                action.execute(tank, obstacles);
            }
        }
    }

    public void registerAction(TankInputAction action) {
        actions.add(action);
    }

    public interface TankInputAction {
        boolean isPressed();

        void execute(TankModel tank, Iterable<TreeModel> obstacles);
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
        public void execute(TankModel tank, Iterable<TreeModel> obstacles) {
            tank.attemptMove(direction, obstacles);
        }
    }
}
