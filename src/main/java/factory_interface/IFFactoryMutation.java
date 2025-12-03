/**
 * @file IFFactoryMutation.java
 * @brief Interfaz Factory para la creación de operadores de mutación.
 * 
 * Define el contrato para las fábricas que crean operadores de mutación
 * utilizados en algoritmos evolutivos para introducir variación en las soluciones.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package factory_interface;

import java.lang.reflect.InvocationTargetException;

import evolutionary_algorithms.complement.Mutation;
import evolutionary_algorithms.complement.MutationType;

/**
 * @interface IFFactoryMutation
 * @brief Interfaz para la fábrica de operadores de mutación.
 */
public interface IFFactoryMutation {
	/**
	 * @brief Crea un operador de mutación según el tipo especificado.
	 * 
	 * @param typeMutation Tipo de operador de mutación a crear
	 * @return Una instancia del operador de mutación solicitado
	 * @throws IllegalArgumentException Si el argumento es inválido
	 * @throws SecurityException Si hay una violación de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación del método
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	Mutation createMutation(MutationType typeMutation)throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException ;
}
