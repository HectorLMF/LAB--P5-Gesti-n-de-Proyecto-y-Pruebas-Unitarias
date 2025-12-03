/**
 * @file ObjetiveFunction.java
 * @brief Clase abstracta que define una función objetivo.
 * 
 * Esta clase representa una función objetivo en un problema de optimización.
 * Incluye el tipo de problema (maximizar/minimizar) y un peso para
 * problemas multi-objetivo.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package problem.definition;

import problem.definition.Problem.ProblemType;

/**
 * @class ObjetiveFunction
 * @brief Función objetivo abstracta para optimización.
 */
public abstract class ObjetiveFunction {
	
	/** Tipo de problema: maximizar o minimizar */
	private ProblemType typeProblem;
	
	/** Peso de esta función objetivo en problemas multi-objetivo */
	private float weight;
	
	/**
	 * @brief Obtiene el peso de la función objetivo.
	 * @return Peso de la función
	 */
	public float getWeight() {
		return weight;
	}

	/**
	 * @brief Establece el peso de la función objetivo.
	 * @param weight Nuevo peso
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * @brief Obtiene el tipo de problema.
	 * @return Tipo de problema (maximizar/minimizar)
	 */
	public ProblemType getTypeProblem() {
		return typeProblem;
	}

	/**
	 * @brief Establece el tipo de problema.
	 * @param typeProblem Tipo de problema
	 */
	public void setTypeProblem(ProblemType typeProblem) {
		this.typeProblem = typeProblem;
	}

	/**
	 * @brief Evalúa un estado según esta función objetivo.
	 * 
	 * Método abstracto que debe ser implementado por las subclases
	 * para definir cómo se evalúa un estado específico.
	 * 
	 * @param state Estado a evaluar
	 * @return Valor de evaluación
	 */
	public abstract Double Evaluation(State state);
}
