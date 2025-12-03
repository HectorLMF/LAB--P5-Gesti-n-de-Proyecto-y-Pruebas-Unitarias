/**
 * @file SimulatedAnnealing.java
 * @brief Implementación del algoritmo de recocido simulado
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import local_search.acceptation_type.AcceptType;
import local_search.acceptation_type.AcceptableCandidate;
import local_search.candidate_type.CandidateType;
import local_search.candidate_type.CandidateValue;
import local_search.complement.StrategyType;
import metaheurictics.strategy.Strategy;

import problem.definition.State;

import factory_interface.IFFactoryAcceptCandidate;
import factory_method.FactoryAcceptCandidate;

/**
 * @class SimulatedAnnealing
 * @brief Algoritmo de recocido simulado con temperatura decreciente
 * 
 * Esta clase implementa el algoritmo de recocido simulado que acepta soluciones
 * peores con probabilidad decreciente según un esquema de temperatura.
 */
public class SimulatedAnnealing extends Generator {

	/** @brief Seleccionador de valores candidatos */
	private CandidateValue candidatevalue;
	
	/** @brief Tipo de aceptación de soluciones */
	private AcceptType typeAcceptation;
	
	/** @brief Tipo de estrategia de búsqueda */
	private StrategyType strategy;
	
	/** @brief Tipo de candidato a seleccionar */
	private CandidateType typeCandidate;
	
	/** @brief Estado de referencia del recocido simulado */
	private State stateReferenceSA;
	
	/** @brief Factoría para crear aceptadores de candidatos */
    private IFFactoryAcceptCandidate ifacceptCandidate;
	
	/** @brief Factor de enfriamiento (alpha) */
    public static Double alpha;
	
	/** @brief Temperatura inicial */
    public static Double tinitial;
	
	/** @brief Temperatura final */
    public static Double tfinal;
	
	/** @brief Número de iteraciones por temperatura */
    public static int countIterationsT;
	
	/** @brief Contador de repeticiones */
    private int countRept;
	
	/** @brief Tipo de generador */
    private GeneratorType typeGenerator;
	
	/** @brief Lista de estados de referencia */
    private List<State> listStateReference = new ArrayList<State>();
	
	/** @brief Peso del generador */
    private float weight;

	/** @brief Contador de mejoras por período para problemas dinámicos */
	private int[] betterCountByPeriod = new int[10];
	
	/** @brief Contador de uso por período para problemas dinámicos */
	private int[] usageCountByPeriod = new int[10];
	
	/** @brief Historial de trazas del peso */
    private float[] listTrace = new float[1200000];


    public GeneratorType getTypeGenerator() {
		return typeGenerator;
	}

	public void setTypeGenerator(GeneratorType typeGenerator) {
		this.typeGenerator = typeGenerator;
	}

	public SimulatedAnnealing(){

    	super();
    	/*SimulatedAnnealing.alpha = 0.93;
    	SimulatedAnnealing.tinitial = 250.0;
    	SimulatedAnnealing.tfinal = 41.66;
    	SimulatedAnnealing.countIterationsT = 50;*/

    	this.typeAcceptation = AcceptType.AcceptNotBadT;
		this.strategy = StrategyType.NORMAL;
		this.typeCandidate = CandidateType.RandomCandidate;
		this.candidatevalue = new CandidateValue();
		this.typeGenerator = GeneratorType.SIMULATED_ANNEALING;
		this.weight = 50;
		listTrace[0] = this.weight;
		betterCountByPeriod[0] = 0;
		usageCountByPeriod[0] = 0;
    }

	@Override
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		//<State>list=new ArrayList<State>();
		List<State> neighborhood = new ArrayList<State>();
		neighborhood = Strategy.getStrategy().getProblem().getOperator().generatedNewState(stateReferenceSA, operatornumber);
	    State statecandidate = candidatevalue.stateCandidate(stateReferenceSA, typeCandidate, strategy, operatornumber, neighborhood);
	   // list.add(statecandidate);
	    return statecandidate;
	}

	@Override
	public State getReference() {
		return stateReferenceSA;
	}

	public void setStateRef(State stateRef) {
		this.stateReferenceSA = stateRef;
	}

	@Override
	public void setInitialReference(State stateInitialRef) {
		this.stateReferenceSA = stateInitialRef;
	}

	@Override
	public void updateReference(State stateCandidate, Integer countIterationsCurrent)throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		countRept = countIterationsT;
		ifacceptCandidate = new FactoryAcceptCandidate();
		AcceptableCandidate candidate = ifacceptCandidate.createAcceptCandidate(typeAcceptation);
		Boolean accept = candidate.acceptCandidate(stateReferenceSA, stateCandidate);
		if(accept.equals(true))
		  stateReferenceSA = stateCandidate;
		if(countIterationsCurrent.equals(countIterationsT)){
			tinitial = tinitial * alpha;
			countIterationsT = countIterationsT + countRept;
		}
//		getReferenceList();
	}

	@Override
	public GeneratorType getType() {
		return this.typeGenerator;
	}

	@Override
	public List<State> getReferenceList() {
		listStateReference.add(stateReferenceSA);
		return listStateReference;
	}

	@Override
	public List<State> getSonList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean awardUpdateREF(State stateCandidate) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public float getWeight() {
		// TODO Auto-generated method stub
		return this.weight;
	}

	@Override
	public void setWeight(float weight) {
		// TODO Auto-generated method stub
		this.weight = weight;
	}

	@Override
	public int[] getListCountBetterGender() {
		return this.betterCountByPeriod;
	}

	@Override
	public int[] getListCountGender() {
		return this.usageCountByPeriod;
	}

	@Override
	public float[] getTrace() {
		// TODO Auto-generated method stub
		return this.listTrace;
	}

}
