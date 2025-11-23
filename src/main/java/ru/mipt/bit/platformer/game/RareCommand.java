package ru.mipt.bit.platformer.game;

import java.util.Objects;
import java.util.Random;

/**
 * Wraps another command and only executes it approximately once every {@code ratio} updates.
 */
public class RareCommand implements ITankCommand {

    private final ITankCommand delegate;
    private final Random random;
    private final int ratio;

    public RareCommand(ITankCommand delegate, Random random, int ratio) {
        if (ratio <= 0) {
            throw new IllegalArgumentException("ratio must be positive");
        }
        this.delegate = Objects.requireNonNull(delegate);
        this.random = Objects.requireNonNull(random);
        this.ratio = ratio;
    }

    @Override
    public void execute() {
        if (random.nextInt(ratio) == 0) {
            delegate.execute();
        }
    }
}
