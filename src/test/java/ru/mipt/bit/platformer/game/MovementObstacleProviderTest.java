package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.config.TankConfig;
import ru.mipt.bit.platformer.config.TreeConfig;
import ru.mipt.bit.platformer.game.level.MovementObstacleProvider;
import ru.mipt.bit.platformer.game.model.BlockingObject;
import ru.mipt.bit.platformer.game.model.TankModel;
import ru.mipt.bit.platformer.game.model.TreeModel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MovementObstacleProviderTest {

    private final TreeModel tree = new TreeModel(new TreeConfig("tree.png", new GridPoint2(5, 5)));
    private final TankModel player = new TankModel(new TankConfig("texture.png", new GridPoint2(1, 1), 1f));
    private final TankModel enemy = new TankModel(new TankConfig("texture.png", new GridPoint2(3, 3), 1f));
    private final MovementObstacleProvider provider = new MovementObstacleProvider(
            List.of(tree), List.of(player, enemy));

    @Test
    void suppliesStaticAndDynamicObstacles() {
        List<BlockingObject> obstacles = collect(provider.getObstaclesFor(player));
        assertEquals(2, obstacles.size());
        assertTrue(obstacles.contains(tree));
        assertTrue(obstacles.stream().anyMatch(o -> o.blocks(enemy.getCoordinates())));
    }

    @Test
    void doesNotExposeTankAsOwnObstacle() {
        List<BlockingObject> obstacles = collect(provider.getObstaclesFor(enemy));
        assertTrue(obstacles.stream()
                .noneMatch(o -> !(o instanceof TreeModel) && o.blocks(enemy.getCoordinates())));
        assertTrue(obstacles.stream().anyMatch(o -> o.blocks(player.getCoordinates())));
    }

    private static List<BlockingObject> collect(Iterable<? extends BlockingObject> obstacles) {
        List<BlockingObject> list = new ArrayList<>();
        for (BlockingObject obstacle : obstacles) {
            list.add(obstacle);
        }
        return list;
    }
}
