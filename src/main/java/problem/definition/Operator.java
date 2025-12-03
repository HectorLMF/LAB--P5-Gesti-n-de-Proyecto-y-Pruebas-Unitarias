/**
 * @file Operator.java
 * @brief Clase abstracta que define operadores para generar nuevos estados.
 * 
 * Esta clase proporciona la interfaz para operadores que pueden generar
 * nuevos estados a partir de estados existentes o de forma aleatoria.
 * Es la base para operadores de búsqueda local y generación aleatoria.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package problem.definition;

import java.util.List;

/**
 * @class Operator
 * @brief Operador abstracto para generación de estados.
 */
public abstract class Operator {
	
		/**
		 * @brief Genera nuevos estados a partir de un estado actual.
		 * 
		 * @param stateCurrent Estado actual desde el cual generar
		 * @param operatornumber Número de operador a aplicar
		 * @return Lista de nuevos estados generados
		 */
		public abstract List<State> generatedNewState(State stateCurrent, Integer operatornumber);
		
		/**
		 * @brief Genera estados de forma aleatoria.
		 * 
		 * @param operatornumber Número de estados aleatorios a generar
		 * @return Lista de estados generados aleatoriamente
		 */
		public abstract List<State> generateRandomState (Integer operatornumber);

	}

