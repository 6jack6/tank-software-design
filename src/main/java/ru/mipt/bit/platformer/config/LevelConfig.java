package ru.mipt.bit.platformer.config;

import com.badlogic.gdx.math.Interpolation;

public class LevelConfig {

    private final String mapPath;
    private final Interpolation interpolation;

    public LevelConfig(String mapPath, Interpolation interpolation) {
        this.mapPath = mapPath;
        this.interpolation = interpolation;
    }

    public String getMapPath() {
        return mapPath;
    }

    public Interpolation getInterpolation() {
        return interpolation;
    }
}
