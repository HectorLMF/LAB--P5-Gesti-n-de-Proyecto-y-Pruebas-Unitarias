/**
 * @file FactoryFatherSelection.java
 * @brief Implementación concreta de la fábrica de estrategias de selección de padres.
 * 
 * Esta clase crea instancias de estrategias de selección de padres
 * utilizadas en algoritmos evolutivos mediante reflexión.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package factory_method;

import java.lang.reflect.InvocationTargetException;


import evolutionary_algorithms.complement.FatherSelection;
import evolutionary_algorithms.complement.SelectionType;
import factory_interface.IFFactoryFatherSelection;

/**
 * @class FactoryFatherSelection
 * @brief Fábrica concreta para crear estrategias de selección de padres.
 */
public class FactoryFatherSelection implements IFFactoryFatherSelection{
	/** Instancia de la estrategia de selección creada */
	private FatherSelection selection;
	
	/**
	 * @brief Crea una estrategia de selección de padres mediante reflexión.
	 * 
	 * @param selectionType Tipo de estrategia de selección a crear
	 * @return Instancia de la estrategia de selección
	 * @throws IllegalArgumentException Si el argumento es inválido
	 * @throws SecurityException Si hay una violación de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación del método
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	public FatherSelection createSelectFather(SelectionType selectionType) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (selectionType == null) {
			throw new IllegalArgumentException("selectionType cannot be null");
		}
		String className = "evolutionary_algorithms.complement." + enumToClassName(selectionType.toString());
		selection = (FatherSelection) FactoryLoader.getInstance(className);
		return selection;
	}
	
	/**
	 * @brief Convierte un nombre de enum en UPPER_CASE a nombre de clase CamelCase
	 * @param enumName Nombre del enum (ej: "TRUNCATION_SELECTION")
	 * @return Nombre de clase (ej: "TruncationSelection")
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
