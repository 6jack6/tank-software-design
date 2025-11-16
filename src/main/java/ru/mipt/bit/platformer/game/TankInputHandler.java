package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.Gdx;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import ru.mipt.bit.platformer.game.ITankCommand;
import ru.mipt.bit.platformer.util.Direction;

public class TankInputHandler implements ITankInputHandler {

    private final List<DirectionalCommand> commands = new ArrayList<>();
    private final List<KeyCommand> keyCommands = new ArrayList<>();

    public TankInputHandler(Function<Direction, ITankCommand> commandFactory) {
        registerDirectionalCommands(commandFactory);
    }

    @Override
    public void handleInput() {
        for (DirectionalCommand command : commands) {
            if (command.isPressed()) {
                command.execute();
            }
        }
        for (KeyCommand command : keyCommands) {
            if (command.isPressed()) {
                command.execute();
            }
        }
    }

    private void registerDirectionalCommands(Function<Direction, ITankCommand> commandFactory) {
        for (Direction direction : Direction.values()) {
            commands.add(new DirectionalCommand(direction, commandFactory.apply(direction)));
        }
    }

    public void registerKeyCommand(int keyCode, boolean justPressed, ITankCommand command) {
        keyCommands.add(new KeyCommand(keyCode, justPressed, command));
    }

    private static final class DirectionalCommand {

        private final Direction direction;
        private final ITankCommand command;

        private DirectionalCommand(Direction direction, ITankCommand command) {
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

    private static final class KeyCommand {

        private final int keyCode;
        private final boolean justPressed;
        private final ITankCommand command;

        private KeyCommand(int keyCode, boolean justPressed, ITankCommand command) {
            this.keyCode = keyCode;
            this.justPressed = justPressed;
            this.command = command;
        }

        private boolean isPressed() {
            if (justPressed) {
                return Gdx.input.isKeyJustPressed(keyCode);
            }
            return Gdx.input.isKeyPressed(keyCode);
        }

        private void execute() {
            command.execute();
        }
    }
}
