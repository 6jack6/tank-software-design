package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelBoundsTest {

    private final LevelBounds bounds = new LevelBounds(10, 5);

    @Test
    void containsCoordinatesInsideBounds() {
        assertTrue(bounds.contains(new GridPoint2(9, 4)));
    }

    @Test
    void doesNotContainCoordinatesOutsideBounds() {
        assertFalse(bounds.contains(new GridPoint2(-1, 2)));
        assertFalse(bounds.contains(new GridPoint2(10, 0)));
        assertFalse(bounds.contains(new GridPoint2(0, 5)));
    }
}
