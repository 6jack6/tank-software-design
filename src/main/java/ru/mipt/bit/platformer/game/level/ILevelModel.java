package ru.mipt.bit.platformer.game.level;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Disposable;
import java.util.List;
import ru.mipt.bit.platformer.game.model.GameObject;
import ru.mipt.bit.platformer.util.TileMovement;

public interface ILevelModel extends Disposable {

    TiledMap getMap();

    TiledMapTileLayer getGroundLayer();

    TileMovement getTileMovement();

    LevelModel.LevelBounds getBounds();

    List<GameObject> getTrees();

    List<GameObject> getEnemyTanks();

    GameObject getPlayerTank();

    List<GameObject> getAllTanks();

    void update(float deltaTime);
}
