package ru.mipt.bit.platformer.game.level;

public class LevelObjectEvent {

    private final LevelObjectType type;
    private final Object model;

    public LevelObjectEvent(LevelObjectType type, Object model) {
        this.type = type;
        this.model = model;
    }

    public LevelObjectType getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    public <T> T getModel() {
        return (T) model;
    }
}
