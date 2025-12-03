/**
 * @file MultiobjectiveHillClimbingRestart.java
 * @brief Implementación de Hill Climbing multiobjetivo con reinicio
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import factory_interface.IFFactoryAcceptCandidate;
import factory_method.FactoryAcceptCandidate;

import problem.definition.State;

import local_search.acceptation_type.AcceptType;
import local_search.acceptation_type.AcceptableCandidate;
import local_search.candidate_type.CandidateType;
import local_search.candidate_type.CandidateValue;
import local_search.complement.StrategyType;
import metaheurictics.strategy.Strategy;



/**
 * @class MultiobjectiveHillClimbingRestart
 * @brief Hill Climbing multiobjetivo que reinicia la búsqueda desde soluciones aleatorias
 * 
 * Esta clase implementa un algoritmo de búsqueda local multiobjetivo que mantiene
 * un frente de Pareto y reinicia desde soluciones aleatorias cuando se estanca.
 */
public class MultiobjectiveHillClimbingRestart extends Generator{

	/** @brief Valor del candidato para selección */
	protected CandidateValue candidatevalue;
	/** @brief Tipo de aceptación de candidatos */
	protected AcceptType typeAcceptation;
	/** @brief Estrategia de búsqueda */
	protected StrategyType strategy;
	/** @brief Tipo de candidato */
	protected CandidateType typeCandidate;
	/** @brief Estado de referencia actual del algoritmo */
	protected State stateReferenceHC;
	/** @brief Fábrica para crear criterios de aceptación */
	protected IFFactoryAcceptCandidate ifacceptCandidate;
	/** @brief Tipo de generador */
	protected GeneratorType Generatortype;
	/** @brief Lista de estados de referencia */
	protected List<State> listStateReference = new ArrayList<State>(); 
	/** @brief Peso del generador */
	protected float weight;
	/** @brief Traza de pesos */
	protected List<Float> listTrace = new ArrayList<Float>();
	/** @brief Lista de estados visitados */
	private List<State> visitedState = new ArrayList<State>();
	/** @brief Tamaño del vecindario */
	public static int sizeNeighbors;

	/**
	 * @brief Constructor por defecto
	 * 
	 * Inicializa el Hill Climbing multiobjetivo con reinicio aleatorio
	 * cuando se estanca en óptimos locales.
	 */
	public MultiobjectiveHillClimbingRestart() {
		super();
		this.typeAcceptation = AcceptType.ACCEPT_NOT_DOMINATED;
		this.strategy = StrategyType.NORMAL;
		//Problem problem = Strategy.getStrategy().getProblem();
		this.typeCandidate = CandidateType.NOT_DOMINATED_CANDIDATE;
		this.candidatevalue = new CandidateValue();
		this.Generatortype = GeneratorType.MULTIOBJECTIVE_HILL_CLIMBING_RESTART;
		this.weight = 50;
		listTrace.add(weight);
	}

