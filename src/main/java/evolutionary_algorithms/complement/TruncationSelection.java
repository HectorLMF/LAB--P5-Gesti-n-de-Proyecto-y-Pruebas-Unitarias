/**
 * @file TruncationSelection.java
 * @brief Implementación del operador de selección por truncamiento
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package evolutionary_algorithms.complement;


import java.util.ArrayList;
import java.util.List;

import metaheurictics.strategy.Strategy;

import problem.definition.State;
import problem.definition.Problem.ProblemType;

/**
 * @class TruncationSelection
 * @brief Clase que implementa el operador de selección por truncamiento
 * 
 * Selecciona los mejores individuos de la población según un umbral de truncamiento.
 */
public class TruncationSelection extends FatherSelection {
	
	/**
	 * @brief Ordena la lista de estados en orden descendente (mejor a peor)
	 * @param listState Lista de estados a ordenar
	 * @return Lista de estados ordenada descendentemente
	 */
	public List<State> OrderBetter (List<State> listState){
		State var = null;
		for (int i = 0; i < listState.size()- 1; i++) {
			for (int j = i+1; j < listState.size(); j++) {
				if(listState.get(i).getEvaluation().get(0) < listState.get(j).getEvaluation().get(0)){
					var = listState.get(i);
					listState.set(i, listState.get(j));
					listState.set(j,var);
				}
			}
		}
		return listState;
	}
	
	/**
	 * @brief Ordena la lista de estados en orden ascendente (mejor a peor para minimización)
	 * @param listState Lista de estados a ordenar
	 * @return Lista de estados ordenada ascendentemente
	 */
	public List<State> ascOrderBetter (List<State> listState){
		State var = null;
		for (int i = 0; i < listState.size()- 1; i++) {
			for (int j = i+1; j < listState.size(); j++) {
				if(listState.get(i).getEvaluation().get(0) > listState.get(j).getEvaluation().get(0)){
					var = listState.get(i);
					listState.set(i, listState.get(j));
					listState.set(j,var);
				}
			}
		}
		return listState;
	}
    
	/**
	 * @brief Selecciona los mejores individuos por truncamiento
	 * @param listState Lista de estados de la población
	 * @param truncation Número de individuos a seleccionar
	 * @return Lista de estados seleccionados
	 */
	@Override
	public List<State> selection(List<State> listState, int truncation) {
		List<State> AuxList = new ArrayList<State>();
		if (Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)) {
			listState = OrderBetter(listState);
		} else {
			if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Minimizar))
				listState = ascOrderBetter(listState);
		}
		int i = 0;
		while(AuxList.size()< truncation){
			AuxList.add(listState.get(i));
			i++;
		}
		return AuxList;
	}
}
