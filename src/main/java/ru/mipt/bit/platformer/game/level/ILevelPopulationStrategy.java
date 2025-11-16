package ru.mipt.bit.platformer.game.level;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public interface ILevelPopulationStrategy {

    LevelPopulation populate(TiledMapTileLayer groundLayer);
}
