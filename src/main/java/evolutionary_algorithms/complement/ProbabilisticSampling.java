/**
 * @file ProbabilisticSampling.java
 * @brief Implementación de muestreo probabilístico
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package evolutionary_algorithms.complement;

import java.util.ArrayList;
import java.util.List;

import config.SecureRandomGenerator;
import metaheurictics.strategy.Strategy;
import metaheuristics.generators.GeneratorType;
import problem.definition.State;

/**
 * @class ProbabilisticSampling
 * @brief Clase que implementa el operador de muestreo probabilístico
 * 
 * Genera nuevos individuos basándose en la distribución de probabilidad de los padres.
 */
public class ProbabilisticSampling extends Sampling {

	/**
	 * @brief Realiza el muestreo probabilístico de los padres
	 * @param fathers Lista de estados padres
	 * @param countInd Número de individuos a generar
	 * @return Lista de estados generados
	 */
	@Override
	public List<State> sampling(List<State> fathers, int countInd) {
		int cantV = fathers.get(0).getCode().size();
		List<State> staList = listState(countInd);
		for (int i = 0; i < cantV; i++) {
			Object[] values = new Object[fathers.size()]; // arreglo de valores de una variable
			Object[] arrtemp = new Object[Strategy.getStrategy().getProblem().getPossibleValue()];
			//llenar el arreglo con todos los valores posibles
			for (int j = 0; j < arrtemp.length; j++) {
				arrtemp[j] = j;
			}
			for (int j = 0; j < values.length; j++) {
				values[j] = fathers.get(j).getCode().get(i);
			}
			int k = 0;
			int sum = 0; // suma acumulativa por cada valor posible
			int arrOcc[] = new int[arrtemp.length]; // arreglo paralelo para contar la cantidad de ocurrencias de un valor posible
			//llenar el arreglo con la cantidad de ocurrencias de cada valor posible, para cada variable
			//recorrer el arreglo de valores de una variable
			while (k < arrtemp.length) {
				int count = 0;
				for (int j = 0; j < values.length; j++) {
					if ((Integer) values[j] != -1 && values[j] == arrtemp[k]) { //
						count++;
						values[j] = -1;
					}
				}
				arrOcc[k] = count;
				sum = sum + count;
				k++;
			}
			for (int l = 0; l < countInd; l++) {
				boolean find = false;
				int p = 0;
				int random = SecureRandomGenerator.nextInt(Math.max(1, sum)) + 1;
				while (p < arrOcc.length && !find) {
					random = random - arrOcc[p];
					if (random <= 0) {
						staList.get(l).getCode().add(arrtemp[p]);
						find = true;
					} else {
						p++;
					}
				}
				if (!find) {
					int bound = Math.max(1, Strategy.getStrategy().getProblem().getCodification().getVariableCount() * 10);
					int value = SecureRandomGenerator.nextInt(bound);
					staList.get(l).getCode().add(value);
				}

			}
		}
		return staList;
	}

	/**
	 * @brief Inicializa la lista de individuos
	 * @param countInd Número de individuos a crear
	 * @return Lista de estados inicializados
	 */
	public List<State> listState(int countInd) {
		List<State> staList = new ArrayList<State>(countInd);
		for (int i = 0; i < countInd; i++) {
			State state = new State();
			state.setCode(new ArrayList<Object>());
			state.setNumber(Strategy.getStrategy().getCountCurrent());
			state.setTypeGenerator(GeneratorType.DISTRIBUTION_ESTIMATION_ALGORITHM);
			staList.add(state);
		}
		return staList;
	}
}
