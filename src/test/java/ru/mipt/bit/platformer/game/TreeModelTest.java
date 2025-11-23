package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.config.TreeConfig;
import ru.mipt.bit.platformer.game.model.TreeModel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TreeModelTest {

    private final TreeConfig config = new TreeConfig("tree.png", new GridPoint2(2, 3));
    private final TreeModel tree = new TreeModel(config);

    @Test
    void reportsBlockingOnExactCoordinates() {
        assertTrue(tree.blocks(new GridPoint2(2, 3)));
        assertFalse(tree.blocks(new GridPoint2(1, 3)));
    }

    @Test
    void exposesConfiguredCoordinates() {
        assertEquals(new GridPoint2(2, 3), tree.getCoordinates());
    }
}
