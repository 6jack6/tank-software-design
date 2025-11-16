package ru.mipt.bit.platformer.game.factory;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import ru.mipt.bit.platformer.config.DefaultGameConfig;
import ru.mipt.bit.platformer.config.GraphicsConfig;
import ru.mipt.bit.platformer.config.LevelConfig;
import ru.mipt.bit.platformer.config.TankConfig;
import ru.mipt.bit.platformer.config.TreeConfig;
import ru.mipt.bit.platformer.config.WindowConfig;
import ru.mipt.bit.platformer.game.ITankAIController;
import ru.mipt.bit.platformer.game.ITankCommand;
import ru.mipt.bit.platformer.game.ITankInputHandler;
import ru.mipt.bit.platformer.game.MoveTankCommand;
import ru.mipt.bit.platformer.game.RandomTankAI;
import ru.mipt.bit.platformer.game.ShootTankCommand;
import ru.mipt.bit.platformer.game.TankInputHandler;
import ru.mipt.bit.platformer.game.ToggleHealthIndicatorCommand;
import ru.mipt.bit.platformer.game.graphics.HealthIndicatorTankGraphics;
import ru.mipt.bit.platformer.game.graphics.HealthIndicatorTankGraphics.Visibility;
import ru.mipt.bit.platformer.game.graphics.IBulletGraphics;
import ru.mipt.bit.platformer.game.graphics.ILevelGraphics;
import ru.mipt.bit.platformer.game.graphics.ITankGraphics;
import ru.mipt.bit.platformer.game.graphics.ITreeGraphics;
import ru.mipt.bit.platformer.game.graphics.LevelGraphics;
import ru.mipt.bit.platformer.game.graphics.LevelObjectGraphicsObserver;
import ru.mipt.bit.platformer.game.graphics.TankGraphics;
import ru.mipt.bit.platformer.game.level.ILevelModel;
import ru.mipt.bit.platformer.game.level.ILevelObserver;
import ru.mipt.bit.platformer.game.level.LevelModel;
import ru.mipt.bit.platformer.game.level.LevelObjectEvent;
import ru.mipt.bit.platformer.game.level.LevelObjectType;
import ru.mipt.bit.platformer.game.level.LevelPopulation;
import ru.mipt.bit.platformer.game.level.MovementObstacleProvider;
import ru.mipt.bit.platformer.game.model.ITankModel;
import ru.mipt.bit.platformer.game.model.ITreeModel;
import ru.mipt.bit.platformer.game.model.TankModel;
import ru.mipt.bit.platformer.game.model.TreeModel;
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
        LevelModel levelModel = new LevelModel(levelConfig);
        ILevelGraphics levelGraphics = new LevelGraphics(levelModel, batch);

        Visibility healthIndicatorVisibility = new Visibility();
        LevelObjectGraphicsObserver graphicsObserver = new LevelObjectGraphicsObserver(levelModel,
                tank -> createTankGraphics(tank, levelModel, healthIndicatorVisibility));
        levelModel.addObserver(graphicsObserver);

        LevelPopulation population = gameConfig.getLevelPopulationStrategy()
                .populate(levelModel.getGroundLayer());

        List<GridPoint2> treeCoordinates = population.getTreeCoordinates();
        List<TreeConfig> treeConfigs = gameConfig.createTreeConfigs(treeCoordinates);
        for (TreeConfig treeConfig : treeConfigs) {
            ITreeModel treeModel = new TreeModel(treeConfig);
            levelModel.addTree(treeModel);
        }

        TankConfig tankConfig = gameConfig.createTankConfig(population.getPlayerSpawn());
        ITankModel playerTank = new TankModel(tankConfig);
        levelModel.addPlayerTank(playerTank);

        List<GridPoint2> enemySpawns = generateEnemySpawns(levelModel.getGroundLayer(),
                population.getPlayerSpawn(), treeCoordinates, gameConfig.getEnemyTankCount());
        for (GridPoint2 spawn : enemySpawns) {
            TankConfig enemyConfig = gameConfig.createEnemyTankConfig(spawn);
            ITankModel enemyTank = new TankModel(enemyConfig);
            levelModel.addEnemyTank(enemyTank);
        }
        List<ITankModel> enemyTanks = levelModel.getEnemyTanks();

        MovementObstacleProvider obstacleProvider = new MovementObstacleProvider(levelModel);

        LevelModel.LevelBounds levelBounds = levelModel.getBounds();

        TankInputHandler playerInputHandler = new TankInputHandler(direction ->
                new MoveTankCommand(playerTank, direction,
                        () -> obstacleProvider.getObstaclesFor(playerTank), levelBounds));
        playerInputHandler.registerKeyCommand(Input.Keys.L, true,
                new ToggleHealthIndicatorCommand(healthIndicatorVisibility));
        playerInputHandler.registerKeyCommand(Input.Keys.SPACE, true,
                new ShootTankCommand(levelModel, playerTank));
        ITankInputHandler tankInputHandler = playerInputHandler;

        List<ITankAIController> enemyControllers = new ArrayList<>();
        Map<ITankModel, ITankAIController> controllerByTank = new IdentityHashMap<>();
        for (ITankModel enemyTank : enemyTanks) {
            List<ITankCommand> commands = new ArrayList<>();
            for (Direction direction : Direction.values()) {
                commands.add(new MoveTankCommand(enemyTank, direction,
                        () -> obstacleProvider.getObstaclesFor(enemyTank), levelBounds));
            }
            commands.add(new ShootTankCommand(levelModel, enemyTank));
            RandomTankAI controller = new RandomTankAI(commands);
            enemyControllers.add(controller);
            controllerByTank.put(enemyTank, controller);
        }

        levelModel.addObserver(new ILevelObserver() {
            @Override
            public void onObjectAdded(LevelObjectEvent event) {
                // no-op
            }

            @Override
            public void onObjectRemoved(LevelObjectEvent event) {
                if (event.getType() == LevelObjectType.ENEMY_TANK) {
                    ITankModel removedTank = event.getModel();
                    ITankAIController controller = controllerByTank.remove(removedTank);
                    if (controller != null) {
                        enemyControllers.remove(controller);
                    }
                }
            }
        });

        ITankGraphics playerTankGraphics = Objects.requireNonNull(
                graphicsObserver.getPlayerTankGraphics(), "Player tank graphics not initialized");
        List<ITreeGraphics> obstacleGraphics = graphicsObserver.getTreeGraphics();
        List<ITankGraphics> enemyTankGraphics = graphicsObserver.getEnemyTankGraphics();
        List<IBulletGraphics> bulletGraphics = graphicsObserver.getBulletGraphics();

        GraphicsConfig graphicsConfig = gameConfig.createGraphicsConfig();

        return new GameContext(batch, levelModel, levelGraphics, playerTank, playerTankGraphics,
                tankInputHandler, obstacleGraphics, enemyTanks, enemyTankGraphics, bulletGraphics,
                enemyControllers, graphicsConfig);
    }

    @Override
    public WindowConfig getWindowConfig() {
        return gameConfig.createWindowConfig();
    }

    private ITankGraphics createTankGraphics(ITankModel tank,
                                            ILevelModel levelModel,
                                            Visibility visibility) {
        ITankGraphics baseGraphics = new TankGraphics(tank,
                levelModel.getTileMovement(), levelModel.getGroundLayer());
        return new HealthIndicatorTankGraphics(baseGraphics, tank, visibility);
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
