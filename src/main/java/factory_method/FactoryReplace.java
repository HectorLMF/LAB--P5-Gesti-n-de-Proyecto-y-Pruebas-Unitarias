/**
 * @file FactoryReplace.java
 * @brief Implementación concreta de la fábrica de estrategias de reemplazo.
 * 
 * Esta clase crea instancias de estrategias de reemplazo utilizadas en
 * algoritmos evolutivos mediante reflexión.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package factory_method;

import java.lang.reflect.InvocationTargetException;


import evolutionary_algorithms.complement.Replace;
import evolutionary_algorithms.complement.ReplaceType;
import factory_interface.IFFactoryReplace;

/**
 * @class FactoryReplace
 * @brief Fábrica concreta para crear estrategias de reemplazo.
 */
public class FactoryReplace implements IFFactoryReplace {

	/** Instancia de la estrategia de reemplazo creada */
	private Replace replace;
	
	/**
	 * @brief Crea una estrategia de reemplazo mediante reflexión.
	 * 
	 * @param typereplace Tipo de estrategia de reemplazo a crear
	 * @return Instancia de la estrategia de reemplazo
	 * @throws IllegalArgumentException Si el argumento es inválido
	 * @throws SecurityException Si hay una violación de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación del método
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	public Replace createReplace( ReplaceType typereplace ) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		String className = "evolutionary_algorithms.complement." + typereplace.toString();
		replace = (Replace) FactoryLoader.getInstance(className);
		return replace;
	}
}
