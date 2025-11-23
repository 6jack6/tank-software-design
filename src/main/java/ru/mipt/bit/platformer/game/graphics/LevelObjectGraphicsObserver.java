package ru.mipt.bit.platformer.game.graphics;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import ru.mipt.bit.platformer.game.level.ILevelObserver;
import ru.mipt.bit.platformer.game.level.ILevelModel;
import ru.mipt.bit.platformer.game.level.LevelObjectEvent;
import ru.mipt.bit.platformer.game.level.LevelObjectType;
import ru.mipt.bit.platformer.game.model.BulletModel;
import ru.mipt.bit.platformer.game.model.TankModel;
import ru.mipt.bit.platformer.game.model.TreeModel;

public class LevelObjectGraphicsObserver implements ILevelObserver {

    private final ILevelModel levelModel;
    private final Function<TankModel, ITankGraphics> tankGraphicsFactory;
    private final List<ITreeGraphics> treeGraphics = new ArrayList<>();
    private final List<ITankGraphics> enemyTankGraphics = new ArrayList<>();
    private final List<IBulletGraphics> bulletGraphics = new ArrayList<>();
    private final Map<Object, Object> graphicsRegistry = new IdentityHashMap<>();

    private ITankGraphics playerTankGraphics;

    public LevelObjectGraphicsObserver(ILevelModel levelModel,
                                       Function<TankModel, ITankGraphics> tankGraphicsFactory) {
        this.levelModel = levelModel;
        this.tankGraphicsFactory = tankGraphicsFactory;
    }

    @Override
    public void onObjectAdded(LevelObjectEvent event) {
        LevelObjectType type = event.getType();
        switch (type) {
            case TREE:
                addTreeGraphics((TreeModel) event.getModel());
                break;
            case PLAYER_TANK:
                addPlayerTankGraphics((TankModel) event.getModel());
                break;
            case ENEMY_TANK:
                addEnemyTankGraphics((TankModel) event.getModel());
                break;
            case BULLET:
                addBulletGraphics((BulletModel) event.getModel());
                break;
            default:
                break;
        }
    }

    @Override
    public void onObjectRemoved(LevelObjectEvent event) {
        Object graphics = graphicsRegistry.remove(event.getModel());
        if (graphics == null) {
            return;
        }
        if (graphics instanceof ITankGraphics) {
            ITankGraphics tankGraphics = (ITankGraphics) graphics;
            tankGraphics.dispose();
            if (event.getType() == LevelObjectType.PLAYER_TANK) {
                playerTankGraphics = null;
            } else {
                enemyTankGraphics.remove(tankGraphics);
            }
        } else if (graphics instanceof ITreeGraphics) {
            ITreeGraphics treeGraphic = (ITreeGraphics) graphics;
            treeGraphic.dispose();
            treeGraphics.remove(treeGraphic);
        } else if (graphics instanceof IBulletGraphics) {
            IBulletGraphics bulletGraphic = (IBulletGraphics) graphics;
            bulletGraphic.dispose();
            bulletGraphics.remove(bulletGraphic);
        }
    }

    private void addTreeGraphics(TreeModel treeModel) {
        ITreeGraphics graphics = new TreeGraphics(treeModel, levelModel.getGroundLayer());
        treeGraphics.add(graphics);
        graphicsRegistry.put(treeModel, graphics);
    }

    private void addPlayerTankGraphics(TankModel tank) {
        ITankGraphics graphics = tankGraphicsFactory.apply(tank);
        playerTankGraphics = graphics;
        graphicsRegistry.put(tank, graphics);
    }

    private void addEnemyTankGraphics(TankModel tank) {
        ITankGraphics graphics = tankGraphicsFactory.apply(tank);
        enemyTankGraphics.add(graphics);
        graphicsRegistry.put(tank, graphics);
    }

    private void addBulletGraphics(BulletModel bulletModel) {
        IBulletGraphics graphics = new BulletGraphics(bulletModel, levelModel.getGroundLayer());
        bulletGraphics.add(graphics);
        graphicsRegistry.put(bulletModel, graphics);
    }

    public ITankGraphics getPlayerTankGraphics() {
        return playerTankGraphics;
    }

    public List<ITankGraphics> getEnemyTankGraphics() {
        return enemyTankGraphics;
    }

    public List<ITreeGraphics> getTreeGraphics() {
        return treeGraphics;
    }

    public List<IBulletGraphics> getBulletGraphics() {
        return bulletGraphics;
    }
}
