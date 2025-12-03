/**
 * @file IFFactoryAcceptCandidate.java
 * @brief Interfaz Factory para la creación de criterios de aceptación de candidatos.
 * 
 * Define el contrato para las fábricas que crean objetos AcceptableCandidate
 * según el tipo de criterio de aceptación especificado. Implementa el patrón
 * Factory Method para la creación de estrategias de aceptación en búsqueda local.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package factory_interface;

import java.lang.reflect.InvocationTargetException;

import local_search.acceptation_type.AcceptType;
import local_search.acceptation_type.AcceptableCandidate;

/**
 * @interface IFFactoryAcceptCandidate
 * @brief Interfaz para la fábrica de criterios de aceptación de candidatos.
 */
public interface IFFactoryAcceptCandidate
{
	/**
	 * @brief Crea una instancia de AcceptableCandidate según el tipo especificado.
	 * 
	 * @param typeacceptation Tipo de criterio de aceptación a crear
	 * @return Una instancia del criterio de aceptación solicitado
	 * @throws IllegalArgumentException Si el argumento es inválido
	 * @throws SecurityException Si hay una violación de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación del método
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	AcceptableCandidate createAcceptCandidate(AcceptType typeacceptation) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;

}
