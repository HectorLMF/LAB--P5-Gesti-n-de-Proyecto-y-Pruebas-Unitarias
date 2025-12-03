/**
 * @file MutationType.java
 * @brief Enumeración de tipos de operadores de mutación
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package evolutionary_algorithms.complement;

/**
 * @enum MutationType
 * @brief Tipos de mutación disponibles en algoritmos evolutivos
 */
public enum MutationType {
	/** Mutación de dos puntos */
	TWO_POINTS_MUTATION,
	/** Mutación de un punto */
	ONE_POINT_MUTATION,
	/** Mutación AIOM */
	AIOM_MUTATION;    
}
