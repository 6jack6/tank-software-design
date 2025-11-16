package ru.mipt.bit.platformer.game;

import java.util.Objects;
import ru.mipt.bit.platformer.game.graphics.HealthIndicatorTankGraphics.Visibility;

public class ToggleHealthIndicatorCommand implements ITankCommand {

    private final Visibility visibility;

    public ToggleHealthIndicatorCommand(Visibility visibility) {
        this.visibility = Objects.requireNonNull(visibility);
    }

    @Override
    public void execute() {
        visibility.toggle();
    }
}
