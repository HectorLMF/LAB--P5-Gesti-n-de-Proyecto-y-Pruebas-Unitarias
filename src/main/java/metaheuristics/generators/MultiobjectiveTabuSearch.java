/**
 * @file MultiobjectiveTabuSearch.java
 * @brief Implementación de búsqueda tabú multiobjetivo
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
import local_search.complement.TabuSolutions;
import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;



/**
 * @class MultiobjectiveTabuSearch
 * @brief Búsqueda tabú adaptada para optimización multiobjetivo
 * 
 * Esta clase implementa el algoritmo de búsqueda tabú que mantiene una lista
 * de soluciones prohibidas y acepta soluciones no dominadas para el frente de Pareto.
 */
public class MultiobjectiveTabuSearch extends Generator {

	/** @brief Valor del candidato para selección */
	private CandidateValue candidatevalue;
	/** @brief Tipo de aceptación de candidatos */
	private AcceptType typeAcceptation;
	/** @brief Estrategia de búsqueda */
	private StrategyType strategy;
	/** @brief Tipo de candidato */
	private CandidateType typeCandidate;
	/** @brief Estado de referencia actual del algoritmo */
	private State stateReferenceTS;
	/** @brief Fábrica para crear criterios de aceptación */
    private IFFactoryAcceptCandidate ifacceptCandidate;
    /** @brief Tipo de generador */
    private GeneratorType typeGenerator;
    /** @brief Lista de estados de referencia */
    private List<State> listStateReference = new ArrayList<State>();
    /** @brief Peso del generador */
    private float weight;
	/** @brief Traza de pesos */
	private List<Float> listTrace = new ArrayList<Float>();

	/**
	 * @brief Obtiene el estado de referencia de la búsqueda tabú
	 * @return Estado de referencia
	 */
    public State getStateReferenceTS() {
		return stateReferenceTS;
	}

	/**
	 * @brief Establece el estado de referencia de la búsqueda tabú
	 * @param stateReferenceTS Nuevo estado de referencia
	 */
	public void setStateReferenceTS(State stateReferenceTS) {
		this.stateReferenceTS = stateReferenceTS;
	}

	/**
	 * @brief Obtiene el tipo de generador
	 * @return Tipo de generador
	 */
	public GeneratorType getTypeGenerator() {
		return typeGenerator;
	}

	/**
	 * @brief Establece el tipo de generador
	 * @param typeGenerator Nuevo tipo de generador
	 */
	public void setTypeGenerator(GeneratorType typeGenerator) {
		this.typeGenerator = typeGenerator;
	}

	/**
	 * @brief Constructor por defecto
	 * 
	 * Inicializa la búsqueda tabú multiobjetivo con lista de soluciones prohibidas
	 * y criterio de aceptación no dominado.
	 */
	public MultiobjectiveTabuSearch() {
    	super();
		this.typeAcceptation = AcceptType.ACCEPT_NOT_DOMINATED_TABU;
		this.strategy = StrategyType.TABU;
		@SuppressWarnings("unused")
		Problem problem = Strategy.getStrategy().getProblem();
		this.typeCandidate = CandidateType.RANDOM_CANDIDATE;
		this.candidatevalue = new CandidateValue();
		this.typeGenerator = GeneratorType.MULTIOBJECTIVE_TABU_SEARCH;
		this.weight = 50;
		listTrace.add(weight);
	}

	/**
	 * @brief Genera un nuevo estado candidato
	 * 
	 * Genera vecinos excluyendo las soluciones en la lista tabú y selecciona
	 * aleatoriamente uno de los candidatos no dominados.
	 * 
	 * @param operatornumber Número de operador para generar el vecindario
	 * @return Estado candidato generado
	 * @throws IllegalArgumentException Si el argumento es ilégal
	 * @throws SecurityException Si hay un problema de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	@Override
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<State> neighborhood = new ArrayList<State>();
		Problem problem = Strategy.getStrategy().getProblem();
		//List<State> list = new ArrayList<State>();
		//Devuelve la lista de soluciones no dominadas de todos los vecinos posibles de stateReferenceTS que no se encuentran en la lista Tabu
		neighborhood = problem.getOperator().generatedNewState(stateReferenceTS, operatornumber);
		//Se escoge uno aleatoriamente como vecino con RandomCandidate
		State statecandidate = candidatevalue.stateCandidate(stateReferenceTS, typeCandidate, strategy, operatornumber, neighborhood);
	    return statecandidate;
	}

	/**
	 * @brief Actualiza la referencia y la lista tabú
	 * 
	 * Acepta soluciones no dominadas, actualiza el frente de Pareto
	 * y gestiona la lista tabú de soluciones prohibidas.
	 * 
	 * @param stateCandidate Estado candidato a evaluar
	 * @param countIterationsCurrent Contador de iteraciones actuales
	 * @throws IllegalArgumentException Si el argumento es ilégal
	 * @throws SecurityException Si hay un problema de seguridad
	 * @throws ClassNotFoundException Si no se encuentra la clase
	 * @throws InstantiationException Si falla la instanciación
	 * @throws IllegalAccessException Si el acceso es ilegal
	 * @throws InvocationTargetException Si falla la invocación
	 * @throws NoSuchMethodException Si no se encuentra el método
	 */
	@Override
	public void updateReference(State stateCandidate, Integer countIterationsCurrent)throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		ifacceptCandidate = new FactoryAcceptCandidate();
		AcceptableCandidate candidate = ifacceptCandidate.createAcceptCandidate(typeAcceptation);
		Boolean acept = candidate.acceptCandidate(stateReferenceTS, stateCandidate);
		if(acept.equals(true))
		  stateReferenceTS = stateCandidate;

