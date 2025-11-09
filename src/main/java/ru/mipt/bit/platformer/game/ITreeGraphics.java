package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;

public interface ITreeGraphics extends Disposable {

    void render(Batch batch);
}
