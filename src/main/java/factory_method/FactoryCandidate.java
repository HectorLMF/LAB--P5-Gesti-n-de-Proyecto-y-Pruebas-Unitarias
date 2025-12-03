/**
 * @file FactoryCandidate.java
 * @brief Implementación concreta de la fábrica de estrategias de búsqueda de candidatos.
 * 
 * Esta clase implementa el patrón Factory Method para crear instancias
 * de diferentes estrategias de búsqueda de candidatos utilizadas en
 * algoritmos de búsqueda local.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package factory_method;

import java.lang.reflect.InvocationTargetException;

import local_search.candidate_type.CandidateType;
import local_search.candidate_type.SearchCandidate;

import factory_interface.IFFactoryCandidate;

/**
 * @class FactoryCandidate
 * @brief Fábrica concreta para crear estrategias de búsqueda de candidatos.
 */
public class FactoryCandidate implements IFFactoryCandidate{
	/** Instancia de la estrategia de búsqueda creada */
	private SearchCandidate searchcandidate;
	
	/**
	 * @brief Crea una instancia de SearchCandidate mediante reflexión.
	 * 
	 * @param typeCandidate Tipo de estrategia de búsqueda a crear
	 * @return Instancia de la estrategia de búsqueda
	 * @throws IllegalArgumentException Si el argumento es inválido
	 * @throws SecurityException Si hay una violación de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación del método
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	public SearchCandidate createSearchCandidate(CandidateType typeCandidate) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String className = "local_search.candidate_type." + typeCandidate.toString();
		searchcandidate = (SearchCandidate) FactoryLoader.getInstance(className);
		return searchcandidate;
	}
}
