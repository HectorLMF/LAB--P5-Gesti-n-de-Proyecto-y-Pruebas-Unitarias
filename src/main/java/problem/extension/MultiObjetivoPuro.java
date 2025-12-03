/**
 * @file MultiObjetivoPuro.java
 * @brief Implementación del método de optimización multiobjetivo puro.
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package problem.extension;

import java.util.ArrayList;

import metaheurictics.strategy.Strategy;

import problem.definition.ObjetiveFunction;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

/**
 * @class MultiObjetivoPuro
 * @brief Clase que implementa el método de optimización multiobjetivo puro.
 * 
 * Evalúa un estado manteniendo múltiples objetivos separados sin agregarlos,
 * permitiendo la comparación de dominancia de Pareto.
 */
public class MultiObjetivoPuro extends SolutionMethod {

	/**
	 * @brief Evalúa un estado manteniendo los objetivos separados.
	 * 
	 * Calcula la evaluación de cada función objetivo individualmente, normalizando según
	 * el tipo de problema (maximizar/minimizar) sin agregar los resultados.
	 * 
	 * @param state Estado a evaluar
	 */
	@Override
	public void evaluationState(State state) {
		// TODO Auto-generated method stub
		double tempEval = -1;
		ArrayList<Double> evaluation = new ArrayList<Double>(Strategy.getStrategy().getProblem().getFunction().size());
		for (int i = 0; i < Strategy.getStrategy().getProblem().getFunction().size(); i++)
		{
			ObjetiveFunction objfunction = (ObjetiveFunction)Strategy.getStrategy().getProblem().getFunction().get(i);
			if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.MAXIMIZAR)){
				if(objfunction.getTypeProblem().equals(ProblemType.MAXIMIZAR))
				{
					tempEval = objfunction.Evaluation(state);
				}
				else{
					tempEval = 1-objfunction.Evaluation(state);
				}
			}
			else{
				if(objfunction.getTypeProblem().equals(ProblemType.MAXIMIZAR))
				{
					tempEval = 1-objfunction.Evaluation(state);
				}
				else{
					tempEval = objfunction.Evaluation(state);
				}
			}
			evaluation.add(tempEval);
		}
		//evaluation.add( (double) -1);
		state.setEvaluation(evaluation);
	}

}
