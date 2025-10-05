package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import java.util.Collections;
import java.util.List;
import ru.mipt.bit.platformer.game.LevelGraphics;
import ru.mipt.bit.platformer.game.LevelModel;
import ru.mipt.bit.platformer.game.TankGraphics;
import ru.mipt.bit.platformer.game.TankInputHandler;
import ru.mipt.bit.platformer.game.TankModel;
import ru.mipt.bit.platformer.game.TreeGraphics;
import ru.mipt.bit.platformer.game.TreeModel;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

    private static final float MOVEMENT_SPEED = 0.4f;

    private Batch batch;
    private LevelModel levelModel;
    private LevelGraphics levelGraphics;
    private TankModel playerTank;
    private TankGraphics playerTankGraphics;
    private TankInputHandler tankInputHandler;
    private TreeModel treeObstacle;
    private TreeGraphics treeObstacleGraphics;
    private List<TreeModel> obstacles;

    @Override
    public void create() {
        batch = new SpriteBatch();

        levelModel = new LevelModel("level.tmx", Interpolation.smooth);
        levelGraphics = new LevelGraphics(levelModel, batch);

        playerTank = new TankModel(new GridPoint2(1, 1), MOVEMENT_SPEED);
        playerTankGraphics = new TankGraphics("images/tank_blue.png", playerTank,
                levelModel.getTileMovement(), levelModel.getGroundLayer());
        tankInputHandler = new TankInputHandler(playerTank);

        treeObstacle = new TreeModel(new GridPoint2(1, 3));
        treeObstacleGraphics = new TreeGraphics("images/greenTree.png", treeObstacle,
                levelModel.getGroundLayer());
        obstacles = Collections.singletonList(treeObstacle);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        tankInputHandler.handleInput(obstacles);
        playerTankGraphics.update();
        playerTank.update(deltaTime);

        levelGraphics.render();

        batch.begin();
        playerTankGraphics.render(batch);
        treeObstacleGraphics.render(batch);
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
        treeObstacleGraphics.dispose();
        playerTankGraphics.dispose();
        levelGraphics.dispose();
        levelModel.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
