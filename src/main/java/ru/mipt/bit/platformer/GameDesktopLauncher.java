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
import ru.mipt.bit.platformer.game.Level;
import ru.mipt.bit.platformer.game.Tank;
import ru.mipt.bit.platformer.game.TankInputHandler;
import ru.mipt.bit.platformer.game.Tree;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

    private static final float MOVEMENT_SPEED = 0.4f;

    private Batch batch;
    private Level level;
    private Tank playerTank;
    private TankInputHandler tankInputHandler;
    private Tree treeObstacle;
    private List<Tree> obstacles;

    @Override
    public void create() {
        batch = new SpriteBatch();

        level = new Level("level.tmx", batch, Interpolation.smooth);
        playerTank = new Tank("images/tank_blue.png", new GridPoint2(1, 1),
                level.getTileMovement(), level.getGroundLayer(), MOVEMENT_SPEED);
        tankInputHandler = new TankInputHandler(playerTank);

        treeObstacle = new Tree("images/greenTree.png", new GridPoint2(1, 3), level.getGroundLayer());
        obstacles = Collections.singletonList(treeObstacle);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        tankInputHandler.handleInput(obstacles);
        playerTank.update(deltaTime);

        level.render();

        batch.begin();
        playerTank.render(batch);
        treeObstacle.render(batch);
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
        treeObstacle.dispose();
        playerTank.dispose();
        level.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
