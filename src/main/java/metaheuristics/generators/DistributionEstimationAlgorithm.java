/**
 * @file DistributionEstimationAlgorithm.java
 * @brief Implementación del algoritmo de estimación de distribuciones (EDA) como metaheurística de optimización
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

import evolutionary_algorithms.complement.DistributionType;
import evolutionary_algorithms.complement.FatherSelection;
import evolutionary_algorithms.complement.Replace;
import evolutionary_algorithms.complement.ReplaceType;
import evolutionary_algorithms.complement.Sampling;
import evolutionary_algorithms.complement.SamplingType;
import evolutionary_algorithms.complement.SelectionType;
import factory_interface.IFFSampling;
import factory_interface.IFFactoryFatherSelection;
import factory_interface.IFFactoryReplace;
import factory_method.FactoryFatherSelection;
import factory_method.FactoryReplace;
import factory_method.FactorySampling;

/**
 * @class DistributionEstimationAlgorithm
 * @brief Algoritmo metaheurístico basado en estimación de distribuciones de probabilidad
 * 
 * Esta clase implementa el algoritmo EDA que genera nuevas soluciones mediante la estimación
 * de distribuciones de probabilidad a partir de un conjunto de soluciones prometedoras.
 */
public class DistributionEstimationAlgorithm extends Generator {

	/** @brief Estado de referencia del algoritmo de estimación de distribuciones */
	private State stateReferenceDA;
	
	/** @brief Lista de estados de referencia para la estimación de distribuciones */
	private List<State> referenceList = new ArrayList<State>(); 
	
	/** @brief Lista de estados hijos generados */
	public static List<State> sonList = new ArrayList<State>(); 
	
	/** @brief Factoría para la selección de padres */
	private IFFactoryFatherSelection iffatherselection;
	
	/** @brief Factoría para el muestreo de soluciones */
	private IFFSampling iffsampling;
	
	/** @brief Factoría para el reemplazo de soluciones */
	private IFFactoryReplace iffreplace;
	
	/** @brief Tipo de distribución utilizada */
	private DistributionType distributionType;
	
	/** @brief Tipo de muestreo utilizado */
	private SamplingType Samplingtype;
	
	/** @brief Tipo de reemplazo de soluciones */
	public static ReplaceType replaceType;
	
	/** @brief Tipo de selección de padres */
	public static SelectionType selectionType;
	
	/** @brief Tipo de generador metaheurístico */
	private GeneratorType generatorType;
	
	/** @brief Tamaño de truncamiento para la selección */
	public static int truncation;
	
	/** @brief Contador de referencias */
	public static int countRef = 0;
	
	/** @brief Peso del generador */
	private float weight;
	
	/** @brief Contador de mejoras por período para problemas dinámicos */
	private int[] betterCountByPeriod = new int[10];
	
	/** @brief Contador de uso por período para problemas dinámicos */
	private int[] usageCountByPeriod = new int[10];
	
	/** @brief Historial de trazas del peso */
	private float[] listTrace = new float[1200000];
	
	/**
	 * @brief Constructor por defecto del algoritmo de estimación de distribuciones
	 * 
	 * Inicializa el algoritmo con los parámetros por defecto, incluyendo tipo de distribución
	 * univariada, muestreo probabilístico y peso inicial de 50.
	 */
	public DistributionEstimationAlgorithm() {
		super();
		this.referenceList = getListStateRef(); // llamada al m�todo que devuelve la lista. 
//		this.selectionType = SelectionType.Truncation;
		//this.replaceType = ReplaceType.Generational;
//		this.replaceType = ReplaceType.Smallest;
		this.generatorType = GeneratorType.DISTRIBUTION_ESTIMATION_ALGORITHM;
		this.distributionType = DistributionType.UNIVARIATE;
		this.Samplingtype = SamplingType.PROBABILISTIC_SAMPLING;
		this.weight = 50;
		listTrace[0] = weight;
		betterCountByPeriod[0] = 0;
		usageCountByPeriod[0] = 0;
	}
	
	/**
	 * @brief Obtiene el estado con el valor máximo de evaluación
	 * @param listInd Lista de estados a evaluar
	 * @return Estado con la mejor evaluación
	 */
	public State MaxValue (List<State> listInd){
		State state = new State(listInd.get(0));
		double max = state.getEvaluation().get(0);
		for (int i = 1; i < listInd.size(); i++) {
			if(listInd.get(i).getEvaluation().get(0) > max){
				max = listInd.get(i).getEvaluation().get(0);
				state = new State(listInd.get(i));
			}
		}
		return state;
	}
	
