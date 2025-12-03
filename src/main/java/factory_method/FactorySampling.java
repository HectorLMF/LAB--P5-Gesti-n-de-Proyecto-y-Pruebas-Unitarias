/**
 * @file FactorySampling.java
 * @brief Implementación concreta de la fábrica de estrategias de muestreo.
 * 
 * Esta clase crea instancias de estrategias de muestreo utilizadas en
 * algoritmos de estimación de distribuciones (EDA) mediante reflexión.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package factory_method;

import java.lang.reflect.InvocationTargetException;


import evolutionary_algorithms.complement.Sampling;
import evolutionary_algorithms.complement.SamplingType;
import factory_interface.IFFSampling;

/**
 * @class FactorySampling
 * @brief Fábrica concreta para crear estrategias de muestreo.
 */
public class FactorySampling implements IFFSampling {
	/** Instancia de la estrategia de muestreo creada */
	private Sampling sampling;
	
	/**
	 * @brief Crea una estrategia de muestreo mediante reflexión.
	 * 
	 * @param typesampling Tipo de estrategia de muestreo a crear
	 * @return Instancia de la estrategia de muestreo
	 * @throws IllegalArgumentException Si el argumento es inválido
	 * @throws SecurityException Si hay una violación de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación del método
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	public Sampling createSampling(SamplingType typesampling) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		String className = "evolutionary_algorithms.complement." + typesampling.toString();
		sampling = (Sampling) FactoryLoader.getInstance(className);
		return sampling;
	}
}
