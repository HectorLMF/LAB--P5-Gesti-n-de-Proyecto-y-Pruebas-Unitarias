/**
 * @file AcceptNotDominated.java
 * @brief Implementación de aceptación basada en dominancia de Pareto
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package local_search.acceptation_type;


import local_search.acceptation_type.Dominance;
import metaheurictics.strategy.Strategy;

import problem.definition.State;

/**
 * @class AcceptNotDominated
 * @brief Clase que acepta candidatos no dominados según criterio de Pareto
 * 
 * Esta clase implementa una estrategia de aceptación para problemas multiobjetivo
 * basada en el concepto de dominancia de Pareto. Un candidato es aceptado si no es
 * dominado por la solución actual y domina a alguna solución del frente de Pareto.
 */
public class AcceptNotDominated extends AcceptableCandidate {
	
	/**
	 * @brief Acepta candidatos no dominados según criterio de Pareto
	 * @param stateCurrent Estado actual de la búsqueda
	 * @param stateCandidate Estado candidato a evaluar
	 * @return Boolean true si el candidato no está dominado y domina a alguna solución
	 */
	@Override
	public Boolean acceptCandidate(State stateCurrent, State stateCandidate) {
		Boolean accept = false;
		Dominance dominace = new Dominance();
		
		if(Strategy.getStrategy().listRefPoblacFinal.size() == 0){
			Strategy.getStrategy().listRefPoblacFinal.add(stateCurrent.getCopy());
		}
		if(dominace.dominance(stateCurrent, stateCandidate)== false)
		{
			//Verificando si la soluci�n candidata domina a alguna de las soluciones de la lista de soluciones no dominadas
			//De ser as� se eliminan de la lista y se adiciona la nueva soluci�n en la lista
			//De lo contrario no se adiciona la soluci�n candidata a la lista
			//Si fue insertada en la lista entonces la solucion candidata se convierte en solucion actual
			if(dominace.ListDominance(stateCandidate, Strategy.getStrategy().listRefPoblacFinal) == true){
				//Se pone la soluci�n candidata como soluci�n actual
				accept = true;
			}
			else{
				accept = false;
			}
		}
		return accept;
	}

}
