/**
 * @file Generator.java
 * @brief Clase abstracta base para todos los generadores metaheurísticos
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package metaheuristics.generators;


import java.lang.reflect.InvocationTargetException;
import java.util.List;

import problem.definition.State;

/**
 * @class Generator
 * @brief Clase abstracta que define la interfaz común para todos los generadores metaheurísticos
 * 
 * Esta clase define los métodos abstractos que deben implementar todos los algoritmos
 * metaheurísticos del sistema, proporcionando una interfaz común para generar soluciones,
 * actualizar referencias y gestionar estados.
 */
public abstract class Generator {

	/**
	 * @brief Genera un nuevo estado candidato
	 * @param operatornumber Número de operador a utilizar
	 * @return Estado candidato generado
	 * @throws IllegalArgumentException Si los argumentos son inválidos
	 * @throws SecurityException Si hay problemas de seguridad
	 * @throws ClassNotFoundException Si no se encuentra una clase
	 * @throws InstantiationException Si hay error en la instanciación
	 * @throws IllegalAccessException Si hay acceso ilegal
	 * @throws InvocationTargetException Si hay error en la invocación
	 * @throws NoSuchMethodException Si no se encuentra un método
	 */
	public abstract State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;

	/**
	 * @brief Actualiza el estado de referencia con un nuevo candidato
	 * @param stateCandidate Estado candidato a considerar
	 * @param countIterationsCurrent Iteración actual del algoritmo
	 * @throws IllegalArgumentException Si los argumentos son inválidos
	 * @throws SecurityException Si hay problemas de seguridad
	 * @throws ClassNotFoundException Si no se encuentra una clase
	 * @throws InstantiationException Si hay error en la instanciación
	 * @throws IllegalAccessException Si hay acceso ilegal
	 * @throws InvocationTargetException Si hay error en la invocación
	 * @throws NoSuchMethodException Si no se encuentra un método
	 */
	public abstract void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;

	/**
	 * @brief Obtiene el estado de referencia actual
	 * @return Estado de referencia actual
	 */
	public abstract State getReference();

	/**
	 * @brief Establece el estado de referencia inicial
	 * @param stateInitialRef Estado de referencia inicial
	 */
	public abstract void setInitialReference (State stateInitialRef);

	/**
	 * @brief Obtiene el tipo de generador
	 * @return Tipo de generador metaheurístico
	 */
	public abstract GeneratorType getType ();

	/**
	 * @brief Obtiene la lista de estados de referencia
	 * @return Lista de estados de referencia
	 */
	public abstract List<State> getReferenceList();

	/**
	 * @brief Obtiene la lista de estados hijos generados
	 * @return Lista de estados hijos
	 */
	public abstract List<State> getSonList ();

	/**
	 * @brief Verifica si se debe actualizar la referencia
	 * @param stateCandidate Estado candidato a verificar
	 * @return true si se debe actualizar, false en caso contrario
	 */
	public abstract boolean awardUpdateREF(State stateCandidate);

	/**
	 * @brief Establece el peso del generador
	 * @param weight Nuevo peso del generador
	 */
	public abstract void setWeight(float weight);

	/**
	 * @brief Obtiene el peso actual del generador
	 * @return Peso del generador
	 */
	public abstract float getWeight();

	/**
	 * @brief Obtiene el historial de trazas
	 * @return Array con el historial de trazas
	 */
	public abstract float[] getTrace();
	
	/** @brief Contador de uso del generador */
	public int countGender;
	
	/** @brief Contador de mejoras del generador */
	public int countBetterGender;
	
	/**
	 * @brief Obtiene el historial de mejoras por período
	 * @return Array con el contador de mejoras
	 */
	public abstract int[] getListCountBetterGender();
	
	/**
	 * @brief Obtiene el historial de uso por período
	 * @return Array con el contador de uso
	 */
	public abstract int[] getListCountGender();
	
	/** @brief Array con las mejoras de cada generador en períodos de 10, acumulativo */
	public int[] listCountBetterGender;
	

}
