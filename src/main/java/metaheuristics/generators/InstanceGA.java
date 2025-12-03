/**
 * @file InstanceGA.java
 * @brief Hilo para crear instancia del algoritmo genético
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package metaheuristics.generators;

import factory_method.FactoryGenerator;
import metaheuristics.generators.MultiGenerator;

/**
 * @class InstanceGA
 * @brief Hilo ejecutable para instanciar el algoritmo genético
 * 
 * Esta clase implementa Runnable para crear instancias del generador GA de forma concurrente
 * y reemplazar el generador correspondiente en la lista del MultiGenerator.
 */
public class InstanceGA implements Runnable {

	/** @brief Indicador de finalización del hilo */
	private boolean terminate = false;
	
	/**
	 * @brief Método run del hilo que crea la instancia del generador GA
	 * 
	 * Crea una nueva instancia del algoritmo genético y la
	 * reemplaza en la lista de generadores del MultiGenerator.
	 */
	public void run() {
		FactoryGenerator ifFactoryGenerator = new FactoryGenerator();
		Generator generatorGA = null;
		try {
			generatorGA = ifFactoryGenerator.createGenerator(GeneratorType.GENETIC_ALGORITHM);
		} catch (Exception e) {
			e.printStackTrace();
		}
		boolean find = false;
		int i = 0;
		while (find == false) {
			if(MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.GENETIC_ALGORITHM)){
				MultiGenerator.getListGenerators()[i] = generatorGA;
				find = true;
			}
			else i++;
		}
		terminate = true;
	}

	public boolean isTerminate() {
		return terminate;
	}

	public void setTerminate(boolean terminate) {
		this.terminate = terminate;
	}

}
