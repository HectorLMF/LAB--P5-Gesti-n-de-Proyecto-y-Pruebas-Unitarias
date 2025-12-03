/**
 * @file UpdateParameter.java
 * @brief Clase para actualización dinámica de parámetros y generadores
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

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

/**
 * @class UpdateParameter
 * @brief Clase que gestiona la actualización de parámetros y cambio de generadores
 * 
 * Esta clase permite la actualización dinámica de parámetros durante la ejecución
 * y el cambio automático entre diferentes generadores metaheurísticos según
 * el número de iteraciones.
 */
public class UpdateParameter {
	
	/** @brief Fábrica para crear instancias de generadores */
	private static IFFactoryGenerator ifFactoryGenerator;
	
	/**
	 * @brief Actualiza parámetros y cambia generador según iteración
	 * @param countIterationsCurrent Número actual de iteraciones
	 * @return Integer Número de iteraciones incrementado
	 * @throws IllegalArgumentException Si los argumentos son inválidos
	 * @throws SecurityException Si hay problemas de seguridad
	 * @throws ClassNotFoundException Si no se encuentra una clase
	 * @throws InstantiationException Si hay problemas al instanciar
	 * @throws IllegalAccessException Si hay problemas de acceso
	 * @throws InvocationTargetException Si hay problemas en la invocación
	 * @throws NoSuchMethodException Si no se encuentra un método
	 */
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



