package ru.mipt.bit.platformer.game.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public interface ITankGraphics extends Disposable {

    void update();

    void render(Batch batch);

    Rectangle getBounds();
}
