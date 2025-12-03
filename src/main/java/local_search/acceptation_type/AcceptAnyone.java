/**
 * @file AcceptAnyone.java
 * @brief Implementación de estrategia de aceptación que acepta cualquier candidato
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package local_search.acceptation_type;

import problem.definition.State;

/**
 * @class AcceptAnyone
 * @brief Clase que acepta cualquier solución candidata sin restricciones
 * 
 * Esta clase extiende AcceptableCandidate e implementa una estrategia de aceptación
 * que siempre retorna verdadero, aceptando cualquier estado candidato propuesto.
 */
public class AcceptAnyone extends AcceptableCandidate{

	/**
	 * @brief Acepta cualquier candidato sin evaluación
	 * @param stateCurrent Estado actual de la búsqueda
	 * @param stateCandidate Estado candidato a evaluar
	 * @return Boolean Siempre retorna true, aceptando cualquier candidato
	 */
	@Override
	public Boolean acceptCandidate(State stateCurrent, State stateCandidate) {
		Boolean accept = true;
		return accept;
	}
	
}
