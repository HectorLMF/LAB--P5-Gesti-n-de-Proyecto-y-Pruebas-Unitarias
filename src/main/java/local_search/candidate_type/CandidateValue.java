/**
 * @file CandidateValue.java
 * @brief Clase para gestión y selección de candidatos en búsqueda local
 * @author BiCIAM
 * @version 1.0
 * @date 2025
 */

package local_search.candidate_type;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import problem.definition.State;

import local_search.complement.StrategyType;
import local_search.complement.TabuSolutions;
import metaheurictics.strategy.Strategy;

//import ceis.grial.problem.Problem;
import factory_interface.IFFactoryCandidate;
import factory_method.FactoryCandidate;

/**
 * @class CandidateValue
 * @brief Clase que gestiona la selección de estados candidatos del vecindario
 * 
 * Esta clase coordina la selección de candidatos utilizando diferentes estrategias,
 * incluyendo soporte para búsqueda tabú y filtrado de vecindarios.
 */
public class CandidateValue {

	/** @brief Tipo de estrategia de búsqueda (TABU o NORMAL) */
	@SuppressWarnings("unused")
	private StrategyType strategy;

	/** @brief Fábrica para crear instancias de búsqueda de candidatos */
	private IFFactoryCandidate ifFactory;

	/** @brief Tipo de candidato a utilizar */
	@SuppressWarnings("unused")
	private CandidateType typecand;

	/** @brief Gestor de soluciones tabú */
	private TabuSolutions tabusolution;

	/** @brief Instancia de búsqueda de candidatos */
	private SearchCandidate searchcandidate;

	/** @brief Constructor por defecto */
	public CandidateValue(){}

	/**
	 * @brief Constructor con parámetros
	 * @param strategy Tipo de estrategia (TABU o NORMAL)
	 * @param ifFactory Fábrica de candidatos
	 * @param typecand Tipo de candidato
	 * @param tabusolution Gestor de soluciones tabú
	 * @param searchcandidate Instancia de búsqueda
	 */
	public CandidateValue(StrategyType strategy, IFFactoryCandidate ifFactory, CandidateType typecand, 
			TabuSolutions tabusolution, SearchCandidate searchcandidate) { //, Strategy executegenerator
		super();
		this.strategy = strategy;
		this.ifFactory = ifFactory;
		this.typecand = typecand;
		this.tabusolution = tabusolution;
		this.searchcandidate = searchcandidate;
	}

	/**
	 * @brief Crea una nueva instancia de SearchCandidate según el tipo
	 * @param typecandidate Tipo de candidato a crear
	 * @return SearchCandidate Nueva instancia del buscador de candidatos
	 * @throws IllegalArgumentException Si los argumentos son inválidos
	 * @throws SecurityException Si hay problemas de seguridad
	 * @throws ClassNotFoundException Si no se encuentra una clase
	 * @throws InstantiationException Si hay problemas al instanciar
	 * @throws IllegalAccessException Si hay problemas de acceso
	 * @throws InvocationTargetException Si hay problemas en la invocación
	 * @throws NoSuchMethodException Si no se encuentra un método
	 */
	public SearchCandidate newSearchCandidate(CandidateType typecandidate) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		ifFactory = new FactoryCandidate();
		searchcandidate = ifFactory.createSearchCandidate(typecandidate);
		return searchcandidate;
	}

	/**
	 * @brief Selecciona un estado candidato del vecindario
	 * @param stateCurrent Estado actual
	 * @param typeCandidate Tipo de estrategia de selección
	 * @param strategy Estrategia de búsqueda (TABU o NORMAL)
	 * @param operatornumber Número del operador a aplicar
	 * @param neighborhood Lista de estados vecinos
	 * @return State Estado candidato seleccionado
	 * @throws IllegalArgumentException Si los argumentos son inválidos
	 * @throws SecurityException Si hay problemas de seguridad
	 * @throws ClassNotFoundException Si no se encuentra una clase
	 * @throws InstantiationException Si hay problemas al instanciar
	 * @throws IllegalAccessException Si hay problemas de acceso
	 * @throws InvocationTargetException Si hay problemas en la invocación
	 * @throws NoSuchMethodException Si no se encuentra un método
	 */
	public State stateCandidate(State stateCurrent, CandidateType typeCandidate, StrategyType strategy, Integer operatornumber, List<State> neighborhood) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		//Problem problem = ExecuteGenerator.getExecuteGenerator().getProblem();
		State stateCandidate;
		List<State> auxList = new ArrayList<State>();
		for (int i = 0; i < neighborhood.size(); i++) {
			auxList.add(neighborhood.get(i));
		}
		this.tabusolution = new TabuSolutions();
		if (strategy.equals(StrategyType.TABU)) {
			try {
				auxList = this.tabusolution.filterNeighborhood(auxList);
			}
			catch (Exception e) {
				Strategy strategys = Strategy.getStrategy();
				if(strategys.getProblem()!=null){
					neighborhood = strategys.getProblem().getOperator().generatedNewState(neighborhood.get(0), operatornumber);
				}
				return stateCandidate(stateCurrent, typeCandidate, strategy, operatornumber, neighborhood);
			}
		}
		SearchCandidate searchCand = newSearchCandidate(typeCandidate);
		stateCandidate = searchCand.stateSearch(auxList);
		return stateCandidate;
	}

	/**
	 * @brief Obtiene el gestor de soluciones tabú
	 * @return TabuSolutions Gestor de soluciones tabú
	 */
	public TabuSolutions getTabusolution() {
		return tabusolution;
	}

	/**
	 * @brief Establece el gestor de soluciones tabú
	 * @param tabusolution Nuevo gestor de soluciones tabú
	 */
	public void setTabusolution(TabuSolutions tabusolution) {
		this.tabusolution = tabusolution;
	}
}
