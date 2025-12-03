/**
 * @file UniformCrossover.java
 * @brief Implementación del operador de cruce uniforme
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package evolutionary_algorithms.complement;

import metaheurictics.strategy.Strategy;

import problem.definition.State;

/**
 * @class UniformCrossover
 * @brief Clase que implementa el operador de cruce uniforme
 * 
 * Usa una máscara aleatoria para seleccionar genes de cada padre.
 */
public class UniformCrossover extends Crossover {
	
	/**
	 * @brief Genera una máscara aleatoria de bits
	 * @param length Longitud de la máscara
	 * @return Array de enteros con valores 0 o 1
	 */
	public int[] mascara(int length){
		int[] mascara = new int[length];
		for (int i = 0; i < mascara.length; i++) {
			int value = (int)(Math.random() * (int)(2));
			mascara[0] = value;
		}
		return mascara;
	}	
    
	/**
	 * @brief Realiza el cruce uniforme entre dos estados padres
	 * @param father1 Primer estado padre
	 * @param father2 Segundo estado padre
	 * @param PC Probabilidad de cruce
	 * @return Estado hijo resultado del cruce
	 */
	@Override
	public State crossover(State father1, State father2, double PC) {
		Object value = new Object();
		State state = (State) father1.getCopy();
		int[] mascara = mascara(father1.getCode().size());
   		for (int k = 0; k < mascara.length; k++) {
   			if(mascara[k] == 1){
   				value = father1.getCode().get(k);
   				state.getCode().set(k, value);
   			}
   			else{
   				if(mascara[k] == 0){
   					value = father2.getCode().get(k);  
   					state.getCode().set(k, value);
   				}
   			}
		}
		return state;
	}
}
