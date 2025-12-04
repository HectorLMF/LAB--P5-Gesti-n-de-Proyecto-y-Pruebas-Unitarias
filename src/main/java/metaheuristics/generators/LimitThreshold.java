/**
 * @file LimitThreshold.java
 * @brief Implementación del algoritmo de búsqueda con umbral límite
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


import local_search.acceptation_type.AcceptType;
import local_search.acceptation_type.AcceptableCandidate;
import local_search.candidate_type.CandidateType;
import local_search.candidate_type.CandidateValue;
import local_search.complement.StrategyType;
import metaheurictics.strategy.Strategy;

import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;



import factory_interface.IFFactoryAcceptCandidate;
import factory_method.FactoryAcceptCandidate;

/**
 * @class LimitThreshold
 * @brief Algoritmo de búsqueda local con umbral de aceptación
 * 
 * Esta clase implementa un algoritmo de búsqueda que acepta soluciones
 * que no son significativamente peores que la actual, usando un umbral.
 */
public class LimitThreshold extends Generator{
	
	private CandidateValue candidatevalue;
	private AcceptType typeAcceptation;
	private StrategyType strategy;
	private CandidateType typeCandidate;
	private State stateReferenceLT;
    private IFFactoryAcceptCandidate ifacceptCandidate;
	private GeneratorType typeGenerator;
	//private List<State> listStateReference = new ArrayList<State>(); // lista de estados referencias por los que va pasando el algoritmo
	private float weight;
	
	//problemas dinamicos
	private float[] listTrace = new float[1200000];
	
	public GeneratorType getTypeGenerator() {
		return typeGenerator;
	}

	public void setTypeGenerator(GeneratorType typeGenerator) {
		this.typeGenerator = typeGenerator;
	}

	public LimitThreshold() {
		super();
		this.typeAcceptation = AcceptType.ACCEPT_NOT_BAD_U;
		this.strategy = StrategyType.NORMAL;


		Problem problem = null;
		if (Strategy.getStrategy() != null) {
			try {
				problem = Strategy.getStrategy().getProblem();
			} catch (Exception e) {
				problem = null;
			}
		}

		if (problem != null && problem.getTypeProblem().equals(ProblemType.MAXIMIZAR)) {
			this.typeCandidate = CandidateType.GREATER_CANDIDATE;
		} else {
			// Default to GREATER_CANDIDATE when problem info is not available to avoid NPEs
			this.typeCandidate = CandidateType.GREATER_CANDIDATE;
		}

		this.candidatevalue = new CandidateValue();
		this.typeGenerator = GeneratorType.LIMIT_THRESHOLD;
		this.weight = (float) 50.0;
		listTrace[0] = weight;
		this.listCountBetterGender = new int[10];
		this.listCountBetterGender[0] = 0;
		this.countGender = 0;
		this.countBetterGender = 0;

	}
	@Override
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<State> neighborhood = Strategy.getStrategy().getProblem().getOperator().generatedNewState(stateReferenceLT, operatornumber);
	    State statecandidate = candidatevalue.stateCandidate(stateReferenceLT, typeCandidate, strategy, operatornumber, neighborhood);
	    return statecandidate;
	}

	@Override
	public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		ifacceptCandidate = new FactoryAcceptCandidate();
		AcceptableCandidate candidate = ifacceptCandidate.createAcceptCandidate(typeAcceptation);
		Boolean accept = candidate.acceptCandidate(stateReferenceLT , stateCandidate);
		if(accept.equals(true)){
			//listStateReference.add(stateCandidate);
			stateReferenceLT = stateCandidate;
		}
		//else listStateReference.add(stateReferenceHC);
	}
	

	@Override
	public State getReference() {
		return stateReferenceLT;
	}

	public void setStateRef(State stateRef) {
		this.stateReferenceLT = stateRef;
	}

	@Override
	public void setInitialReference(State stateInitialRef) {
		this.stateReferenceLT = stateInitialRef;
	}


	@Override
	public GeneratorType getType() {
		return this.typeGenerator;
	}

	@Override
	public List<State> getReferenceList() {
		return null;
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
		return this.listCountBetterGender;
	}

	@Override
	public int[] getListCountGender() {
		int[] listCountGender = new int[10];
		listCountGender[0] = this.countGender;
		return listCountGender;
	}

	@Override
	public float[] getTrace() {
		// TODO Auto-generated method stub
		return this.listTrace;
	}

}