		if (strategy.equals(StrategyType.TABU) && acept.equals(true)) {
			if (TabuSolutions.listTabu.size() < TabuSolutions.maxelements) {
				Boolean find = false;
				int count = 0;
				while ((TabuSolutions.listTabu.size() > count) && (find.equals(false))) {
					if (TabuSolutions.listTabu.get(count).equals(stateCandidate)) {
						find = true;
					}
					count++;
				}
				if (find.equals(false)) {
					TabuSolutions.listTabu.add(stateCandidate);
				}
			} else {
				TabuSolutions.listTabu.remove(0);
				Boolean find = false;
				int count = 0;
				while (TabuSolutions.listTabu.size() > count && find.equals(false)) {
					if (TabuSolutions.listTabu.get(count).equals(stateCandidate)) {
						find = true;
					}
					count++;
				}
				if (find.equals(false)) {
					TabuSolutions.listTabu.add(stateCandidate);
				}
			}
	}
		getReferenceList();
  }
	
	/**
	 * @brief Obtiene el tipo de generador
	 * @return Tipo de generador
	 */
	@Override
	public GeneratorType getType() {
		return this.typeGenerator;
	}

	/**
	 * @brief Obtiene la lista de referencias
	 * @return Lista de estados de referencia
	 */
	@Override
	public List<State> getReferenceList() {
		listStateReference.add(stateReferenceTS);
		return listStateReference;
	}

	/**
	 * @brief Obtiene el estado de referencia actual
	 * @return Estado de referencia
	 */
	@Override
	public State getReference() {
		return stateReferenceTS;
	}

	/**
	 * @brief Establece la referencia inicial
	 * @param stateInitialRef Estado inicial de referencia
	 */
	@Override
	public void setInitialReference(State stateInitialRef) {
		this.stateReferenceTS = stateInitialRef;
	}

	/**
	 * @brief Establece el estado de referencia
	 * @param stateRef Nuevo estado de referencia
	 */
	public void setStateRef(State stateRef) {
		this.stateReferenceTS = stateRef;
	}
	
	/**
	 * @brief Obtiene lista de hijos
	 * @return null (no implementado para este algoritmo)
	 */
	@Override
	public List<State> getSonList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @brief Establece el tipo de candidato
	 * @param typeCandidate Nuevo tipo de candidato
	 */
	public void setTypeCandidate(CandidateType typeCandidate){
		this.typeCandidate = typeCandidate;
	}

	/**
	 * @brief Actualiza referencia con premio
	 * @param stateCandidate Estado candidato
	 * @return false (no implementado)
	 */
	@Override
	public boolean awardUpdateREF(State stateCandidate) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @brief Obtiene el peso del generador
	 * @return Peso actual del generador
	 */
	public float getWeight() {
		// TODO Auto-generated method stub
		return this.weight;
	}

	/**
	 * @brief Establece el peso del generador
	 * @param weight Nuevo peso
	 */
	@Override
	public void setWeight(float weight) {
		// TODO Auto-generated method stub
		this.weight = weight;
	}

	/**
	 * @brief Obtiene lista de contadores de mejores géneros
	 * @return null (no implementado)
	 */
	@Override
	public int[] getListCountBetterGender() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @brief Obtiene lista de contadores de géneros
	 * @return null (no implementado)
	 */
	@Override
	public int[] getListCountGender() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @brief Obtiene la traza de pesos
	 * @return null (no implementado)
	 */
	@Override
	public float[] getTrace() {
		// TODO Auto-generated method stub
		return null;
	}

}


