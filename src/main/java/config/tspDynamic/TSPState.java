package config.tspDynamic;

/**
 * @file TSPState.java
 * @brief Clase que representa el estado de una ciudad en el problema del viajante (TSP).
 * 
 * Esta clase almacena información sobre una ciudad específica en el contexto
 * del problema del viajante, incluyendo su valor y su identificador.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
public class TSPState {
	
	/** Valor asociado a la ciudad */
	private int value;
	
	/** Identificador único de la ciudad */
	/** Identificador único de la ciudad */
	private int idCity;
	//private boolean pool;
	
	/**
	 * @brief Obtiene el valor asociado a la ciudad.
	 * @return El valor de la ciudad
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * @brief Establece el valor asociado a la ciudad.
	 * @param value El nuevo valor para la ciudad
	 */
	public void setValue(int value) {
		this.value = value;
	}
	/*public boolean isPool() {
		return pool;
	}
	public void setPool(boolean pool) {
		this.pool = pool;
	}*/
	
	/**
	 * @brief Obtiene el identificador de la ciudad.
	 * @return El ID de la ciudad
	 */
	public int getIdCity() {
		return idCity;
	}
	
	/**
	 * @brief Establece el identificador de la ciudad.
	 * @param idCity El nuevo ID para la ciudad
	 */
	public void setIdCity(int idCity) {
		this.idCity = idCity;
	}

}
