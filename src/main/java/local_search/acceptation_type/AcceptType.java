/**
 * @file AcceptType.java
 * @brief Enumeración de tipos de estrategias de aceptación disponibles
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package local_search.acceptation_type;

/**
 * @enum AcceptType
 * @brief Define los tipos de criterios de aceptación en búsqueda local
 * 
 * Esta enumeración lista todas las estrategias de aceptación disponibles para
 * determinar si un estado candidato debe ser aceptado durante la búsqueda.
 */
public enum AcceptType
{
	/** @brief Acepta solo mejores soluciones */
	ACCEPT_BEST,
	/** @brief Acepta cualquier solución */
	ACCEPT_ANYONE,
	/** @brief Acepta soluciones con criterio de temperatura */
	ACCEPT_NOT_BAD_T,
	/** @brief Acepta soluciones dentro de un umbral */
	ACCEPT_NOT_BAD_U,
	/** @brief Acepta soluciones no dominadas (Pareto) */
	ACCEPT_NOT_DOMINATED,
	/** @brief Acepta soluciones no dominadas con lista tabú */
	ACCEPT_NOT_DOMINATED_TABU,
	/** @brief Acepta soluciones que no empeoran */
	ACCEPT_NOT_BAD,
	/** @brief Acepta soluciones con criterio multiobjetivo */
	ACCEPT_MULTICASE;
	
}
