/**
 * @file AcceptNotBadU.java
 * @brief Implementación de aceptación con umbral de deterioro
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
 * @class AcceptNotBadU
 * @brief Clase que acepta candidatos basándose en un umbral de deterioro
 * 
 * Esta clase implementa una estrategia de aceptación que permite soluciones peores
 * dentro de un umbral (threshold) definido. Permite cierta exploración del espacio
 * de búsqueda sin alejarse demasiado de la solución actual.
 */
public class AcceptNotBadU extends AcceptableCandidate{

	/**
	 * @brief Acepta candidatos dentro de un umbral de deterioro permitido
	 * @param stateCurrent Estado actual de la búsqueda
	 * @param stateCandidate Estado candidato a evaluar
	 * @return Boolean true si el deterioro está dentro del umbral, false en caso contrario
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
		Double threshold = Strategy.getStrategy().getThreshold();
		double effectiveThreshold = Math.abs(threshold);
		
		if (problem.getTypeProblem().equals(ProblemType.MAXIMIZAR)) {
			// For maximization: accept if candidate > current - abs(threshold)
			if (stateCandidate.getEvaluation().get(0) > stateCurrent.getEvaluation().get(0) - effectiveThreshold) {
				accept = true;
			} else {
				accept = false;
			}
		} else {
			// For minimization: accept if candidate < current + abs(threshold)
			if (stateCandidate.getEvaluation().get(0) < stateCurrent.getEvaluation().get(0) + effectiveThreshold) {
				accept = true;
			} else {
				accept = false;
			}
		}
		return accept;
	}
}
