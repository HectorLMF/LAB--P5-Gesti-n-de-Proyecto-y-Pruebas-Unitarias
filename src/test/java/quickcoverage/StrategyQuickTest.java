package quickcoverage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;

public class StrategyQuickTest {

    @Test
    public void singletonAndBasicSetters() {
        // Ensure clean singleton
        Strategy.destroyExecute();
        Strategy s = Strategy.getStrategy();
        assertNotNull(s, "Strategy singleton should not be null");

        // Basic setters/getters
        s.setCountMax(10);
        assertEquals(10, s.getCountMax());

        s.setThreshold(0.123);
        assertEquals(0.123, s.getThreshold(), 1e-9);

        // calculateOffLinePerformance requires a non-zero countPeriodChange
        s.countPeriodChange = 1;
        s.calculateOffLinePerformance(50.0f, 0);
        assertEquals(50.0f / s.countPeriodChange, s.listOfflineError[0], 1e-6);
    }
}
