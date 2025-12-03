/**
 * @file Probability.java
 * @brief Clase para representar una probabilidad asociada a una clave-valor
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package evolutionary_algorithms.complement;

/**
 * @class Probability
 * @brief Representa una probabilidad asociada a un par clave-valor
 */
public class Probability {
    /** @brief Clave identificadora */
    private Object key;
    /** @brief Valor asociado */
    private Object value;
	/** @brief Valor de probabilidad */
	private float probability;
	
	
	/**
	 * @brief Obtiene el valor de probabilidad
	 * @return Valor de probabilidad
	 */
	public float getProbability() {
		return probability;
	}
	/**
	 * @brief Establece el valor de probabilidad
	 * @param probability Nuevo valor de probabilidad
	 */
	public void setProbability(float probability) {
		this.probability = probability;
	}
	/**
	 * @brief Obtiene la clave
	 * @return Clave
	 */
	public Object getKey() {
		return key;
	}
	/**
	 * @brief Establece la clave
	 * @param key Nueva clave
	 */
	public void setKey(Object key) {
		this.key = key;
	}
	/**
	 * @brief Obtiene el valor
	 * @return Valor
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * @brief Establece el valor
	 * @param value Nuevo valor
	 */
	public void setValue(Object value) {
		this.value = value;
	}
}
