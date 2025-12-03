/**
 * @file LimitRoulette.java
 * @brief Clase auxiliar para implementar selección por ruleta con límites
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package metaheuristics.generators;

/**
 * @class LimitRoulette
 * @brief Clase que define un rango de límites para selección por ruleta
 * 
 * Esta clase almacena los límites inferior y superior de un segmento de ruleta
 * asociado a un generador específico, utilizada en el MultiGenerator.
 */
public class LimitRoulette {

	/** @brief Límite inferior del segmento de ruleta */
	private float limitLow;
	
	/** @brief Límite superior del segmento de ruleta */
	private float limitHigh;
	
	/** @brief Generador asociado a este segmento de ruleta */
	private Generator generator;
	
	/**
	 * @brief Obtiene el generador asociado
	 * @return Generador asociado al segmento
	 */
	public Generator getGenerator() {
		return generator;
	}
	
	/**
	 * @brief Establece el generador asociado
	 * @param generator Nuevo generador a asociar
	 */
	public void setGenerator(Generator generator) {
		this.generator = generator;
	}
	
	/**
	 * @brief Obtiene el límite superior
	 * @return Límite superior del segmento
	 */
	public float getLimitHigh() {
		return limitHigh;
	}
	
	/**
	 * @brief Establece el límite superior
	 * @param limitHigh Nuevo límite superior
	 */
	public void setLimitHigh(float limitHigh) {
		this.limitHigh = limitHigh;
	}
	
	/**
	 * @brief Obtiene el límite inferior
	 * @return Límite inferior del segmento
	 */
	public float getLimitLow() {
		return limitLow;
	}
	
	/**
	 * @brief Establece el límite inferior
	 * @param limitLow Nuevo límite inferior
	 */
	public void setLimitLow(float limitLow) {
		this.limitLow = limitLow;
	}
	
	
}
