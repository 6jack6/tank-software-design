package ru.mipt.bit.platformer.game.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import java.util.Objects;
import ru.mipt.bit.platformer.game.model.TankModel;

import static com.badlogic.gdx.math.MathUtils.clamp;

public class HealthIndicatorTankGraphics implements ITankGraphics {

    private static final float INDICATOR_OFFSET = 4f;
    private static final float BAR_HEIGHT = 4f;
    private static final float BAR_MARGIN = 2f;
    private static final Color BAR_BACKGROUND = new Color(0f, 0f, 0f, 0.7f);
    private static final Color BAR_FILL = new Color(0.1f, 0.85f, 0.1f, 1f);

    private final ITankGraphics delegate;
    private final TankModel model;
    private final Visibility visibility;
    private final Texture pixelTexture;

    public HealthIndicatorTankGraphics(ITankGraphics delegate,
                                       TankModel model,
                                       Visibility visibility) {
        this.delegate = Objects.requireNonNull(delegate);
        this.model = Objects.requireNonNull(model);
        this.visibility = Objects.requireNonNull(visibility);
        this.pixelTexture = createPixelTexture();
    }

    @Override
    public void update() {
        delegate.update();
    }

    @Override
    public void render(Batch batch) {
        delegate.render(batch);
        if (!visibility.isEnabled()) {
            return;
        }
        Rectangle bounds = delegate.getBounds();
        float barWidth = Math.max(0f, bounds.width - BAR_MARGIN * 2f);
        float barX = bounds.x + BAR_MARGIN;
        float barY = bounds.y + bounds.height + INDICATOR_OFFSET;
        float ratio = clamp(model.getHealthPoints() / 100f, 0f, 1f);

        batch.setColor(BAR_BACKGROUND);
        batch.draw(pixelTexture, barX, barY, barWidth, BAR_HEIGHT);

        if (ratio > 0f) {
            batch.setColor(BAR_FILL);
            batch.draw(pixelTexture, barX, barY, barWidth * ratio, BAR_HEIGHT);
        }

        batch.setColor(Color.WHITE);
    }

    @Override
    public void dispose() {
        delegate.dispose();
        pixelTexture.dispose();
    }

    @Override
    public Rectangle getBounds() {
        return delegate.getBounds();
    }

    private static Texture createPixelTexture() {
        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    public static final class Visibility {

        private boolean enabled;

        public void toggle() {
            enabled = !enabled;
        }

        public boolean isEnabled() {
            return enabled;
        }
    }
}
