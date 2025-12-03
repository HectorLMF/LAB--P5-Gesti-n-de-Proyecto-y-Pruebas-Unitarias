/**
 * @file IFFactorySolutionMethod.java
 * @brief Interfaz Factory para la creación de métodos de solución.
 * 
 * Define el contrato para las fábricas que crean métodos de solución
 * utilizados para evaluar problemas multi-objetivo.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package factory_interface;

import java.lang.reflect.InvocationTargetException;

import problem.extension.SolutionMethod;
import problem.extension.TypeSolutionMethod;

/**
 * @interface IFFactorySolutionMethod
 * @brief Interfaz para la fábrica de métodos de solución.
 */
public interface IFFactorySolutionMethod {
	
	/**
	 * @brief Crea un método de solución según el tipo especificado.
	 * 
	 * @param method Tipo de método de solución a crear
	 * @return Una instancia del método de solución solicitado
	 * @throws IllegalArgumentException Si el argumento es inválido
	 * @throws SecurityException Si hay una violación de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación del método
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	SolutionMethod createdSolutionMethod(TypeSolutionMethod  method) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException ;

}
