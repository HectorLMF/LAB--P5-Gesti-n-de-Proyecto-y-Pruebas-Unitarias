/**
 * @file NotDominatedCandidate.java
 * @brief Implementación de búsqueda de candidatos no dominados
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package local_search.candidate_type;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import local_search.acceptation_type.Dominance;
import metaheurictics.strategy.Strategy;

import problem.definition.State;

/**
 * @class NotDominatedCandidate
 * @brief Clase que selecciona candidatos basados en dominancia de Pareto
 * 
 * Esta clase implementa una estrategia de selección para problemas multiobjetivo,
 * eligiendo el primer candidato que no es dominado por otros en el vecindario.
 */
public class NotDominatedCandidate extends SearchCandidate {

	/**
	 * @brief Busca un estado no dominado en el vecindario
	 * @param listNeighborhood Lista de estados vecinos
	 * @return State Primer estado no dominado encontrado
	 * @throws IllegalArgumentException Si los argumentos son inválidos
	 * @throws SecurityException Si hay problemas de seguridad
	 * @throws ClassNotFoundException Si no se encuentra una clase
	 * @throws InstantiationException Si hay problemas al instanciar
	 * @throws IllegalAccessException Si hay problemas de acceso
	 * @throws InvocationTargetException Si hay problemas en la invocación
	 * @throws NoSuchMethodException Si no se encuentra un método
	 */
	@Override
	public State stateSearch(List<State> listNeighborhood) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		State state = new State();
		State stateA = listNeighborhood.get(0);
		boolean stop = false;
		if(listNeighborhood.size() == 1){
			stop = true;
			state = stateA;
		}
		else {
			Strategy.getStrategy().getProblem().Evaluate(stateA);
			State stateB = new State();
			Dominance dominance = new Dominance();
			for (int i = 1; i < listNeighborhood.size(); i++) {
				while(stop == false){
					stateB = listNeighborhood.get(i);
					Strategy.getStrategy().getProblem().Evaluate(stateB);
					if(dominance.dominance(stateB, stateA) == true){
						stateA = stateB;
					}else{
						stop = true;
						state = stateA;
					}
				}
			}
		}
		return state;
	}

}
