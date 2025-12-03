/**
 * @file HillClimbingRestart.java
 * @brief Implementación del algoritmo Hill Climbing con reinicio
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import factory_interface.IFFactoryAcceptCandidate;
import factory_method.FactoryAcceptCandidate;

import local_search.acceptation_type.AcceptType;
import local_search.acceptation_type.AcceptableCandidate;
import local_search.candidate_type.CandidateType;
import local_search.candidate_type.CandidateValue;
import local_search.complement.StrategyType;
import metaheurictics.strategy.Strategy;

import problem.definition.State;
import problem.definition.Problem.ProblemType;

/**
 * @class HillClimbingRestart
 * @brief Algoritmo Hill Climbing con reinicio periódico
 * 
 * Esta clase implementa Hill Climbing que reinicia la búsqueda desde una
 * solución aleatoria después de un número determinado de iteraciones.
 */
public class HillClimbingRestart extends Generator{

	public static int count;
	public static int countCurrent;
	private List<State> listRef = new ArrayList<State>();
	protected CandidateValue candidatevalue;
	protected AcceptType typeAcceptation;
	protected StrategyType strategy;
	protected CandidateType typeCandidate;
	protected State stateReferenceHC;
	protected IFFactoryAcceptCandidate ifacceptCandidate;
	protected GeneratorType Generatortype;
	protected List<State> listStateReference = new ArrayList<State>(); 
	protected float weight;
	
	//problemas dinamicos
	private float[] listTrace = new float[1200000];

	public HillClimbingRestart() {
		super();
//		countIterations = Strategy.getStrategy().getCountCurrent();
//		countSame = 1;
		countCurrent = count;
		this.typeAcceptation = AcceptType.ACCEPT_BEST;
		this.strategy = StrategyType.NORMAL;
		if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.MAXIMIZAR)) {
			this.typeCandidate = CandidateType.GREATER_CANDIDATE;
		}
		else{
			this.typeCandidate = CandidateType.SMALLER_CANDIDATE;
		}
		this.candidatevalue = new CandidateValue();
		this.Generatortype = GeneratorType.HILL_CLIMBING_RESTART;
		this.weight = 50;
		listTrace[0] = this.weight;
		this.listCountBetterGender = new int[10];
		this.listCountBetterGender[0] = 0;
		this.countGender = 0;
		this.countBetterGender = 0;
	}



	public State generate (Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		//ArrayList<State>list=new ArrayList<State>();
		State statecandidate = new State();
		if(count == Strategy.getStrategy().getCountCurrent()){
			State stateR = new State(stateReferenceHC);
			listRef.add(stateR);
			stateReferenceHC = Strategy.getStrategy().getProblem().getOperator().generateRandomState(1).get(0);
			Strategy.getStrategy().getProblem().Evaluate(stateReferenceHC);
			count = count + countCurrent;
		}
		List<State> neighborhood = Strategy.getStrategy().getProblem().getOperator().generatedNewState(stateReferenceHC, operatornumber);
		statecandidate = candidatevalue.stateCandidate(stateReferenceHC, typeCandidate, strategy, operatornumber, neighborhood);
		//list.add(statecandidate);
		return statecandidate;
	}

	@Override
	public void updateReference(State stateCandidate,
			Integer countIterationsCurrent) throws IllegalArgumentException,
			SecurityException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setWeight(float weight) {
		// TODO Auto-generated method stub
		
	}

	
	/*public State generate2(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		State statecandidate = new State();
		countIterations = Strategy.getStrategy().getCountCurrent();
		if (countIterations>0){
			if(Strategy.getStrategy().Statistics.getbestListStates().get(countIterations)==Strategy.getStrategy().Statistics.getbestListStates().get(countIterations-1)){
				countSame++;
				if(countSame == count-1){
					State stateR = new State(stateReferenceHC);
					listRef.add(stateR);
					stateReferenceHC = Strategy.getStrategy().getProblem().getOperator().generateRandomState(1).get(0);
				}
			}
			else
				countSame = 1;
		}
		List<State> neighborhood = Strategy.getStrategy().getProblem().getOperator().generatedNewState(stateReferenceHC, operatornumber);
		statecandidate = candidatevalue.stateCandidate(stateReferenceHC, typeCandidate, strategy, operatornumber, neighborhood);
		return statecandidate;
	}
*/
	
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
