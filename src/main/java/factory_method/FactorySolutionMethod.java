/**
 * @file FactorySolutionMethod.java
 * @brief Implementación concreta de la fábrica de métodos de solución.
 * 
 * Esta clase crea instancias de métodos de solución utilizados para
 * evaluar problemas multi-objetivo mediante reflexión.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package factory_method;

import java.lang.reflect.InvocationTargetException;

import problem.extension.SolutionMethod;
import problem.extension.TypeSolutionMethod;
import factory_interface.IFFactorySolutionMethod;

/**
 * @class FactorySolutionMethod
 * @brief Fábrica concreta para crear métodos de solución.
 */
public class FactorySolutionMethod implements IFFactorySolutionMethod {

	/** Instancia del método de solución creado */
	private SolutionMethod solutionMethod;
	
	/**
	 * @brief Crea un método de solución mediante reflexión.
	 * 
	 * @param method Tipo de método de solución a crear
	 * @return Instancia del método de solución
	 * @throws IllegalArgumentException Si el argumento es inválido
	 * @throws SecurityException Si hay una violación de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación del método
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	@Override
	public SolutionMethod createdSolutionMethod(TypeSolutionMethod method) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String className = "problem.extension." + method.toClassName();
		solutionMethod = (SolutionMethod) FactoryLoader.getInstance(className);
		return solutionMethod;
	}

}
