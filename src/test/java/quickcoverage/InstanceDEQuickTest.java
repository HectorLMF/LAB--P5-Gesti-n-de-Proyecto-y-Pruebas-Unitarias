package quickcoverage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class InstanceDEQuickTest {
    @Test
    public void classLoads() {
        assertDoesNotThrow(() -> Class.forName("metaheuristics.generators.InstanceDE"));
    }
}
