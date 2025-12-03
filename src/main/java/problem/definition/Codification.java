/**
 * @file Codification.java
 * @brief Clase abstracta que define el sistema de codificación de soluciones.
 * 
 * Esta clase proporciona la interfaz para sistemas de codificación que
 * representan soluciones en el espacio de búsqueda. Define métodos para
 * validar estados, generar valores aleatorios y gestionar variables.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package problem.definition;

/**
 * @class Codification
 * @brief Sistema de codificación abstracto para representar soluciones.
 */
public abstract class Codification {

	/**
	 * @brief Valida si un estado es válido según las restricciones.
	 * @param state Estado a validar
	 * @return true si el estado es válido, false en caso contrario
	 */
	public abstract boolean validState(State state);
	
	/**
	 * @brief Obtiene un valor aleatorio para una variable.
	 * @param key Clave de la variable
	 * @return Valor aleatorio válido para la variable
	 */
	public abstract Object getVariableAleatoryValue(int key);
	
	/**
	 * @brief Obtiene una clave aleatoria de variable.
	 * @return Clave de variable aleatoria
	 */
	public abstract int getAleatoryKey ();
	
	/**
	 * @brief Obtiene el número total de variables.
	 * @return Cantidad de variables en la codificación
	 */
	public abstract int getVariableCount();

}