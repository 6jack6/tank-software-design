package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.config.TankConfig;
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
        ITreeModel blockingTree = new FixedObstacle(new GridPoint2(0, 1));

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

    private static final class FixedObstacle implements ITreeModel {

        private final GridPoint2 coordinates;

        private FixedObstacle(GridPoint2 coordinates) {
            this.coordinates = coordinates;
        }

        @Override
        public boolean blocks(GridPoint2 tileCoordinates) {
            return coordinates.equals(tileCoordinates);
        }

        @Override
        public GridPoint2 getCoordinates() {
            return coordinates;
        }

        @Override
        public String getTexturePath() {
            return "tree.png";
        }
    }
}
