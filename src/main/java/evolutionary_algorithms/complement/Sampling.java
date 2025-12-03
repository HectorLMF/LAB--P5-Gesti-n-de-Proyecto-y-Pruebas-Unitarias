/**
 * @file Sampling.java
 * @brief Clase abstracta base para operadores de muestreo
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package evolutionary_algorithms.complement;

import java.util.List;

import problem.definition.State;


/**
 * @class Sampling
 * @brief Clase abstracta que define la interfaz para operadores de muestreo
 */
public abstract class Sampling {
	/**
	 * @brief Realiza el muestreo de individuos a partir de los padres
	 * @param fathers Lista de estados padres
	 * @param countInd Número de individuos a generar
	 * @return Lista de estados generados mediante muestreo
	 */
	public abstract List<State> sampling (List<State> fathers, int countInd);
}
