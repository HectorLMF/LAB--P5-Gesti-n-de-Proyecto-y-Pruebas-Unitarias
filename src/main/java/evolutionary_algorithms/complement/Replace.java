/**
 * @file Replace.java
 * @brief Clase abstracta base para operadores de reemplazo
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package evolutionary_algorithms.complement;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import problem.definition.State;


/**
 * @class Replace
 * @brief Clase abstracta que define la interfaz para operadores de reemplazo en algoritmos evolutivos
 */
public abstract class Replace {

	/**
	 * @brief Reemplaza un estado candidato en la población
	 * @param stateCandidate Estado candidato a insertar
	 * @param listState Lista de estados de la población
	 * @return Lista de estados actualizada
	 * @throws IllegalArgumentException Si los argumentos son inválidos
	 * @throws SecurityException Si hay un problema de seguridad
	 * @throws ClassNotFoundException Si no se encuentra una clase
	 * @throws InstantiationException Si hay error en la instanciación
	 * @throws IllegalAccessException Si hay error de acceso
	 * @throws InvocationTargetException Si hay error en la invocación
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	public abstract List<State> replace(State stateCandidate, List<State>listState) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;
}
