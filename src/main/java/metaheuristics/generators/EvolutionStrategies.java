/**
 * @file EvolutionStrategies.java
 * @brief Implementación del algoritmo de estrategias evolutivas como metaheurística de optimización
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */
package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import metaheurictics.strategy.Strategy;
import problem.definition.Problem.ProblemType;
import problem.definition.State;
import evolutionary_algorithms.complement.FatherSelection;
import evolutionary_algorithms.complement.Mutation;
import evolutionary_algorithms.complement.MutationType;
import evolutionary_algorithms.complement.Replace;
import evolutionary_algorithms.complement.ReplaceType;
import evolutionary_algorithms.complement.SelectionType;
import factory_interface.IFFactoryFatherSelection;
import factory_interface.IFFactoryMutation;
import factory_interface.IFFactoryReplace;
import factory_method.FactoryFatherSelection;
import factory_method.FactoryMutation;
import factory_method.FactoryReplace;

/**
 * @class EvolutionStrategies
 * @brief Algoritmo metaheurístico basado en estrategias evolutivas con mutación
 * 
 * Esta clase implementa estrategias evolutivas que seleccionan padres, aplican mutación
 * y reemplazan soluciones en la población basándose en criterios evolutivos.
 */
public class EvolutionStrategies extends Generator {
	
	/** @brief Estado de referencia del algoritmo de estrategias evolutivas */
	private State stateReferenceES;
	
	/** @brief Lista de estados de referencia */
	private List<State> listStateReference = new ArrayList<State>(); 
	
	/** @brief Factoría para la selección de padres */
	private IFFactoryFatherSelection iffatherselection;
	
	/** @brief Factoría para operadores de mutación */
	private IFFactoryMutation iffactorymutation;
	
	/** @brief Factoría para reemplazo de soluciones */
	private IFFactoryReplace iffreplace;
	
	/** @brief Tipo de generador metaheurístico */
	private GeneratorType generatorType;
	
	/** @brief Probabilidad de mutación */
	public static double PM;
	
	/** @brief Tipo de mutación utilizada */
	public static MutationType mutationType;
	
	/** @brief Tipo de reemplazo de soluciones */
	public static ReplaceType replaceType;
	
	/** @brief Tipo de selección de padres */
	public static SelectionType selectionType;
	
	/** @brief Contador de referencias */
	public static int countRef = 0;
	
	/** @brief Tamaño de truncamiento para selección */
	public static int truncation;
    
	// sensible defaults to avoid nulls during tests
	static {
		PM = 0.1;
		mutationType = MutationType.ONE_POINT_MUTATION;
		replaceType = ReplaceType.GENERATIONAL_REPLACE;
		selectionType = SelectionType.TRUNCATION_SELECTION;
		truncation = 1;
	}
	
	/** @brief Peso del generador */
	private float weight = 50;
	
	/** @brief Contador de mejoras por período para problemas dinámicos */
	private int[] betterCountByPeriod = new int[10];
	
	/** @brief Contador de uso por período para problemas dinámicos */
	private int[] usageCountByPeriod = new int[10];
	
	/** @brief Historial de trazas del peso */
	private float[] listTrace = new float[1200000];
	
	/**
	 * @brief Constructor por defecto de estrategias evolutivas
	 * 
	 * Inicializa el algoritmo con tipo de mutación uniforme, reemplazo de los peores
	 * y selección por truncamiento, con peso inicial de 50.
	 */
	
	/**
	 * @brief Constructor por defecto de estrategias evolutivas
	 * 
	 * Inicializa el algoritmo con tipo de mutación uniforme, reemplazo de los peores
	 * y selección por truncamiento, con peso inicial de 50.
	 */
	public EvolutionStrategies() {
		super();
		try {
			this.listStateReference = getListStateRef(); 
		} catch (Exception e) {
			// If strategy is not available or any issue occurs, fallback to empty list
			this.listStateReference = new ArrayList<State>();
		}
		this.generatorType = GeneratorType.EVOLUTION_STRATEGIES;
		this.weight = 50;
		listTrace[0] = this.weight;
		betterCountByPeriod[0] = 0;
		usageCountByPeriod[0] = 0;
	}

