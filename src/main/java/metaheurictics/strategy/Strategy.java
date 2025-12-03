/**
 * @file Strategy.java
 * @brief Clase central que coordina la ejecución de generadores metaheurísticos
 * 
 * Esta clase implementa el patrón Singleton y actúa como coordinador principal
 * del sistema de optimización metaheurística. Gestiona la ejecución de diferentes
 * generadores, mantiene el estado global, controla las iteraciones y recopila
 * métricas de rendimiento durante la búsqueda de soluciones.
 * 
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package metaheurictics.strategy;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import factory_interface.IFFactoryGenerator;
import factory_method.FactoryGenerator;

import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

import local_search.acceptation_type.Dominance;
import local_search.complement.StopExecute;
import local_search.complement.UpdateParameter;
import metaheuristics.generators.DistributionEstimationAlgorithm;
import metaheuristics.generators.EvolutionStrategies;
import metaheuristics.generators.Generator;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.GeneticAlgorithm;
import metaheuristics.generators.MultiGenerator;
import metaheuristics.generators.ParticleSwarmOptimization;
import metaheuristics.generators.RandomSearch;

/**
 * @class Strategy
 * @brief Coordinador central para la ejecución de algoritmos metaheurísticos
 * 
 * Esta clase singleton gestiona la estrategia global de optimización, coordinando
 * la ejecución de diferentes generadores metaheurísticos, manteniendo el seguimiento
 * del mejor estado encontrado, gestionando las iteraciones y recopilando estadísticas
 * de rendimiento. Soporta múltiples modos de operación incluyendo algoritmos de un
 * solo generador y portafolios de múltiples generadores (MultiGenerator).
 * 
 * La clase también gestiona entornos dinámicos donde el problema puede cambiar
 * durante la ejecución, permitiendo adaptación y reinicio de referencias.
 */
public class Strategy {

	/** @brief Instancia única del Singleton Strategy */
	private static Strategy strategy = null;
	
	/** @brief Mejor estado encontrado durante la ejecución del algoritmo */
	private State bestState;
	
	/** @brief Problema de optimización que se está resolviendo */
	private Problem problem;
	
	/** @brief Mapa que asocia tipos de generadores con sus instancias */
	public SortedMap<GeneratorType, Generator> mapGenerators;
	
	/** @brief Criterio de parada para controlar la finalización de la ejecución */
	private StopExecute stopexecute;
	
	/** @brief Gestor de actualización de parámetros durante la ejecución */
	private UpdateParameter updateparameter;
	
	/** @brief Factoría para crear instancias de generadores */
	private IFFactoryGenerator ifFactoryGenerator;
	
	/** @brief Contador de iteraciones actuales */
	private int countCurrent;
	
	/** @brief Número máximo de iteraciones permitidas */
	private int countMax;
	
	/** @brief Generador activo actual */
	public Generator generator;
	
	/** @brief Umbral de tolerancia para comparaciones */
	public double threshold;

	/** @brief Lista de todos los estados generados en cada iteración */
	public ArrayList<State> listStates;
	
	/** @brief Lista de las mejores soluciones encontradas en cada iteración */
	public ArrayList<State> listBest;
	
	/** @brief Lista de soluciones no dominadas (frente de Pareto) */
	public List<State> listRefPoblacFinal = new ArrayList<State> ();
	
	/** @brief Gestor de dominancia para calcular soluciones no dominadas */
	public Dominance notDominated; 

	/** @brief Indica si se debe guardar la lista de estados generados */
	public boolean saveListStates;
	
	/** @brief Indica si se debe guardar la lista de mejores estados en cada iteración */
	public boolean saveListBestStates;
	
	/** @brief Indica si se debe guardar el frente de Pareto en problemas monoobjetivo */
	public boolean saveFreneParetoMonoObjetivo;
	
	/** @brief Indica si se debe calcular el tiempo de ejecución del algoritmo */
	public boolean calculateTime;
	
	/** @brief Tiempo inicial de ejecución en milisegundos */
	long initialTime;
	
	/** @brief Tiempo final de ejecución en milisegundos */
	long finalTime;
	
	/** @brief Tiempo total de ejecución del algoritmo en milisegundos */
	public static long timeExecute;
	
	/** @brief Array para almacenar la métrica de rendimiento offline (Offline Performance) */
	public float[] listOfflineError = new float[100];
	
