/**
 * @file GeneticAlgorithm.java
 * @brief Implementación del algoritmo genético como metaheurística de optimización
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import metaheurictics.strategy.Strategy;

import problem.definition.State;
import problem.definition.Problem.ProblemType;

import evolutionary_algorithms.complement.Crossover;
import evolutionary_algorithms.complement.CrossoverType;
import evolutionary_algorithms.complement.FatherSelection;
import evolutionary_algorithms.complement.Mutation;
import evolutionary_algorithms.complement.MutationType;
import evolutionary_algorithms.complement.Replace;
import evolutionary_algorithms.complement.ReplaceType;
import evolutionary_algorithms.complement.SelectionType;
import factory_interface.IFFactoryCrossover;
import factory_interface.IFFactoryFatherSelection;
import factory_interface.IFFactoryMutation;
import factory_interface.IFFactoryReplace;
import factory_method.FactoryCrossover;
import factory_method.FactoryFatherSelection;
import factory_method.FactoryMutation;
import factory_method.FactoryReplace;

/**
 * @class GeneticAlgorithm
 * @brief Algoritmo genético con selección, cruzamiento, mutación y reemplazo
 * 
 * Esta clase implementa un algoritmo genético clásico que evoluciona poblaciones de
 * soluciones mediante operadores genéticos de selección, cruzamiento y mutación.
 */
public class GeneticAlgorithm extends Generator {

	/** @brief Estado de referencia del algoritmo genético */
	private State stateReferenceGA;
	
	/** @brief Lista de estados de la población */
	private List<State> listState = new ArrayList<State>(); 
	
	/** @brief Factoría para la selección de padres */
	private IFFactoryFatherSelection iffatherselection;
	
	/** @brief Factoría para el operador de cruzamiento */
	private IFFactoryCrossover iffactorycrossover;
	
	/** @brief Factoría para el operador de mutación */
	private IFFactoryMutation iffactorymutation;
	
	/** @brief Factoría para el reemplazo de soluciones */
	private IFFactoryReplace iffreplace;
	
	/** @brief Tipo de mutación utilizada */
	public static MutationType mutationType;
	
	/** @brief Tipo de cruzamiento utilizado */
	public static CrossoverType crossoverType;
	
	/** @brief Tipo de reemplazo de soluciones */
	public static ReplaceType replaceType;
	
	/** @brief Tipo de selección de padres */
	public static SelectionType selectionType;
	
	/** @brief Tipo de generador metaheurístico */
	private GeneratorType generatorType;
	
	/** @brief Probabilidad de cruzamiento */
	public static double PC;
	
	/** @brief Probabilidad de mutación */
	public static double PM;
	
	/** @brief Contador de referencias */
	public static int countRef = 0;
	
	/** @brief Tamaño de truncamiento para selección */
	public static int truncation;
	
	/** @brief Peso del generador */
	private float weight;
	
	/** @brief Contador de mejoras por período para problemas dinámicos */
	private int[] betterCountByPeriod = new int[10];
	
	/** @brief Contador de uso por período para problemas dinámicos */
	private int[] usageCountByPeriod = new int[10];
	
	/** @brief Historial de trazas del peso */
	private float[] listTrace = new float[1200000];
	
	/**
	 * @brief Constructor por defecto del algoritmo genético
	 * 
	 * Inicializa el algoritmo con cruzamiento uniforme, mutación uniforme,
	 * reemplazo de los peores y selección por truncamiento, con peso inicial de 50.
	 */
    public GeneticAlgorithm() {
		super();
		// Ensure static operator type defaults are set so factories receive non-null values
		if (selectionType == null) selectionType = SelectionType.TRUNCATION_SELECTION;
		if (crossoverType == null) crossoverType = CrossoverType.UNIFORM_CROSSOVER;
		if (mutationType == null) mutationType = MutationType.ONE_POINT_MUTATION;
		if (replaceType == null) replaceType = ReplaceType.GENERATIONAL_REPLACE;
		try {
			this.listState = getListStateRef(); // llamada al método que devuelve la lista.
		} catch (Throwable t) {
			// In test environments Strategy or mapGenerators may be mocked/absent.
			this.listState = new ArrayList<State>();
		}
//		this.selectionType = SelectionType.Truncation;
//		this.crossoverType = CrossoverType.UniformCrossover;
//		this.mutationType = MutationType.UniformMutation;
//		this.replaceType = ReplaceType.Smallest;
		this.generatorType = GeneratorType.GENETIC_ALGORITHM;
		this.weight = 50;
		listTrace[0] = this.weight;
		betterCountByPeriod[0] = 0;
		usageCountByPeriod[0] = 0;
	}
    
