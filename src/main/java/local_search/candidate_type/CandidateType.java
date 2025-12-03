/**
 * @file CandidateType.java
 * @brief Enumeración de tipos de estrategias para selección de candidatos
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package local_search.candidate_type;

/**
 * @enum CandidateType
 * @brief Define los tipos de búsqueda de candidatos en el vecindario
 * 
 * Esta enumeración lista todas las estrategias disponibles para seleccionar
 * un estado candidato del vecindario durante la búsqueda local.
 */
public enum CandidateType{
	/** @brief Selecciona el candidato con menor evaluación */
	SmallerCandidate,
	/** @brief Selecciona el candidato con mayor evaluación */
	GreaterCandidate,
	/** @brief Selecciona un candidato aleatorio */
	RandomCandidate,
	/** @brief Selecciona candidatos no dominados (multiobjetivo) */
	NotDominatedCandidate;
}
