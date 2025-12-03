/**
 * @file SteadyStateReplace.java
 * @brief Implementación del operador de reemplazo de estado estacionario
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package evolutionary_algorithms.complement;


import java.util.List;
import metaheurictics.strategy.Strategy;
import problem.definition.State;
import problem.definition.Problem.ProblemType;


/**
 * @class SteadyStateReplace
 * @brief Clase que implementa el operador de reemplazo de estado estacionario
 * 
 * Reemplaza solo el peor individuo de la población si el candidato es mejor.
 */
public class SteadyStateReplace extends Replace {

	/**
	 * @brief Reemplaza el peor individuo si el candidato es mejor
	 * @param stateCandidate Estado candidato a insertar
	 * @param listState Lista de estados de la población
	 * @return Lista de estados actualizada
	 */
	@Override
	public List<State> replace(State stateCandidate, List<State> listState) {
		State stateREP = null;
		if (Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)) {
			stateREP = MinValue(listState);
			if(stateCandidate.getEvaluation().get(0) >= stateREP.getEvaluation().get(0)){
				Boolean find = false;
		        int count = 0;
		        while ((find.equals(false)) && (listState.size() > count)){
		        	if(listState.get(count).equals(stateREP)){
		        		listState.remove(count);
						listState.add(count, stateCandidate);
						find = true;
					}
		        	else count ++;
				}
			}
		}
		else {
			if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Minimizar)){
				stateREP = MaxValue(listState);
				if(stateCandidate.getEvaluation().get(0) <= stateREP.getEvaluation().get(0)){
					Boolean find = false;
			        int count = 0;
			        while ((find.equals(false)) && (listState.size() > count)){
			        	if(listState.get(count).equals(stateREP)){
			        		listState.remove(count);
							listState.add(count, stateCandidate);
							find = true;
						}
			        	else count ++;
					}
				}
			}
		}
		return listState;
	}
	
	/**
	 * @brief Encuentra el estado con el valor mínimo en la lista
	 * @param listState Lista de estados a evaluar
	 * @return Estado con el valor mínimo de evaluación
	 */
	public State MinValue (List<State> listState){
		State value = listState.get(0);
		double min = listState.get(0).getEvaluation().get(0);
		for (int i = 1; i < listState.size(); i++) {
			if(listState.get(i).getEvaluation().get(0) < min){
				min = listState.get(i).getEvaluation().get(0);
				value = listState.get(i);
			}
		}
		return value;
	}
	
	/**
	 * @brief Encuentra el estado con el valor máximo en la lista
	 * @param listState Lista de estados a evaluar
	 * @return Estado con el valor máximo de evaluación
	 */
	public State MaxValue (List<State> listState){
		State value = listState.get(0);
		double max = listState.get(0).getEvaluation().get(0);
		for (int i = 1; i < listState.size(); i++) {
			if(listState.get(i).getEvaluation().get(0) > max){
				max = listState.get(i).getEvaluation().get(0);
				value = listState.get(i);
			}
		}
		return value;
	}
}
