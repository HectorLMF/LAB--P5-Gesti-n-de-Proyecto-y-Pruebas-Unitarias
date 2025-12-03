package local_search.complement;

import factory_interface.IFFactoryGenerator;
import factory_method.FactoryGenerator;

import java.lang.reflect.InvocationTargetException;

import metaheurictics.strategy.Strategy;
import metaheuristics.generators.DistributionEstimationAlgorithm;
import metaheuristics.generators.EvolutionStrategies;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.GeneticAlgorithm;
import metaheuristics.generators.ParticleSwarmOptimization;


public class UpdateParameter {
	
	private static IFFactoryGenerator ifFactoryGenerator;
	
	public static Integer updateParameter(Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {//HashMap<String, Object> map, 
		countIterationsCurrent = countIterationsCurrent + 1;
		//		Here update parameter for update and change generator.
		if(countIterationsCurrent.equals(GeneticAlgorithm.countRef - 1)){
			ifFactoryGenerator = new FactoryGenerator();
			Strategy.getStrategy().generator = ifFactoryGenerator.createGenerator(GeneratorType.GENETIC_ALGORITHM);
		}
		else{
			if(countIterationsCurrent.equals(EvolutionStrategies.countRef - 1)){
				ifFactoryGenerator = new FactoryGenerator();
				Strategy.getStrategy().generator = ifFactoryGenerator.createGenerator(GeneratorType.EVOLUTION_STRATEGIES);
			}			
			if(countIterationsCurrent.equals(DistributionEstimationAlgorithm.countRef - 1)){
				ifFactoryGenerator = new FactoryGenerator();
				Strategy.getStrategy().generator = ifFactoryGenerator.createGenerator(GeneratorType.DISTRIBUTION_ESTIMATION_ALGORITHM);
			}
			if(countIterationsCurrent.equals(ParticleSwarmOptimization.countRef - 1)){
				ifFactoryGenerator = new FactoryGenerator();
				Strategy.getStrategy().generator = ifFactoryGenerator.createGenerator(GeneratorType.PARTICLE_SWARM_OPTIMIZATION);
			}
		}
		return countIterationsCurrent;
	}
}



