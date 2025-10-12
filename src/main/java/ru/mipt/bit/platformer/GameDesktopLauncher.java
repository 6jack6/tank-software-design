package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import java.util.List;
import ru.mipt.bit.platformer.config.GraphicsConfig;
import ru.mipt.bit.platformer.config.WindowConfig;
import ru.mipt.bit.platformer.game.ILevelGraphics;
import ru.mipt.bit.platformer.game.ILevelModel;
import ru.mipt.bit.platformer.game.ITankGraphics;
import ru.mipt.bit.platformer.game.ITankInputHandler;
import ru.mipt.bit.platformer.game.ITankModel;
import ru.mipt.bit.platformer.game.ITreeGraphics;
import ru.mipt.bit.platformer.game.ITreeModel;
import ru.mipt.bit.platformer.game.factory.DefaultGameFactory;
import ru.mipt.bit.platformer.game.factory.GameContext;
import ru.mipt.bit.platformer.game.factory.IGameFactory;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

    private final IGameFactory gameFactory;
    private Batch batch;
    private ILevelModel levelModel;
    private ILevelGraphics levelGraphics;
    private ITankModel playerTank;
    private ITankGraphics playerTankGraphics;
    private ITankInputHandler tankInputHandler;
    private List<ITreeModel> obstacles;
    private List<ITreeGraphics> obstacleGraphics;
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
        obstacles = context.getObstacles();
        obstacleGraphics = context.getObstacleGraphics();
        graphicsConfig = context.getGraphicsConfig();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(graphicsConfig.getClearColorR(), graphicsConfig.getClearColorG(),
                graphicsConfig.getClearColorB(), graphicsConfig.getClearColorA());
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        tankInputHandler.handleInput(obstacles);
        playerTankGraphics.update();
        playerTank.update(deltaTime);

        levelGraphics.render();

        batch.begin();
        playerTankGraphics.render(batch);
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
        playerTankGraphics.dispose();
        levelGraphics.dispose();
        levelModel.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        IGameFactory gameFactory = new DefaultGameFactory(
                new ru.mipt.bit.platformer.config.DefaultGameConfig());
        WindowConfig windowConfig = gameFactory.getWindowConfig();
        config.setWindowedMode(windowConfig.getWidth(), windowConfig.getHeight());
        new Lwjgl3Application(new GameDesktopLauncher(gameFactory), config);
    }
}
