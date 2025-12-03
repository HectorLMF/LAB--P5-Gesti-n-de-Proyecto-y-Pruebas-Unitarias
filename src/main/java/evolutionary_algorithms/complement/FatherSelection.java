/**
 * @file FatherSelection.java
 * @brief Clase abstracta base para operadores de selección de padres
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package evolutionary_algorithms.complement;

import java.util.List;

import problem.definition.State;


/**
 * @class FatherSelection
 * @brief Clase abstracta que define la interfaz para operadores de selección de padres
 */
public abstract class FatherSelection {
	
	/**
	 * @brief Selecciona estados padres de la población
	 * @param listState Lista de estados de la población
	 * @param truncation Número de individuos a seleccionar
	 * @return Lista de estados seleccionados como padres
	 */
	public abstract List<State> selection(List<State> listState, int truncation);

}
