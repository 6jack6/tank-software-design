package ru.mipt.bit.platformer.game;

import java.util.ArrayList;
import java.util.List;
import ru.mipt.bit.platformer.util.Direction;

public class TankInputHandler {

    private final Tank tank;
    private final List<TankInputAction> actions = new ArrayList<>();

    public TankInputHandler(Tank tank) {
        this.tank = tank;
        registerDirectionalMovement();
    }

    public void handleInput(Iterable<Tree> obstacles) {
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

        void execute(Tank tank, Iterable<Tree> obstacles);
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
        public void execute(Tank tank, Iterable<Tree> obstacles) {
            tank.tryMove(direction, obstacles);
        }
    }
}
