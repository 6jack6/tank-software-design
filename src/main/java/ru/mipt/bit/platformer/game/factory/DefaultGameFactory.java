package ru.mipt.bit.platformer.game.factory;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import ru.mipt.bit.platformer.config.DefaultGameConfig;
import ru.mipt.bit.platformer.config.GraphicsConfig;
import ru.mipt.bit.platformer.config.LevelConfig;
import ru.mipt.bit.platformer.config.TankConfig;
import ru.mipt.bit.platformer.config.TreeConfig;
import ru.mipt.bit.platformer.config.WindowConfig;
import ru.mipt.bit.platformer.game.HealthIndicatorTankGraphics;
import ru.mipt.bit.platformer.game.HealthIndicatorVisibility;
import ru.mipt.bit.platformer.game.ILevelGraphics;
import ru.mipt.bit.platformer.game.ILevelModel;
import ru.mipt.bit.platformer.game.ITankGraphics;
import ru.mipt.bit.platformer.game.ITankInputHandler;
import ru.mipt.bit.platformer.game.ITankModel;
import ru.mipt.bit.platformer.game.ITreeGraphics;
import ru.mipt.bit.platformer.game.ITreeModel;
import ru.mipt.bit.platformer.game.LevelGraphics;
import ru.mipt.bit.platformer.game.LevelModel;
import ru.mipt.bit.platformer.game.LevelBounds;
import ru.mipt.bit.platformer.game.MoveTankCommand;
import ru.mipt.bit.platformer.game.MovementObstacleProvider;
import ru.mipt.bit.platformer.game.RandomTankAI;
import ru.mipt.bit.platformer.game.TankGraphics;
import ru.mipt.bit.platformer.game.TankInputHandler;
import ru.mipt.bit.platformer.game.TankAIController;
import ru.mipt.bit.platformer.game.TankCommand;
import ru.mipt.bit.platformer.game.TankModel;
import ru.mipt.bit.platformer.game.ToggleHealthIndicatorCommand;
import ru.mipt.bit.platformer.game.TreeGraphics;
import ru.mipt.bit.platformer.game.TreeModel;
import ru.mipt.bit.platformer.game.level.LevelPopulation;
import ru.mipt.bit.platformer.util.Direction;

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

        List<GridPoint2> treeCoordinates = population.getTreeCoordinates();
        List<ITreeModel> obstacles = new ArrayList<>();
        List<ITreeGraphics> obstacleGraphics = new ArrayList<>();
        List<TreeConfig> treeConfigs = gameConfig.createTreeConfigs(treeCoordinates);
        for (TreeConfig treeConfig : treeConfigs) {
            ITreeModel treeModel = new TreeModel(treeConfig);
            obstacles.add(treeModel);
            obstacleGraphics.add(new TreeGraphics(treeModel, levelModel.getGroundLayer()));
        }

        HealthIndicatorVisibility healthIndicatorVisibility = new HealthIndicatorVisibility();

        TankConfig tankConfig = gameConfig.createTankConfig(population.getPlayerSpawn());
        ITankModel playerTank = new TankModel(tankConfig);
        ITankGraphics playerTankGraphics = createTankGraphics(playerTank, levelModel,
                healthIndicatorVisibility);

        List<GridPoint2> enemySpawns = generateEnemySpawns(levelModel.getGroundLayer(),
                population.getPlayerSpawn(), treeCoordinates, gameConfig.getEnemyTankCount());
        List<ITankModel> enemyTanks = new ArrayList<>();
        List<ITankGraphics> enemyTankGraphics = new ArrayList<>();
        for (GridPoint2 spawn : enemySpawns) {
            TankConfig enemyConfig = gameConfig.createEnemyTankConfig(spawn);
            ITankModel enemyTank = new TankModel(enemyConfig);
            enemyTanks.add(enemyTank);
            enemyTankGraphics.add(createTankGraphics(enemyTank, levelModel,
                    healthIndicatorVisibility));
        }

        List<ITankModel> allTanks = new ArrayList<>();
        allTanks.add(playerTank);
        allTanks.addAll(enemyTanks);

        MovementObstacleProvider obstacleProvider = new MovementObstacleProvider(obstacles, allTanks);

        LevelBounds levelBounds = new LevelBounds(levelModel.getGroundLayer().getWidth(),
                levelModel.getGroundLayer().getHeight());

        TankInputHandler playerInputHandler = new TankInputHandler(direction ->
                new MoveTankCommand(playerTank, direction,
                        () -> obstacleProvider.getObstaclesFor(playerTank), levelBounds));
        playerInputHandler.registerKeyCommand(Input.Keys.L, true,
                new ToggleHealthIndicatorCommand(healthIndicatorVisibility));
        ITankInputHandler tankInputHandler = playerInputHandler;

        List<TankAIController> enemyControllers = new ArrayList<>();
        for (ITankModel enemyTank : enemyTanks) {
            List<TankCommand> commands = new ArrayList<>();
            for (Direction direction : Direction.values()) {
                commands.add(new MoveTankCommand(enemyTank, direction,
                        () -> obstacleProvider.getObstaclesFor(enemyTank), levelBounds));
            }
            enemyControllers.add(new RandomTankAI(commands));
        }

        GraphicsConfig graphicsConfig = gameConfig.createGraphicsConfig();

        return new GameContext(batch, levelModel, levelGraphics, playerTank, playerTankGraphics,
                tankInputHandler, obstacles, obstacleGraphics, enemyTanks, enemyTankGraphics,
                enemyControllers, graphicsConfig);
    }

    @Override
    public WindowConfig getWindowConfig() {
        return gameConfig.createWindowConfig();
    }

    private ITankGraphics createTankGraphics(ITankModel tank,
                                             ILevelModel levelModel,
                                             HealthIndicatorVisibility healthIndicatorVisibility) {
        ITankGraphics baseGraphics = new TankGraphics(tank,
                levelModel.getTileMovement(), levelModel.getGroundLayer());
        return new HealthIndicatorTankGraphics(baseGraphics, tank, healthIndicatorVisibility);
    }

    private List<GridPoint2> generateEnemySpawns(TiledMapTileLayer groundLayer,
                                                 GridPoint2 playerSpawn,
                                                 List<GridPoint2> treeCoordinates,
                                                 int enemyCount) {
        if (enemyCount <= 0) {
            return Collections.emptyList();
        }
        int width = groundLayer.getWidth();
        int height = groundLayer.getHeight();
        List<GridPoint2> freeTiles = new ArrayList<>();
        Set<Long> blocked = new HashSet<>();
        blocked.add(encode(playerSpawn.x, playerSpawn.y));
        for (GridPoint2 tree : treeCoordinates) {
            blocked.add(encode(tree.x, tree.y));
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                long key = encode(x, y);
                if (!blocked.contains(key)) {
                    freeTiles.add(new GridPoint2(x, y));
                }
            }
        }
        if (freeTiles.isEmpty()) {
            return Collections.emptyList();
        }
        Collections.shuffle(freeTiles, new Random());
        int spawnCount = Math.min(enemyCount, freeTiles.size());
        return new ArrayList<>(freeTiles.subList(0, spawnCount));
    }

    private static long encode(int x, int y) {
        return (((long) x) << 32) | (y & 0xffffffffL);
    }
}
