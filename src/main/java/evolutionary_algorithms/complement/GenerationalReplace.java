/**
 * @file GenerationalReplace.java
 * @brief Implementación de reemplazo generacional
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package evolutionary_algorithms.complement;


import java.lang.reflect.InvocationTargetException;
import java.util.List;

import problem.definition.State;

/**
 * @class GenerationalReplace
 * @brief Clase que implementa el operador de reemplazo generacional
 * 
 * Reemplaza toda la población con la nueva generación de individuos.
 */
public class GenerationalReplace extends Replace {

	/**
	 * @brief Reemplaza un estado en la población usando estrategia generacional
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
	@Override
	public List<State> replace(State stateCandidate, List<State> listState) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		listState.remove(0);
		listState.add(stateCandidate);
		/*List<State> sonList = Strategy.getStrategy().generator.getSonList();
		for (int i = 0; i < listState.size(); i++) {
			listState.set(i, sonList.get(i));
		}*/
		return listState;
	}
}