	/** @brief Cantidad de iteraciones antes de que ocurra un cambio en el entorno dinámico */
	public int countPeriodChange = 0;
	
	/** @brief Contador para el próximo cambio de entorno */
	public int countChange = 0;
	
	/** @brief Contador para determinar cuándo guardar estadísticas de generadores */
	private int countPeriodo;
	
	/** @brief Contador para controlar el periodo actual de guardado */
	private int periodo;

	/**
	 * @brief Constructor privado para implementar el patrón Singleton
	 * 
	 * Inicializa la instancia de Strategy. Este constructor es privado para
	 * prevenir la instanciación directa desde fuera de la clase.
	 */
	private Strategy(){
		super();
	}

	/**
	 * @brief Obtiene la instancia única de Strategy (patrón Singleton)
	 * 
	 * Implementación lazy del patrón Singleton. Crea la instancia única
	 * si no existe, o devuelve la existente.
	 * 
	 * @return La instancia única de Strategy
	 */
	public static Strategy getStrategy() {
		if (strategy == null) {
			strategy = new Strategy();
		}
		return strategy;
	}

	/**
	 * @brief Ejecuta la estrategia de optimización metaheurística
	 * 
	 * Método principal que coordina la ejecución completa del algoritmo metaheurístico.
	 * Inicializa el estado, ejecuta el ciclo de optimización, gestiona cambios dinámicos
	 * del entorno y recopila métricas de rendimiento.
	 * 
	 * @param countmaxIterations Número máximo de iteraciones a ejecutar
	 * @param countIterationsChange Número de iteraciones entre cambios de entorno
	 * @param operatornumber Número de operadores a utilizar en la generación
	 * @param generatorType Tipo de generador metaheurístico a utilizar
	 * @throws IllegalArgumentException Si los argumentos no son válidos
	 * @throws SecurityException Si hay problemas de seguridad en la reflexión
	 * @throws ClassNotFoundException Si no se encuentra la clase del generador
	 * @throws InstantiationException Si no se puede instanciar el generador
	 * @throws IllegalAccessException Si hay problemas de acceso en la reflexión
	 * @throws InvocationTargetException Si hay errores al invocar métodos
	 * @throws NoSuchMethodException Si no se encuentra el método requerido
	 */
	public void executeStrategy (int countmaxIterations, int countIterationsChange, int operatornumber, GeneratorType generatorType) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		// si se quiere calcular el tiempo de ejecucion del un algoritmo
		if(calculateTime == true){
			initialTime = System.currentTimeMillis();
		}
		this.countMax = countmaxIterations; // max cantidad de iteraciones
		//generar estado inicial de la estrategia
		Generator randomInitial = new RandomSearch();
		State initialState = randomInitial.generate(operatornumber);
		problem.Evaluate(initialState); //evaluar ese estado
		initialState.setTypeGenerator(generatorType);
		getProblem().setState(initialState);
		//si se va a salvar la lista de estados generados, adicionar el estado
		if(saveListStates ==  true){
			listStates = new ArrayList<State>(); //list de estados generados
			listStates.add(initialState);
		}
		//si se va a salvar la lista de mejores soluciones encontradas en cada iteracion
		if(saveListBestStates == true){
			listBest = new ArrayList<State>(); // list de mejores estados encontrados
			listBest.add(initialState);
		}
		if(saveFreneParetoMonoObjetivo == true){
			notDominated = new Dominance();
		}
		// crear el generador a ejecutar
		generator = newGenerator(generatorType);
		generator.setInitialReference(initialState);
		bestState = initialState;
		countCurrent = 0;
		listRefPoblacFinal = new ArrayList<State>();
		MultiGenerator multiGenerator = null;
		countPeriodChange = countIterationsChange;
		countChange = countIterationsChange;
		countPeriodo = countIterationsChange / 10; //cantidad de iteraciones de un periodo 
		//verificar que es portafolio e inicializar los generadores del portafolio
		if(generatorType.equals(GeneratorType.MULTI_GENERATOR)){
			initializeGenerators();
			MultiGenerator.initializeGenerators();
			MultiGenerator.listGeneratedPP.clear();
			multiGenerator = ((MultiGenerator)generator).copy();
		}
		else initialize(); //crea el mapa de generadores
		update(countCurrent);
		
