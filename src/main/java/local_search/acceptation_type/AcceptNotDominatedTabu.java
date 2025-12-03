/**
 * @file AcceptNotDominatedTabu.java
 * @brief Implementación de aceptación basada en dominancia con lista tabú
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package local_search.acceptation_type;

import java.util.List;

import metaheurictics.strategy.Strategy;

import problem.definition.State;

/**
 * @class AcceptNotDominatedTabu
 * @brief Clase que mantiene un frente de Pareto con estrategia tabú
 * 
 * Esta clase implementa una estrategia de aceptación para problemas multiobjetivo
 * que mantiene y actualiza el frente de Pareto de soluciones no dominadas,
 * incorporando el concepto de búsqueda tabú.
 */
public class AcceptNotDominatedTabu extends AcceptableCandidate {

	/**
	 * @brief Actualiza el frente de Pareto con soluciones no dominadas
	 * @param stateCurrent Estado actual de la búsqueda
	 * @param stateCandidate Estado candidato a evaluar
	 * @return Boolean Siempre retorna true después de actualizar el frente
	 */
	@Override
	public Boolean acceptCandidate(State stateCurrent, State stateCandidate) {
		List<State> list = Strategy.getStrategy().listRefPoblacFinal;
		
		Dominance dominance = new Dominance();
		if(list.size() == 0){
			list.add(stateCurrent.getCopy());
		}
		//Verificando si la soluci�n candidata domina a alguna de las soluciones de la lista de soluciones no dominadas
		//De ser as� se eliminan de la lista y se adiciona la nueva soluci�n en la lista
		//De lo contrario no se adiciona la soluci�n candidata a la lista
		//Si fue insertada en la lista entonces la solucion candidata se convierte en solucion actual
		dominance.ListDominance(stateCandidate, list);
		return true;
	}
}
