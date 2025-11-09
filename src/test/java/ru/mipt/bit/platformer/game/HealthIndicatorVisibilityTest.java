package ru.mipt.bit.platformer.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HealthIndicatorVisibilityTest {

    private final HealthIndicatorVisibility visibility = new HealthIndicatorVisibility();

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