	/**
	 * @brief Genera un nuevo estado mediante selección, cruzamiento y mutación
	 * @param operatornumber Número de operador a utilizar
	 * @return Estado candidato generado por operadores genéticos
	 * @throws IllegalArgumentException Si los argumentos son inválidos
	 * @throws SecurityException Si hay problemas de seguridad
	 * @throws ClassNotFoundException Si no se encuentra una clase
	 * @throws InstantiationException Si hay error en la instanciación
	 * @throws IllegalAccessException Si hay acceso ilegal
	 * @throws InvocationTargetException Si hay error en la invocación
	 * @throws NoSuchMethodException Si no se encuentra un método
	 */
	@Override
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    	
		//******************selection*******************************
		//ArrayList<State>list=new ArrayList<State>();
		List<State> refList = new ArrayList<State>(this.listState); 
    	iffatherselection = new FactoryFatherSelection();
    	FatherSelection selection = iffatherselection.createSelectFather(selectionType);
    	List<State> fathers = selection.selection(refList, truncation);
    	int pos1 = (int)(Math.random() * fathers.size());
    	int pos2 = (int)(Math.random() * fathers.size());
    	
		// Safely obtain a base state to copy from. Tests may not set Problem.state, so
		// fall back to a generator-local state list or RandomSearch reference when needed.
		State baseState1 = null;
		try {
			baseState1 = Strategy.getStrategy().getProblem().getState();
		} catch (Exception e) {
			baseState1 = null;
		}
		if (baseState1 == null) {
			if (this.listState != null && !this.listState.isEmpty()) baseState1 = this.listState.get(0);
			else if (RandomSearch.listStateReference != null && !RandomSearch.listStateReference.isEmpty()) baseState1 = RandomSearch.listStateReference.get(0);
			else baseState1 = new State();
		}
		State auxState1 = (State) baseState1.getCopy();
		auxState1.setCode(new ArrayList<Object>(fathers.get(pos1).getCode()));
    	auxState1.setEvaluation(fathers.get(pos1).getEvaluation());
    	auxState1.setNumber(fathers.get(pos1).getNumber());
    	auxState1.setTypeGenerator(fathers.get(pos1).getTypeGenerator());
    	
		State baseState2 = null;
		try {
			baseState2 = Strategy.getStrategy().getProblem().getState();
		} catch (Exception e) {
			baseState2 = null;
		}
		if (baseState2 == null) {
			if (this.listState != null && !this.listState.isEmpty()) baseState2 = this.listState.get(0);
			else if (RandomSearch.listStateReference != null && !RandomSearch.listStateReference.isEmpty()) baseState2 = RandomSearch.listStateReference.get(0);
			else baseState2 = new State();
		}
		State auxState2 = (State) baseState2.getCopy();
		auxState2.setCode(new ArrayList<Object>(fathers.get(pos2).getCode()));
    	auxState2.setEvaluation(fathers.get(pos2).getEvaluation());
    	auxState2.setNumber(fathers.get(pos2).getNumber());
    	auxState2.setTypeGenerator(fathers.get(pos2).getTypeGenerator());
    	
	    //**********cruzamiento*************************************
	    iffactorycrossover = new FactoryCrossover();
    	Crossover crossover = iffactorycrossover.createCrossover(crossoverType);
    	auxState1 = crossover.crossover(auxState1, auxState2, PC);
	    
