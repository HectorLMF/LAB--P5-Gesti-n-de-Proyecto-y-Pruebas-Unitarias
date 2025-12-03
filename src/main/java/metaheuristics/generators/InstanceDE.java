/**
 * @file InstanceDE.java
 * @brief Hilo para crear instancia del algoritmo de estimación de distribuciones
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package metaheuristics.generators;

import factory_method.FactoryGenerator;
import metaheuristics.generators.MultiGenerator;

/**
 * @class InstanceDE
 * @brief Hilo ejecutable para instanciar el algoritmo de estimación de distribuciones
 * 
 * Esta clase implementa Runnable para crear instancias del generador DE de forma concurrente
 * y reemplazar el generador correspondiente en la lista del MultiGenerator.
 */
public class InstanceDE implements Runnable {

	/** @brief Indicador de finalización del hilo */
	private boolean terminate = false;
	
	/**
	 * @brief Método run del hilo que crea la instancia del generador DE
	 * 
	 * Crea una nueva instancia del algoritmo de estimación de distribuciones y la
	 * reemplaza en la lista de generadores del MultiGenerator.
	 */
	public void run() {
		FactoryGenerator ifFactoryGenerator = new FactoryGenerator();
		Generator generatorDE = null;
		try {
			generatorDE = ifFactoryGenerator.createGenerator(GeneratorType.DISTRIBUTION_ESTIMATION_ALGORITHM);
		} catch (Exception e) {
			e.printStackTrace();
		}
		boolean find = false;
		int i = 0;
		while (find == false) {
			if(MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.DISTRIBUTION_ESTIMATION_ALGORITHM)){
				MultiGenerator.getListGenerators()[i] = generatorDE;
				find = true;
			}
			else i++;	
		}
		terminate = true;
	}

	/**
	 * @brief Verifica si el hilo ha terminado
	 * @return true si el hilo terminó, false en caso contrario
	 */
	public boolean isTerminate() {
		return terminate;
	}

	/**
	 * @brief Establece el estado de terminación del hilo
	 * @param terminate Nuevo estado de terminación
	 */
	public void setTerminate(boolean terminate) {
		this.terminate = terminate;
	}
}
