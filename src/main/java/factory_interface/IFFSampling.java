/**
 * @file IFFSampling.java
 * @brief Interfaz Factory para la creación de estrategias de muestreo.
 * 
 * Define el contrato para las fábricas que crean estrategias de muestreo
 * utilizadas en algoritmos de estimación de distribuciones (EDA).
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package factory_interface;

import java.lang.reflect.InvocationTargetException;

import evolutionary_algorithms.complement.Sampling;
import evolutionary_algorithms.complement.SamplingType;

/**
 * @interface IFFSampling
 * @brief Interfaz para la fábrica de estrategias de muestreo.
 */
public interface IFFSampling {
	/**
	 * @brief Crea una estrategia de muestreo según el tipo especificado.
	 * 
	 * @param typesampling Tipo de estrategia de muestreo a crear
	 * @return Una instancia de la estrategia de muestreo solicitada
	 * @throws IllegalArgumentException Si el argumento es inválido
	 * @throws SecurityException Si hay una violación de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación del método
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	Sampling createSampling(SamplingType typesampling) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;
}
