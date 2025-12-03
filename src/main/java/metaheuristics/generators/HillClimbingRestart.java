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
	// Rename fields to avoid confusion with Generator's fields
	public static int usageCount = 0;
	public static int improvementCount = 0;
	private int[] improvementCountHistory = new int[10];
	private int[] usageCountHistory = new int[10];
	private float[] listTrace = new float[1200000];

	public HillClimbingRestart() {
		super();
//		countIterations = Strategy.getStrategy().getCountCurrent();
//		countSame = 1;
		countCurrent = count;
		this.typeAcceptation = AcceptType.AcceptBest;
		this.strategy = StrategyType.NORMAL;
		if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)) {
			this.typeCandidate = CandidateType.GreaterCandidate;
		}
		else{
			this.typeCandidate = CandidateType.SmallerCandidate;
		}
		this.candidatevalue = new CandidateValue();
		this.Generatortype = GeneratorType.HILL_CLIMBING_RESTART;
		this.weight = 50;
		listTrace[0] = this.weight;
		improvementCountHistory[0] = 0;
		usageCountHistory[0] = 0;
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
		// Backward-compatible getter returns renamed field
		return this.improvementCountHistory;
	}

	@Override
	public int[] getListCountGender() {
		return this.usageCountHistory;
	}

	@Override
	public float[] getTrace() {
		// TODO Auto-generated method stub
		return this.listTrace;
	}
}
