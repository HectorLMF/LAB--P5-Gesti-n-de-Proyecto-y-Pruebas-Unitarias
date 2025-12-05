/**
 * @file FactoryGenerator.java
 * @brief Implementación concreta de la fábrica de generadores metaheurísticos.
 * 
 * Esta clase crea instancias de diferentes algoritmos metaheurísticos
 * mediante reflexión.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package factory_method;


import java.lang.reflect.InvocationTargetException;

import factory_interface.IFFactoryGenerator;

import metaheuristics.generators.Generator;
import metaheuristics.generators.GeneratorType;

/**
 * @class FactoryGenerator
 * @brief Fábrica concreta para crear generadores metaheurísticos.
 */
public class FactoryGenerator implements IFFactoryGenerator {

	/** Instancia del generador creado */
	private Generator generator;
	
	/**
	 * @brief Crea un generador metaheurístico mediante reflexión.
	 * 
	 * @param generatorType Tipo de generador a crear
	 * @return Instancia del generador
	 * @throws IllegalArgumentException Si el argumento es inválido
	 * @throws SecurityException Si hay una violación de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación del método
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	public Generator createGenerator(GeneratorType generatorType) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		// Try direct enum name first (legacy behavior)
		String className = "metaheuristics.generators." + generatorType.toString();
		Object inst = FactoryLoader.getInstance(className);
		if (inst != null && inst instanceof Generator) {
			generator = (Generator) inst;
			return generator;
		}

		// Fallback: convert enum name like "GENETIC_ALGORITHM" to CamelCase class name "GeneticAlgorithm"
		String enumName = generatorType.toString();
		StringBuilder camel = new StringBuilder();
		for (String part : enumName.toLowerCase().split("_")) {
			if (part.length() == 0) continue;
			camel.append(Character.toUpperCase(part.charAt(0)));
			if (part.length() > 1) camel.append(part.substring(1));
		}
		String altClassName = "metaheuristics.generators." + camel.toString();
		inst = FactoryLoader.getInstance(altClassName);
		if (inst != null && inst instanceof Generator) {
			generator = (Generator) inst;
			return generator;
		}

		// If neither works, return null (same behavior as before when class cannot be loaded)
		return null;
	}
}
