package ru.mipt.bit.platformer.game;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.game.graphics.HealthIndicatorTankGraphics.Visibility;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ToggleHealthIndicatorCommandTest {

    @Test
    void togglesVisibility() {
        Visibility visibility = new Visibility();
        ToggleHealthIndicatorCommand command = new ToggleHealthIndicatorCommand(visibility);

        command.execute();

        assertTrue(visibility.isEnabled(), "Toggle command should flip visibility on execute");
    }
}
