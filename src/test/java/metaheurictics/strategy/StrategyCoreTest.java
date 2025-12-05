package metaheurictics.strategy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import metaheuristics.generators.RandomSearch;
import problem.definition.Problem;

public class StrategyCoreTest {

    @BeforeEach
    void clean() {
        Strategy.destroyExecute();
        RandomSearch.listStateReference.clear();
    }

    @Test
    @DisplayName("setProblem/getProblem roundtrip works")
    void testSetGetProblem() {
        Problem p = new Problem();
        Strategy.setProblem(p);
        assertNotNull(Strategy.getStrategy());
        assertSame(p, Strategy.getStrategy().getProblem());
    }

    @Test
    @DisplayName("destroyExecute resets singleton and clears RandomSearch list")
    void testDestroyExecute() {
        RandomSearch.listStateReference.add(new problem.definition.State());
        assertFalse(RandomSearch.listStateReference.isEmpty());
        Strategy.destroyExecute();
        assertNull(getPrivateStrategyIfAny());
        assertNotNull(RandomSearch.listStateReference);
        assertTrue(RandomSearch.listStateReference.isEmpty());
    }

    @Test
    @DisplayName("countCurrent setter/getter work")
    void testCountCurrent() {
        Strategy.setProblem(new Problem());
        Strategy.getStrategy().setCountCurrent(42);
        assertEquals(42, Strategy.getStrategy().getCountCurrent());
    }

    @Test
    @DisplayName("threshold setter/getter work")
    void testThreshold() {
        Strategy.setProblem(new Problem());
        Strategy.getStrategy().setThreshold(0.123);
        assertEquals(0.123, Strategy.getStrategy().getThreshold());
    }

    private Strategy getPrivateStrategyIfAny() {
        try {
            java.lang.reflect.Field f = Strategy.class.getDeclaredField("strategy");
            f.setAccessible(true);
            return (Strategy) f.get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
