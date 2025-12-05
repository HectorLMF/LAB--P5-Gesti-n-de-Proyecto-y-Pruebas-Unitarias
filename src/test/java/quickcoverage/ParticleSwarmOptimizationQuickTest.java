package quickcoverage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import metaheuristics.generators.ParticleSwarmOptimization;
import metaheuristics.generators.GeneratorType;

public class ParticleSwarmOptimizationQuickTest {

    @Test
    public void constructorAndGetters() {
        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        assertNotNull(pso, "PSO instance should not be null");

        // generator type should be set to PARTICLE_SWARM_OPTIMIZATION
        assertEquals(GeneratorType.PARTICLE_SWARM_OPTIMIZATION, pso.getGeneratorType());

        // default weight is 0
        assertEquals(0.0f, pso.getWeight(), 1e-6f);

        // particle list exists (may be empty)
        assertNotNull(pso.getListParticle());
    }
}
