/**
 * @file MultiobjectiveHillClimbingDistance.java
 * @brief Implementación de Hill Climbing multiobjetivo basado en distancia
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
 * @class MultiobjectiveHillClimbingDistance
 * @brief Hill Climbing multiobjetivo que selecciona soluciones basadas en distancia
 * 
 * Esta clase implementa un algoritmo de búsqueda local multiobjetivo que mantiene
 * un frente de Pareto y selecciona la solución más alejada para explorar diversidad.
 */
public class MultiobjectiveHillClimbingDistance extends Generator{

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
	/** @brief Lista que contiene las distancias de cada solución del frente de Pareto estimado */
	public static List<Double> distanceSolution = new ArrayList<Double>();

	/**
	 * @brief Constructor por defecto
	 * 
	 * Inicializa el Hill Climbing multiobjetivo con criterio de aceptación no dominado
	 * y selección basada en distancia.
	 */
	public MultiobjectiveHillClimbingDistance() {
		super();
		this.typeAcceptation = AcceptType.AcceptNotDominated;
		this.strategy = StrategyType.NORMAL;
		this.typeCandidate = CandidateType.NotDominatedCandidate;
		this.candidatevalue = new CandidateValue();
		this.Generatortype = GeneratorType.MULTIOBJECTIVE_HILL_CLIMBING_DISTANCE;
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
	 * @brief Actualiza la referencia con el candidato evaluado
	 * 
	 * Actualiza el frente de Pareto y selecciona la solución más alejada
	 * para explorar diversidad en el espacio de búsqueda.
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
	public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, 
	InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		//Agregando la primera soluci�n a la lista de soluciones no dominadas
		if(Strategy.getStrategy().listRefPoblacFinal.size() == 0){
			Strategy.getStrategy().listRefPoblacFinal.add(stateReferenceHC.getCopy());
			distanceSolution.add(new Double(0));
		}
		ifacceptCandidate = new FactoryAcceptCandidate();
		AcceptableCandidate candidate = ifacceptCandidate.createAcceptCandidate(typeAcceptation);
		State lastState = Strategy.getStrategy().listRefPoblacFinal.get(Strategy.getStrategy().listRefPoblacFinal.size()-1);
		List<State> neighborhood = new ArrayList<State>();
		neighborhood = Strategy.getStrategy().getProblem().getOperator().generatedNewState(stateReferenceHC, sizeNeighbors);
		int i= 0;
//		Boolean restart= true;

//		while (restart==true) {
			Boolean accept = candidate.acceptCandidate(lastState, stateCandidate.getCopy());
			if(accept.equals(true)){
				stateReferenceHC = stateCandidate.getCopy();
				visitedState = new ArrayList<State>();
				lastState=stateReferenceHC.getCopy();
//				restart=false;
			}

			else{

				boolean stop = false;
				while (i < neighborhood.size()&& stop==false) {
					if (Contain(neighborhood.get(i))==false) {
						stateReferenceHC = SolutionMoreDistance(Strategy.getStrategy().listRefPoblacFinal, distanceSolution);
						visitedState.add(stateReferenceHC);
						stop=true;
						lastState=stateReferenceHC.getCopy();
//						restart=false;
					}
					i++;
				}
				int coutrestart=0;
				while (stop == false && coutrestart < sizeNeighbors && accept==false) {
					stateCandidate = Strategy.getStrategy().getProblem().getOperator().generateRandomState(1).get(0);
					if (Contain(stateCandidate)==false) {
						Strategy.getStrategy().getProblem().Evaluate(stateCandidate);  
						visitedState.add(stateCandidate);
						stop=true;
						coutrestart++;
						accept = candidate.acceptCandidate(lastState, stateCandidate.getCopy());
					}
				}
				if(accept.equals(true)){
					stateReferenceHC = stateCandidate.getCopy();
					visitedState = new ArrayList<State>();
					lastState = stateReferenceHC.getCopy();
					//tomar xc q pertenesca a la vecindad de xa
				}
			}

		getReferenceList();
	}

	/**
	 * @brief Encuentra la solución más alejada del frente de Pareto
	 * @param state Lista de estados del frente de Pareto
	 * @param distanceSolution Lista de distancias correspondientes
	 * @return Estado con mayor distancia o null si no hay soluciones
	 */
	private State SolutionMoreDistance(List<State> state, List<Double> distanceSolution) {
		Double max = (double) -1;
		int pos = -1;
		Double[] distance = distanceSolution.toArray(new Double[distanceSolution.size()]);
		State[] solutions = state.toArray(new State[state.size()]);
		for (int i = 0; i < distance.length; i++) {
			Double dist = distance[i];
			if(dist > max){
				max = dist;
				pos = i;
			}
		}
		if(pos != -1)
			return solutions[pos];
		else
			return null;
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
	 * @brief Calcula y actualiza las distancias al añadir una nueva solución
	 * @param solution Lista de soluciones del frente de Pareto
	 * @return Lista actualizada de distancias
	 */
	public static List<Double> DistanceCalculateAdd(List<State> solution) {
		State[] solutions = solution.toArray(new State[solution.size()]);
		Double distance = 0.0;
		List<Double>listDist=new ArrayList<Double>();
		State lastSolution = solution.get(solution.size()-1);
		//Actualizando las distancias de todos los elmentos excepto el nuevo insertando
		for (int k = 0; k < solutions.length-1; k++) {
			State solA = solutions[k];
			distance = solA.Distance(lastSolution);
			listDist.add(distanceSolution.get(k)+distance);
//			distanceSolution.set(k, distanceSolution.get(k) + distance);
		}
		distance = 0.0;
		//Calculando la distancia del �ltimo elemento (elemento insertado) respecto al resto de los elementos
		if (solutions.length==1) {
			return distanceSolution;
		
		}else {
		
			for (int l = 0; l < solutions.length-1; l++) {
				State solB = solutions[l];
				distance += lastSolution.Distance(solB);
			}
			listDist.add(distance);
//			distanceSolution.add(distance);
			distanceSolution=listDist;
			
			return distanceSolution;
		}

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
			if(element.Comparator(state)){
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

	/**
	 * @brief Obtiene la traza de pesos
	 * @return null (no implementado)
	 */
	@Override
	public float[] getTrace() {
		// TODO Auto-generated method stub
		return null;
	}
}
