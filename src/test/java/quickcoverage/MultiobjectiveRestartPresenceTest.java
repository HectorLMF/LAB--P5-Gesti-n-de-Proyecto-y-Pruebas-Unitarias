package quickcoverage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MultiobjectiveRestartPresenceTest {

    @Test
    public void classLoads() {
        assertDoesNotThrow(() -> Class.forName("metaheuristics.generators.MultiobjectiveHillClimbingRestart"));
    }
}
