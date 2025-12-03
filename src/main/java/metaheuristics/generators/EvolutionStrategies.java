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
		this.listStateReference = getListStateRef(); 
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
    	iffatherselection = new FactoryFatherSelection();
    	FatherSelection selection = iffatherselection.createSelectFather(selectionType);
    	List<State> fathers = selection.selection(this.listStateReference, truncation);
    	int pos1 = (int)(Math.random() * fathers.size());
    	State candidate = (State) Strategy.getStrategy().getProblem().getState().getCopy();
    	candidate.setCode(new ArrayList<Object>(fathers.get(pos1).getCode()));
    	candidate.setEvaluation(fathers.get(pos1).getEvaluation());
    	candidate.setNumber(fathers.get(pos1).getNumber());
    	candidate.setTypeGenerator(fathers.get(pos1).getTypeGenerator());
    	
    	//**********mutacion******************************************** 	
    	iffactorymutation = new FactoryMutation();
    	Mutation mutation = iffactorymutation.createMutation(mutationType);
    	candidate = mutation.mutation(candidate, PM);
    	//list.add(candidate);    	
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
