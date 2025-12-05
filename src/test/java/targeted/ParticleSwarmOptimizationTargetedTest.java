package targeted;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import metaheuristics.generators.ParticleSwarmOptimization;
import metaheuristics.generators.RandomSearch;
import metaheurictics.strategy.Strategy;

public class ParticleSwarmOptimizationTargetedTest {

    @AfterEach
    public void tearDown() {
        Strategy.destroyExecute();
        RandomSearch.listStateReference = new java.util.ArrayList<>();
    }

    @Test
    public void constructor_withEmptyRandomSearch_initializesSafely() {
        // ensure RandomSearch global list is empty
        RandomSearch.listStateReference = new java.util.ArrayList<>();
        Strategy.destroyExecute();
        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        assertNotNull(pso);
        // particle list should be empty when there is no RandomSearch state reference
        assertTrue(pso.getListParticle().isEmpty());
        assertEquals(metaheuristics.generators.GeneratorType.PARTICLE_SWARM_OPTIMIZATION, pso.getGeneratorType());
    }
}
