/**
 * @file FactoryMutation.java
 * @brief Implementación concreta de la fábrica de operadores de mutación.
 * 
 * Esta clase crea instancias de operadores de mutación utilizados en
 * algoritmos evolutivos mediante reflexión.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package factory_method;

import java.lang.reflect.InvocationTargetException;


import evolutionary_algorithms.complement.Mutation;
import evolutionary_algorithms.complement.MutationType;
import factory_interface.IFFactoryMutation;

/**
 * @class FactoryMutation
 * @brief Fábrica concreta para crear operadores de mutación.
 */
public class FactoryMutation implements IFFactoryMutation {
	/** Instancia del operador de mutación creado */
	private Mutation mutation;

	/**
	 * @brief Crea un operador de mutación mediante reflexión.
	 * 
	 * @param typeMutation Tipo de operador de mutación a crear
	 * @return Instancia del operador de mutación
	 * @throws IllegalArgumentException Si el argumento es inválido
	 * @throws SecurityException Si hay una violación de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación del método
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	public Mutation createMutation(MutationType typeMutation) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (typeMutation == null) {
			throw new IllegalArgumentException("typeMutation cannot be null");
		}
		String className = "evolutionary_algorithms.complement." + enumToClassName(typeMutation.toString());
		mutation = (Mutation) FactoryLoader.getInstance(className);
		return mutation;
	}
	
	/**
	 * @brief Convierte un nombre de enum en UPPER_CASE a nombre de clase CamelCase
	 * @param enumName Nombre del enum (ej: "ONE_POINT_MUTATION")
	 * @return Nombre de clase (ej: "OnePointMutation")
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
