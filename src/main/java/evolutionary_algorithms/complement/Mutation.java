/**
 * @file Mutation.java
 * @brief Clase abstracta base para operadores de mutación
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package evolutionary_algorithms.complement;

import problem.definition.State;

/**
 * @class Mutation
 * @brief Clase abstracta que define la interfaz para operadores de mutación
 */
public abstract class Mutation {
	
	/**
	 * @brief Aplica la mutación a un estado
	 * @param state Estado a mutar
	 * @param PM Probabilidad de mutación
	 * @return Estado mutado
	 */
	public abstract State mutation (State state, double PM);

}
