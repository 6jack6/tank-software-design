package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import java.util.List;
import ru.mipt.bit.platformer.config.DefaultGameConfig;
import ru.mipt.bit.platformer.config.GraphicsConfig;
import ru.mipt.bit.platformer.config.WindowConfig;
import ru.mipt.bit.platformer.game.ITankInputHandler;
import ru.mipt.bit.platformer.game.ITankAIController;
import ru.mipt.bit.platformer.game.factory.DefaultGameFactory;
import ru.mipt.bit.platformer.game.factory.GameContext;
import ru.mipt.bit.platformer.game.factory.IGameFactory;
import ru.mipt.bit.platformer.game.graphics.IBulletGraphics;
import ru.mipt.bit.platformer.game.graphics.ILevelGraphics;
import ru.mipt.bit.platformer.game.graphics.ITankGraphics;
import ru.mipt.bit.platformer.game.graphics.ITreeGraphics;
import ru.mipt.bit.platformer.game.level.FileLevelPopulationStrategy;
import ru.mipt.bit.platformer.game.level.ILevelModel;
import ru.mipt.bit.platformer.game.level.ILevelPopulationStrategy;
import ru.mipt.bit.platformer.game.level.RandomLevelPopulationStrategy;
import ru.mipt.bit.platformer.game.model.ITankModel;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {
    private static final String RANDOM_FLAG = "--random";
    private static final String LAYOUT_PREFIX = "--layout=";
    private static final int DEFAULT_RANDOM_TREES = 12;

    private final IGameFactory gameFactory;
    private Batch batch;
    private ILevelModel levelModel;
    private ILevelGraphics levelGraphics;
    private ITankModel playerTank;
    private ITankGraphics playerTankGraphics;
    private ITankInputHandler tankInputHandler;
    private List<ITankModel> enemyTanks;
    private List<ITankGraphics> enemyTankGraphics;
    private List<ITankAIController> enemyControllers;
    private List<ITreeGraphics> obstacleGraphics;
    private List<IBulletGraphics> bulletGraphics;
    private GraphicsConfig graphicsConfig;

    public GameDesktopLauncher(IGameFactory gameFactory) {
        this.gameFactory = gameFactory;
    }

    @Override
    public void create() {
        GameContext context = gameFactory.createGameContext();
        batch = context.getBatch();
        levelModel = context.getLevelModel();
        levelGraphics = context.getLevelGraphics();
        playerTank = context.getTankModel();
        playerTankGraphics = context.getTankGraphics();
        tankInputHandler = context.getTankInputHandler();
        enemyTanks = context.getEnemyTanks();
        enemyTankGraphics = context.getEnemyTankGraphics();
        enemyControllers = context.getEnemyControllers();
        obstacleGraphics = context.getObstacleGraphics();
        bulletGraphics = context.getBulletGraphics();
        graphicsConfig = context.getGraphicsConfig();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(graphicsConfig.getClearColorR(), graphicsConfig.getClearColorG(),
                graphicsConfig.getClearColorB(), graphicsConfig.getClearColorA());
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        tankInputHandler.handleInput();
        for (ITankAIController controller : enemyControllers) {
            controller.update();
        }

        playerTankGraphics.update();
        for (ITankGraphics tankGraphics : enemyTankGraphics) {
            tankGraphics.update();
        }
        playerTank.update(deltaTime);
        for (ITankModel enemyTank : enemyTanks) {
            enemyTank.update(deltaTime);
        }
        levelModel.update(deltaTime);
        for (IBulletGraphics graphics : bulletGraphics) {
            graphics.update();
        }

        levelGraphics.render();

        batch.begin();
        playerTankGraphics.render(batch);
        for (ITankGraphics tankGraphics : enemyTankGraphics) {
            tankGraphics.render(batch);
        }
        for (ITankGraphics tankGraphics : enemyTankGraphics) {
            tankGraphics.render(batch);
        }
        for (IBulletGraphics graphics : bulletGraphics) {
            graphics.render(batch);
        }
        for (ITreeGraphics treeGraphics : obstacleGraphics) {
            treeGraphics.render(batch);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }

    @Override
    public void dispose() {
        for (ITreeGraphics treeGraphics : obstacleGraphics) {
            treeGraphics.dispose();
        }
        for (IBulletGraphics graphics : bulletGraphics) {
            graphics.dispose();
        }
        playerTankGraphics.dispose();
        for (ITankGraphics tankGraphics : enemyTankGraphics) {
            tankGraphics.dispose();
        }
        levelGraphics.dispose();
        levelModel.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        DefaultGameConfig gameConfig = createGameConfig(args);
        IGameFactory gameFactory = new DefaultGameFactory(gameConfig);
        WindowConfig windowConfig = gameFactory.getWindowConfig();
        config.setWindowedMode(windowConfig.getWidth(), windowConfig.getHeight());
        new Lwjgl3Application(new GameDesktopLauncher(gameFactory), config);
    }

    private static DefaultGameConfig createGameConfig(String[] args) {
        ILevelPopulationStrategy strategy = resolveLevelPopulationStrategy(args);
        if (strategy == null) {
            return new DefaultGameConfig();
        }
        return new DefaultGameConfig(strategy);
    }

    private static ILevelPopulationStrategy resolveLevelPopulationStrategy(String[] args) {
        for (String arg : args) {
            if (arg.startsWith(RANDOM_FLAG)) {
                int treeCount = DEFAULT_RANDOM_TREES;
                int equalsIndex = arg.indexOf('=');
                if (equalsIndex >= 0) {
                    String value = arg.substring(equalsIndex + 1);
                    if (value.isEmpty()) {
                        throw new IllegalArgumentException("Tree count value in --random must not be empty");
                    }
                    treeCount = parseTreeCount(value);
                }
                return new RandomLevelPopulationStrategy(treeCount);
            }
            if (arg.startsWith(LAYOUT_PREFIX)) {
                String path = arg.substring(LAYOUT_PREFIX.length());
                if (path.isEmpty()) {
                    throw new IllegalArgumentException("Layout path must not be empty");
                }
                return new FileLevelPopulationStrategy(path);
            }
        }
        return null;
    }

    private static int parseTreeCount(String value) {
        try {
            int count = Integer.parseInt(value);
            if (count < 0) {
                throw new IllegalArgumentException("Tree count must be non-negative");
            }
            return count;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Failed to parse tree count: " + value, e);
        }
    }
}
