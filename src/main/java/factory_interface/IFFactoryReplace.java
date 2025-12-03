/**
 * @file IFFactoryReplace.java
 * @brief Interfaz Factory para la creación de estrategias de reemplazo.
 * 
 * Define el contrato para las fábricas que crean estrategias de reemplazo
 * utilizadas en algoritmos evolutivos para actualizar la población.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package factory_interface;

import java.lang.reflect.InvocationTargetException;

import evolutionary_algorithms.complement.Replace;
import evolutionary_algorithms.complement.ReplaceType;

/**
 * @interface IFFactoryReplace
 * @brief Interfaz para la fábrica de estrategias de reemplazo.
 */
public interface IFFactoryReplace {
	/**
	 * @brief Crea una estrategia de reemplazo según el tipo especificado.
	 * 
	 * @param typereplace Tipo de estrategia de reemplazo a crear
	 * @return Una instancia de la estrategia de reemplazo solicitada
	 * @throws IllegalArgumentException Si el argumento es inválido
	 * @throws SecurityException Si hay una violación de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación del método
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	Replace createReplace(ReplaceType typereplace)throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException ;
}
