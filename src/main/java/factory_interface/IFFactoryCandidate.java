/**
 * @file IFFactoryCandidate.java
 * @brief Interfaz Factory para la creación de estrategias de búsqueda de candidatos.
 * 
 * Define el contrato para las fábricas que crean objetos SearchCandidate
 * según el tipo de estrategia de búsqueda especificado. Parte del patrón
 * Factory Method para la generación de candidatos en búsqueda local.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package factory_interface;

import java.lang.reflect.InvocationTargetException;

import local_search.candidate_type.CandidateType;
import local_search.candidate_type.SearchCandidate;

/**
 * @interface IFFactoryCandidate
 * @brief Interfaz para la fábrica de estrategias de búsqueda de candidatos.
 */
public interface IFFactoryCandidate
{
	/**
	 * @brief Crea una instancia de SearchCandidate según el tipo especificado.
	 * 
	 * @param typeCandidate Tipo de estrategia de búsqueda de candidatos
	 * @return Una instancia de la estrategia de búsqueda solicitada
	 * @throws IllegalArgumentException Si el argumento es inválido
	 * @throws SecurityException Si hay una violación de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación del método
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	SearchCandidate createSearchCandidate(CandidateType typeCandidate) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;
}
