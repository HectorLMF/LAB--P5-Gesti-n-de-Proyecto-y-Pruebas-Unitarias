package quickcoverage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DistributionPresenceTest {

    @Test
    public void distributionClassLoads() {
        assertDoesNotThrow(() -> Class.forName("metaheuristics.generators.DistributionEstimationAlgorithm"));
    }
}
