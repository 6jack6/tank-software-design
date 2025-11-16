package ru.mipt.bit.platformer.game;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class RandomTankAI implements ITankAIController {

    private final List<ITankCommand> commands;
    private final Random random;

    public RandomTankAI(List<ITankCommand> commands) {
        this(commands, new Random());
    }

    public RandomTankAI(List<ITankCommand> commands, Random random) {
        if (commands == null || commands.isEmpty()) {
            throw new IllegalArgumentException("Commands must not be null or empty");
        }
        this.commands = Collections.unmodifiableList(List.copyOf(commands));
        this.random = Objects.requireNonNull(random);
    }

    @Override
    public void update() {
        commands.get(random.nextInt(commands.size())).execute();
    }
}
