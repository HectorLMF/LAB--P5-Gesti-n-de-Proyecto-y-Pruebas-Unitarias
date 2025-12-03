/**
 * @file FactoryDistribution.java
 * @brief Implementación concreta de la fábrica de distribuciones de probabilidad.
 * 
 * Esta clase crea instancias de distribuciones utilizadas en algoritmos
 * de estimación de distribuciones (EDA) mediante reflexión.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package factory_method;

import java.lang.reflect.InvocationTargetException;


import evolutionary_algorithms.complement.Distribution;
import evolutionary_algorithms.complement.DistributionType;
import factory_interface.IFFactoryDistribution;

/**
 * @class FactoryDistribution
 * @brief Fábrica concreta para crear distribuciones de probabilidad.
 */
public class FactoryDistribution implements IFFactoryDistribution {
	/** Instancia de la distribución creada */
	private Distribution distribution;

	/**
	 * @brief Crea una distribución mediante reflexión.
	 * 
	 * @param distributiontype Tipo de distribución a crear
	 * @return Instancia de la distribución
	 * @throws IllegalArgumentException Si el argumento es inválido
	 * @throws SecurityException Si hay una violación de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación del método
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	public Distribution createDistribution(DistributionType distributiontype) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		String className = "evolutionary_algorithms.complement." + distributiontype.toString();
		distribution = (Distribution) FactoryLoader.getInstance(className);
		return distribution;
	}
}
