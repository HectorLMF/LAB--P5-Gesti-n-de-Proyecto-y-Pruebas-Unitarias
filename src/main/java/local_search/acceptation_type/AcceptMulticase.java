/**
 * @file AcceptMulticase.java
 * @brief Implementación de estrategia de aceptación multiobjetivo con Simulated Annealing
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package local_search.acceptation_type;

import metaheuristics.generators.*;
import metaheurictics.strategy.*;

import java.util.List;
import java.util.Random;

import problem.definition.State;

/**
 * @class AcceptMulticase
 * @brief Clase que implementa aceptación multiobjetivo basada en dominancia y temperatura
 * 
 * Esta clase utiliza conceptos de dominancia de Pareto y Simulated Annealing para
 * decidir la aceptación de soluciones candidatas en problemas multiobjetivo. Considera
 * el rango de dominancia y aplica criterios probabilísticos basados en temperatura.
 */
public class AcceptMulticase extends AcceptableCandidate {

	/** @brief Generador de números aleatorios para decisiones probabilísticas */
	private static final Random RNG = new Random();

	/**
	 * @brief Acepta candidatos basándose en dominancia de Pareto y criterios de temperatura
	 * @param stateCurrent Estado actual de la búsqueda
	 * @param stateCandidate Estado candidato a evaluar
	 * @return Boolean true si el candidato es aceptado, false en caso contrario
	 */
	@Override
	public Boolean acceptCandidate(State stateCurrent, State stateCandidate) {
		// TODO Auto-generated method stub
		Boolean accept = false;
		List<State> list = Strategy.getStrategy().listRefPoblacFinal;
		
		if(list.size() == 0){
			list.add(stateCurrent.getCopy());
		}
		Double T = MultiCaseSimulatedAnnealing.tinitial;
		double pAccept = 0;
		// Reuse a single Random instance for reliability
		Dominance dominance= new Dominance();
		//Verificando si la soluci�n candidata domina a la soluci�n actual
		//Si la soluci�n candidata domina a la soluci�n actual
		if(dominance.dominance(stateCandidate, stateCurrent) == true){
			//Se asigna como soluci�n actual la soluci�n candidata con probabilidad 1
			pAccept = 1; 
		}
		else if(dominance.dominance(stateCandidate, stateCurrent)== false){	
			if(DominanceCounter(stateCandidate, list) > 0){
				pAccept = 1;
			}
			else if(DominanceRank(stateCandidate, list) == 0){
				pAccept = 1;
			}
			else if(DominanceRank(stateCandidate, list) < DominanceRank(stateCurrent, list)){
				pAccept = 1;
			}
			else if(DominanceRank(stateCandidate, list) == DominanceRank(stateCurrent, list)){
				//Calculando la probabilidad de aceptaci�n
				List<Double> evaluations = stateCurrent.getEvaluation();
				double total = 0;
				for (int i = 0; i < evaluations.size()-1; i++) {
					Double evalA = evaluations.get(i);
					Double evalB = stateCandidate.getEvaluation().get(i);
					if (evalA != 0 && evalB != 0) {
						total += (evalA - evalB)/((evalA + evalB)/2);
					}
				}	
				pAccept = Math.exp(-(1-total)/T);
			}
			else if (DominanceRank(stateCandidate, list) > DominanceRank(stateCurrent, list) && DominanceRank(stateCurrent, list)!= 0){
				float value = (float) DominanceRank(stateCandidate, list) / (float) DominanceRank(stateCurrent, list);
				pAccept = Math.exp(-(value+1)/T);
			}
			else{
				//Calculando la probabilidad de aceptaci�n
				List<Double> evaluations = stateCurrent.getEvaluation();
				double total = 0;
				for (int i = 0; i < evaluations.size()-1; i++) {
					Double evalA = evaluations.get(i);
					Double evalB = stateCandidate.getEvaluation().get(i);
					if (evalA != 0 && evalB != 0) {
						total += (evalA - evalB)/((evalA + evalB)/2);
					}
				}
				pAccept = Math.exp(-(1-total)/T);
			}
		}
		//Generar un n�mero aleatorio
		if((RNG.nextFloat()) < pAccept){
			stateCurrent = stateCandidate.getCopy();
			//Verificando que la soluci�n candidata domina a alguna de las soluciones
			accept = dominance.ListDominance(stateCandidate, list);
		}
		return accept;
	}


	/**
	 * @brief Cuenta el número de soluciones dominadas por el candidato
	 * @param stateCandidate Estado candidato que se evalúa
	 * @param list Lista de soluciones del frente de Pareto
	 * @return int Número de soluciones dominadas por el candidato
	 */
	private int DominanceCounter(State stateCandidate, List<State> list) { //chequea el número de soluciones de Pareto que son dominados por la nueva solución
		int counter = 0;
		for (int i = 0; i < list.size(); i++) {
			State solution = list.get(i);
			Dominance dominance = new Dominance();
			if(dominance.dominance(stateCandidate, solution) == true)
				counter++;
		}
		return counter;
	}

	/**
	 * @brief Calcula el rango de dominancia del candidato
	 * @param stateCandidate Estado candidato que se evalúa
	 * @param list Lista de soluciones del frente de Pareto
	 * @return int Número de soluciones que dominan al candidato
	 */
	private int DominanceRank(State stateCandidate, List<State> list) { //calculando el número de soluciones en el conjunto de Pareto que dominan a la solución
		int rank = 0;
		for (int i = 0; i < list.size(); i++) {
			State solution = list.get(i);
			Dominance dominance = new Dominance();
			if(dominance.dominance(solution, stateCandidate) == true){
				rank++;
			}
		}
		
		return rank;
	}

}
