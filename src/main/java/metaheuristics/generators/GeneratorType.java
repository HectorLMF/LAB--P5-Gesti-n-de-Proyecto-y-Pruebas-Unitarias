/**
 * @file GeneratorType.java
 * @brief Enumeración de tipos de generadores metaheurísticos disponibles
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package metaheuristics.generators;

/**
 * @enum GeneratorType
 * @brief Define todos los tipos de algoritmos metaheurísticos disponibles en el sistema
 * 
 * Esta enumeración incluye algoritmos de búsqueda local, algoritmos poblacionales,
 * algoritmos evolutivos y variantes multiobjetivo.
 */
public enum GeneratorType {
	/** @brief Algoritmo Hill Climbing (Ascenso de Colina) */
	HILL_CLIMBING,
	
	/** @brief Algoritmo de Búsqueda Tabú */
	TABU_SEARCH,
	
	/** @brief Algoritmo de Recocido Simulado */
	SIMULATED_ANNEALING,
	
	/** @brief Algoritmo de Búsqueda Aleatoria */
	RANDOM_SEARCH,
	
	/** @brief Algoritmo de Umbral Límite */
	LIMIT_THRESHOLD,
	
	/** @brief Algoritmo Hill Climbing con Reinicio */
	HILL_CLIMBING_RESTART,
	
	/** @brief Algoritmo Genético */
	GENETIC_ALGORITHM,
	
	/** @brief Algoritmo de Estrategias Evolutivas */
	EVOLUTION_STRATEGIES,
	
	/** @brief Algoritmo de Estimación de Distribuciones */
	DISTRIBUTION_ESTIMATION_ALGORITHM,
	
	/** @brief Algoritmo de Optimización por Enjambre de Partículas */
	PARTICLE_SWARM_OPTIMIZATION,
	
	/** @brief Generador Múltiple (combina varios generadores) */
	MULTI_GENERATOR,
	
	/** @brief Búsqueda Tabú Multiobjetivo */
	MULTIOBJECTIVE_TABU_SEARCH,
	
	/** @brief Hill Climbing Estocástico Multiobjetivo */
	MULTIOBJECTIVE_STOCHASTIC_HILL_CLIMBING,
	
	/** @brief Recocido Simulado Multicaso */
	MULTI_CASE_SIMULATED_ANNEALING,
	
	/** @brief Hill Climbing Multiobjetivo con Reinicio */
	MULTIOBJECTIVE_HILL_CLIMBING_RESTART,
	
	/** @brief Hill Climbing Multiobjetivo basado en Distancia */
	MULTIOBJECTIVE_HILL_CLIMBING_DISTANCE;
}
