package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import java.util.List;
import ru.mipt.bit.platformer.config.GraphicsConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.mipt.bit.platformer.config.GameApplicationConfiguration;
import ru.mipt.bit.platformer.config.GameArguments;
import ru.mipt.bit.platformer.game.ITankInputHandler;
import ru.mipt.bit.platformer.game.ITankAIController;
import ru.mipt.bit.platformer.game.factory.GameContext;
import ru.mipt.bit.platformer.game.factory.IGameFactory;
import ru.mipt.bit.platformer.game.graphics.IBulletGraphics;
import ru.mipt.bit.platformer.game.graphics.ILevelGraphics;
import ru.mipt.bit.platformer.game.graphics.ITankGraphics;
import ru.mipt.bit.platformer.game.graphics.ITreeGraphics;
import ru.mipt.bit.platformer.game.level.ILevelModel;
import ru.mipt.bit.platformer.game.model.TankModel;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {
    private final IGameFactory gameFactory;
    private Batch batch;
    private ILevelModel levelModel;
    private ILevelGraphics levelGraphics;
    private TankModel playerTank;
    private ITankGraphics playerTankGraphics;
    private ITankInputHandler tankInputHandler;
    private List<TankModel> enemyTanks;
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
        for (TankModel enemyTank : enemyTanks) {
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
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(GameApplicationConfiguration.class);
            context.registerBean(GameArguments.class, () -> new GameArguments(args));
            context.refresh();

            Lwjgl3ApplicationConfiguration lwjglConfig =
                    context.getBean(Lwjgl3ApplicationConfiguration.class);
            GameDesktopLauncher launcher = context.getBean(GameDesktopLauncher.class);
            new Lwjgl3Application(launcher, lwjglConfig);
        }
    }
}
