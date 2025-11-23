package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.config.TankConfig;
import ru.mipt.bit.platformer.game.level.LevelModel.LevelBounds;
import ru.mipt.bit.platformer.game.model.BlockingObject;
import ru.mipt.bit.platformer.game.model.TankModel;
import ru.mipt.bit.platformer.util.Direction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveTankCommandTest {

    private final TankModel tank = createTank(new GridPoint2(4, 4));
    private final LevelBounds bounds = new LevelBounds(10, 10);

    @Test
    void executesMoveWhenDestinationIsInsideBounds() {
        MoveTankCommand command = new MoveTankCommand(tank, Direction.UP,
                Collections::<BlockingObject>emptyList, bounds);

        command.execute();

        assertEquals(new GridPoint2(4, 5), tank.getDestination());
    }

    @Test
    void skipsMoveWhenDestinationIsOutsideBounds() {
        TankModel edgeTank = createTank(new GridPoint2(0, 0));
        MoveTankCommand command = new MoveTankCommand(edgeTank, Direction.LEFT,
                Collections::<BlockingObject>emptyList, bounds);

        command.execute();

        assertEquals(new GridPoint2(0, 0), edgeTank.getDestination());
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

    private static TankModel createTank(GridPoint2 coordinates) {
        return new TankModel(new TankConfig("texture.png", coordinates, 1f));
    }
}
