package ru.mipt.bit.platformer.game.factory;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.List;
import ru.mipt.bit.platformer.config.DefaultGameConfig;
import ru.mipt.bit.platformer.config.GraphicsConfig;
import ru.mipt.bit.platformer.config.LevelConfig;
import ru.mipt.bit.platformer.config.TankConfig;
import ru.mipt.bit.platformer.config.TreeConfig;
import ru.mipt.bit.platformer.config.WindowConfig;
import ru.mipt.bit.platformer.game.ILevelGraphics;
import ru.mipt.bit.platformer.game.ILevelModel;
import ru.mipt.bit.platformer.game.ITankGraphics;
import ru.mipt.bit.platformer.game.ITankInputHandler;
import ru.mipt.bit.platformer.game.ITankModel;
import ru.mipt.bit.platformer.game.ITreeGraphics;
import ru.mipt.bit.platformer.game.ITreeModel;
import ru.mipt.bit.platformer.game.LevelGraphics;
import ru.mipt.bit.platformer.game.LevelModel;
import ru.mipt.bit.platformer.game.TankGraphics;
import ru.mipt.bit.platformer.game.TankInputHandler;
import ru.mipt.bit.platformer.game.TankModel;
import ru.mipt.bit.platformer.game.TreeGraphics;
import ru.mipt.bit.platformer.game.TreeModel;
import ru.mipt.bit.platformer.game.level.LevelPopulation;

public class DefaultGameFactory implements IGameFactory {

    private final DefaultGameConfig gameConfig;

    public DefaultGameFactory(DefaultGameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    @Override
    public GameContext createGameContext() {
        Batch batch = new SpriteBatch();

        LevelConfig levelConfig = gameConfig.createLevelConfig();
        ILevelModel levelModel = new LevelModel(levelConfig);
        ILevelGraphics levelGraphics = new LevelGraphics(levelModel, batch);

        LevelPopulation population = gameConfig.getLevelPopulationStrategy()
                .populate(levelModel.getGroundLayer());

        TankConfig tankConfig = gameConfig.createTankConfig(population.getPlayerSpawn());
        ITankModel tankModel = new TankModel(tankConfig);
        ITankGraphics tankGraphics = new TankGraphics(tankModel,
                levelModel.getTileMovement(), levelModel.getGroundLayer());
        ITankInputHandler tankInputHandler = new TankInputHandler(tankModel);

        List<ITreeModel> obstacles = new ArrayList<>();
        List<ITreeGraphics> obstacleGraphics = new ArrayList<>();
        List<TreeConfig> treeConfigs = gameConfig.createTreeConfigs(population.getTreeCoordinates());
        for (TreeConfig treeConfig : treeConfigs) {
            ITreeModel treeModel = new TreeModel(treeConfig);
            obstacles.add(treeModel);
            obstacleGraphics.add(new TreeGraphics(treeModel, levelModel.getGroundLayer()));
        }

        GraphicsConfig graphicsConfig = gameConfig.createGraphicsConfig();

        return new GameContext(batch, levelModel, levelGraphics, tankModel, tankGraphics,
                tankInputHandler, obstacles, obstacleGraphics, graphicsConfig);
    }

    @Override
    public WindowConfig getWindowConfig() {
        return gameConfig.createWindowConfig();
    }
}
