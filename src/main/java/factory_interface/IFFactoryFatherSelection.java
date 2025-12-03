/**
 * @file IFFactoryFatherSelection.java
 * @brief Interfaz Factory para la creación de estrategias de selección de padres.
 * 
 * Define el contrato para las fábricas que crean objetos FatherSelection
 * utilizados en algoritmos evolutivos para seleccionar individuos padres.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package factory_interface;

import java.lang.reflect.InvocationTargetException;

import evolutionary_algorithms.complement.FatherSelection;
import evolutionary_algorithms.complement.SelectionType;

/**
 * @interface IFFactoryFatherSelection
 * @brief Interfaz para la fábrica de estrategias de selección de padres.
 */
public interface IFFactoryFatherSelection {
	
	/**
	 * @brief Crea una estrategia de selección de padres según el tipo especificado.
	 * 
	 * @param selectionType Tipo de estrategia de selección a crear
	 * @return Una instancia de la estrategia de selección solicitada
	 * @throws IllegalArgumentException Si el argumento es inválido
	 * @throws SecurityException Si hay una violación de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación del método
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	FatherSelection createSelectFather(SelectionType selectionType)throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException ;
}