    	//**********mutacion******************************************** 	
    	iffactorymutation = new FactoryMutation();
    	Mutation mutation =iffactorymutation.createMutation(mutationType);
    	auxState1 = mutation.mutation(auxState1, PM);
    	//list.add(auxState1);
    	
    	return auxState1;
	}
    
	@Override
	public State getReference() {
		return stateReferenceGA;
	}

	public void setStateRef(State stateRef) {
		this.stateReferenceGA = stateRef;
	}
	@Override
	public void setInitialReference(State stateInitialRef) {
		this.stateReferenceGA = stateInitialRef;
	}

	@Override
	public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException,	NoSuchMethodException {
		iffreplace = new FactoryReplace();
		Replace replace = iffreplace.createReplace(replaceType);
		listState = replace.replace(stateCandidate, listState);
	}
	
	public List<State> getListState() {
		return listState;
	}


	public void setListState(List<State> listState) {
		this.listState = listState;
	}
	
	public List<State> getListStateRef(){
		Boolean found = false;
		List<String> key = Strategy.getStrategy().getListKey();
		int count = 0;
		//if(Strategy.getStrategy().Statistics.getAllStates().size() == 0){
		/*if(RandomSearch.listStateReference.size() == 0){
			return this.listState = new ArrayList<State>();
		}*/
		while((found.equals(false)) && (Strategy.getStrategy().mapGenerators.size() > count)){
			if(key.get(count).equals(GeneratorType.GENETIC_ALGORITHM.toString())){
				GeneratorType keyGenerator = GeneratorType.valueOf(String.valueOf(key.get(count)));
				GeneticAlgorithm generator = (GeneticAlgorithm) Strategy.getStrategy().mapGenerators.get(keyGenerator);
				if(generator.getListState().isEmpty()){
					listState.addAll(RandomSearch.listStateReference);
					//for (int j = 1; j < Strategy.getStrategy().Statistics.getAllStates().size(); j++) {
//					for (int j = 1; j < RandomSearch.listStateReference.size(); j++) {
						//if(Strategy.getStrategy().Statistics.getAllStates().get(j).getTypeGenerator().equals(GeneratorType.RandomSearch) && (listState.size()!= countRef)){
//						if(listState.size() != countRef){
							//listState.add(Strategy.getStrategy().Statistics.getAllStates().get(j));
//							listState.add(RandomSearch.listStateReference.get(j));
//						}
//					}  
				}
				else{
					listState = generator.getListState();
				}
			        found = true;
			}
			count++;
		}
		// Fallback: if still empty, try to populate from RandomSearch global list
		if ((listState == null || listState.isEmpty()) && RandomSearch.listStateReference != null && !RandomSearch.listStateReference.isEmpty()) {
			listState = new ArrayList<State>(RandomSearch.listStateReference);
		}
		return listState;
	}

	public GeneratorType getGeneratorType() {
		return generatorType;
	}

	public void setGeneratorType(GeneratorType generatorType) {
		this.generatorType = generatorType;
	}

	@Override
	public GeneratorType getType() {
		return this.generatorType;
	}

	@Override
	public List<State> getReferenceList() {
		List<State> ReferenceList = new ArrayList<State>();
		for (int i = 0; i < listState.size(); i++) {
			State value = listState.get(i);
			ReferenceList.add(value);
		}
		return ReferenceList;
	}

	@Override
	public List<State> getSonList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean awardUpdateREF(State stateCandidate) {
		boolean update = false;
		if (Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.MAXIMIZAR)) {
			if (stateCandidate.getEvaluation().get(0) > stateReferenceGA.getEvaluation().get(0)) {
				update = true;
				countBetterGender++;
			}
		} else {
			if (stateCandidate.getEvaluation().get(0) < stateReferenceGA.getEvaluation().get(0)) {
				update = true;
				countBetterGender++;
			}
		}
		return update;
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
		return new int[]{countGender, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	}

	@Override
	public float[] getTrace() {
		// TODO Auto-generated method stub
		return this.listTrace;
	}

}