		float sumMax = 0; // suma acumulativa para almacenar la evaluacion de la mejor solucion encotrada y calcular el OfflinePerformance
		int countOff = 0; // variable par contar los OfflinePerformance que se van salvando en el arreglo
		//ciclio de ejecuci�n del algoritmo
		while (!stopexecute.stopIterations(countCurrent, countmaxIterations)){
			//si se detecta un cambio
			if(countCurrent == countChange){
				//calcular offlinePerformance
				calculateOffLinePerformance(sumMax, countOff);
				countOff++;
				sumMax = 0;
//				countIterationsChange = countIterationsChange + countPeriodChange; // actualizar la cantidad de iteraciones
				//actualizar la referencia luego de un cambio
				updateRef(generatorType);
				countChange = countChange + countPeriodChange;
				//generar un nuevo candidato en la iteracion, dependiendo del generador
				State stateCandidate = null;
				if(generatorType.equals(GeneratorType.MULTI_GENERATOR)){
					if(countPeriodo == countCurrent){
						updateCountGender();
						countPeriodo = countPeriodo + countPeriodChange / 10;
						periodo = 0;
						MultiGenerator.activeGenerator.countBetterGender = 0;
					}
					updateWeight();//actualizar el peso de los generadores si se reinician cuando ocurre un cambio
					//generar el estado candidato de la iteración
					stateCandidate = multiGenerator.generate(operatornumber);
					problem.Evaluate(stateCandidate);
					stateCandidate.setEvaluation(stateCandidate.getEvaluation());
					stateCandidate.setNumber(countCurrent);
					stateCandidate.setTypeGenerator(generatorType);
					multiGenerator.updateReference(stateCandidate, countCurrent);
				}
				else {
					stateCandidate = generator.generate(operatornumber);
					problem.Evaluate(stateCandidate);
					stateCandidate.setEvaluation(stateCandidate.getEvaluation());
					stateCandidate.setNumber(countCurrent);
					stateCandidate.setTypeGenerator(generatorType);
					generator.updateReference(stateCandidate, countCurrent);
					if(saveListStates ==  true){
						listStates.add(stateCandidate);
					}
//					listStates.add(stateCandidate);
				}
				//actualizar el mejor estado encontrado solo tiene sentido para algoritmos monoobjetivos
				
				//actualizar el mejor estado encontrado solo tiene sentido para algoritmos monoobjetivos
				if ((getProblem().getTypeProblem().equals(ProblemType.Maximizar)) && bestState.getEvaluation().get(bestState.getEvaluation().size() - 1) < stateCandidate.getEvaluation().get(bestState.getEvaluation().size() - 1)) {
					bestState = stateCandidate;
				}
				if ((problem.getTypeProblem().equals(ProblemType.Minimizar)) && bestState.getEvaluation().get(bestState.getEvaluation().size() - 1) > stateCandidate.getEvaluation().get(bestState.getEvaluation().size() - 1)) {
					bestState = stateCandidate;
				}
				//					System.out.println("Evaluacion: "+ bestState.getEvaluation());
				if(saveListBestStates == true){
					listBest.add(bestState);
				}
				sumMax = (float) (sumMax + bestState.getEvaluation().get(0));

			}
			// no ha ocurrido un cambio
			else {
				State stateCandidate = null;
				if(generatorType.equals(GeneratorType.MULTI_GENERATOR)){
					if(countPeriodo == countCurrent){
						updateCountGender();
						countPeriodo = countPeriodo + countPeriodChange / 10;
						periodo++;
						MultiGenerator.activeGenerator.countBetterGender = 0;
					}
					stateCandidate = multiGenerator.generate(operatornumber);
					problem.Evaluate(stateCandidate);
					stateCandidate.setEvaluation(stateCandidate.getEvaluation());
					stateCandidate.setNumber(countCurrent);
					stateCandidate.setTypeGenerator(generatorType);
					multiGenerator.updateReference(stateCandidate, countCurrent);
				}
				else {
					//generar estado candidato y evaluar si es aceptado o no 
					stateCandidate = generator.generate(operatornumber);
					problem.Evaluate(stateCandidate);
					stateCandidate.setEvaluation(stateCandidate.getEvaluation());
					stateCandidate.setNumber(countCurrent);
					stateCandidate.setTypeGenerator(generatorType);
					generator.updateReference(stateCandidate, countCurrent); // actualizar la referencia del estado
					if(saveListStates ==  true){
						listStates.add(stateCandidate);
					}
					if(saveFreneParetoMonoObjetivo == true){
						notDominated.ListDominance(stateCandidate, listRefPoblacFinal);
					}
				}
				countCurrent = UpdateParameter.updateParameter(countCurrent);
				//actualizar el mejor estado encontrado solo tiene sentido para algoritmos monoobjetivos
				if ((getProblem().getTypeProblem().equals(ProblemType.Maximizar)) && bestState.getEvaluation().get(bestState.getEvaluation().size() - 1) < stateCandidate.getEvaluation().get(bestState.getEvaluation().size() - 1)) {
					bestState = stateCandidate;
				}
				if ((problem.getTypeProblem().equals(ProblemType.Minimizar)) && bestState.getEvaluation().get(bestState.getEvaluation().size() - 1) > stateCandidate.getEvaluation().get(bestState.getEvaluation().size() - 1)) {
					bestState = stateCandidate;
				}
				//					System.out.println("Evaluacion: "+ bestState.getEvaluation());
				if(saveListBestStates == true){
					listBest.add(bestState);
				}
				sumMax = (float) (sumMax + bestState.getEvaluation().get(0));
			}
//			System.out.println("Iteracion: " + countCurrent);
		}
		//calcular tiempo final
		if(calculateTime == true){
			finalTime = System.currentTimeMillis();
			timeExecute = finalTime - initialTime;
			System.out.println("El tiempo de ejecucion: " + timeExecute);
		}
		if(generatorType.equals(GeneratorType.MULTI_GENERATOR)){
			listBest = (ArrayList<State>) multiGenerator.getReferenceList();
			//calcular offlinePerformance
			calculateOffLinePerformance(sumMax, countOff);
			if(countPeriodo == countCurrent){
				updateCountGender();
			}
		}
		else{
			listBest = (ArrayList<State>) generator.getReferenceList();
			calculateOffLinePerformance(sumMax, countOff);
		} 
	}
	
	/**
	 * @brief Actualiza los contadores de uso y mejoras de cada generador
	 * 
	 * Actualiza las estadísticas de cuántas veces se usó cada generador y cuántas
	 * mejoras produjo durante un periodo determinado. Reinicia los contadores para
	 * el siguiente periodo.
	 */
	public void updateCountGender(){
		for (int i = 0; i < MultiGenerator.getListGenerators().length; i++) {
			if(!MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.MULTI_GENERATOR) ){/*&& !MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.MULTI_CASE_SIMULATED_ANNEALING) &&
				!MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.MULTIOBJECTIVE_HILL_CLIMBING_DISTANCE) && !MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.MULTIOBJECTIVE_HILL_CLIMBING_RESTART) &&
				!MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.MULTIOBJECTIVE_STOCHASTIC_HILL_CLIMBING) && !MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.MULTIOBJECTIVE_TABU_SEARCH) && 
				!MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.PARTICLE_SWARM_OPTIMIZATION)*/
				MultiGenerator.getListGenerators()[i].getListCountGender()[periodo] = MultiGenerator.getListGenerators()[i].countGender + MultiGenerator.getListGenerators()[i].getListCountGender()[periodo];
				MultiGenerator.getListGenerators()[i].getListCountBetterGender()[periodo] = MultiGenerator.getListGenerators()[i].countBetterGender + MultiGenerator.getListGenerators()[i].getListCountBetterGender()[periodo];
				MultiGenerator.getListGenerators()[i].countGender = 0;
				MultiGenerator.getListGenerators()[i].countBetterGender = 0;
			}
		}
	}
	
	/**
	 * @brief Reinicia los pesos de los generadores en el MultiGenerator
	 * 
	 * Establece el peso de todos los generadores a un valor predeterminado (50.0)
	 * después de un cambio en el entorno dinámico.
	 */
	public void updateWeight(){
			for (int i = 0; i < MultiGenerator.getListGenerators().length; i++) {
				if(!MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.MULTI_GENERATOR) ){
				MultiGenerator.getListGenerators()[i].setWeight((float) 50.0);
			}
		}
	}
	
	/**
	 * @brief Actualiza el generador activo basándose en la iteración actual
	 * 
	 * Cambia dinámicamente el tipo de generador cuando se alcanza un umbral
	 * específico de iteraciones, permitiendo transiciones entre diferentes
	 * algoritmos metaheurísticos durante la ejecución.
	 * 
	 * @param countIterationsCurrent Número de iteración actual
	 * @throws IllegalArgumentException Si los argumentos no son válidos
	 * @throws SecurityException Si hay problemas de seguridad en la reflexión
	 * @throws ClassNotFoundException Si no se encuentra la clase del generador
	 * @throws InstantiationException Si no se puede instanciar el generador
	 * @throws IllegalAccessException Si hay problemas de acceso en la reflexión
	 * @throws InvocationTargetException Si hay errores al invocar métodos
	 * @throws NoSuchMethodException Si no se encuentra el método requerido
	 */
	public void update(Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException { 
		//		Here update parameter for update and change generator.
		if(countIterationsCurrent.equals(GeneticAlgorithm.countRef - 1)){
			ifFactoryGenerator = new FactoryGenerator();
			Strategy.getStrategy().generator = ifFactoryGenerator.createGenerator(GeneratorType.GENETIC_ALGORITHM);
		}
		if(countIterationsCurrent.equals(EvolutionStrategies.countRef - 1)){
			ifFactoryGenerator = new FactoryGenerator();
			Strategy.getStrategy().generator = ifFactoryGenerator.createGenerator(GeneratorType.EVOLUTION_STRATEGIES);
		}			
		if(countIterationsCurrent.equals(DistributionEstimationAlgorithm.countRef - 1)){
			ifFactoryGenerator = new FactoryGenerator();
			Strategy.getStrategy().generator = ifFactoryGenerator.createGenerator(GeneratorType.DISTRIBUTION_ESTIMATION_ALGORITHM);
		}
		if(countIterationsCurrent.equals(ParticleSwarmOptimization.countRef - 1)){
			ifFactoryGenerator = new FactoryGenerator();
			Strategy.getStrategy().generator = ifFactoryGenerator.createGenerator(GeneratorType.PARTICLE_SWARM_OPTIMIZATION);
		}
	}

	/**
	 * @brief Crea una nueva instancia de generador del tipo especificado
	 * 
	 * Utiliza el patrón Factory para crear una instancia del generador
	 * metaheurístico solicitado.
	 * 
	 * @param Generatortype Tipo de generador a crear
	 * @return Nueva instancia del generador solicitado
	 * @throws IllegalArgumentException Si el tipo de generador no es válido
	 * @throws SecurityException Si hay problemas de seguridad en la reflexión
	 * @throws ClassNotFoundException Si no se encuentra la clase del generador
	 * @throws InstantiationException Si no se puede instanciar el generador
	 * @throws IllegalAccessException Si hay problemas de acceso en la reflexión
	 * @throws InvocationTargetException Si hay errores al invocar métodos
	 * @throws NoSuchMethodException Si no se encuentra el método requerido
	 */
	public Generator newGenerator(GeneratorType Generatortype) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		ifFactoryGenerator = new FactoryGenerator();
		Generator generator = ifFactoryGenerator.createGenerator(Generatortype);
		return generator;
	}

	/**
	 * @brief Obtiene el mejor estado encontrado
	 * @return El mejor estado encontrado durante la ejecución
	 */
	public State getBestState() {
		return bestState;
	}

	/**
	 * @brief Establece el mejor estado
	 * @param besState Nuevo mejor estado
	 */
	public void setBestState(State besState) {
		this.bestState = besState;
	}
	
	/**
	 * @brief Obtiene el criterio de parada
	 * @return Objeto StopExecute que controla la finalización
	 */
	public StopExecute getStopexecute() {
		return stopexecute;
	}

	/**
	 * @brief Obtiene el número máximo de iteraciones
	 * @return Número máximo de iteraciones configurado
	 */
	public int getCountMax() {
		return countMax;
	}

	/**
	 * @brief Establece el número máximo de iteraciones
	 * @param countMax Nuevo número máximo de iteraciones
	 */
	public void setCountMax(int countMax) {
		this.countMax = countMax;
	}

	/**
	 * @brief Establece el criterio de parada
	 * @param stopexecute Nuevo criterio de parada
	 */
	public void setStopexecute(StopExecute stopexecute) {
		this.stopexecute = stopexecute;
	}

	/**
	 * @brief Obtiene el gestor de actualización de parámetros
	 * @return Objeto UpdateParameter
	 */
	public UpdateParameter getUpdateparameter() {
		return updateparameter;
	}

	/**
	 * @brief Establece el gestor de actualización de parámetros
	 * @param updateparameter Nuevo gestor de actualización
	 */
	public void setUpdateparameter(UpdateParameter updateparameter) {
		this.updateparameter = updateparameter;
	}

	/**
	 * @brief Obtiene el problema de optimización
	 * @return El problema actual
	 */
	public Problem getProblem() {
		return problem;
	}

	/**
	 * @brief Establece el problema de optimización
	 * @param problem Nuevo problema a resolver
	 */
	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	/**
	 * @brief Obtiene la lista de claves de tipos de generadores disponibles
	 * 
	 * Extrae y formatea las claves del mapa de generadores en una lista de cadenas.
	 * 
	 * @return Lista de nombres de tipos de generadores
	 */
	public ArrayList<String> getListKey(){
		ArrayList<String> listKeys = new ArrayList<String>();
		String key = mapGenerators.keySet().toString();
		String returnString = key.substring(1, key.length() - 1);
		returnString = returnString + ", ";
		int countKey = mapGenerators.size();
		for (int i = 0; i < countKey; i++) {
			String r = returnString.substring(0, returnString.indexOf(','));
			returnString = returnString.substring(returnString.indexOf(',') + 2);
			listKeys.add(r);
		}
		return listKeys;
	}

	/**
	 * @brief Inicializa todos los tipos de generadores disponibles para MultiGenerator
	 * 
	 * Crea instancias de todos los tipos de generadores definidos en GeneratorType
	 * y los almacena en el mapa de generadores. Utilizado específicamente para
	 * configurar portafolios de generadores.
	 * 
	 * @throws IllegalArgumentException Si los argumentos no son válidos
	 * @throws SecurityException Si hay problemas de seguridad en la reflexión
	 * @throws ClassNotFoundException Si no se encuentra la clase del generador
	 * @throws InstantiationException Si no se puede instanciar el generador
	 * @throws IllegalAccessException Si hay problemas de acceso en la reflexión
	 * @throws InvocationTargetException Si hay errores al invocar métodos
	 * @throws NoSuchMethodException Si no se encuentra el método requerido
	 */
	public void initializeGenerators()throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<GeneratorType>	listType = new ArrayList<GeneratorType>();
		this.mapGenerators = new TreeMap<GeneratorType, Generator>();
		GeneratorType type[]= GeneratorType.values();
		for (GeneratorType generator : type) {
			listType.add(generator);
		}
		for (int i = 0; i < listType.size(); i++) {
			Generator generator = newGenerator(listType.get(i));
			mapGenerators.put(listType.get(i), generator);
			//ExecuteGeneratorParall.getExecuteGeneratorParall().listGenerators.add(generator);
//			MultiGenerator.getListGenerators()[i] = generator;
		}
	}

	/**
	 * @brief Inicializa el mapa de generadores
	 * 
	 * Crea instancias de todos los tipos de generadores y los almacena en el mapa.
	 * Similar a initializeGenerators pero para uso general.
	 * 
	 * @throws IllegalArgumentException Si los argumentos no son válidos
	 * @throws SecurityException Si hay problemas de seguridad en la reflexión
	 * @throws ClassNotFoundException Si no se encuentra la clase del generador
	 * @throws InstantiationException Si no se puede instanciar el generador
	 * @throws IllegalAccessException Si hay problemas de acceso en la reflexión
	 * @throws InvocationTargetException Si hay errores al invocar métodos
	 * @throws NoSuchMethodException Si no se encuentra el método requerido
	 */
	public void initialize()throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<GeneratorType>	listType = new ArrayList<GeneratorType>();
		this.mapGenerators = new TreeMap<GeneratorType, Generator>();
		GeneratorType type[]= GeneratorType.values();
		for (GeneratorType generator : type) {
			listType.add(generator);
		}
		for (int i = 0; i < listType.size(); i++) {
			Generator generator = newGenerator(listType.get(i));
			mapGenerators.put(listType.get(i), generator);
		}
	}

	/**
	 * @brief Obtiene el contador de iteraciones actual
	 * @return Número de iteración actual
	 */
	public int getCountCurrent() {
		return countCurrent;
	}

	/**
	 * @brief Establece el contador de iteraciones actual
	 * @param countCurrent Nuevo valor del contador de iteraciones
	 */
	public void setCountCurrent(int countCurrent) {
		this.countCurrent = countCurrent;
	}

	/**
	 * @brief Destruye la instancia del Singleton y limpia recursos
	 * 
	 * Libera la instancia de Strategy y limpia las referencias de estado
	 * para permitir una nueva ejecución limpia.
	 */
	public static void destroyExecute() {
		strategy = null;
		RandomSearch.listStateReference = null;
	}
	
	/**
	 * @brief Obtiene el umbral de tolerancia
	 * @return Valor del umbral
	 */
	public double getThreshold() {
		return threshold;
	}

	/**
	 * @brief Establece el umbral de tolerancia
	 * @param threshold Nuevo valor del umbral
	 */
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
	
	/**
	 * @brief Calcula y almacena la métrica Offline Performance
	 * 
	 * Calcula el rendimiento offline dividiendo la suma acumulada de evaluaciones
	 * por el número de iteraciones del periodo y lo almacena en el array.
	 * 
	 * @param sumMax Suma acumulada de las evaluaciones del mejor estado
	 * @param countOff Índice donde almacenar el resultado en el array
	 */
	public void calculateOffLinePerformance(float sumMax, int countOff){
		float off = sumMax / countPeriodChange;
		listOfflineError[countOff] = off;
	}
	
	/**
	 * @brief Actualiza las referencias después de un cambio en el entorno dinámico
	 * 
	 * Reevalúa las soluciones de referencia del generador cuando ocurre un cambio
	 * en el problema dinámico, actualizando el mejor estado encontrado.
	 * 
	 * @param generatorType Tipo de generador cuya referencia se actualizará
	 */
	public void updateRef(GeneratorType generatorType){
//		State ref = problem.getOperator().newRef(problem.getRef());
//		problem.setRef(ref);
		if(generatorType.equals(GeneratorType.MULTI_GENERATOR)){
			updateRefMultiG();
			bestState = MultiGenerator.listStateReference.get( MultiGenerator.listStateReference.size() - 1);
		}
		else{
			updateRefGenerator(generator);
			bestState = generator.getReference();
		}
	}
	
	/**
	 * @brief Actualiza las referencias de todos los generadores en un MultiGenerator
	 * 
	 * Itera sobre todos los generadores del portafolio y actualiza sus referencias
	 * después de un cambio en el entorno.
	 */
	public void updateRefMultiG() {
		for (int i = 0; i < MultiGenerator.getListGenerators().length; i++) {
			updateRefGenerator(MultiGenerator.getListGenerators()[i]);
		}
	}
	
	/**
	 * @brief Actualiza la referencia de un generador específico
	 * 
	 * Reevalúa las soluciones de referencia del generador después de un cambio
	 * en el problema. El comportamiento depende del tipo de generador:
	 * - Para generadores de búsqueda local: actualiza una única referencia
	 * - Para algoritmos poblacionales: actualiza toda la población de referencia
	 * 
	 * @param generator Generador cuya referencia se actualizará
	 */
	public void updateRefGenerator(Generator generator) {
		if(generator.getType().equals(GeneratorType.HILL_CLIMBING) || generator.getType().equals(GeneratorType.TABU_SEARCH) || generator.getType().equals(GeneratorType.RANDOM_SEARCH) || generator.getType().equals(GeneratorType.SIMULATED_ANNEALING)){
			double evaluation = getProblem().getFunction().get(0).Evaluation(generator.getReference());
			generator.getReference().getEvaluation().set(0, evaluation);
//			State state = new State();
//			state.setEvaluation(evaluation);
//			state.setCode(new ArrayList<Object>(generator.getReference().getCode()));
//			state.setTypeGenerator(generator.getType());
//			generator.setInitialReference(state);
			/*generator.getReferenceList().remove(generator.getReferenceList().size() - 1);
			generator.setInitialReference(state);
			generator.getReferenceList().add(state);*/
		}
		if(generator.getType().equals(GeneratorType.GENETIC_ALGORITHM) || generator.getType().equals(GeneratorType.DISTRIBUTION_ESTIMATION_ALGORITHM) || generator.getType().equals(GeneratorType.EVOLUTION_STRATEGIES)){
			for (int j = 0; j < generator.getReferenceList().size(); j++) {
				double evaluation = getProblem().getFunction().get(0).Evaluation(generator.getReferenceList().get(j));
				generator.getReferenceList().get(j).getEvaluation().set(0, evaluation);
			}
		}
	}

}
