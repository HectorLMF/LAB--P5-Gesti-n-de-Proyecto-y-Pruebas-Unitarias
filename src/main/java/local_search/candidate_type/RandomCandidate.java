/**
 * @file RandomCandidate.java
 * @brief Implementación de selección aleatoria de candidatos
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package local_search.candidate_type;

import java.util.List;

import problem.definition.State;

/**
 * @class RandomCandidate
 * @brief Clase que selecciona un candidato aleatorio del vecindario
 * 
 * Esta clase implementa una estrategia de selección que elige un estado
 * aleatorio del vecindario, promoviendo la exploración diversificada.
 */
public class RandomCandidate extends SearchCandidate {

	/**
	 * @brief Selecciona aleatoriamente un estado del vecindario
	 * @param listNeighborhood Lista de estados vecinos
	 * @return State Estado seleccionado aleatoriamente
	 */
	@Override
	public State stateSearch(List<State> listNeighborhood) {
		int pos = (int)(Math.random() * (double)(listNeighborhood.size() - 1));
		State stateAleatory = listNeighborhood.get(pos);
		return stateAleatory;
	}
}
