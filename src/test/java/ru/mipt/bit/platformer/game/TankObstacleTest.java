package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankObstacleTest {

    private final TestTank tank = new TestTank(new GridPoint2(2, 2), new GridPoint2(3, 2));
    private final TankObstacle obstacle = new TankObstacle(tank);

    @Test
    void blocksOriginAndDestinationTiles() {
        assertTrue(obstacle.blocks(new GridPoint2(2, 2)));
        assertTrue(obstacle.blocks(new GridPoint2(3, 2)));
    }

    @Test
    void doesNotBlockOtherTiles() {
        assertFalse(obstacle.blocks(new GridPoint2(1, 1)));
    }

    private static final class TestTank implements ITankModel {

        private final GridPoint2 coordinates;
        private final GridPoint2 destination;

        private TestTank(GridPoint2 coordinates, GridPoint2 destination) {
            this.coordinates = coordinates;
            this.destination = destination;
        }

        @Override
        public void attemptMove(ru.mipt.bit.platformer.util.Direction direction, Iterable<? extends ITreeModel> obstacles) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void update(float deltaTime) {
        }

        @Override
        public GridPoint2 getCoordinates() {
            return coordinates;
        }

        @Override
        public GridPoint2 getDestination() {
            return destination;
        }

        @Override
        public float getMovementProgress() {
            return 0;
        }

        @Override
        public float getRotation() {
            return 0;
        }

        @Override
        public String getTexturePath() {
            return "test";
        }

        @Override
        public int getHealthPoints() {
            return 100;
        }
    }
}
