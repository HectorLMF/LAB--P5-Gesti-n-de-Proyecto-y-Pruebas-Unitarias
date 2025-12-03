/**
 * @file SearchCandidate.java
 * @brief Clase abstracta base para estrategias de búsqueda de candidatos
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package local_search.candidate_type;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import problem.definition.State;

/**
 * @class SearchCandidate
 * @brief Clase abstracta que define la interfaz para búsqueda de candidatos
 * 
 * Esta clase abstracta establece el contrato que deben cumplir todas las
 * implementaciones de estrategias de búsqueda de candidatos en el vecindario.
 */
public abstract class SearchCandidate {
	
	/**
	 * @brief Busca y selecciona un estado del vecindario
	 * @param listNeighborhood Lista de estados vecinos disponibles
	 * @return State Estado seleccionado según la estrategia implementada
	 * @throws IllegalArgumentException Si los argumentos son inválidos
	 * @throws SecurityException Si hay problemas de seguridad
	 * @throws ClassNotFoundException Si no se encuentra una clase
	 * @throws InstantiationException Si hay problemas al instanciar
	 * @throws IllegalAccessException Si hay problemas de acceso
	 * @throws InvocationTargetException Si hay problemas en la invocación
	 * @throws NoSuchMethodException Si no se encuentra un método
	 */
	public abstract State stateSearch(List<State> listNeighborhood) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException ;
}
