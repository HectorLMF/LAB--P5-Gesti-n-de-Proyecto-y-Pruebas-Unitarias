/**
 * @file FactoryLoader.java
 * @brief Utilidad para carga dinámica de clases mediante reflexión.
 * 
 * Esta clase proporciona un mecanismo centralizado para instanciar objetos
 * dinámicamente a partir de nombres de clase. Se utiliza como soporte
 * para todas las fábricas del sistema.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package factory_method;
import java.lang.reflect.InvocationTargetException;

/**
 * @class FactoryLoader
 * @brief Clase utilitaria para carga dinámica de clases.
 */
public class FactoryLoader {

	/**
	 * @brief Crea una instancia de una clase dado su nombre completo.
	 * 
	 * Utiliza reflexión para buscar y crear una instancia de la clase especificada.
	 * Si la clase no existe o no puede ser instanciada, retorna null.
	 * 
	 * @param className Nombre completo de la clase (incluyendo paquete)
	 * @return Nueva instancia de la clase, o null si no puede crearse
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws IllegalArgumentException Si el argumento es inválido
	 * @throws SecurityException Si hay una violación de seguridad
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación del constructor
	 * @throws NoSuchMethodException Si no se encuentra el constructor
	 */
	public static Object getInstance(String className) throws ClassNotFoundException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		@SuppressWarnings("rawtypes")
		Class c = null;
		try {
			c = Class.forName(className);
		} catch (ClassNotFoundException e) {
			// Clase no encontrada: comportamiento esperado es retornar null
			return null;
		}

		try {
			return c.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			// No se puede instanciar (abstracta, sin ctor por defecto, etc.)
			return null;
		}
	}
}
