/**
 * @file AcceptNotBadT.java
 * @brief Implementación de aceptación con criterio de temperatura (Simulated Annealing)
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package local_search.acceptation_type;

import java.lang.reflect.InvocationTargetException;

import metaheurictics.strategy.Strategy;
import metaheuristics.generators.SimulatedAnnealing;
import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;
import config.SecureRandomGenerator;

/**
 * @class AcceptNotBadT
 * @brief Clase que acepta candidatos basándose en temperatura (Simulated Annealing)
 * 
 * Esta clase implementa el criterio de aceptación de Simulated Annealing, donde
 * soluciones peores pueden ser aceptadas con una probabilidad que depende de la
 * temperatura actual del sistema. Permite escapar de óptimos locales.
 */
public class AcceptNotBadT extends AcceptableCandidate{

	/**
	 * @brief Acepta candidatos usando criterio de temperatura de Simulated Annealing
	 * @param stateCurrent Estado actual de la búsqueda
	 * @param stateCandidate Estado candidato a evaluar
	 * @return Boolean true si el candidato es aceptado según el criterio probabilístico
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
		if (problem.getTypeProblem().equals(ProblemType.MAXIMIZAR)) {
			double result = (stateCandidate.getEvaluation().get(0) - stateCurrent.getEvaluation().get(0)) / SimulatedAnnealing.tinitial;
			double probaleatory = Math.random();
			double exp = Math.exp(result);
			if ((stateCandidate.getEvaluation().get(0) >= stateCurrent.getEvaluation().get(0))
					|| (probaleatory < exp))
 				accept = true;
			else
				accept = false;
		} else {
			double result_min = (stateCandidate.getEvaluation().get(0) - stateCurrent.getEvaluation().get(0)) / SimulatedAnnealing.tinitial;
			if ((stateCandidate.getEvaluation().get(0) <= stateCurrent.getEvaluation().get(0))
					|| (SecureRandomGenerator.nextDouble() < Math.exp(result_min)))
				accept = true;
			else
				accept = false;
		}
		return accept;
	}
}
