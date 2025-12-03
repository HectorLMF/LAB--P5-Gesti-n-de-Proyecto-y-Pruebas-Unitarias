/**
 * @file FactoryAcceptCandidate.java
 * @brief Implementación concreta de la fábrica de criterios de aceptación.
 * 
 * Esta clase implementa el patrón Factory Method para crear instancias
 * de diferentes criterios de aceptación de candidatos en búsqueda local.
 * Utiliza reflexión para instanciar dinámicamente las clases según el tipo.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package factory_method;

import java.lang.reflect.InvocationTargetException;

import local_search.acceptation_type.AcceptType;
import local_search.acceptation_type.AcceptableCandidate;


import factory_interface.IFFactoryAcceptCandidate;

/**
 * @class FactoryAcceptCandidate
 * @brief Fábrica concreta para crear criterios de aceptación de candidatos.
 */
public class FactoryAcceptCandidate implements IFFactoryAcceptCandidate{
	/** Instancia del criterio de aceptación creado */
	private AcceptableCandidate acceptCandidate;
	
	/**
	 * @brief Crea una instancia de AcceptableCandidate mediante reflexión.
	 * 
	 * Utiliza el nombre del tipo para construir el nombre de clase completo
	 * y crear una instancia dinámicamente usando FactoryLoader.
	 * 
	 * @param typeacceptation Tipo de criterio de aceptación a crear
	 * @return Instancia del criterio de aceptación
	 * @throws IllegalArgumentException Si el argumento es inválido
	 * @throws SecurityException Si hay una violación de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación del método
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	public AcceptableCandidate createAcceptCandidate( AcceptType typeacceptation ) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		String className = "local_search.acceptation_type." + typeacceptation.toString();
		acceptCandidate = (AcceptableCandidate) FactoryLoader.getInstance(className);
		return acceptCandidate;
	}
}
