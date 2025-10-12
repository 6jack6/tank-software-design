package ru.mipt.bit.platformer.config;

public class GraphicsConfig {

    private final float clearColorR;
    private final float clearColorG;
    private final float clearColorB;
    private final float clearColorA;

    public GraphicsConfig(float clearColorR, float clearColorG, float clearColorB, float clearColorA) {
        this.clearColorR = clearColorR;
        this.clearColorG = clearColorG;
        this.clearColorB = clearColorB;
        this.clearColorA = clearColorA;
    }

    public float getClearColorR() {
        return clearColorR;
    }

    public float getClearColorG() {
        return clearColorG;
    }

    public float getClearColorB() {
        return clearColorB;
    }

    public float getClearColorA() {
        return clearColorA;
    }
}
