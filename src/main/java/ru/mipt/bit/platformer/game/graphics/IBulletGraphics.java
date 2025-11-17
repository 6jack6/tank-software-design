package ru.mipt.bit.platformer.game.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;

public interface IBulletGraphics extends Disposable {

    void update();

    void render(Batch batch);
}
