/**
 * @file HillClimbing.java
 * @brief Implementación del algoritmo Hill Climbing (Ascenso de Colina)
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
import problem.definition.Problem.ProblemType;



import factory_interface.IFFactoryAcceptCandidate;
import factory_method.FactoryAcceptCandidate;

/**
 * @class HillClimbing
 * @brief Algoritmo de búsqueda local Hill Climbing (Ascenso de Colina)
 * 
 * Esta clase implementa el algoritmo de búsqueda local Hill Climbing que explora
 * el vecindario de la solución actual y se mueve hacia mejores soluciones.
 */
public class HillClimbing extends Generator{

	/** @brief Seleccionador de candidatos del vecindario */
    protected CandidateValue candidatevalue;
	
	/** @brief Tipo de aceptación de soluciones */
	protected AcceptType typeAcceptation;
	
	/** @brief Tipo de estrategia de búsqueda */
	protected StrategyType strategy;
	
	/** @brief Tipo de candidato a seleccionar */
	protected CandidateType typeCandidate;
	
	/** @brief Estado de referencia del Hill Climbing */
	protected State stateReferenceHC;
	
	/** @brief Factoría para crear aceptadores de candidatos */
	protected IFFactoryAcceptCandidate ifacceptCandidate;
	
	/** @brief Tipo de generador metaheurístico */
	protected GeneratorType Generatortype;
	
	/** @brief Lista de estados de referencia */
	protected List<State> listStateReference = new ArrayList<State>(); 
	
	/** @brief Peso del generador */
	protected float weight;
	
	/** @brief Contador de mejoras por período para problemas dinámicos */
	private int[] betterCountByPeriod = new int[10];
	
	/** @brief Contador de uso por período para problemas dinámicos */
	private int[] usageCountByPeriod = new int[10];
	
	/** @brief Historial de trazas del peso */
	private float[] listTrace = new float[1200000];
	
	/**
	 * @brief Constructor por defecto del Hill Climbing
	 * 
	 * Inicializa el algoritmo con aceptación del mejor candidato y estrategia normal.
	 * El tipo de candidato se determina según el tipo de problema (maximización o minimización).
	 */
	public HillClimbing() {
		super();
		this.typeAcceptation = AcceptType.ACCEPT_BEST;
		this.strategy = StrategyType.NORMAL;
		// Guard against Strategy or Problem being null during test setups
		try {
			if (Strategy.getStrategy() != null && Strategy.getStrategy().getProblem() != null &&
				Strategy.getStrategy().getProblem().getTypeProblem() != null &&
				Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.MAXIMIZAR)) {
				this.typeCandidate = CandidateType.GREATER_CANDIDATE;
			} else {
				this.typeCandidate = CandidateType.SMALLER_CANDIDATE;
			}
		} catch (Exception e) {
			this.typeCandidate = CandidateType.GREATER_CANDIDATE;
		}
		this.candidatevalue = new CandidateValue();
		this.Generatortype = GeneratorType.HILL_CLIMBING;
		this.weight = 0;
		listTrace[0] = this.weight;
		betterCountByPeriod[0] = 0;
		usageCountByPeriod[0] = 0;
	}

	@Override
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<State> neighborhood = new ArrayList<State>();
		neighborhood = Strategy.getStrategy().getProblem().getOperator().generatedNewState(stateReferenceHC, operatornumber);
	    State statecandidate = candidatevalue.stateCandidate(stateReferenceHC, typeCandidate, strategy, operatornumber, neighborhood);
	  
	    return statecandidate;
	}

	@Override
	public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		ifacceptCandidate = new FactoryAcceptCandidate();
		AcceptableCandidate candidate = ifacceptCandidate.createAcceptCandidate(typeAcceptation);
		Boolean accept = candidate.acceptCandidate(stateReferenceHC, stateCandidate);
		if(accept.equals(true))
		  stateReferenceHC = stateCandidate;
//		getReferenceList();
	}
	
	@Override
	public List<State> getReferenceList() {
		listStateReference.add(stateReferenceHC);
		return listStateReference;
	}

	@Override
	public State getReference() {
		return stateReferenceHC;
	}

	public void setStateRef(State stateRef) {
		this.stateReferenceHC = stateRef;
	}

	@Override
	public void setInitialReference(State stateInitialRef) {
		this.stateReferenceHC = stateInitialRef;
	}

	public GeneratorType getGeneratorType() {
		return Generatortype;
	}

	public void setGeneratorType(GeneratorType Generatortype) {
		this.Generatortype = Generatortype;
	}

	@Override
	public GeneratorType getType() {
		return this.Generatortype;
	}

	@Override
	public List<State> getSonList() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTypeCandidate(CandidateType typeCandidate){
		this.typeCandidate = typeCandidate;
	}

	@Override
	public boolean awardUpdateREF(State stateCandidate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float getWeight() {
		return this.weight;
	}

	@Override
	public void setWeight(float weight) {
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
		return this.listTrace;
	}
}
