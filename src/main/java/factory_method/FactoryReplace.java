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
		if (typereplace == null) {
			throw new IllegalArgumentException("typereplace cannot be null");
		}
		String className = "evolutionary_algorithms.complement." + enumToClassName(typereplace.toString());
		replace = (Replace) FactoryLoader.getInstance(className);
		return replace;
	}
	
	/**
	 * @brief Convierte un nombre de enum en UPPER_CASE a nombre de clase CamelCase
	 * @param enumName Nombre del enum (ej: "GENERATIONAL_REPLACE")
	 * @return Nombre de clase (ej: "GenerationalReplace")
	 */
	private String enumToClassName(String enumName) {
		String[] parts = enumName.toLowerCase().split("_");
		StringBuilder sb = new StringBuilder();
		for (String part : parts) {
			if (!part.isEmpty()) {
				sb.append(Character.toUpperCase(part.charAt(0)));
				if (part.length() > 1) {
					sb.append(part.substring(1));
				}
			}
		}
		return sb.toString();
	}
}
