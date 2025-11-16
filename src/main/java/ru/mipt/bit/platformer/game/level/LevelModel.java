package ru.mipt.bit.platformer.game.level;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import ru.mipt.bit.platformer.config.LevelConfig;
import ru.mipt.bit.platformer.game.model.BulletModel;
import ru.mipt.bit.platformer.game.model.ITankModel;
import ru.mipt.bit.platformer.game.model.ITreeModel;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class LevelModel implements ILevelModel {

    private final TiledMap map;
    private final TiledMapTileLayer groundLayer;
    private final TileMovement tileMovement;
    private final LevelBounds bounds;
    private final List<ILevelObserver> observers = new ArrayList<>();
    private final List<ITreeModel> trees = new ArrayList<>();
    private final List<ITankModel> enemyTanks = new ArrayList<>();
    private final List<BulletModel> bullets = new ArrayList<>();

    private ITankModel playerTank;

    public LevelModel(LevelConfig config) {
        map = new TmxMapLoader().load(config.getMapPath());
        groundLayer = getSingleLayer(map);
        tileMovement = new TileMovement(groundLayer, config.getInterpolation());
        bounds = new LevelBounds(groundLayer.getWidth(), groundLayer.getHeight());
    }

    public void addObserver(ILevelObserver observer) {
        observers.add(Objects.requireNonNull(observer));
    }

    public void removeObserver(ILevelObserver observer) {
        observers.remove(observer);
    }

    private void notifyAdded(LevelObjectType type, Object model) {
        LevelObjectEvent event = new LevelObjectEvent(type, model);
        for (ILevelObserver observer : observers) {
            observer.onObjectAdded(event);
        }
    }

    private void notifyRemoved(LevelObjectType type, Object model) {
        LevelObjectEvent event = new LevelObjectEvent(type, model);
        for (ILevelObserver observer : observers) {
            observer.onObjectRemoved(event);
        }
    }

    public void addTree(ITreeModel tree) {
        trees.add(tree);
        notifyAdded(LevelObjectType.TREE, tree);
    }

    public void addPlayerTank(ITankModel tank) {
        playerTank = tank;
        notifyAdded(LevelObjectType.PLAYER_TANK, tank);
    }

    public void addEnemyTank(ITankModel tank) {
        enemyTanks.add(tank);
        notifyAdded(LevelObjectType.ENEMY_TANK, tank);
    }

    public void removeTank(ITankModel tank) {
        if (tank == null) {
            return;
        }
        if (tank == playerTank) {
            playerTank = null;
            notifyRemoved(LevelObjectType.PLAYER_TANK, tank);
        } else if (enemyTanks.remove(tank)) {
            notifyRemoved(LevelObjectType.ENEMY_TANK, tank);
        }
    }

    public void addBullet(BulletModel bullet) {
        bullets.add(bullet);
        notifyAdded(LevelObjectType.BULLET, bullet);
    }

    @Override
    public TiledMap getMap() {
        return map;
    }

    @Override
    public TiledMapTileLayer getGroundLayer() {
        return groundLayer;
    }

    @Override
    public TileMovement getTileMovement() {
        return tileMovement;
    }

    @Override
    public LevelBounds getBounds() {
        return bounds;
    }

    @Override
    public List<ITreeModel> getTrees() {
        return Collections.unmodifiableList(trees);
    }

    @Override
    public List<ITankModel> getEnemyTanks() {
        return Collections.unmodifiableList(enemyTanks);
    }

    @Override
    public ITankModel getPlayerTank() {
        return playerTank;
    }

    @Override
    public List<ITankModel> getAllTanks() {
        List<ITankModel> result = new ArrayList<>(enemyTanks);
        if (playerTank != null) {
            result.add(playerTank);
        }
        return result;
    }

    @Override
    public void update(float deltaTime) {
        for (Iterator<BulletModel> iterator = bullets.iterator(); iterator.hasNext();) {
            BulletModel bullet = iterator.next();
            bullet.update(deltaTime);
            if (!bullet.isActive()) {
                iterator.remove();
                notifyRemoved(LevelObjectType.BULLET, bullet);
            }
        }
    }

    public boolean isInsideBounds(GridPoint2 coordinates) {
        return bounds.contains(coordinates);
    }

    public ITankModel findTank(GridPoint2 coordinates) {
        if (playerTank != null && playerTank.getCoordinates().equals(coordinates)) {
            return playerTank;
        }
        for (ITankModel tank : enemyTanks) {
            if (tank.getCoordinates().equals(coordinates)) {
                return tank;
            }
        }
        return null;
    }

    public ITankModel findTankByDestination(GridPoint2 coordinates) {
        if (playerTank != null && playerTank.getDestination().equals(coordinates)) {
            return playerTank;
        }
        for (ITankModel tank : enemyTanks) {
            if (tank.getDestination().equals(coordinates)) {
                return tank;
            }
        }
        return null;
    }

    public ITreeModel findTree(GridPoint2 coordinates) {
        for (ITreeModel tree : trees) {
            if (tree.getCoordinates().equals(coordinates)) {
                return tree;
            }
        }
        return null;
    }

    public BulletModel findBullet(GridPoint2 coordinates, BulletModel excluded) {
        for (BulletModel bullet : bullets) {
            if (bullet == excluded) {
                continue;
            }
            if (bullet.getCoordinates().equals(coordinates)) {
                return bullet;
            }
        }
        return null;
    }

    @Override
    public void dispose() {
        map.dispose();
    }

    public static class LevelBounds {

        private final int width;
        private final int height;

        public LevelBounds(int width, int height) {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("Width and height must be positive");
            }
            this.width = width;
            this.height = height;
        }

        public boolean contains(GridPoint2 coordinates) {
            return coordinates.x >= 0 && coordinates.x < width
                    && coordinates.y >= 0 && coordinates.y < height;
        }
    }
}
