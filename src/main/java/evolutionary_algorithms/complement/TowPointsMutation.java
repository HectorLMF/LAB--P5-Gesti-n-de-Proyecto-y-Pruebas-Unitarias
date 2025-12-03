/**
 * @file TowPointsMutation.java
 * @brief Implementación del operador de mutación de dos puntos
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package evolutionary_algorithms.complement;


import metaheurictics.strategy.Strategy;
import problem.definition.State;

/**
 * @class TowPointsMutation
 * @brief Clase que implementa el operador de mutación de dos puntos
 * 
 * Intercambia los valores de dos genes seleccionados aleatoriamente.
 */
public class TowPointsMutation extends Mutation {

	/*@Override
	public ProblemState mutation(SortedMap<Object, Object> newind, double PM) {
		
		int pos1 = (int) (Math.random() * (int)Problem.countvariable);
		int pos2 = (int) (Math.random() * (int)Problem.countvariable);
		
		Object value1 = (Integer)(newind.get("x" + pos1));
		Object value2 = (Integer)(newind.get("x" + pos2));
		newind.put("x" + pos1, value2);
		newind.put("x" + pos2, value1);
	
		return newind;
	}*/

	/**
	 * @brief Aplica mutación de dos puntos al estado dado
	 * @param newind Estado a mutar
	 * @param PM Probabilidad de mutación
	 * @return Estado mutado
	 */
	@Override
	public State mutation(State newind, double PM) {
		Object key1 = Strategy.getStrategy().getProblem().getCodification().getAleatoryKey();
		Object key2 = Strategy.getStrategy().getProblem().getCodification().getAleatoryKey();
		Object value1 = Strategy.getStrategy().getProblem().getCodification().getVariableAleatoryValue((Integer) key1);
		Object value2 = Strategy.getStrategy().getProblem().getCodification().getVariableAleatoryValue((Integer) key2);
		newind.getCode().set((Integer) key1, (Integer)value2);
		newind.getCode().set((Integer) key2, (Integer)value1);
		return newind;
	}
}
  