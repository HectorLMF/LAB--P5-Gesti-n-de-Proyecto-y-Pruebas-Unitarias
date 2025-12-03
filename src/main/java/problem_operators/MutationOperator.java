/**
 * @file MutationOperator.java
 * @brief Operador de mutación para generar vecindarios de soluciones.
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package problem_operators;

import java.util.ArrayList;
import java.util.List;

import metaheurictics.strategy.Strategy;

import problem.definition.Operator;
import problem.definition.State;

/**
 * @class MutationOperator
 * @brief Implementa el operador de mutación para algoritmos metaheurísticos.
 * 
 * Genera nuevos estados mutando aleatoriamente variables del estado actual,
 * útil para exploración del espacio de búsqueda.
 */
public class MutationOperator extends Operator {

	/**
	 * @brief Genera nuevos estados mediante mutación del estado actual.
	 * 
	 * Crea un vecindario de soluciones mutando aleatoriamente una variable
	 * del estado actual en cada iteración.
	 * 
	 * @param stateCurrent Estado actual a mutar
	 * @param operatornumber Número de estados vecinos a generar
	 * @return Lista de estados generados por mutación
	 */
	public List<State> generatedNewState(State stateCurrent, Integer operatornumber){
		List<State> listNeigborhood = new ArrayList<State>();
		for (int i = 0; i < operatornumber; i++){
			int key = Strategy.getStrategy().getProblem().getCodification().getAleatoryKey();
			Object candidate = Strategy.getStrategy().getProblem().getCodification().getVariableAleatoryValue(key);
			State state = (State) stateCurrent.getCopy();
			state.getCode().set(key, candidate);
			listNeigborhood.add(state);
		}
		return listNeigborhood;
	}

	/**
	 * @brief Genera estados aleatorios (no implementado).
	 * 
	 * @param operatornumber Número de estados aleatorios a generar
	 * @return null (no implementado)
	 */
	@Override
	public List<State> generateRandomState(Integer operatornumber) {
		// TODO Auto-generated method stub
		return null;
	}

}
