package ru.mipt.bit.platformer.game;

/**
 * Shared state object that keeps track of whether health indicators should be rendered.
 * Acts as a collaboration point between the rendering decorator and the toggle command.
 */
public class HealthIndicatorVisibility {

    private boolean enabled;

    public void toggle() {
        enabled = !enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
