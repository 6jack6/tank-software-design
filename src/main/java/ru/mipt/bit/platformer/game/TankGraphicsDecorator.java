package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import java.util.Objects;

public abstract class TankGraphicsDecorator implements ITankGraphics {

    private final ITankGraphics delegate;

    protected TankGraphicsDecorator(ITankGraphics delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public void update() {
        delegate.update();
    }

    @Override
    public void render(Batch batch) {
        delegate.render(batch);
    }

    @Override
    public void dispose() {
        delegate.dispose();
    }

    @Override
    public Rectangle getBounds() {
        return delegate.getBounds();
    }

    protected ITankGraphics getDelegate() {
        return delegate;
    }
}
