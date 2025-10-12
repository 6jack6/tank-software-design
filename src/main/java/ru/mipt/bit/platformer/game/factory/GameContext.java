package ru.mipt.bit.platformer.game.factory;

import com.badlogic.gdx.graphics.g2d.Batch;
import java.util.Collections;
import java.util.List;
import ru.mipt.bit.platformer.config.GraphicsConfig;
import ru.mipt.bit.platformer.game.ILevelGraphics;
import ru.mipt.bit.platformer.game.ILevelModel;
import ru.mipt.bit.platformer.game.ITankGraphics;
import ru.mipt.bit.platformer.game.ITankInputHandler;
import ru.mipt.bit.platformer.game.ITankModel;
import ru.mipt.bit.platformer.game.ITreeGraphics;
import ru.mipt.bit.platformer.game.ITreeModel;

public class GameContext {

    private final Batch batch;
    private final ILevelModel levelModel;
    private final ILevelGraphics levelGraphics;
    private final ITankModel tankModel;
    private final ITankGraphics tankGraphics;
    private final ITankInputHandler tankInputHandler;
    private final List<ITreeModel> obstacles;
    private final List<ITreeGraphics> obstacleGraphics;
    private final GraphicsConfig graphicsConfig;

    public GameContext(Batch batch,
                       ILevelModel levelModel,
                       ILevelGraphics levelGraphics,
                       ITankModel tankModel,
                       ITankGraphics tankGraphics,
                       ITankInputHandler tankInputHandler,
                       List<ITreeModel> obstacles,
                       List<ITreeGraphics> obstacleGraphics,
                       GraphicsConfig graphicsConfig) {
        this.batch = batch;
        this.levelModel = levelModel;
        this.levelGraphics = levelGraphics;
        this.tankModel = tankModel;
        this.tankGraphics = tankGraphics;
        this.tankInputHandler = tankInputHandler;
        this.obstacles = Collections.unmodifiableList(obstacles);
        this.obstacleGraphics = Collections.unmodifiableList(obstacleGraphics);
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

    public GraphicsConfig getGraphicsConfig() {
        return graphicsConfig;
    }
}
