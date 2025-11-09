package ru.mipt.bit.platformer.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ToggleHealthIndicatorCommandTest {

    @Test
    void togglesVisibility() {
        HealthIndicatorVisibility visibility = new HealthIndicatorVisibility();
        ToggleHealthIndicatorCommand command = new ToggleHealthIndicatorCommand(visibility);

        command.execute();

        assertTrue(visibility.isEnabled(), "Toggle command should flip visibility on execute");
    }
}