	/**
	 * @brief Genera un nuevo estado candidato mediante estimación de distribuciones
	 * @param operatornumber Número de operador a utilizar
	 * @return Estado candidato generado
	 * @throws IllegalArgumentException Si los argumentos son inválidos
	 * @throws SecurityException Si hay problemas de seguridad
	 * @throws ClassNotFoundException Si no se encuentra una clase
	 * @throws InstantiationException Si hay error en la instanciación
	 * @throws IllegalAccessException Si hay acceso ilegal
	 * @throws InvocationTargetException Si hay error en la invocación
	 * @throws NoSuchMethodException Si no se encuentra un método
	 */
	@Override
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException,	NoSuchMethodException {
		//********************selection*****************************
		//ProblemState candidate = new ProblemState();
		/*State candidate = new State();//(State) Strategy.getStrategy().getProblem().getState().clone()
		List<State> fathers = new ArrayList<State>();
		fathers = getfathersList();
		//**************************distribution***********************************
		iffdistribution = new FactoryDistribution();
    	Distribution distribution = iffdistribution.createDistribution(distributionType);
    	List<Probability> probability = distribution.distribution(fathers);
		
    	//***************************muestreo*****************************************
    	iffsampling = new FactorySampling();
    	Sampling samplingG = iffsampling.createSampling(Samplingtype);
    	List<State> ind = samplingG.sampling(probability, referenceList.size());
    	sonList = ind;
    	candidate = MaxValue(ind);
       // this.candidate = candidate;
    	return candidate;*/	
    	
    	//***************************version 1.0
    	//ArrayList<State> listcandidate = new ArrayList<State>();//(State) Strategy.getStrategy().getProblem().getState().clone()
		
    	List<State> fathers = new ArrayList<State>();
		fathers = getfathersList();
		iffsampling = new FactorySampling();
    	Sampling samplingG = iffsampling.createSampling(Samplingtype);
    	List<State> ind = samplingG.sampling(fathers, operatornumber);
    	State candidate = null;
    	if(ind.size() > 1){
    		for (int i = 0; i < ind.size(); i++) {
    			double evaluation = Strategy.getStrategy().getProblem().getFunction().get(0).Evaluation(ind.get(i));
    			ArrayList<Double> listEval = new ArrayList<Double>();
    			listEval.add(evaluation);
    			ind.get(0).setEvaluation(listEval);
    		}
    		candidate = MaxValue(ind);
    	}
    	else{
    		candidate = ind.get(0);
    	}
//    	sonList = ind;
    	//listcandidate.add(candidate);
       // this.candidate = candidate;
    	return candidate;
    	
    }
    		
