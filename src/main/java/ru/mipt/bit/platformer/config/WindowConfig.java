package ru.mipt.bit.platformer.config;

public class WindowConfig {

    private final int width;
    private final int height;

    public WindowConfig(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
