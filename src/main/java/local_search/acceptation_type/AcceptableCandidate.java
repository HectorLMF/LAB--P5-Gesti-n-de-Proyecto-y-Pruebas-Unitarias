/**
 * @file AcceptableCandidate.java
 * @brief Clase abstracta base para estrategias de aceptación de candidatos
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package local_search.acceptation_type;

import java.lang.reflect.InvocationTargetException;

import problem.definition.State;

/**
 * @class AcceptableCandidate
 * @brief Clase abstracta que define la interfaz para criterios de aceptación
 * 
 * Esta clase abstracta establece el contrato que deben cumplir todas las
 * implementaciones de estrategias de aceptación en algoritmos de búsqueda local.
 */
public abstract class AcceptableCandidate {
  
	/**
	 * @brief Determina si un estado candidato debe ser aceptado
	 * @param stateCurrent Estado actual de la búsqueda
	 * @param stateCandidate Estado candidato a evaluar
	 * @return Boolean true si el candidato es aceptado, false en caso contrario
	 * @throws IllegalArgumentException Si los argumentos son inválidos
	 * @throws SecurityException Si hay problemas de seguridad
	 * @throws ClassNotFoundException Si no se encuentra una clase
	 * @throws InstantiationException Si hay problemas al instanciar
	 * @throws IllegalAccessException Si hay problemas de acceso
	 * @throws InvocationTargetException Si hay problemas en la invocación
	 * @throws NoSuchMethodException Si no se encuentra un método
	 */
	public abstract Boolean acceptCandidate(State stateCurrent, State stateCandidate) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException ;
}
