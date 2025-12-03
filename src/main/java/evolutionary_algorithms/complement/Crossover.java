/**
 * @file Crossover.java
 * @brief Clase abstracta base para operadores de cruce
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package evolutionary_algorithms.complement;


import problem.definition.State;

/**
 * @class Crossover
 * @brief Clase abstracta que define la interfaz para operadores de cruce en algoritmos evolutivos
 */
public abstract class Crossover {
	
	/**
	 * @brief Realiza el cruce entre dos estados padres
	 * @param father1 Primer estado padre
	 * @param father2 Segundo estado padre
	 * @param PC Probabilidad de cruce
	 * @return Estado hijo resultado del cruce
	 */
	public abstract State crossover(State father1, State father2, double PC);
}
