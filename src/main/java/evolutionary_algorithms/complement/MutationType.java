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
	TowPointsMutation,
	/** Mutación de un punto */
	OnePointMutation,
	/** Mutación AIOM */
	AIOMutation;    
}
