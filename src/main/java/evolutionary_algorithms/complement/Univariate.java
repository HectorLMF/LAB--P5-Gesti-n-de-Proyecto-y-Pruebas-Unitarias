/**
 * @file Univariate.java
 * @brief Implementación de distribución univariada
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package evolutionary_algorithms.complement;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import problem.definition.State;



/**
 * @class Univariate
 * @brief Clase que implementa la distribución univariada
 * 
 * Calcula la distribución de probabilidad para cada variable independientemente.
 */
public class Univariate extends Distribution {

	/**
	 * @brief Calcula la distribución univariada de probabilidad
	 * @param fathers Lista de estados padres
	 * @return Lista de probabilidades calculadas para cada variable
	 */
	@Override
	public List<Probability> distribution(List<State> fathers) {

		List<Probability> ListProbability = new ArrayList<Probability>();
		int cantV = fathers.get(0).getCode().size();
		for (int i = 0; i < cantV; i++) {
			int[] values = new int[fathers.size()];
			//llenar los valores de cada variable
			for (int j = 0; j < fathers.size(); j++){
				values[j] = (Integer) fathers.get(j).getCode().get(i);
			}
			//para cada valor contar la cantidad de veces que se repite
			int k = 0;
			while (k < values.length) {
				float count = 0;
				if(values[k] != -1){
					int l = k;
					int temp = values[k];
					while (l < values.length) {
						if(temp == values[l]){
							count++;
							values[l] = -1;
						}
						l++;	
					}
					Probability probability = new Probability();
					float prob = count / values.length;
					probability.setKey(i);
					probability.setValue(temp);
					probability.setProbability(prob);
					ListProbability.add(probability);
				}
				k++;
			}
		}
		return ListProbability;
	}

	/**
	 * @brief Extrae las claves de un mapa ordenado
	 * @param map Mapa ordenado de String a Object
	 * @return Lista de claves como strings
	 */
	public List<String> getListKey(SortedMap<String, Object> map){
		List<String> listKey = new ArrayList<String>();
		String key = map.keySet().toString();
		String returnString = key.substring(1, key.length() - 1);
		returnString = returnString + ", ";
		int countKey = map.size();
		for (int j = 0; j < countKey; j++) {
			String r = returnString.substring(0, returnString.indexOf(','));
			returnString = returnString.substring(returnString.indexOf(',')+2);
			listKey.add(r);
	   }
	   return listKey;
	}
}
