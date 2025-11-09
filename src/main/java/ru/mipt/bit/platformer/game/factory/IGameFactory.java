package ru.mipt.bit.platformer.game.factory;

import ru.mipt.bit.platformer.config.WindowConfig;

public interface IGameFactory {

    GameContext createGameContext();

    WindowConfig getWindowConfig();
}
