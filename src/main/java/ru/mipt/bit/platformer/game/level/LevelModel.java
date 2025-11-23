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
import ru.mipt.bit.platformer.game.model.GameObject;
import ru.mipt.bit.platformer.game.model.TankModel;
import ru.mipt.bit.platformer.game.model.TreeModel;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class LevelModel implements ILevelModel {

    private final TiledMap map;
    private final TiledMapTileLayer groundLayer;
    private final TileMovement tileMovement;
    private final LevelBounds bounds;
    private final List<ILevelObserver> observers = new ArrayList<>();
    private final List<GameObject> trees = new ArrayList<>();
    private final List<GameObject> enemyTanks = new ArrayList<>();
    private final List<GameObject> bullets = new ArrayList<>();

    private GameObject playerTank;

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

    public void addTree(GameObject tree) {
        TreeModel treeModel = castModel(tree, TreeModel.class, "tree");
        trees.add(treeModel);
        notifyAdded(LevelObjectType.TREE, treeModel);
    }

    public void addPlayerTank(GameObject tank) {
        TankModel tankModel = castModel(tank, TankModel.class, "player tank");
        playerTank = tankModel;
        notifyAdded(LevelObjectType.PLAYER_TANK, tankModel);
    }

    public void addEnemyTank(GameObject tank) {
        TankModel tankModel = castModel(tank, TankModel.class, "enemy tank");
        enemyTanks.add(tankModel);
        notifyAdded(LevelObjectType.ENEMY_TANK, tankModel);
    }

    public void removeTank(GameObject tank) {
        if (tank == null) {
            return;
        }
        TankModel tankModel = castModel(tank, TankModel.class, "tank");
        if (tankModel == playerTank) {
            playerTank = null;
            notifyRemoved(LevelObjectType.PLAYER_TANK, tankModel);
        } else if (enemyTanks.remove(tankModel)) {
            notifyRemoved(LevelObjectType.ENEMY_TANK, tankModel);
        }
    }

    public void addBullet(GameObject bullet) {
        BulletModel bulletModel = castModel(bullet, BulletModel.class, "bullet");
        bullets.add(bulletModel);
        notifyAdded(LevelObjectType.BULLET, bulletModel);
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
    public List<GameObject> getTrees() {
        return Collections.unmodifiableList(trees);
    }

    @Override
    public List<GameObject> getEnemyTanks() {
        return Collections.unmodifiableList(enemyTanks);
    }

    @Override
    public GameObject getPlayerTank() {
        return playerTank;
    }

    @Override
    public List<GameObject> getAllTanks() {
        List<GameObject> result = new ArrayList<>(enemyTanks);
        if (playerTank != null) {
            result.add(playerTank);
        }
        return result;
    }

    @Override
    public void update(float deltaTime) {
        for (Iterator<GameObject> iterator = bullets.iterator(); iterator.hasNext();) {
            BulletModel bullet = (BulletModel) iterator.next();
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

    public TankModel findTank(GridPoint2 coordinates) {
        if (playerTank != null) {
            TankModel tank = (TankModel) playerTank;
            if (tank.getCoordinates().equals(coordinates)) {
                return tank;
            }
        }
        for (GameObject object : enemyTanks) {
            TankModel tank = (TankModel) object;
            if (tank.getCoordinates().equals(coordinates)) {
                return tank;
            }
        }
        return null;
    }

    public TankModel findTankByDestination(GridPoint2 coordinates) {
        if (playerTank != null) {
            TankModel tank = (TankModel) playerTank;
            if (tank.getDestination().equals(coordinates)) {
                return tank;
            }
        }
        for (GameObject object : enemyTanks) {
            TankModel tank = (TankModel) object;
            if (tank.getDestination().equals(coordinates)) {
                return tank;
            }
        }
        return null;
    }

    public TreeModel findTree(GridPoint2 coordinates) {
        for (GameObject object : trees) {
            TreeModel tree = (TreeModel) object;
            if (tree.getCoordinates().equals(coordinates)) {
                return tree;
            }
        }
        return null;
    }

    public BulletModel findBullet(GridPoint2 coordinates, BulletModel excluded) {
        for (GameObject object : bullets) {
            BulletModel bullet = (BulletModel) object;
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

    private static <T> T castModel(GameObject object, Class<T> type, String name) {
        if (object == null) {
            throw new IllegalArgumentException(name + " must not be null");
        }
        if (!type.isInstance(object)) {
            throw new IllegalArgumentException("Expected " + type.getSimpleName() + " for " + name);
        }
        return type.cast(object);
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
