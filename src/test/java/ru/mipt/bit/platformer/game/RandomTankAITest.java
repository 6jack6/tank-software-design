package ru.mipt.bit.platformer.game;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RandomTankAITest {

    @Test
    void executesExactlyOneCommandPerUpdate() {
        CountingCommand first = new CountingCommand();
        CountingCommand second = new CountingCommand();
        RandomTankAI ai = new RandomTankAI(List.of(first, second), new java.util.Random(42), 1);

        ai.update(); // delay countdown
        ai.update(); // executes once

        assertEquals(1, first.invocations.get() + second.invocations.get());
    }

    @Test
    void rejectsEmptyCommandList() {
        assertThrows(IllegalArgumentException.class, () -> new RandomTankAI(List.of(), new java.util.Random()));
    }

    private static final class CountingCommand implements ITankCommand {
        private final AtomicInteger invocations = new AtomicInteger();

        @Override
        public void execute() {
            invocations.incrementAndGet();
        }
    }
}
