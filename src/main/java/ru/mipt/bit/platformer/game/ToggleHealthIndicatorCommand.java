package ru.mipt.bit.platformer.game;

import java.util.Objects;

public class ToggleHealthIndicatorCommand implements TankCommand {

    private final HealthIndicatorVisibility visibility;

    public ToggleHealthIndicatorCommand(HealthIndicatorVisibility visibility) {
        this.visibility = Objects.requireNonNull(visibility);
    }

    @Override
    public void execute() {
        visibility.toggle();
    }
}
