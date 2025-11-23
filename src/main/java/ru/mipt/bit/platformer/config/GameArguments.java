package ru.mipt.bit.platformer.config;

import ru.mipt.bit.platformer.game.level.FileLevelPopulationStrategy;
import ru.mipt.bit.platformer.game.level.ILevelPopulationStrategy;
import ru.mipt.bit.platformer.game.level.RandomLevelPopulationStrategy;

/**
 * Parses command line arguments and produces game configuration objects.
 */
public class GameArguments {
    private static final String RANDOM_FLAG = "--random";
    private static final String LAYOUT_PREFIX = "--layout=";
    private static final int DEFAULT_RANDOM_TREES = 12;

    private final String[] args;

    public GameArguments(String[] args) {
        this.args = args == null ? new String[0] : args.clone();
    }

    public DefaultGameConfig createGameConfig() {
        ILevelPopulationStrategy strategy = resolveLevelPopulationStrategy();
        if (strategy == null) {
            return new DefaultGameConfig();
        }
        return new DefaultGameConfig(strategy);
    }

    private ILevelPopulationStrategy resolveLevelPopulationStrategy() {
        for (String arg : args) {
            if (arg.startsWith(RANDOM_FLAG)) {
                int treeCount = DEFAULT_RANDOM_TREES;
                int equalsIndex = arg.indexOf('=');
                if (equalsIndex >= 0) {
                    String value = arg.substring(equalsIndex + 1);
                    if (value.isEmpty()) {
                        throw new IllegalArgumentException("Tree count value in --random must not be empty");
                    }
                    treeCount = parseTreeCount(value);
                }
                return new RandomLevelPopulationStrategy(treeCount);
            }
            if (arg.startsWith(LAYOUT_PREFIX)) {
                String path = arg.substring(LAYOUT_PREFIX.length());
                if (path.isEmpty()) {
                    throw new IllegalArgumentException("Layout path must not be empty");
                }
                return new FileLevelPopulationStrategy(path);
            }
        }
        return null;
    }

    private static int parseTreeCount(String value) {
        try {
            int count = Integer.parseInt(value);
            if (count < 0) {
                throw new IllegalArgumentException("Tree count must be non-negative");
            }
            return count;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Failed to parse tree count: " + value, e);
        }
    }
}
