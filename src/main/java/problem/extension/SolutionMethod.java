/**
 * @file SolutionMethod.java
 * @brief Clase abstracta base para métodos de solución de problemas de optimización.
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package problem.extension;

import problem.definition.State;

/**
 * @class SolutionMethod
 * @brief Clase abstracta que define la interfaz para métodos de evaluación de estados.
 * 
 * Proporciona la base para implementar diferentes estrategias de evaluación de soluciones
 * en problemas de optimización.
 */
public abstract class SolutionMethod {

	/**
	 * @brief Evalúa un estado según el método de solución implementado.
	 * 
	 * @param state Estado a evaluar
	 */
	public abstract void evaluationState(State state);
}
