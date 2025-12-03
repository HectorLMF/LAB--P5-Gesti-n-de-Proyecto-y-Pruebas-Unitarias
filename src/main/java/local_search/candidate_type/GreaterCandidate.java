/**
 * @file GreaterCandidate.java
 * @brief Implementación de búsqueda del candidato con mayor evaluación
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package local_search.candidate_type;


import java.lang.reflect.InvocationTargetException;
import java.util.List;

import problem.definition.State;

/**
 * @class GreaterCandidate
 * @brief Clase que selecciona el candidato con mayor valor de evaluación
 * 
 * Esta clase implementa una estrategia de selección que busca el estado con
 * la mayor evaluación en el vecindario, útil para problemas de maximización.
 */
public class GreaterCandidate extends SearchCandidate {
	
	/**
	 * @brief Busca el estado con mayor evaluación en el vecindario
	 * @param listNeighborhood Lista de estados vecinos
	 * @return State Estado con la mayor evaluación encontrada
	 * @throws IllegalArgumentException Si los argumentos son inválidos
	 * @throws SecurityException Si hay problemas de seguridad
	 * @throws ClassNotFoundException Si no se encuentra una clase
	 * @throws InstantiationException Si hay problemas al instanciar
	 * @throws IllegalAccessException Si hay problemas de acceso
	 * @throws InvocationTargetException Si hay problemas en la invocación
	 * @throws NoSuchMethodException Si no se encuentra un método
	 */
	@Override
	public State stateSearch(List<State> listNeighborhood) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		State stateGreater = null;
		if(listNeighborhood.size() > 1){
			double counter = 0;
			double currentCount = listNeighborhood.get(0).getEvaluation().get(0);;
			for (int i = 1; i < listNeighborhood.size(); i++) {
				counter = listNeighborhood.get(i).getEvaluation().get(0);
				if (counter > currentCount) {
					currentCount = counter;
					stateGreater = listNeighborhood.get(i);
				}
				counter = 0;
			}
			if(stateGreater == null){
				int pos = (int)(Math.random() * (double)(listNeighborhood.size() - 1));
				stateGreater = listNeighborhood.get(pos);
			}
		}
		else stateGreater = listNeighborhood.get(0);
		return stateGreater;
	}
}