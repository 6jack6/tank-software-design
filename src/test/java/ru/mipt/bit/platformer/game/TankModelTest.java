package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.config.TankConfig;
import ru.mipt.bit.platformer.config.TreeConfig;
import ru.mipt.bit.platformer.game.model.TankModel;
import ru.mipt.bit.platformer.game.model.TreeModel;
import ru.mipt.bit.platformer.util.Direction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankModelTest {

    private static final TankConfig CONFIG = new TankConfig("texture.png", new GridPoint2(0, 0), 0.5f);

    @Test
    void movesToRequestedTileWhenUnblocked() {
        TankModel tank = new TankModel(CONFIG);

        tank.attemptMove(Direction.UP, Collections.emptyList());

        assertEquals(new GridPoint2(0, 1), tank.getDestination());
    }

    @Test
    void staysInPlaceWhenBlockedByObstacle() {
        TankModel tank = new TankModel(CONFIG);
        TreeModel blockingTree = new TreeModel(new TreeConfig("tree.png", new GridPoint2(0, 1)));

        tank.attemptMove(Direction.UP, List.of(blockingTree));

        assertEquals(new GridPoint2(0, 0), tank.getDestination());
    }

    @Test
    void updatesCoordinatesAfterCompletingMovement() {
        TankModel tank = new TankModel(CONFIG);
        tank.attemptMove(Direction.RIGHT, Collections.emptyList());

        tank.update(0.5f);

        assertEquals(new GridPoint2(1, 0), tank.getCoordinates());
        assertTrue(tank.getMovementProgress() >= 1f);
    }

    @Test
    void healthPointsAreWithinExpectedRange() {
        TankModel tank = new TankModel(CONFIG);

        assertTrue(tank.getHealthPoints() >= 80 && tank.getHealthPoints() <= 100,
                "Health points should be between 80 and 100 inclusive");
    }

}
