/**
 * @file TabuSolutions.java
 * @brief Clase para gestión de lista tabú en búsqueda local
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package local_search.complement;

import java.util.ArrayList;
import java.util.List;

import problem.definition.State;

/**
 * @class TabuSolutions
 * @brief Clase que gestiona la lista tabú de soluciones prohibidas
 * 
 * Esta clase mantiene una lista de soluciones visitadas recientemente que
 * no deben ser revisitadas, implementando el mecanismo de memoria a corto
 * plazo de la búsqueda tabú.
 */
public class TabuSolutions {
	
	/** @brief Lista de soluciones tabú (prohibidas) */
	public static List<State> listTabu = new ArrayList<State>();

	/** @brief Número máximo de elementos en la lista tabú */
	public static int maxelements; 

	/**
	 * @brief Filtra el vecindario eliminando soluciones tabú
	 * @param listNeighborhood Lista de estados vecinos a filtrar
	 * @return List<State> Lista filtrada sin soluciones tabú
	 * @throws Exception Si todos los vecinos están en la lista tabú
	 */
	public List<State> filterNeighborhood(List<State> listNeighborhood) throws Exception {
		List<State> listFiltrate = new ArrayList<State>();
		//List<ProblemState> auxList = new ArrayList<ProblemState>();
		//auxList = listNeighborhood;
		//Problem problem = new Problem();
		if (!listTabu.isEmpty()) {
			for (int i = listNeighborhood.size() - 1; i >= 0 ; i--) {
				int count_tabu = 0; 
				while (listTabu.size() > count_tabu) {
					if (listNeighborhood.get(i).equals(listTabu.get(count_tabu))) {
						listNeighborhood.remove(i);
					}
					count_tabu++;
				}
			}
			listFiltrate = listNeighborhood;
			if (listFiltrate.isEmpty()) {
				throw new Exception();
			}
		} else {
			listFiltrate = listNeighborhood;
		}
		return listFiltrate;
	}
}