	/**
	 * @brief Genera un nuevo estado candidato mediante mutación
	 * @param operatornumber Número de operador a utilizar
	 * @return Estado candidato generado por mutación
	 * @throws IllegalArgumentException Si los argumentos son inválidos
	 * @throws SecurityException Si hay problemas de seguridad
	 * @throws ClassNotFoundException Si no se encuentra una clase
	 * @throws InstantiationException Si hay error en la instanciación
	 * @throws IllegalAccessException Si hay acceso ilegal
	 * @throws InvocationTargetException Si hay error en la invocación
	 * @throws NoSuchMethodException Si no se encuentra un método
	 */
	@Override
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException,	NoSuchMethodException {    	//ArrayList<State> list = new ArrayList<State>();
//		List<State> refList = new ArrayList<State>(this.listStateReference); 
    	// Ensure we have a usable reference list; fall back to RandomSearch if necessary
    	if (this.listStateReference == null || this.listStateReference.isEmpty()) {
    		if (RandomSearch.listStateReference != null && !RandomSearch.listStateReference.isEmpty()) {
    			this.listStateReference.addAll(RandomSearch.listStateReference);
    		}
    	}

    	iffatherselection = new FactoryFatherSelection();
    	FatherSelection selection = iffatherselection.createSelectFather(selectionType);
    	List<State> fathers = null;
    	try {
    		fathers = selection.selection(this.listStateReference, truncation);
    	} catch (Exception e) {
    		// If selection fails, try to use the available state reference lists
    		fathers = (this.listStateReference != null && !this.listStateReference.isEmpty()) ? this.listStateReference : RandomSearch.listStateReference;
    	}

    	if (fathers == null || fathers.isEmpty()) {
    		// No father available - try fallback to RandomSearch explicitly
    		if (RandomSearch.listStateReference != null && !RandomSearch.listStateReference.isEmpty()) {
    			fathers = RandomSearch.listStateReference;
    		}
    	}

    	if (fathers == null || fathers.isEmpty()) {
    		// Nothing we can do safely - return null to allow callers/tests to handle absence
    		return null;
    	}

    	int pos1 = (int)(Math.random() * fathers.size());
    	State father = fathers.get(pos1);
    	State candidate = null;
    	try {
    		// Prefer copying the father state when available
    		if (father != null) {
    			candidate = (State) father.getCopy();
    		}
    	} catch (Exception ex) {
    		candidate = null;
    	}

    	// If cannot copy father, try Strategy.problem.state copy, or use first available reference
    	if (candidate == null) {
    		try {
    			if (Strategy.getStrategy() != null && Strategy.getStrategy().getProblem() != null && Strategy.getStrategy().getProblem().getState() != null) {
    				candidate = (State) Strategy.getStrategy().getProblem().getState().getCopy();
    			}
    		} catch (Exception ex) {
    			candidate = null;
    		}
    	}
    	if (candidate == null) {
    		if (this.listStateReference != null && !this.listStateReference.isEmpty()) {
    			candidate = this.listStateReference.get(0).getCopy();
    		} else if (RandomSearch.listStateReference != null && !RandomSearch.listStateReference.isEmpty()) {
    			candidate = RandomSearch.listStateReference.get(0).getCopy();
    		} else {
    			return null;
    		}
    	}

    	// Ensure the candidate reflects the selected father's code/evaluation/number/type
    	if (father != null) {
    		try {
    			candidate.setCode(new ArrayList<Object>(father.getCode()));
    			candidate.setEvaluation(father.getEvaluation());
    			candidate.setNumber(father.getNumber());
    			candidate.setTypeGenerator(father.getTypeGenerator());
    		} catch (Exception ignore) {}
    	}

    	//**********mutacion******************************************** 
    	iffactorymutation = new FactoryMutation();
    	Mutation mutation = iffactorymutation.createMutation(mutationType);
    	candidate = mutation.mutation(candidate, PM);
    	return candidate;
	}

