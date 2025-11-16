package ru.mipt.bit.platformer.game.level;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Disposable;
import java.util.List;
import ru.mipt.bit.platformer.game.model.ITankModel;
import ru.mipt.bit.platformer.game.model.ITreeModel;
import ru.mipt.bit.platformer.util.TileMovement;

public interface ILevelModel extends Disposable {

    TiledMap getMap();

    TiledMapTileLayer getGroundLayer();

    TileMovement getTileMovement();

    LevelModel.LevelBounds getBounds();

    List<ITreeModel> getTrees();

    List<ITankModel> getEnemyTanks();

    ITankModel getPlayerTank();

    List<ITankModel> getAllTanks();

    void update(float deltaTime);
}