	/**
	 * @brief Genera un nuevo estado candidato
	 * @param operatornumber Número de operador para generar el vecindario
	 * @return Estado candidato generado
	 * @throws IllegalArgumentException Si el argumento es ilégal
	 * @throws SecurityException Si hay un problema de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	@Override
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<State> neighborhood = new ArrayList<State>();
		neighborhood = Strategy.getStrategy().getProblem().getOperator().generatedNewState(stateReferenceHC, operatornumber);
		State statecandidate = candidatevalue.stateCandidate(stateReferenceHC, typeCandidate, strategy, operatornumber, neighborhood);
		return statecandidate;
	}

	/**
	 * @brief Actualiza la referencia con reinicio si es necesario
	 * 
	 * Actualiza el frente de Pareto y reinicia desde soluciones aleatorias
	 * si no encuentra mejoras en el vecindario.
	 * 
	 * @param stateCandidate Estado candidato a evaluar
	 * @param countIterationsCurrent Contador de iteraciones actuales
	 * @throws IllegalArgumentException Si el argumento es ilégal
	 * @throws SecurityException Si hay un problema de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	@Override
	public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		//Agregando la primera soluci�n a la lista de soluciones no dominadas

		if(Strategy.getStrategy().listRefPoblacFinal.size() == 0){
			Strategy.getStrategy().listRefPoblacFinal.add(stateReferenceHC.getCopy());
		}

		ifacceptCandidate = new FactoryAcceptCandidate();
		AcceptableCandidate candidate = ifacceptCandidate.createAcceptCandidate(typeAcceptation);
		State lastState = Strategy.getStrategy().listRefPoblacFinal.get(Strategy.getStrategy().listRefPoblacFinal.size()-1);
		List<State> neighborhood = new ArrayList<State>();
		neighborhood = Strategy.getStrategy().getProblem().getOperator().generatedNewState(stateReferenceHC, sizeNeighbors);
		int i= 0;

		Boolean accept = candidate.acceptCandidate(lastState, stateCandidate.getCopy());

		if(accept.equals(true)){
			stateReferenceHC = stateCandidate.getCopy();
			visitedState = new ArrayList<State>();
			lastState = stateCandidate.getCopy();
			//tomar xc q pertenesca a la vecindad de xa
		}
		else{
			boolean stop = false;
			while (i < neighborhood.size()&& stop==false) {
				if (Contain(neighborhood.get(i))==false) {
					stateCandidate = neighborhood.get(i);
					Strategy.getStrategy().getProblem().Evaluate(stateCandidate);  
					visitedState.add(stateCandidate);
					accept = candidate.acceptCandidate(lastState, stateCandidate.getCopy());
					stop=true;
				}
				i++;
			}
			while (stop == false) {
				stateCandidate = Strategy.getStrategy().getProblem().getOperator().generateRandomState(1).get(0);
				if (Contain(stateCandidate)==false) {
					Strategy.getStrategy().getProblem().Evaluate(stateCandidate);  
					stop=true;
					accept = candidate.acceptCandidate(lastState, stateCandidate.getCopy());

				}
			}
			if(accept.equals(true)){
				stateReferenceHC = stateCandidate.getCopy();
				visitedState = new ArrayList<State>();
				lastState = stateCandidate.getCopy();
				//tomar xc q pertenesca a la vecindad de xa
			}
		}


		getReferenceList();
	}

	/**
	 * @brief Obtiene la lista de referencias
	 * @return Lista de estados de referencia
	 */
	@Override
	public List<State> getReferenceList() {
		listStateReference.add(stateReferenceHC.getCopy());
		return listStateReference;
	}

	/**
	 * @brief Obtiene el estado de referencia actual
	 * @return Estado de referencia
	 */
	@Override
	public State getReference() {
		return stateReferenceHC;
	}

	/**
	 * @brief Establece el estado de referencia
	 * @param stateRef Nuevo estado de referencia
	 */
	public void setStateRef(State stateRef) {
		this.stateReferenceHC = stateRef;
	}

	/**
	 * @brief Establece la referencia inicial
	 * @param stateInitialRef Estado inicial de referencia
	 */
	@Override
	public void setInitialReference(State stateInitialRef) {
		this.stateReferenceHC = stateInitialRef;
	}

	/**
	 * @brief Obtiene el tipo de generador
	 * @return Tipo de generador
	 */
	public GeneratorType getGeneratorType() {
		return Generatortype;
	}

	/**
	 * @brief Establece el tipo de generador
	 * @param Generatortype Nuevo tipo de generador
	 */
	public void setGeneratorType(GeneratorType Generatortype) {
		this.Generatortype = Generatortype;
	}

	/**
	 * @brief Obtiene el tipo de generador
	 * @return Tipo de generador
	 */
	@Override
	public GeneratorType getType() {
		return this.Generatortype;
	}

	/**
	 * @brief Obtiene lista de hijos
	 * @return null (no implementado para este algoritmo)
	 */
	@Override
	public List<State> getSonList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @brief Verifica si un estado ya fue visitado
	 * @param state Estado a verificar
	 * @return true si el estado ya fue visitado, false en caso contrario
	 */
	private boolean Contain(State state){
		boolean found = false;
		for (Iterator<State> iter = visitedState.iterator(); iter.hasNext();) {
			State element = (State) iter.next();
			if(element.Comparator(state)==true){
				found = true;
			}
		}
		return found;
	}

	/**
	 * @brief Actualiza referencia con premio
	 * @param stateCandidate Estado candidato
	 * @return false (no implementado)
	 */
	@Override
	public boolean awardUpdateREF(State stateCandidate) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @brief Obtiene el peso del generador
	 * @return 0 (no implementado)
	 */
	@Override
	public float getWeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @brief Establece el peso del generador
	 * @param weight Nuevo peso
	 */
	@Override
	public void setWeight(float weight) {
		// TODO Auto-generated method stub

	}

	/**
	 * @brief Obtiene la traza de pesos
	 * @return null (no implementado)
	 */
	@Override
	public float[] getTrace() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @brief Obtiene lista de contadores de mejores géneros
	 * @return null (no implementado)
	 */
	@Override
	public int[] getListCountBetterGender() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @brief Obtiene lista de contadores de géneros
	 * @return null (no implementado)
	 */
	@Override
	public int[] getListCountGender() {
		// TODO Auto-generated method stub
		return null;
	}
}
