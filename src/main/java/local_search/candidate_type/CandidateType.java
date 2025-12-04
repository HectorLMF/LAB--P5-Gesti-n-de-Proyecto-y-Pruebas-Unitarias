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
	SMALLER_CANDIDATE {
		public String toString() {
			return "SmallerCandidate";
		}
	},
	/** @brief Selecciona el candidato con mayor evaluación */
	GREATER_CANDIDATE {
		public String toString() {
			return "GreaterCandidate";
		}
	},
	/** @brief Selecciona un candidato aleatorio */
	RANDOM_CANDIDATE {
		public String toString() {
			return "RandomCandidate";
		}
	},
	/** @brief Selecciona candidatos no dominados (multiobjetivo) */
	NOT_DOMINATED_CANDIDATE {
		public String toString() {
			return "NotDominatedCandidate";
		}
	};
}
