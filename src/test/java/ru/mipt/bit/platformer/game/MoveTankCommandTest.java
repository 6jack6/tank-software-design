package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.game.level.LevelModel.LevelBounds;
import ru.mipt.bit.platformer.game.model.ITankModel;
import ru.mipt.bit.platformer.game.model.ITreeModel;
import ru.mipt.bit.platformer.util.Direction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveTankCommandTest {

    private final RecordingTank tank = new RecordingTank(new GridPoint2(4, 4));
    private final LevelBounds bounds = new LevelBounds(10, 10);

    @Test
    void executesMoveWhenDestinationIsInsideBounds() {
        MoveTankCommand command = new MoveTankCommand(tank, Direction.UP, Collections::emptyList, bounds);

        command.execute();

        assertTrue(tank.attemptTriggered.get());
        assertEquals(Direction.UP, tank.lastDirection.get());
    }

    @Test
    void skipsMoveWhenDestinationIsOutsideBounds() {
        RecordingTank edgeTank = new RecordingTank(new GridPoint2(0, 0));
        MoveTankCommand command = new MoveTankCommand(edgeTank, Direction.LEFT, Collections::emptyList, bounds);

        command.execute();

        assertFalse(edgeTank.attemptTriggered.get());
    }

    @Test
    void defersObstacleResolutionUntilExecution() {
        AtomicBoolean called = new AtomicBoolean(false);
        MoveTankCommand command = new MoveTankCommand(tank, Direction.RIGHT, () -> {
            called.set(true);
            return Collections.emptyList();
        }, bounds);

        command.execute();

        assertTrue(called.get());
    }

    private static final class RecordingTank implements ITankModel {

        private final GridPoint2 coordinates;
        private final GridPoint2 destination = new GridPoint2();
        private final AtomicBoolean attemptTriggered = new AtomicBoolean(false);
        private final AtomicReference<Direction> lastDirection = new AtomicReference<>();

        private RecordingTank(GridPoint2 coordinates) {
            this.coordinates = coordinates;
        }

        @Override
        public void attemptMove(Direction direction, Iterable<? extends ITreeModel> obstacles) {
            attemptTriggered.set(true);
            lastDirection.set(direction);
            direction.destinationFrom(coordinates, destination);
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
            return 1f;
        }

        @Override
        public float getRotation() {
            return 0f;
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
