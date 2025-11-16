package ru.mipt.bit.platformer.game;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.game.graphics.HealthIndicatorTankGraphics.Visibility;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HealthIndicatorVisibilityTest {

    private final Visibility visibility = new Visibility();

    @Test
    void initiallyDisabled() {
        assertFalse(visibility.isEnabled());
    }

    @Test
    void togglesState() {
        visibility.toggle();
        assertTrue(visibility.isEnabled());

        visibility.toggle();
        assertFalse(visibility.isEnabled());
    }
}
