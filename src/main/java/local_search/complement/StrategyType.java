/**
 * @file StrategyType.java
 * @brief Enumeración de tipos de estrategias de búsqueda local
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package local_search.complement;

/**
 * @enum StrategyType
 * @brief Define los tipos de estrategias disponibles para búsqueda local
 * 
 * Esta enumeración lista las estrategias de búsqueda que pueden ser utilizadas,
 * incluyendo búsqueda tabú y búsqueda normal.
 */
public enum StrategyType
{
	/** @brief Búsqueda tabú con lista de soluciones prohibidas */
	TABU,
	/** @brief Búsqueda normal sin restricciones tabú */
	NORMAL;	
}
