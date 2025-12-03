/**
 * @file IFFactoryDistribution.java
 * @brief Interfaz Factory para la creación de distribuciones de probabilidad.
 * 
 * Define el contrato para las fábricas que crean objetos Distribution
 * utilizados en algoritmos de estimación de distribuciones (EDA).
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package factory_interface;

import java.lang.reflect.InvocationTargetException;

import evolutionary_algorithms.complement.Distribution;
import evolutionary_algorithms.complement.DistributionType;

/**
 * @interface IFFactoryDistribution
 * @brief Interfaz para la fábrica de distribuciones de probabilidad.
 */
public interface IFFactoryDistribution {
	/**
	 * @brief Crea una distribución de probabilidad según el tipo especificado.
	 * 
	 * @param typedistribution Tipo de distribución a crear
	 * @return Una instancia de la distribución solicitada
	 * @throws IllegalArgumentException Si el argumento es inválido
	 * @throws SecurityException Si hay una violación de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación del método
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	Distribution createDistribution(DistributionType typedistribution) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;
}
