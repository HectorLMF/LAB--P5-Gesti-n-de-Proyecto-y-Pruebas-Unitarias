/**
 * @file State.java
 * @brief Representa el estado de una solución en un problema de optimización.
 * 
 * Esta clase encapsula la información de un estado o solución, incluyendo
 * su codificación, evaluación, número identificador y tipo de generador.
 * Proporciona métodos para comparar, copiar y calcular distancias entre estados.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package problem.definition;



import java.util.ArrayList;

import metaheuristics.generators.GeneratorType;

/**
 * @class State
 * @brief Clase que representa un estado o solución en el espacio de búsqueda.
 */
public class State {
	
	/** Tipo de generador que creó este estado */
	protected GeneratorType typeGenerator;
	
	/** Lista de valores de evaluación del estado (para multi-objetivo) */
	protected ArrayList<Double> evaluation;
	
	/** Número identificador del estado */
	protected int number;
	
	/** Codificación del estado (representación de la solución) */
	protected ArrayList<Object> code;
	
	/**
	 * @brief Constructor de copia.
	 * 
	 * Crea un nuevo estado copiando los valores de otro estado.
	 * 
	 * @param ps Estado a copiar
	 */
	public State(State ps) {
		typeGenerator = ps.getTypeGenerator();
		evaluation = ps.getEvaluation();
		number = ps.getNumber();
		code = new ArrayList<Object>(ps.getCode());
	}
	
	/**
	 * @brief Constructor con codificación.
	 * 
	 * Crea un nuevo estado con la codificación especificada.
	 * 
	 * @param code Codificación inicial del estado
	 */
	public State(ArrayList<Object> code) {
		super();
		this.code = code == null ? new ArrayList<Object>() : new ArrayList<Object>(code);
	}
	
	/**
	 * @brief Constructor por defecto.
	 * 
	 * Crea un nuevo estado con codificación vacía.
	 */
	public State() {
		code=new ArrayList<Object>();
	}	
	
	public ArrayList<Object> getCode() {
		return code == null ? new ArrayList<Object>() : new ArrayList<Object>(code);
	}

	public void setCode(ArrayList<Object> listCode) {
		this.code = listCode == null ? new ArrayList<Object>() : new ArrayList<Object>(listCode);
	}

	public GeneratorType getTypeGenerator() {
		return typeGenerator;
	}
	public void setTypeGenerator(GeneratorType typeGenerator) {
		this.typeGenerator = typeGenerator;
	}

	
	public ArrayList<Double> getEvaluation() {
		return evaluation == null ? null : new ArrayList<Double>(evaluation);
	}

	public void setEvaluation(ArrayList<Double> evaluation) {
		this.evaluation = evaluation == null ? null : new ArrayList<Double>(evaluation);
	}

	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	/**
	 * @brief Obtiene una copia profunda del estado.
	 * 
	 * @return Nueva instancia de State con los mismos valores
	 */
	public State getCopy(){
		State s = new State();
		s.typeGenerator = this.typeGenerator;
		s.number = this.number;
		s.code = this.code == null ? new ArrayList<Object>() : new ArrayList<Object>(this.code);
		s.evaluation = this.evaluation == null ? null : new ArrayList<Double>(this.evaluation);
		return s;
	}
	
	/**
	 * @brief Compara si dos estados tienen la misma codificación.
	 * 
	 * @param state Estado a comparar
	 * @return true si las codificaciones son iguales, false en caso contrario
	 */
	public boolean Comparator(State state){

		boolean result=false;
		if(state.getCode().equals(getCode())){
			result=true;
		}
		return result;
	}
	
	/**
	 * @brief Calcula la distancia de Hamming entre dos estados.
	 * 
	 * La distancia es el número de posiciones en las que difieren las codificaciones.
	 * 
	 * @param state Estado con el que calcular la distancia
	 * @return Número de posiciones diferentes
	 */
	public double Distance(State state){
		double distancia = 0;
		for (int i = 0; i < state.getCode().size(); i++) {
			if (!(state.getCode().get(i).equals(this.getCode().get(i)))) {
				distancia++;
			}
		}
	return distancia;
	}
}
