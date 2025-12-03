/**
 * @file OnePointMutation.java
 * @brief Implementación del operador de mutación de un punto
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package evolutionary_algorithms.complement;

import config.SecureRandomGenerator;
import metaheurictics.strategy.Strategy;
import problem.definition.State;

/**
 * @class OnePointMutation
 * @brief Clase que implementa el operador de mutación de un punto
 * 
 * Muta un solo gen aleatorio del individuo con un nuevo valor aleatorio.
 */
public class OnePointMutation extends Mutation {

	/**
	 * @brief Aplica mutación de un punto al estado dado
	 * @param state Estado a mutar
	 * @param PM Probabilidad de mutación
	 * @return Estado mutado
	 */
	@Override
	public State mutation(State state, double PM) {
		double probM = SecureRandomGenerator.nextDouble();
		if (PM >= probM) {
			Object key = Strategy.getStrategy().getProblem().getCodification().getAleatoryKey();
			Object value = Strategy.getStrategy().getProblem().getCodification().getVariableAleatoryValue((Integer) key);
			state.getCode().set((Integer) key, value);
		}
		return state;
	}
}
