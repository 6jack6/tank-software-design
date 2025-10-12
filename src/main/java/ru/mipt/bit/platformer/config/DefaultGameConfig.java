package ru.mipt.bit.platformer.config;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;

import java.util.Collections;
import java.util.List;

public class DefaultGameConfig {

    private static final String LEVEL_MAP_PATH = "level.tmx";
    private static final Interpolation LEVEL_INTERPOLATION = Interpolation.smooth;

    private static final String TANK_TEXTURE_PATH = "images/tank_blue.png";
    private static final GridPoint2 TANK_INITIAL_COORDINATES = new GridPoint2(1, 1);
    private static final float TANK_MOVEMENT_SPEED = 0.4f;

    private static final String TREE_TEXTURE_PATH = "images/greenTree.png";
    private static final GridPoint2 TREE_COORDINATES = new GridPoint2(1, 3);

    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 1024;

    private static final float CLEAR_COLOR_R = 0f;
    private static final float CLEAR_COLOR_G = 0f;
    private static final float CLEAR_COLOR_B = 0.2f;
    private static final float CLEAR_COLOR_A = 1f;

    public LevelConfig createLevelConfig() {
        return new LevelConfig(LEVEL_MAP_PATH, LEVEL_INTERPOLATION);
    }

    public TankConfig createTankConfig() {
        return new TankConfig(TANK_TEXTURE_PATH, TANK_INITIAL_COORDINATES, TANK_MOVEMENT_SPEED);
    }

    public List<TreeConfig> createTreeConfigs() {
        return Collections.singletonList(new TreeConfig(TREE_TEXTURE_PATH, TREE_COORDINATES));
    }

    public WindowConfig createWindowConfig() {
        return new WindowConfig(WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public GraphicsConfig createGraphicsConfig() {
        return new GraphicsConfig(CLEAR_COLOR_R, CLEAR_COLOR_G, CLEAR_COLOR_B, CLEAR_COLOR_A);
    }
}
