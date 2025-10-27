package ru.mipt.bit.platformer.config;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.mipt.bit.platformer.game.level.FileLevelPopulationStrategy;
import ru.mipt.bit.platformer.game.level.LevelPopulationStrategy;

public class DefaultGameConfig {

    private static final String LEVEL_MAP_PATH = "level.tmx";
    private static final Interpolation LEVEL_INTERPOLATION = Interpolation.smooth;

    private static final String TANK_TEXTURE_PATH = "images/tank_blue.png";
    private static final float TANK_MOVEMENT_SPEED = 0.4f;

    private static final String TREE_TEXTURE_PATH = "images/greenTree.png";

    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 1024;

    private static final float CLEAR_COLOR_R = 0f;
    private static final float CLEAR_COLOR_G = 0f;
    private static final float CLEAR_COLOR_B = 0.2f;
    private static final float CLEAR_COLOR_A = 1f;

    private static final String DEFAULT_LEVEL_LAYOUT = "level.txt";

    private final LevelPopulationStrategy levelPopulationStrategy;

    public DefaultGameConfig() {
        this(new FileLevelPopulationStrategy(DEFAULT_LEVEL_LAYOUT));
    }

    public DefaultGameConfig(LevelPopulationStrategy levelPopulationStrategy) {
        this.levelPopulationStrategy = Objects.requireNonNull(levelPopulationStrategy);
    }

    public LevelConfig createLevelConfig() {
        return new LevelConfig(LEVEL_MAP_PATH, LEVEL_INTERPOLATION);
    }

    public TankConfig createTankConfig(GridPoint2 initialCoordinates) {
        return new TankConfig(TANK_TEXTURE_PATH, initialCoordinates, TANK_MOVEMENT_SPEED);
    }

    public List<TreeConfig> createTreeConfigs(List<GridPoint2> coordinates) {
        List<TreeConfig> configs = new ArrayList<>(coordinates.size());
        for (GridPoint2 coordinate : coordinates) {
            configs.add(new TreeConfig(TREE_TEXTURE_PATH, coordinate));
        }
        return configs;
    }

    public WindowConfig createWindowConfig() {
        return new WindowConfig(WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public GraphicsConfig createGraphicsConfig() {
        return new GraphicsConfig(CLEAR_COLOR_R, CLEAR_COLOR_G, CLEAR_COLOR_B, CLEAR_COLOR_A);
    }

    public LevelPopulationStrategy getLevelPopulationStrategy() {
        return levelPopulationStrategy;
    }
}