	/**
	 * @brief Obtiene el estado de referencia con mejor evaluación
	 * @return Estado de referencia óptimo
	 */
	@Override
	public State getReference() {
		stateReferenceES = listStateReference.get(0);
		if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.MAXIMIZAR)){
			for (int i = 1; i < listStateReference.size(); i++) {
				if(stateReferenceES.getEvaluation().get(0) < listStateReference.get(i).getEvaluation().get(0))
					stateReferenceES = listStateReference.get(i);
			}
		}
		else{
			for (int i = 1; i < listStateReference.size(); i++) {
				if(stateReferenceES.getEvaluation().get(0) > listStateReference.get(i).getEvaluation().get(0))
					stateReferenceES = listStateReference.get(i);
			}
		}
		return stateReferenceES;
	}
	
	public void setStateRef(State stateRef) {
		this.stateReferenceES = stateRef;
	}

	@Override
	public GeneratorType getType() {
		return this.generatorType;
	}

	@Override
	public void setInitialReference(State stateInitialRef) {
		this.stateReferenceES = stateInitialRef;
	}

	@Override
	public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		iffreplace = new FactoryReplace();
		Replace replace = iffreplace.createReplace(replaceType);
		listStateReference = replace.replace(stateCandidate, listStateReference);

	}
	
	public List<State> getListStateRef(){
		Boolean found = false;
		// If Strategy singleton is not available, fallback to RandomSearch global list
		if (Strategy.getStrategy() == null || Strategy.getStrategy().getListKey() == null || Strategy.getStrategy().mapGenerators == null) {
			if (RandomSearch.listStateReference != null && !RandomSearch.listStateReference.isEmpty()) {
				listStateReference.addAll(RandomSearch.listStateReference);
			}
			return listStateReference;
		}
		List<String> key = Strategy.getStrategy().getListKey();
		int count = 0;
		//if(Strategy.getStrategy().Statistics.getAllStates().size() == 0){
		/*if(RandomSearch.listStateReference.size() == 0){
			return this.listStateReference = new ArrayList<State>();
		}*/
		while((found.equals(false)) && (Strategy.getStrategy().mapGenerators.size() > count)){
			if(key.get(count).equals(GeneratorType.EVOLUTION_STRATEGIES.toString())){
				GeneratorType keyGenerator = GeneratorType.valueOf(String.valueOf(key.get(count)));
				EvolutionStrategies generator = (EvolutionStrategies) Strategy.getStrategy().mapGenerators.get(keyGenerator);
				if(generator.getListStateReference().isEmpty()){
					listStateReference.addAll(RandomSearch.listStateReference);
					//for (int j = 1; j < Strategy.getStrategy().Statistics.getAllStates().size(); j++) {
//					for (int j = 1; j < RandomSearch.listStateReference.size(); j++) {
//						if(listStateReference.size() != countRef){
							//State problemState = Strategy.getStrategy().Statistics.getAllStates().get(j);
//							listStateReference.add(RandomSearch.listStateReference.get(j));
//						}
//					}  
				}
				else{
					listStateReference = generator.getListStateReference();
				}
			        found = true;
			}
			count++;
		}
		return listStateReference;
	}

	public List<State> getListStateReference() {
		return listStateReference;
	}

	public void setListStateReference(List<State> listStateReference) {
		this.listStateReference = listStateReference;
	}

	public GeneratorType getTypeGenerator() {
		return generatorType;
	}

	public void setTypeGenerator(GeneratorType generatorType) {
		this.generatorType = generatorType;
	}

	@Override
	public List<State> getReferenceList() {
		List<State> ReferenceList = new ArrayList<State>();
		for (int i = 0; i < listStateReference.size(); i++) {
			State value = listStateReference.get(i);
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
