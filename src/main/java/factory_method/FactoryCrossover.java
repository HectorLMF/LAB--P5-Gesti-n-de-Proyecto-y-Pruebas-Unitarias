/**
 * @file FactoryCrossover.java
 * @brief Implementación concreta de la fábrica de operadores de cruce.
 * 
 * Esta clase crea instancias de operadores de cruce utilizados en
 * algoritmos evolutivos mediante reflexión.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package factory_method;

import java.lang.reflect.InvocationTargetException;


import evolutionary_algorithms.complement.Crossover;
import evolutionary_algorithms.complement.CrossoverType;
import factory_interface.IFFactoryCrossover;

/**
 * @class FactoryCrossover
 * @brief Fábrica concreta para crear operadores de cruce.
 */
public class FactoryCrossover implements IFFactoryCrossover {
	/** Instancia del operador de cruce creado */
	private Crossover crossing;

	/**
	 * @brief Crea un operador de cruce mediante reflexión.
	 * 
	 * @param Crossovertype Tipo de operador de cruce a crear
	 * @return Instancia del operador de cruce
	 * @throws IllegalArgumentException Si el argumento es inválido
	 * @throws SecurityException Si hay una violación de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación del método
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	public Crossover createCrossover(CrossoverType Crossovertype) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		String className = "evolutionary_algorithms.complement." + Crossovertype.toString();
		crossing = (Crossover) FactoryLoader.getInstance(className);
		return crossing;
	}
}
