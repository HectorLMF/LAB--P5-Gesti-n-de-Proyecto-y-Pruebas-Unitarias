/**
 * @file Distribution.java
 * @brief Clase abstracta base para distribuciones de probabilidad
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package evolutionary_algorithms.complement;

import java.util.List;

import problem.definition.State;


/**
 * @class Distribution
 * @brief Clase abstracta que define la interfaz para distribuciones de probabilidad
 */
public abstract class Distribution {
	/**
	 * @brief Calcula la distribución de probabilidad basada en los padres
	 * @param fathers Lista de estados padres
	 * @return Lista de probabilidades calculadas
	 */
	public abstract List<Probability> distribution(List<State> fathers);

}
