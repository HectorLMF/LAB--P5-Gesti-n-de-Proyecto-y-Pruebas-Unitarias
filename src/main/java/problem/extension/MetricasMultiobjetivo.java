/**
 * @file MetricasMultiobjetivo.java
 * @brief Clase para calcular métricas de calidad en optimización multiobjetivo.
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package problem.extension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jxl.read.biff.BiffException;
import problem.definition.State;

/**
 * @class MetricasMultiobjetivo
 * @brief Proporciona métricas para evaluar la calidad de soluciones en problemas multiobjetivo.
 * 
 * Incluye métricas como tasa de error, distancia generacional y dispersión para comparar
 * frentes de Pareto actuales con frentes de Pareto verdaderos.
 */
public class MetricasMultiobjetivo {

	/**
	 * @brief Calcula el porcentaje de soluciones que no son miembros del frente de Pareto verdadero.
	 * 
	 * @param solutionsFPcurrent Frente de Pareto actual obtenido
	 * @param solutionsFPtrue Frente de Pareto verdadero de referencia
	 * @return Tasa de error como valor entre 0 y 1
	 * @throws BiffException Si hay error al leer datos
	 * @throws IOException Si hay error de entrada/salida
	 */
	public double TasaError(List<State> solutionsFPcurrent, List<State> solutionsFPtrue) throws BiffException, IOException{
		float tasaError = 0;
		for (int i = 0; i < solutionsFPcurrent.size() ; i++) { // frente de pareto actual
			State solutionVO = solutionsFPcurrent.get(i);
			if(!Contains(solutionVO, solutionsFPtrue)){ // no esta en el frente de pareto verdadero 
				tasaError++;
			}
		}
		double total = tasaError/solutionsFPcurrent.size();
		//System.out.println(solutionsFP.size() + "/" + solutions.size() + "/" + total);
		return total;
	}
	
	/**
	 * @brief Calcula la distancia generacional entre el frente de Pareto actual y el verdadero.
	 * 
	 * Indica qué tan lejos están los elementos del frente de Pareto actual respecto al frente de Pareto verdadero
	 * usando distancia euclidiana.
	 * 
	 * @param solutionsFPcurrent Frente de Pareto actual obtenido
	 * @param solutionsFPtrue Frente de Pareto verdadero de referencia
	 * @return Distancia generacional promedio
	 * @throws BiffException Si hay error al leer datos
	 * @throws IOException Si hay error de entrada/salida
	 */
	public double DistanciaGeneracional(List<State> solutionsFPcurrent, List<State> solutionsFPtrue) throws BiffException, IOException{
		float min = 1000;
		float distancia = 0;
		float distanciaGeneracional = 0;
		for (int i = 0; i < solutionsFPcurrent.size();i++) {
			State solutionVO = solutionsFPcurrent.get(i);
			//Calculando la distancia euclideana entre solutionVO y el miembro m�s cercano del frente de Pareto verdadero
			min = 1000;
			for (int j = 0; j < solutionsFPtrue.size();j++) { 
				for (int j2 = 0; j2 < solutionVO.getEvaluation().size(); j2++) {
					State solutionFPV = solutionsFPtrue.get(j);
					// porq elevar la distancia al cuadrado
					distancia += (solutionVO.getEvaluation().get(j2) - solutionFPV.getEvaluation().get(j2))*  
							(solutionVO.getEvaluation().get(j2) - solutionFPV.getEvaluation().get(j2)); //ceros si el argumento es el cero, 1.0 si el argumento es mayor que el cero, -1.0 si el argumento est� menos del cero
				}
				if(distancia < min){
					min = distancia;
				}
			}
			distanciaGeneracional += min;
		}
		double total = Math.sqrt(distanciaGeneracional)/solutionsFPcurrent.size();
		//System.out.println(total);
		return total;
	}

