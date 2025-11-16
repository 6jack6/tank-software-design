package ru.mipt.bit.platformer.game.factory;

import com.badlogic.gdx.graphics.g2d.Batch;
import java.util.Collections;
import java.util.List;
import ru.mipt.bit.platformer.config.GraphicsConfig;
import ru.mipt.bit.platformer.game.ITankAIController;
import ru.mipt.bit.platformer.game.ITankInputHandler;
import ru.mipt.bit.platformer.game.graphics.ILevelGraphics;
import ru.mipt.bit.platformer.game.graphics.ITankGraphics;
import ru.mipt.bit.platformer.game.graphics.ITreeGraphics;
import ru.mipt.bit.platformer.game.level.ILevelModel;
import ru.mipt.bit.platformer.game.model.ITankModel;
import ru.mipt.bit.platformer.game.model.ITreeModel;

public class GameContext {

    private final Batch batch;
    private final ILevelModel levelModel;
    private final ILevelGraphics levelGraphics;
    private final ITankModel tankModel;
    private final ITankGraphics tankGraphics;
    private final ITankInputHandler tankInputHandler;
    private final List<ITreeModel> obstacles;
    private final List<ITreeGraphics> obstacleGraphics;
    private final List<ITankModel> enemyTanks;
    private final List<ITankGraphics> enemyTankGraphics;
    private final List<ITankAIController> enemyControllers;
    private final GraphicsConfig graphicsConfig;

    public GameContext(Batch batch,
                       ILevelModel levelModel,
                       ILevelGraphics levelGraphics,
                       ITankModel tankModel,
                       ITankGraphics tankGraphics,
                       ITankInputHandler tankInputHandler,
                       List<ITreeModel> obstacles,
                       List<ITreeGraphics> obstacleGraphics,
                       List<ITankModel> enemyTanks,
                       List<ITankGraphics> enemyTankGraphics,
                       List<ITankAIController> enemyControllers,
                       GraphicsConfig graphicsConfig) {
        this.batch = batch;
        this.levelModel = levelModel;
        this.levelGraphics = levelGraphics;
        this.tankModel = tankModel;
        this.tankGraphics = tankGraphics;
        this.tankInputHandler = tankInputHandler;
        this.obstacles = Collections.unmodifiableList(obstacles);
        this.obstacleGraphics = Collections.unmodifiableList(obstacleGraphics);
        this.enemyTanks = Collections.unmodifiableList(enemyTanks);
        this.enemyTankGraphics = Collections.unmodifiableList(enemyTankGraphics);
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

    public ITankModel getTankModel() {
        return tankModel;
    }

    public ITankGraphics getTankGraphics() {
        return tankGraphics;
    }

    public ITankInputHandler getTankInputHandler() {
        return tankInputHandler;
    }

    public List<ITreeModel> getObstacles() {
        return obstacles;
    }

    public List<ITreeGraphics> getObstacleGraphics() {
        return obstacleGraphics;
    }

    public List<ITankModel> getEnemyTanks() {
        return enemyTanks;
    }

    public List<ITankGraphics> getEnemyTankGraphics() {
        return enemyTankGraphics;
    }

    public List<ITankAIController> getEnemyControllers() {
        return enemyControllers;
    }

    public GraphicsConfig getGraphicsConfig() {
        return graphicsConfig;
    }
}
