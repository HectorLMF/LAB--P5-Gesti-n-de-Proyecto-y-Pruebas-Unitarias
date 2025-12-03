/**
 * @file StopExecute.java
 * @brief Clase para criterio de parada basado en número de iteraciones
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package local_search.complement;

/**
 * @class StopExecute
 * @brief Clase que determina cuándo detener la ejecución del algoritmo
 * 
 * Esta clase implementa un criterio de parada simple basado en el número
 * máximo de iteraciones permitidas para el algoritmo de búsqueda.
 */
public class StopExecute {
		
	/**
	 * @brief Determina si se debe detener la ejecución
	 * @param countIterationsCurrent Número actual de iteraciones ejecutadas
	 * @param countmaxIterations Número máximo de iteraciones permitidas
	 * @return Boolean true si se alcanzó el máximo de iteraciones, false en caso contrario
	 */
	public Boolean stopIterations(int countIterationsCurrent, int countmaxIterations) {
		if (countIterationsCurrent < countmaxIterations) {
			return false;
		} else {
			return true;
		}
	}
}