	/**
	 * @brief Calcula la dispersión de las soluciones obtenidas.
	 * 
	 * Mide qué tan distribuidas están las soluciones en el espacio de objetivos,
	 * calculando la desviación estándar de las distancias entre soluciones vecinas.
	 * 
	 * @param solutions Lista de soluciones a analizar
	 * @return Valor de dispersión
	 * @throws BiffException Si hay error al leer datos
	 * @throws IOException Si hay error de entrada/salida
	 */
	public double Dispersion(ArrayList<State> solutions) throws BiffException, IOException{
		//Soluciones obtenidas con la ejecuci�n del algoritmo X
		LinkedList<Float> distancias = new LinkedList<Float>();
		float distancia = 0;
		float min = 1000;
		for (Iterator<State> iter = solutions.iterator(); iter.hasNext();) {
			State solutionVO = (State) iter.next();
			min = 1000;
			for (Iterator<State> iterator = solutions.iterator(); iterator.hasNext();) {
				State solVO = (State) iterator.next();
				for (int i = 0; i < solutionVO.getEvaluation().size(); i++) {
					if(!solutionVO.getEvaluation().equals(solVO.getEvaluation())){
						distancia += (solutionVO.getEvaluation().get(i)- solVO.getEvaluation().get(i));
					}}
				if(distancia < min){
					min = distancia;
				}
			}
			distancias.add(Float.valueOf(min));
		}
		//Calculando las media de las distancias 
		float sum = 0;
		for (Iterator<Float> iter = distancias.iterator(); iter.hasNext();) {
			Float dist = (Float) iter.next();
			sum += dist;
		}
		float media = sum/distancias.size();
		float sumDistancias = 0;
		for (Iterator<Float> iter = distancias.iterator(); iter.hasNext();) {
			Float dist = (Float) iter.next();
			sumDistancias += Math.pow((media - dist),2);
		}
		//Calculando la dispersion
		double dispersion = 0;
		if(solutions.size() > 1){
			dispersion = Math.sqrt((1.0/(solutions.size()-1))*sumDistancias);
		}
		//System.out.println(dispersion);
		return dispersion;
	}
	
	/**
	 * @brief Verifica si una solución está contenida en una lista de soluciones.
	 * 
	 * @param solA Solución a buscar
	 * @param solutions Lista de soluciones donde buscar
	 * @return true si la solución está en la lista, false en caso contrario
	 */
	private boolean Contains(State solA, List<State> solutions){
		int i = 0;
		boolean result = false;
		while(i<solutions.size()&& result==false){
			if(solutions.get(i).getEvaluation().equals(solA.getEvaluation()))
				result=true;
			else
				i++;
		}
		return result;
	}
	
	/**
	 * @brief Calcula el valor mínimo de una lista de métricas.
	 * 
	 * @param allMetrics Lista de valores de métricas
	 * @return Valor mínimo encontrado
	 */
	public double CalcularMin(ArrayList<Double> allMetrics){
		double min = 1000;
		for (Iterator<Double> iter = allMetrics.iterator(); iter.hasNext();) {
			double element = (Double) iter.next();
			if(element < min){
				min = element;
			}
		}
		return min;
	}

	/**
	 * @brief Calcula el valor máximo de una lista de métricas.
	 * 
	 * @param allMetrics Lista de valores de métricas
	 * @return Valor máximo encontrado
	 */
	public double CalcularMax(ArrayList<Double> allMetrics){
		double max = 0;
		for (Iterator<Double> iter = allMetrics.iterator(); iter.hasNext();) {
			double element = (Double) iter.next();
			if(element > max){
				max = element;
			}
		}
		return max;
	}
	
	/**
	 * @brief Calcula la media aritmética de una lista de métricas.
	 * 
	 * @param allMetrics Lista de valores de métricas
	 * @return Media aritmética de los valores
	 */
	public double CalcularMedia(ArrayList<Double> allMetrics){
		double sum = 0;
		for (Iterator<Double> iter = allMetrics.iterator(); iter.hasNext();) {
			double element = (Double) iter.next();
			sum = sum + element;
		}
		double media = sum/allMetrics.size();
		return media;
	}
}
