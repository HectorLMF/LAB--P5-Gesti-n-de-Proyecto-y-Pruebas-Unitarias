/**
 * @file Problem.java
 * @brief Define la estructura de un problema de optimización.
 * 
 * Esta clase encapsula todos los elementos necesarios para definir un problema
 * de optimización: funciones objetivo, codificación, operadores, tipo de problema
 * (maximización/minimización) y métodos de evaluación.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package problem.definition;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import problem.extension.SolutionMethod;
import problem.extension.TypeSolutionMethod;


import factory_interface.IFFactorySolutionMethod;
import factory_method.FactorySolutionMethod;

/**
 * @class Problem
 * @brief Representa un problema de optimización completo.
 */
public class Problem {

	/**
	 * @enum ProblemType
	 * @brief Tipo de problema: maximización o minimización.
	 */
	public enum ProblemType {Maximizar,Minimizar;}

	/** Lista de funciones objetivo del problema */
	private ArrayList<ObjetiveFunction> function;
	
	/** Estado prototipo para generar nuevas soluciones */
	private State state;
	
	/** Tipo de problema (maximizar o minimizar) */
	private ProblemType typeProblem;
	
	/** Sistema de codificación de las soluciones */
	private Codification codification;
	
	/** Operadores para generar nuevos estados */
	private Operator operator;
	
	/** Número de valores posibles en la codificación */
	private int possibleValue;
	
	/** Tipo de método de solución para problemas multi-objetivo */
	private TypeSolutionMethod typeSolutionMethod;
	
	/** Fábrica para crear métodos de solución */
	private IFFactorySolutionMethod factorySolutionMethod;
	
	/**
	 * @brief Constructor por defecto.
	 */
	public Problem() {
		super();
	}

	public ArrayList<ObjetiveFunction> getFunction() {
		return function;
	}

	public void setFunction(ArrayList<ObjetiveFunction> function) {
		this.function = function;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public ProblemType getTypeProblem() {
		return typeProblem;
	}
	public void setTypeProblem(ProblemType typeProblem) {
		this.typeProblem = typeProblem;
	}

	public Codification getCodification() {
		return codification;
	}
	public void setCodification(Codification codification) {
		this.codification = codification;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public int getPossibleValue() {
		return possibleValue;
	}

	public void setPossibleValue(int possibleValue) {
		this.possibleValue = possibleValue;
	}

	/**
	 * @brief Evalúa un estado según las funciones objetivo definidas.
	 * 
	 * Si no hay método de solución definido, usa la primera función objetivo.
	 * Si hay método de solución (multi-objetivo), lo utiliza para evaluar.
	 * 
	 * @param state Estado a evaluar
	 * @throws IllegalArgumentException Si los argumentos son inválidos
	 * @throws SecurityException Si hay violación de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	public void Evaluate(State state) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		double eval = 0;       
		ArrayList<Double> evaluation = new ArrayList<Double>(this.function.size());
		if (typeSolutionMethod == null) {
			eval= function.get(0).Evaluation(state);
			evaluation.add(evaluation.size(), eval);
			state.setEvaluation(evaluation);
		}
		else {
			SolutionMethod method = newSolutionMethod(typeSolutionMethod);
			method.evaluationState(state);
		}
	}
	
	public TypeSolutionMethod getTypeSolutionMethod() {
		return typeSolutionMethod;
	}
	public void setTypeSolutionMethod(TypeSolutionMethod typeSolutionMethod) {
		this.typeSolutionMethod = typeSolutionMethod;
	}
	public IFFactorySolutionMethod getFactorySolutionMethod() {
		return factorySolutionMethod;
	}
	public void setFactorySolutionMethod(
			IFFactorySolutionMethod factorySolutionMethod) {
		this.factorySolutionMethod = factorySolutionMethod;
	}
	
	public SolutionMethod newSolutionMethod(TypeSolutionMethod typeSolutionMethod) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		factorySolutionMethod = new FactorySolutionMethod();
		SolutionMethod solutionMethod = factorySolutionMethod.createdSolutionMethod(typeSolutionMethod);
		return solutionMethod;
	}
}

	
	