	/**
	 * @brief Obtiene el estado de referencia con mejor evaluación
	 * @return Estado de referencia óptimo de la lista
	 */
	@Override
	public State getReference() {
		stateReferenceDA = referenceList.get(0);
		if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.MAXIMIZAR)){
			for (int i = 1; i < referenceList.size(); i++) {
				if(stateReferenceDA.getEvaluation().get(0) < referenceList.get(i).getEvaluation().get(0))
					stateReferenceDA = referenceList.get(i);
			}
		}
		else{
			for (int i = 1; i < referenceList.size(); i++) {
				if(stateReferenceDA.getEvaluation().get(0) > referenceList.get(i).getEvaluation().get(0))
					stateReferenceDA = referenceList.get(i);
			}
		}
		return stateReferenceDA;
	}

	/**
	 * @brief Obtiene una copia de la lista de referencias
	 * @return Lista de estados de referencia
	 */
	@Override
	public List<State> getReferenceList() {
		List<State> ReferenceList = new ArrayList<State>();
		for (int i = 0; i < referenceList.size(); i++) {
			State value = referenceList.get(i);
			ReferenceList.add( value);
		}
		return ReferenceList;
	}

	/**
	 * @brief Obtiene el tipo de generador
	 * @return Tipo de generador DISTRIBUTION_ESTIMATION_ALGORITHM
	 */
	@Override
	public GeneratorType getType() {
		return this.generatorType;
	}

	/**
	 * @brief Establece el estado de referencia inicial
	 * @param stateInitialRef Estado de referencia inicial
	 */
	@Override
	public void setInitialReference(State stateInitialRef) {
		this.stateReferenceDA = stateInitialRef;
	}

	/**
	 * @brief Actualiza la lista de referencias con un nuevo candidato
	 * @param stateCandidate Estado candidato a evaluar
	 * @param countIterationsCurrent Iteración actual
	 * @throws IllegalArgumentException Si los argumentos son inválidos
	 * @throws SecurityException Si hay problemas de seguridad
	 * @throws ClassNotFoundException Si no se encuentra una clase
	 * @throws InstantiationException Si hay error en la instanciación
	 * @throws IllegalAccessException Si hay acceso ilegal
	 * @throws InvocationTargetException Si hay error en la invocación
	 * @throws NoSuchMethodException Si no se encuentra un método
	 */
	@Override
	public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException,	NoSuchMethodException {
		iffreplace = new FactoryReplace();
		Replace replace = iffreplace.createReplace(replaceType);
		referenceList = replace.replace(stateCandidate, referenceList);
	}
	
	/**
	 * @brief Obtiene la lista de estados de referencia desde la estrategia
	 * @return Lista de estados de referencia
	 */
	public List<State> getListStateRef(){
		Boolean found = false;
		List<String> key = Strategy.getStrategy().getListKey();
		int count = 0;
		//if(Strategy.getStrategy().Statistics.getAllStates().size() == 0){
		/*if(RandomSearch.listStateReference.size() == 0){
			return this.referenceList = new ArrayList<State>();
		}*/
		while((found.equals(false)) && (Strategy.getStrategy().mapGenerators.size() > count)){
			if(key.get(count).equals(GeneratorType.DISTRIBUTION_ESTIMATION_ALGORITHM.toString())){
				GeneratorType keyGenerator = GeneratorType.valueOf(String.valueOf(key.get(count)));
				DistributionEstimationAlgorithm generator = (DistributionEstimationAlgorithm)Strategy.getStrategy().mapGenerators.get(keyGenerator);
				if(generator.getListReference().isEmpty()){
					referenceList.addAll(RandomSearch.listStateReference);
					//for (int j = 1; j < Strategy.getStrategy().Statistics.getAllStates().size(); j++) {
//					for (int j = 1; j < RandomSearch.listStateReference.size(); j++) {
//						//if((Strategy.getStrategy().Statistics.getAllStates().get(j).getTypeGenerator().equals(GeneratorType.RandomSearch)) && (referenceList.size()!= countRef)){
//						if(referenceList.size() != countRef){
//							//State problemState = Strategy.getStrategy().Statistics.getAllStates().get(j);
//							referenceList.add(RandomSearch.listStateReference.get(j));
//						}
//					}  
				}
				else{
					referenceList = generator.getListReference();
				}
			    found = true;
			}
			count++;
		}
		return referenceList;
	}

	/**
	 * @brief Obtiene la lista de referencias
	 * @return Lista de estados de referencia
	 */
	public List<State> getListReference() {
		return referenceList;
	}

	/**
	 * @brief Establece la lista de referencias
	 * @param listReference Nueva lista de estados de referencia
	 */
	public void setListReference(List<State> listReference) {
		referenceList = listReference;
	}

	/**
	 * @brief Obtiene el tipo de generador
	 * @return Tipo de generador
	 */
	public GeneratorType getGeneratorType() {
		return generatorType;
	}

	/**
	 * @brief Establece el tipo de generador
	 * @param generatorType Nuevo tipo de generador
	 */
	public void setGeneratorType(GeneratorType generatorType) {
		this.generatorType = generatorType;
	}

	/**
	 * @brief Obtiene la lista de padres seleccionados
	 * @return Lista de estados padres seleccionados
	 * @throws IllegalArgumentException Si los argumentos son inválidos
	 * @throws SecurityException Si hay problemas de seguridad
	 * @throws ClassNotFoundException Si no se encuentra una clase
	 * @throws InstantiationException Si hay error en la instanciación
	 * @throws IllegalAccessException Si hay acceso ilegal
	 * @throws InvocationTargetException Si hay error en la invocación
	 * @throws NoSuchMethodException Si no se encuentra un método
	 */
	public List<State> getfathersList() throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<State> refList = new ArrayList<State>(this.referenceList); 
    	iffatherselection = new FactoryFatherSelection();
    	FatherSelection selection = iffatherselection.createSelectFather(selectionType);
    	List<State> fathers = selection.selection(refList, truncation);
    	return fathers;
	}

	@Override
	public List<State> getSonList() {
		return sonList;
	}

	/**
	 * @brief Verifica si el candidato está en la lista de referencias
	 * @param stateCandidate Estado candidato a verificar
	 * @return true si el candidato está en las referencias, false en caso contrario
	 */
	public boolean awardUpdateREF(State stateCandidate) {
		boolean find = false;
		int i = 0;
		while (find == false && i < this.referenceList.size()) {
			if(stateCandidate.equals(this.referenceList.get(i)))
				find = true;
			else i++;
		}
		return find;
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

	/**
	 * @brief Obtiene el tipo de distribución
	 * @return Tipo de distribución utilizada
	 */
	public DistributionType getDistributionType() {
		return distributionType;
	}

	/**
	 * @brief Establece el tipo de distribución
	 * @param distributionType Nuevo tipo de distribución
	 */
	public void setDistributionType(DistributionType distributionType) {
		this.distributionType = distributionType;
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