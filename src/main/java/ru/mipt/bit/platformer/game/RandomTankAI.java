package ru.mipt.bit.platformer.game;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class RandomTankAI implements ITankAIController {

    private static final int DEFAULT_DELAY_UPDATES = 10;

    private final List<ITankCommand> commands;
    private final Random random;
    private final int delayUpdates;
    private int remainingDelay;

    public RandomTankAI(List<ITankCommand> commands) {
        this(commands, new Random(), DEFAULT_DELAY_UPDATES);
    }

    public RandomTankAI(List<ITankCommand> commands, Random random) {
        this(commands, random, DEFAULT_DELAY_UPDATES);
    }

    public RandomTankAI(List<ITankCommand> commands, Random random, int delayUpdates) {
        if (commands == null || commands.isEmpty()) {
            throw new IllegalArgumentException("Commands must not be null or empty");
        }
        if (delayUpdates < 0) {
            throw new IllegalArgumentException("Delay must be non-negative");
        }
        this.commands = Collections.unmodifiableList(List.copyOf(commands));
        this.random = Objects.requireNonNull(random);
        this.delayUpdates = delayUpdates;
        this.remainingDelay = delayUpdates;
    }

    @Override
    public void update() {
        if (remainingDelay > 0) {
            remainingDelay--;
            return;
        }
        commands.get(random.nextInt(commands.size())).execute();
        remainingDelay = delayUpdates;
    }
}
