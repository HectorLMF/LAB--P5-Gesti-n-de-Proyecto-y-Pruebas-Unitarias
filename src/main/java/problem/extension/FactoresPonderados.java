/**
 * @file FactoresPonderados.java
 * @brief Implementación del método de solución de factores ponderados para problemas multiobjetivo.
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package problem.extension;

import java.util.ArrayList;

import metaheurictics.strategy.Strategy;

import problem.definition.State;
import problem.definition.Problem.ProblemType;

/**
 * @class FactoresPonderados
 * @brief Clase que implementa el método de factores ponderados para evaluar estados en problemas multiobjetivo.
 * 
 * Esta clase combina múltiples funciones objetivo mediante pesos asignados a cada una,
 * produciendo una única evaluación agregada del estado.
 */
public class FactoresPonderados extends SolutionMethod {

	/**
	 * @brief Evalúa un estado utilizando el método de factores ponderados.
	 * 
	 * Calcula la evaluación agregada del estado combinando las evaluaciones de cada función objetivo
	 * ponderadas por sus respectivos pesos. Maneja la normalización según el tipo de problema.
	 * 
	 * @param state Estado a evaluar
	 */
	@Override
	public void evaluationState(State state) {
		// TODO Auto-generated method stub
		double eval = 0;       
		double tempWeight = 0;	
		ArrayList<Double> evaluation = new ArrayList<Double>(Strategy.getStrategy().getProblem().getFunction().size());
		
		for (int i = 0; i < Strategy.getStrategy().getProblem().getFunction().size(); i++) {

			tempWeight = 0;
			if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.MAXIMIZAR)){
				if(Strategy.getStrategy().getProblem().getFunction().get(i).getTypeProblem().equals(ProblemType.MAXIMIZAR)){
					tempWeight = Strategy.getStrategy().getProblem().getFunction().get(i).Evaluation(state);
					tempWeight = tempWeight * Strategy.getStrategy().getProblem().getFunction().get(i).getWeight();
				}
				else{
					tempWeight = 1 - Strategy.getStrategy().getProblem().getFunction().get(i).Evaluation(state);
					tempWeight = tempWeight * Strategy.getStrategy().getProblem().getFunction().get(i).getWeight();
				}
			}
			else{
				if(Strategy.getStrategy().getProblem().getFunction().get(i).getTypeProblem().equals(ProblemType.MAXIMIZAR)){
					tempWeight = 1 - Strategy.getStrategy().getProblem().getFunction().get(i).Evaluation(state);
					tempWeight = tempWeight * Strategy.getStrategy().getProblem().getFunction().get(i).getWeight();
				}
				else{
					tempWeight = Strategy.getStrategy().getProblem().getFunction().get(i).Evaluation(state);
					tempWeight = tempWeight * Strategy.getStrategy().getProblem().getFunction().get(i).getWeight();
				}
			}
			eval += tempWeight;
		}
		evaluation.add(evaluation.size(), eval);
		state.setEvaluation(evaluation);
		
	}

}
