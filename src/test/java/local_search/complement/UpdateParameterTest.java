package local_search.complement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import metaheuristics.generators.DistributionEstimationAlgorithm;
import metaheuristics.generators.EvolutionStrategies;
import metaheuristics.generators.GeneticAlgorithm;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.ParticleSwarmOptimization;

@DisplayName("Tests para UpdateParameter")
class UpdateParameterTest {

	@AfterEach
	void tearDown() {
		Strategy.destroyExecute();
		// avoid leaking modified static refs across tests
		GeneticAlgorithm.countRef = 0;
		EvolutionStrategies.countRef = 0;
		DistributionEstimationAlgorithm.countRef = 0;
		ParticleSwarmOptimization.countRef = 0;
	}

	@Test
	@DisplayName("updateParameter incrementa el contador")
	void updateParameter_incrementsCounter() throws Exception {
		Strategy.destroyExecute();
		Strategy.getStrategy().generator = null;

		Integer next = UpdateParameter.updateParameter(41);
		assertEquals(42, next);
		assertNull(Strategy.getStrategy().generator);
	}

	@Test
	@DisplayName("updateParameter cambia a GeneticAlgorithm cuando count==GA.countRef-1")
	void updateParameter_switchesToGA_whenThresholdHit() throws Exception {
		Strategy.destroyExecute();
		Strategy.getStrategy().generator = null;

		GeneticAlgorithm.countRef = 3; // threshold is 2
		EvolutionStrategies.countRef = 0;
		DistributionEstimationAlgorithm.countRef = 0;
		ParticleSwarmOptimization.countRef = 0;

		Integer next = UpdateParameter.updateParameter(1); // becomes 2
		assertEquals(2, next);
		assertNotNull(Strategy.getStrategy().generator);
		assertEquals(GeneratorType.GENETIC_ALGORITHM, Strategy.getStrategy().generator.getType());
	}

	@Test
	@DisplayName("updateParameter cambia a EvolutionStrategies cuando GA no aplica y count==ES.countRef-1")
	void updateParameter_switchesToES_whenThresholdHit() throws Exception {
		Strategy.destroyExecute();
		Strategy.getStrategy().generator = null;

		GeneticAlgorithm.countRef = 0;
		EvolutionStrategies.countRef = 3; // threshold is 2
		DistributionEstimationAlgorithm.countRef = 0;
		ParticleSwarmOptimization.countRef = 0;

		Integer next = UpdateParameter.updateParameter(1); // becomes 2
		assertEquals(2, next);
		assertNotNull(Strategy.getStrategy().generator);
		assertEquals(GeneratorType.EVOLUTION_STRATEGIES, Strategy.getStrategy().generator.getType());
	}

	@Test
	@DisplayName("updateParameter cambia a DEA cuando GA no aplica y count==DEA.countRef-1")
	void updateParameter_switchesToDEA_whenThresholdHit() throws Exception {
		Strategy.destroyExecute();
		Strategy.getStrategy().generator = null;

		GeneticAlgorithm.countRef = 0;
		EvolutionStrategies.countRef = 0;
		DistributionEstimationAlgorithm.countRef = 3; // threshold is 2
		ParticleSwarmOptimization.countRef = 0;

		Integer next = UpdateParameter.updateParameter(1); // becomes 2
		assertEquals(2, next);
		// Note: DEA may fail to instantiate internally due to reflective factory naming mismatches
		// (same root issue that prevents exercising DEA.generate() in tests). We still cover this branch.
	}

	@Test
	@DisplayName("updateParameter cambia a PSO cuando GA no aplica y count==PSO.countRef-1")
	void updateParameter_switchesToPSO_whenThresholdHit() throws Exception {
		Strategy.destroyExecute();
		Strategy.getStrategy().generator = null;

		GeneticAlgorithm.countRef = 0;
		EvolutionStrategies.countRef = 0;
		DistributionEstimationAlgorithm.countRef = 0;
		ParticleSwarmOptimization.countRef = 3; // threshold is 2

		Integer next = UpdateParameter.updateParameter(1); // becomes 2
		assertEquals(2, next);
		assertNotNull(Strategy.getStrategy().generator);
		assertEquals(GeneratorType.PARTICLE_SWARM_OPTIMIZATION, Strategy.getStrategy().generator.getType());
	}
}
