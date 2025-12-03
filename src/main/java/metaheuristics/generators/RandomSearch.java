/**
 * @file RandomSearch.java
 * @brief Implementación del algoritmo de búsqueda aleatoria
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
 * @class RandomSearch
 * @brief Algoritmo de búsqueda completamente aleatoria
 * 
 * Esta clase implementa un algoritmo de búsqueda que genera soluciones de forma
 * aleatoria, sirviendo como línea base para comparar con otras metaheurísticas.
 */
public class RandomSearch extends Generator {

	/** @brief Seleccionador de valores candidatos */
	private CandidateValue candidatevalue;
	
	/** @brief Tipo de aceptación de soluciones */
	private AcceptType typeAcceptation;
	
	/** @brief Tipo de estrategia utilizada */
	private StrategyType strategy;
	
	/** @brief Tipo de candidato a generar */
	private CandidateType typeCandidate;
	
	/** @brief Estado de referencia actual */
	private State stateReferenceRS;
	
	/** @brief Factoría para crear aceptadores de candidatos */
    private IFFactoryAcceptCandidate ifacceptCandidate;
	
	/** @brief Tipo de generador */
    private GeneratorType typeGenerator;

	/** @brief Peso del generador */
    private float weight;
	
	/** @brief Lista de estados de referencia para acceso desde algoritmos basados en poblaciones */
	public static List<State> listStateReference = new ArrayList<State>();
	
	/** @brief Contador de mejoras por período para problemas dinámicos */
	private int[] betterCountByPeriod = new int[10];
	
	/** @brief Contador de uso por período para problemas dinámicos */
	private int[] usageCountByPeriod = new int[10];
	
	/** @brief Historial de trazas del peso */
    private float[] listTrace = new float[1200000];
	
	/**
	 * @brief Constructor por defecto de búsqueda aleatoria
	 * 
	 * Inicializa el algoritmo con aceptación del mejor, estrategia normal y
	 * generación de candidatos aleatoria, con peso inicial de 50.
	 */
	public RandomSearch() {
		super();
		this.typeAcceptation = AcceptType.ACCEPT_BEST;
		this.strategy = StrategyType.NORMAL;
		this.typeCandidate = CandidateType.RANDOM_CANDIDATE;
		this.candidatevalue = new CandidateValue();
		this.typeGenerator = GeneratorType.RANDOM_SEARCH;
		this.weight = 50;
		listTrace[0] = this.weight;
		betterCountByPeriod[0] = 0;
		usageCountByPeriod[0] = 0;
		listStateReference = new ArrayList<State>();
	}
	
	@Override
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		//ArrayList<State>list =new ArrayList<State>();
		List<State> neighborhood = new ArrayList<State>();
		neighborhood = Strategy.getStrategy().getProblem().getOperator().generateRandomState(operatornumber);
	    State statecandidate = candidatevalue.stateCandidate(stateReferenceRS, typeCandidate, strategy, operatornumber, neighborhood);
	    if(GeneticAlgorithm.countRef != 0 || EvolutionStrategies.countRef != 0 || DistributionEstimationAlgorithm.countRef != 0 || ParticleSwarmOptimization.countRef != 0)
	    	listStateReference.add(statecandidate);
	    return statecandidate;
	}

	@Override
	public State getReference() {
		return stateReferenceRS;
	}

	@Override
	public void setInitialReference(State stateInitialRef) {
	  this.stateReferenceRS = stateInitialRef;
	}

	@Override
	public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException,	IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		ifacceptCandidate = new FactoryAcceptCandidate();
		AcceptableCandidate candidate = ifacceptCandidate.createAcceptCandidate(typeAcceptation);
		Boolean accept = candidate.acceptCandidate(stateReferenceRS, stateCandidate);
		if(accept.equals(true))
		  stateReferenceRS = stateCandidate;
//		getReferenceList();
	}


	@Override
	public GeneratorType getType() {
		return this.typeGenerator;
	}
	
	public GeneratorType getTypeGenerator() {
		return typeGenerator;
	}

	public void setTypeGenerator(GeneratorType typeGenerator) {
		this.typeGenerator = typeGenerator;
	}

	@Override
	public List<State> getReferenceList() {
		listStateReference.add(stateReferenceRS);
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
