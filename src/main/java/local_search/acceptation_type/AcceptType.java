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
	ACCEPT_BEST {
		public String toString() {
			return "AcceptBest";
		}
	},
	/** @brief Acepta cualquier solución */
	ACCEPT_ANYONE {
		public String toString() {
			return "AcceptAnyone";
		}
	},
	/** @brief Acepta soluciones con criterio de temperatura */
	ACCEPT_NOT_BAD_T {
		public String toString() {
			return "AcceptNotBadT";
		}
	},
	/** @brief Acepta soluciones dentro de un umbral */
	ACCEPT_NOT_BAD_U {
		public String toString() {
			return "AcceptNotBadU";
		}
	},
	/** @brief Acepta soluciones no dominadas (Pareto) */
	ACCEPT_NOT_DOMINATED {
		public String toString() {
			return "AcceptNotDominated";
		}
	},
	/** @brief Acepta soluciones no dominadas con lista tabú */
	ACCEPT_NOT_DOMINATED_TABU {
		public String toString() {
			return "AcceptNotDominatedTabu";
		}
	},
	/** @brief Acepta soluciones que no empeoran */
	ACCEPT_NOT_BAD {
		public String toString() {
			return "AcceptNotBad";
		}
	},
	/** @brief Acepta soluciones con criterio multiobjetivo */
	ACCEPT_MULTICASE {
		public String toString() {
			return "AcceptMulticase";
		}
	};
	
}
