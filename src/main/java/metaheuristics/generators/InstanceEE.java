/**
 * @file InstanceEE.java
 * @brief Hilo para crear instancia del algoritmo de estrategias evolutivas
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package metaheuristics.generators;

import factory_method.FactoryGenerator;
import metaheuristics.generators.MultiGenerator;

/**
 * @class InstanceEE
 * @brief Hilo ejecutable para instanciar el algoritmo de estrategias evolutivas
 * 
 * Esta clase implementa Runnable para crear instancias del generador EE de forma concurrente
 * y reemplazar el generador correspondiente en la lista del MultiGenerator.
 */
public class InstanceEE implements Runnable {

	/** @brief Indicador de finalización del hilo */
	private boolean terminate = false;
	
	/**
	 * @brief Método run del hilo que crea la instancia del generador EE
	 * 
	 * Crea una nueva instancia del algoritmo de estrategias evolutivas y la
	 * reemplaza en la lista de generadores del MultiGenerator.
	 */
	public void run() {
		FactoryGenerator ifFactoryGenerator = new FactoryGenerator();
		Generator generatorEE = null;
		try {
			generatorEE = ifFactoryGenerator.createGenerator(GeneratorType.EVOLUTION_STRATEGIES);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean find = false;
		int i = 0;
		while (find == false) {
			if(MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.EVOLUTION_STRATEGIES)){
				MultiGenerator.getListGenerators()[i] = generatorEE;
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
