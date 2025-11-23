package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.config.TankConfig;
import ru.mipt.bit.platformer.game.model.TankModel;
import ru.mipt.bit.platformer.game.model.TankObstacle;
import ru.mipt.bit.platformer.util.Direction;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankObstacleTest {

    private final TankModel tank = new TankModel(new TankConfig("texture.png", new GridPoint2(2, 2), 1f));
    private final TankObstacle obstacle;

    TankObstacleTest() {
        tank.attemptMove(Direction.RIGHT, java.util.Collections.emptyList());
        obstacle = new TankObstacle(tank);
    }

    @Test
    void blocksOriginAndDestinationTiles() {
        assertTrue(obstacle.blocks(new GridPoint2(2, 2)));
        assertTrue(obstacle.blocks(new GridPoint2(3, 2)));
    }

    @Test
    void doesNotBlockOtherTiles() {
        assertFalse(obstacle.blocks(new GridPoint2(1, 1)));
    }

}
