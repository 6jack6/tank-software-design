package ru.mipt.bit.platformer.game.level;

public interface ILevelObserver {

    void onObjectAdded(LevelObjectEvent event);

    void onObjectRemoved(LevelObjectEvent event);
}
