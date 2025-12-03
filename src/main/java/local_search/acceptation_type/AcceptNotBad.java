/**
 * @file AcceptNotBad.java
 * @brief Implementación de estrategia de aceptación que rechaza soluciones peores
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package local_search.acceptation_type;

import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

/**
 * @class AcceptNotBad
 * @brief Clase que acepta candidatos que no empeoran la solución actual
 * 
 * Esta clase implementa una estrategia de aceptación que solo acepta soluciones
 * candidatas que mejoren o igualen la evaluación de la solución actual, considerando
 * el tipo de problema (maximización o minimización).
 */
public class AcceptNotBad extends AcceptableCandidate{

	/**
	 * @brief Acepta candidatos que no empeoran la evaluación actual
	 * @param stateCurrent Estado actual de la búsqueda
	 * @param stateCandidate Estado candidato a evaluar
	 * @return Boolean true si el candidato no empeora la solución, false en caso contrario
	 */
	@Override
	public Boolean acceptCandidate(State stateCurrent, State stateCandidate) {
		Boolean accept = null;
		Problem problem = Strategy.getStrategy().getProblem();
		for (int i = 0; i < problem.getFunction().size(); i++) {
		if (problem.getTypeProblem().equals(ProblemType.Maximizar)) {
			if (stateCandidate.getEvaluation().get(0) >= stateCurrent.getEvaluation().get(0)) {
				accept = true;
			} else {
				accept = false;
			}
		} else {
			if (stateCandidate.getEvaluation().get(0) <= stateCurrent.getEvaluation().get(0)) {
				accept = true;
			} else {
				accept = false;
			}
		}}
		return accept;
	}
}
