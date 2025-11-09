package ru.mipt.bit.platformer.game;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import ru.mipt.bit.platformer.util.Direction;

public class TankInputHandler implements ITankInputHandler {

    private final List<DirectionalCommand> commands = new ArrayList<>();

    public TankInputHandler(Function<Direction, TankCommand> commandFactory) {
        registerDirectionalCommands(commandFactory);
    }

    @Override
    public void handleInput() {
        for (DirectionalCommand command : commands) {
            if (command.isPressed()) {
                command.execute();
            }
        }
    }

    private void registerDirectionalCommands(Function<Direction, TankCommand> commandFactory) {
        for (Direction direction : Direction.values()) {
            commands.add(new DirectionalCommand(direction, commandFactory.apply(direction)));
        }
    }

    private static final class DirectionalCommand {

        private final Direction direction;
        private final TankCommand command;

        private DirectionalCommand(Direction direction, TankCommand command) {
            this.direction = direction;
            this.command = command;
        }

        private boolean isPressed() {
            return direction.isPressed();
        }

        private void execute() {
            command.execute();
        }
    }
}
