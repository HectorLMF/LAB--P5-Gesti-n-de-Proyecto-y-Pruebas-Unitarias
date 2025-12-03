/**
 * @file IFFactoryCrossover.java
 * @brief Interfaz Factory para la creación de operadores de cruce.
 * 
 * Define el contrato para las fábricas que crean operadores de cruce (crossover)
 * utilizados en algoritmos evolutivos. Implementa el patrón Factory Method.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package factory_interface;

import java.lang.reflect.InvocationTargetException;

import evolutionary_algorithms.complement.Crossover;
import evolutionary_algorithms.complement.CrossoverType;

/**
 * @interface IFFactoryCrossover
 * @brief Interfaz para la fábrica de operadores de cruce en algoritmos evolutivos.
 */
public interface IFFactoryCrossover {
	/**
	 * @brief Crea un operador de cruce según el tipo especificado.
	 * 
	 * @param Crossovertype Tipo de operador de cruce a crear
	 * @return Una instancia del operador de cruce solicitado
	 * @throws IllegalArgumentException Si el argumento es inválido
	 * @throws SecurityException Si hay una violación de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación del método
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	Crossover createCrossover(CrossoverType Crossovertype)throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException ;
}
