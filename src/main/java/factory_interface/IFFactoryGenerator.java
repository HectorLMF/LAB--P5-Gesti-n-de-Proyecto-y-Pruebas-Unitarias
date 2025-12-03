/**
 * @file IFFactoryGenerator.java
 * @brief Interfaz Factory para la creación de generadores metaheurísticos.
 * 
 * Define el contrato para las fábricas que crean objetos Generator
 * que implementan diferentes algoritmos metaheurísticos.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package factory_interface;

import java.lang.reflect.InvocationTargetException;

import metaheuristics.generators.Generator;
import metaheuristics.generators.GeneratorType;

/**
 * @interface IFFactoryGenerator
 * @brief Interfaz para la fábrica de generadores metaheurísticos.
 */
public interface IFFactoryGenerator {
	
	/**
	 * @brief Crea un generador metaheurístico según el tipo especificado.
	 * 
	 * @param Generatortype Tipo de generador a crear
	 * @return Una instancia del generador solicitado
	 * @throws IllegalArgumentException Si el argumento es inválido
	 * @throws SecurityException Si hay una violación de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación del método
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	Generator createGenerator(GeneratorType Generatortype)throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException ;
}
