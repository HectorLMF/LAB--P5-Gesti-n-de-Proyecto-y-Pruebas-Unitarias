/**
 * @file Dominance.java
 * @brief Implementación de operaciones de dominancia de Pareto
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package local_search.acceptation_type;

import java.util.List;

import metaheurictics.strategy.Strategy;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.MultiobjectiveHillClimbingDistance;
import problem.definition.Problem.ProblemType;
import problem.definition.State;

/**
 * @class Dominance
 * @brief Clase que gestiona operaciones de dominancia de Pareto multiobjetivo
 * 
 * Esta clase proporciona métodos para evaluar relaciones de dominancia entre
 * soluciones en problemas de optimización multiobjetivo, incluyendo la gestión
 * del frente de Pareto de soluciones no dominadas.
 */
public class Dominance {

	//---------------------------------Métodos que se utilizan en los algoritmos multiobjetivo-------------------------------------------------------//
	/**
	 * @brief Determina si una solución domina a alguna del frente de Pareto
	 * @param solutionX Solución candidata a evaluar
	 * @param list Lista de soluciones del frente de Pareto actual
	 * @return boolean true si la solución fue agregada al frente, false en caso contrario
	 * 
	 * Este método verifica si solutionX domina a alguna solución de la lista,
	 * eliminando las dominadas y agregando solutionX si no está dominada.
	 */
	public boolean ListDominance(State solutionX, List<State> list){
		boolean domain = false;
		for (int i = 0; i < list.size() && domain == false; i++) {
			//Si la soluci�n X domina a la soluci�n de la lista
			if(dominance(solutionX, list.get(i)) == true){
				//Se elimina el elemento de la lista
				list.remove(i);
				if (i!=0) {
					i--;	
				}
				if (Strategy.getStrategy().generator.getType().equals(GeneratorType.MULTIOBJECTIVE_HILL_CLIMBING_DISTANCE)&&list.size()!=0) {
					MultiobjectiveHillClimbingDistance.DistanceCalculateAdd(list);
				}
			}
			if (list.size()>0) {
				if(dominance(list.get(i), solutionX) == true){
					domain = true;
				}
			}

		}
		//Si la soluci�n X no fue dominada
		if(domain == false){
			//Comprobando que la soluci�n no exista
			boolean found = false;
			for (int k = 0; k < list.size() && found == false; k++) {
				State element = list.get(k);
				found = solutionX.Comparator(element);
			}
			//Si la soluci�n no existe
			if(found == false){
				//Se guarda la soluci�n candidata en la lista de soluciones �ptimas de Pareto
				list.add(solutionX.getCopy());
				if (Strategy.getStrategy().generator.getType().equals(GeneratorType.MULTIOBJECTIVE_HILL_CLIMBING_DISTANCE)) {
					MultiobjectiveHillClimbingDistance.DistanceCalculateAdd(list);
				}
				return true;
			}
		}
		return false;

		/*boolean domain = false;
		List<State> deletedSolution = new ArrayList<State>();
		for (int i = 0; i < list.size() && domain == false; i++) {
			State element = list.get(i);
			//Si la soluci�n X domina a la soluci�n de la lista
			if(dominance(solutionX, element) == true){
				//Se elimina el elemento de la lista
				deletedSolution.add(element);
			}
			if(dominance(element, solutionX) == true){
				domain = true;
			}
		}
		//Si la soluci�n X no fue dominada
		if(domain == false){
			//Comprobando que la soluci�n no exista
			boolean found = false;
			for (int k = 0; k < list.size() && found == false; k++) {
				State element = list.get(k);
				found = solutionX.Comparator(element);
			}
			//Si la soluci�n no existe
			if(found == false){

				//Se eliminan de la lista de soluciones optimas de pareto aquellas que fueron dominadas por la soluci�n candidata
				list.removeAll(deletedSolution);
				//Se guarda la soluci�n candidata en la lista de soluciones �ptimas de Pareto
				list.add(solutionX.getCopy());
				if(Strategy.getStrategy().getProblem()!= null){
					Strategy.getStrategy().listRefPoblacFinal = list;
				}
				return true;
			}
		}

		return false;*/
	}


	/**
	 * @brief Verifica si solutionX domina a solutionY según criterio de Pareto
	 * @param solutionX Primera solución a comparar
	 * @param solutionY Segunda solución a comparar
	 * @return boolean true si solutionX domina a solutionY, false en caso contrario
	 * 
	 * Una solución X domina a Y si es al menos igual en todos los objetivos
	 * y estrictamente mejor en al menos uno.
	 */
	public boolean dominance(State solutionX,  State solutionY)	{
		boolean dominance = false;
		int countBest = 0;
		int countEquals = 0;
		//Si solutionX domina a solutionY
		if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)) {
			//Recorriendo las evaluaciones de las funciones objetivo
			for (int i = 0; i < solutionX.getEvaluation().size(); i++) {
				if(solutionX.getEvaluation().get(i).floatValue() > solutionY.getEvaluation().get(i).floatValue()){
					countBest++;
				}
				if(solutionX.getEvaluation().get(i).floatValue() == solutionY.getEvaluation().get(i).floatValue()){
					countEquals++;
				}	
			}
		}
		else{
			//Recorriendo las evaluaciones de las funciones objetivo
			for (int i = 0; i < solutionX.getEvaluation().size(); i++) {
				if(solutionX.getEvaluation().get(i).floatValue() < solutionY.getEvaluation().get(i).floatValue()){
					countBest++;
				}
				if(solutionX.getEvaluation().get(i).floatValue() == solutionY.getEvaluation().get(i).floatValue()){
					countEquals++;
				}	
			}
		}
		if((countBest >= 1) && (countEquals + countBest == solutionX.getEvaluation().size())) {
			dominance = true;
		}
		return dominance;
	} 
}
