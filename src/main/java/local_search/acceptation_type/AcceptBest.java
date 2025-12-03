/**
 * @file AcceptBest.java
 * @brief Implementación de estrategia de aceptación basada en mejora de la solución
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package local_search.acceptation_type;

import java.lang.reflect.InvocationTargetException;

import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

/**
 * @class AcceptBest
 * @brief Clase que acepta candidatos que mejoran o igualan la solución actual
 * 
 * Esta clase implementa una estrategia de aceptación determinista que solo acepta
 * soluciones candidatas que sean mejores o iguales a la solución actual, según el
 * tipo de problema (maximización o minimización).
 */
public class AcceptBest extends AcceptableCandidate {

	/**
	 * @brief Acepta candidatos que mejoran o igualan la evaluación actual
	 * @param stateCurrent Estado actual de la búsqueda
	 * @param stateCandidate Estado candidato a evaluar
	 * @return Boolean true si el candidato es mejor o igual, false en caso contrario
	 * @throws IllegalArgumentException Si los argumentos son inválidos
	 * @throws SecurityException Si hay problemas de seguridad
	 * @throws ClassNotFoundException Si no se encuentra una clase
	 * @throws InstantiationException Si hay problemas al instanciar
	 * @throws IllegalAccessException Si hay problemas de acceso
	 * @throws InvocationTargetException Si hay problemas en la invocación
	 * @throws NoSuchMethodException Si no se encuentra un método
	 */
	@Override
	public Boolean acceptCandidate(State stateCurrent, State stateCandidate) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Boolean accept = null;
		Problem problem = Strategy.getStrategy().getProblem();
		if(problem.getTypeProblem().equals(ProblemType.Maximizar)) {
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
		}
		return accept;
	}
}
