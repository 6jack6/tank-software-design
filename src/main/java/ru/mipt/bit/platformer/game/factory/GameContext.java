package ru.mipt.bit.platformer.game.factory;

import com.badlogic.gdx.graphics.g2d.Batch;
import java.util.Collections;
import java.util.List;
import ru.mipt.bit.platformer.config.GraphicsConfig;
import ru.mipt.bit.platformer.game.ITankAIController;
import ru.mipt.bit.platformer.game.ITankInputHandler;
import ru.mipt.bit.platformer.game.graphics.IBulletGraphics;
import ru.mipt.bit.platformer.game.graphics.ILevelGraphics;
import ru.mipt.bit.platformer.game.graphics.ITankGraphics;
import ru.mipt.bit.platformer.game.graphics.ITreeGraphics;
import ru.mipt.bit.platformer.game.level.ILevelModel;
import ru.mipt.bit.platformer.game.model.TankModel;

public class GameContext {

    private final Batch batch;
    private final ILevelModel levelModel;
    private final ILevelGraphics levelGraphics;
    private final TankModel tankModel;
    private final ITankGraphics tankGraphics;
    private final ITankInputHandler tankInputHandler;
    private final List<ITreeGraphics> obstacleGraphics;
    private final List<TankModel> enemyTanks;
    private final List<ITankGraphics> enemyTankGraphics;
    private final List<IBulletGraphics> bulletGraphics;
    private final List<ITankAIController> enemyControllers;
    private final GraphicsConfig graphicsConfig;

    public GameContext(Batch batch,
                       ILevelModel levelModel,
                       ILevelGraphics levelGraphics,
                       TankModel tankModel,
                       ITankGraphics tankGraphics,
                       ITankInputHandler tankInputHandler,
                       List<ITreeGraphics> obstacleGraphics,
                       List<TankModel> enemyTanks,
                       List<ITankGraphics> enemyTankGraphics,
                       List<IBulletGraphics> bulletGraphics,
                       List<ITankAIController> enemyControllers,
                       GraphicsConfig graphicsConfig) {
        this.batch = batch;
        this.levelModel = levelModel;
        this.levelGraphics = levelGraphics;
        this.tankModel = tankModel;
        this.tankGraphics = tankGraphics;
        this.tankInputHandler = tankInputHandler;
        this.obstacleGraphics = Collections.unmodifiableList(obstacleGraphics);
        this.enemyTanks = Collections.unmodifiableList(enemyTanks);
        this.enemyTankGraphics = Collections.unmodifiableList(enemyTankGraphics);
        this.bulletGraphics = Collections.unmodifiableList(bulletGraphics);
        this.enemyControllers = Collections.unmodifiableList(enemyControllers);
        this.graphicsConfig = graphicsConfig;
    }

    public Batch getBatch() {
        return batch;
    }

    public ILevelModel getLevelModel() {
        return levelModel;
    }

    public ILevelGraphics getLevelGraphics() {
        return levelGraphics;
    }

    public TankModel getTankModel() {
        return tankModel;
    }

    public ITankGraphics getTankGraphics() {
        return tankGraphics;
    }

    public ITankInputHandler getTankInputHandler() {
        return tankInputHandler;
    }

    public List<ITreeGraphics> getObstacleGraphics() {
        return obstacleGraphics;
    }

    public List<TankModel> getEnemyTanks() {
        return enemyTanks;
    }

    public List<ITankGraphics> getEnemyTankGraphics() {
        return enemyTankGraphics;
    }

    public List<IBulletGraphics> getBulletGraphics() {
        return bulletGraphics;
    }

    public List<ITankAIController> getEnemyControllers() {
        return enemyControllers;
    }

    public GraphicsConfig getGraphicsConfig() {
        return graphicsConfig;
    }
}
