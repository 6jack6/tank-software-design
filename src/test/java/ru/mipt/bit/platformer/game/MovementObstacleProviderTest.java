package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.game.level.MovementObstacleProvider;
import ru.mipt.bit.platformer.game.model.ITankModel;
import ru.mipt.bit.platformer.game.model.ITreeModel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MovementObstacleProviderTest {

    private final FixedObstacle tree = new FixedObstacle(new GridPoint2(5, 5));
    private final TestTank player = new TestTank(new GridPoint2(1, 1));
    private final TestTank enemy = new TestTank(new GridPoint2(3, 3));
    private final MovementObstacleProvider provider = new MovementObstacleProvider(
            List.of(tree), List.of(player, enemy));

    @Test
    void suppliesStaticAndDynamicObstacles() {
        List<ITreeModel> obstacles = collect(provider.getObstaclesFor(player));
        assertEquals(2, obstacles.size());
        assertTrue(obstacles.contains(tree));
        assertTrue(obstacles.stream().anyMatch(o -> o.blocks(enemy.getCoordinates())));
    }

    @Test
    void doesNotExposeTankAsOwnObstacle() {
        List<ITreeModel> obstacles = collect(provider.getObstaclesFor(enemy));
        assertTrue(obstacles.stream()
                .noneMatch(o -> !(o instanceof FixedObstacle) && o.blocks(enemy.getCoordinates())));
        assertTrue(obstacles.stream().anyMatch(o -> o.blocks(player.getCoordinates())));
    }

    private static List<ITreeModel> collect(Iterable<ITreeModel> obstacles) {
        List<ITreeModel> list = new ArrayList<>();
        for (ITreeModel obstacle : obstacles) {
            list.add(obstacle);
        }
        return list;
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
            return "tree";
        }
    }

    private static final class TestTank implements ITankModel {

        private final GridPoint2 coordinates;

        private TestTank(GridPoint2 coordinates) {
            this.coordinates = coordinates;
        }

        @Override
        public void attemptMove(ru.mipt.bit.platformer.util.Direction direction,
                                Iterable<? extends ITreeModel> obstacles) {
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
            return coordinates;
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

        @Override
        public ru.mipt.bit.platformer.util.Direction getDirection() {
            return ru.mipt.bit.platformer.util.Direction.UP;
        }

        @Override
        public void applyDamage(int damage) {
        }

        @Override
        public boolean isDestroyed() {
            return false;
        }
    }
}
