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
	AcceptBest,
	/** @brief Acepta cualquier solución */
	AcceptAnyone,
	/** @brief Acepta soluciones con criterio de temperatura */
	AcceptNotBadT,
	/** @brief Acepta soluciones dentro de un umbral */
	AcceptNotBadU,
	/** @brief Acepta soluciones no dominadas (Pareto) */
	AcceptNotDominated,
	/** @brief Acepta soluciones no dominadas con lista tabú */
	AcceptNotDominatedTabu,
	/** @brief Acepta soluciones que no empeoran */
	AcceptNotBad,
	/** @brief Acepta soluciones con criterio multiobjetivo */
	AcceptMulticase;
	
}
