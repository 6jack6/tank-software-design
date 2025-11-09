package ru.mipt.bit.platformer.game.level;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Random;
import java.util.Objects;

public class RandomLevelPopulationStrategy implements LevelPopulationStrategy {

    private final int requestedTreeCount;
    private final Random random;

    public RandomLevelPopulationStrategy(int treeCount) {
        this(treeCount, new Random());
    }

    public RandomLevelPopulationStrategy(int treeCount, long seed) {
        this(treeCount, new Random(seed));
    }

    public RandomLevelPopulationStrategy(int treeCount, Random random) {
        if (treeCount < 0) {
            throw new IllegalArgumentException("Tree count must be non-negative");
        }
        this.requestedTreeCount = treeCount;
        this.random = Objects.requireNonNull(random);
    }

    @Override
    public LevelPopulation populate(TiledMapTileLayer groundLayer) {
        int width = groundLayer.getWidth();
        int height = groundLayer.getHeight();
        int totalTiles = width * height;
        if (totalTiles == 0) {
            throw new IllegalStateException("Map layer has no tiles");
        }

        int treeCount = Math.min(requestedTreeCount, Math.max(totalTiles - 1, 0));
        List<GridPoint2> trees = new ArrayList<>(treeCount);
        Set<Long> occupied = new HashSet<>();

        while (trees.size() < treeCount) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            if (occupied.add(encode(x, y))) {
                trees.add(new GridPoint2(x, y));
            }
        }

        GridPoint2 spawn = pickFreeTile(width, height, occupied);
        return new LevelPopulation(spawn, trees);
    }

    private GridPoint2 pickFreeTile(int width, int height, Set<Long> occupied) {
        int totalTiles = width * height;
        if (occupied.size() >= totalTiles) {
            throw new IllegalStateException("No free tiles left for the player spawn");
        }
        while (true) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            if (occupied.add(encode(x, y))) {
                return new GridPoint2(x, y);
            }
        }
    }

    private static long encode(int x, int y) {
        return (((long) x) << 32) | (y & 0xffffffffL);
    }
}
