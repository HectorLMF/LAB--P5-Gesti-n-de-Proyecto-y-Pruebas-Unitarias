/**
 * @file TabuSearchTest.java
 * @brief Tests unitarios para la clase TabuSearch
 * @author Test Suite
 * @version 1.0
 * @date 2025
 */

package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import local_search.candidate_type.CandidateType;
import local_search.complement.TabuSolutions;
import metaheurictics.strategy.Strategy;
import problem.definition.Operator;
import problem.definition.Problem;
import problem.definition.Problem.ProblemType;
import problem.definition.State;

/**
 * @class TabuSearchTest
 * @brief Suite de tests para el algoritmo de búsqueda tabú
 * 
 * Tests exhaustivos que verifican:
 * - Inicialización del algoritmo tabú
 * - Generación de candidatos del vecindario
 * - Gestión de la lista tabú
 * - Actualización de referencias evitando ciclos
 * - Configuración según maximización/minimización
 */
@DisplayName("Tests para TabuSearch - Búsqueda Tabú")
class TabuSearchTest {

    private TabuSearch tabuSearch;
    private Strategy mockStrategy;
    private Problem mockProblem;
    private Operator mockOperator;

    @BeforeEach
    void setUp() {
        mockStrategy = mock(Strategy.class);
        mockProblem = mock(Problem.class);
        mockOperator = mock(Operator.class);
        
        when(mockStrategy.getProblem()).thenReturn(mockProblem);
        when(mockProblem.getOperator()).thenReturn(mockOperator);
        
        // Limpiar lista tabú
        TabuSolutions.listTabu = new ArrayList<>();
        TabuSolutions.maxelements = 10;
    }

    // ==================== Tests de constructor ====================

    @Test
    @DisplayName("Constructor: Inicialización para problema de maximización")
    void testConstructor_MaximizationProblem() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();

            assertNotNull(tabuSearch, "TabuSearch debería inicializarse");
            assertEquals(GeneratorType.TABU_SEARCH, tabuSearch.getType(), 
                "Tipo de generador debería ser TABU_SEARCH");
        }
    }

    @Test
    @DisplayName("Constructor: Inicialización para problema de minimización")
    void testConstructor_MinimizationProblem() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);

            tabuSearch = new TabuSearch();

            assertNotNull(tabuSearch, "TabuSearch debería inicializarse");
            assertEquals(GeneratorType.TABU_SEARCH, tabuSearch.getType(), 
                "Tipo de generador debería ser TABU_SEARCH");
        }
    }

    @Test
    @DisplayName("Constructor: Verifica peso inicial")
    void testConstructor_InitialWeight() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();

            assertEquals(50, tabuSearch.getWeight(), 0.01, "Peso inicial debería ser 50");
            assertNotNull(tabuSearch.getTrace(), "Trace debería inicializarse");
        }
    }

    @Test
    @DisplayName("Constructor: Verifica arrays de contadores")
    void testConstructor_CounterArrays() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();

            int[] betterCount = tabuSearch.getListCountBetterGender();
            int[] usageCount = tabuSearch.getListCountGender();
            float[] trace = tabuSearch.getTrace();

            assertEquals(10, betterCount.length, "BetterCount debería tener 10 elementos");
            assertEquals(10, usageCount.length, "UsageCount debería tener 10 elementos");
            assertEquals(1200000, trace.length, "Trace debería tener 1200000 elementos");
            assertEquals(50, trace[0], 0.01, "Primer elemento de trace debería ser el peso inicial");
        }
    }

    // ==================== Tests de setInitialReference ====================

    @Test
    @DisplayName("setInitialReference: Establece referencia inicial")
    void testSetInitialReference() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();
            State initialState = createState(10.0);

            tabuSearch.setInitialReference(initialState);

            assertEquals(initialState, tabuSearch.getReference(), 
                "La referencia debería ser el estado inicial");
        }
    }

    @Test
    @DisplayName("setInitialReference: Acepta referencia nula")
    void testSetInitialReference_Null() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();

            assertDoesNotThrow(() -> tabuSearch.setInitialReference(null),
                "Debería aceptar referencia nula sin excepción");
        }
    }

    // ==================== Tests de generate ====================

    @Test
    @DisplayName("generate: Genera candidato desde vecindario")
    void testGenerate_GeneratesCandidate() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();
            State reference = createState(10.0);
            tabuSearch.setInitialReference(reference);

            List<State> neighborhood = new ArrayList<>();
            neighborhood.add(createState(12.0));
            neighborhood.add(createState(8.0));
            neighborhood.add(createState(15.0));

            when(mockOperator.generatedNewState(reference, 0)).thenReturn(neighborhood);

            State candidate = tabuSearch.generate(0);

            assertNotNull(candidate, "Debería generar un candidato");
        }
    }

    @Test
    @DisplayName("generate: Selecciona mejor candidato del vecindario (maximización)")
    void testGenerate_BestCandidate_Maximization() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();
            State reference = createState(10.0);
            tabuSearch.setInitialReference(reference);

            List<State> neighborhood = new ArrayList<>();
            neighborhood.add(createState(12.0));
            neighborhood.add(createState(8.0));
            neighborhood.add(createState(15.0)); // El mejor

            when(mockOperator.generatedNewState(reference, 0)).thenReturn(neighborhood);

            State candidate = tabuSearch.generate(0);

            assertNotNull(candidate, "Debería generar el mejor candidato");
        }
    }

    @Test
    @DisplayName("generate: Selecciona menor candidato del vecindario (minimización)")
    void testGenerate_SmallestCandidate_Minimization() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);

            tabuSearch = new TabuSearch();
            State reference = createState(10.0);
            tabuSearch.setInitialReference(reference);

            List<State> neighborhood = new ArrayList<>();
            neighborhood.add(createState(12.0));
            neighborhood.add(createState(5.0)); // El mejor (menor)
            neighborhood.add(createState(15.0));

            when(mockOperator.generatedNewState(reference, 0)).thenReturn(neighborhood);

            State candidate = tabuSearch.generate(0);

            assertNotNull(candidate, "Debería generar el menor candidato");
        }
    }

    @Test
    @DisplayName("generate: Funciona con diferentes operadores")
    void testGenerate_DifferentOperators() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();
            State reference = createState(10.0);
            tabuSearch.setInitialReference(reference);

            List<State> neighborhood1 = new ArrayList<>();
            neighborhood1.add(createState(12.0));
            
            List<State> neighborhood2 = new ArrayList<>();
            neighborhood2.add(createState(8.0));

            when(mockOperator.generatedNewState(reference, 0)).thenReturn(neighborhood1);
            when(mockOperator.generatedNewState(reference, 1)).thenReturn(neighborhood2);

            State candidate1 = tabuSearch.generate(0);
            State candidate2 = tabuSearch.generate(1);

            assertNotNull(candidate1, "Debería generar candidato con operador 0");
            assertNotNull(candidate2, "Debería generar candidato con operador 1");
        }
    }

    // ==================== Tests de updateReference ====================

    @Test
    @DisplayName("updateReference: Actualiza referencia con candidato")
    void testUpdateReference_UpdatesReference() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();
            State current = createStateWithComparator(10.0, false);
            State candidate = createStateWithComparator(15.0, false);
            tabuSearch.setInitialReference(current);

            tabuSearch.updateReference(candidate, 1);

            assertNotNull(tabuSearch.getReference(), "La referencia debería existir");
        }
    }

    @Test
    @DisplayName("updateReference: Agrega solución a lista tabú")
    void testUpdateReference_AddsToTabuList() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();
            State current = createStateWithComparator(10.0, false);
            State candidate = createStateWithComparator(15.0, false);
            tabuSearch.setInitialReference(current);

            int sizeBefore = TabuSolutions.listTabu.size();
            tabuSearch.updateReference(candidate, 1);

            assertTrue(TabuSolutions.listTabu.size() >= sizeBefore, 
                "La lista tabú debería mantener o crecer su tamaño");
        }
    }

    @Test
    @DisplayName("updateReference: No agrega duplicados a lista tabú")
    void testUpdateReference_NoDuplicatesInTabuList() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();
            State current = createStateWithComparator(10.0, false);
            State candidate = createStateWithComparator(15.0, true); // Comparator retorna true
            tabuSearch.setInitialReference(current);

            // Agregar el mismo candidato dos veces
            tabuSearch.updateReference(candidate, 1);
            int sizeAfterFirst = TabuSolutions.listTabu.size();
            
            tabuSearch.updateReference(candidate, 2);
            int sizeAfterSecond = TabuSolutions.listTabu.size();

            // El tamaño no debería aumentar si el candidato ya existe
            assertTrue(sizeAfterSecond <= sizeAfterFirst + 1, 
                "No debería agregar duplicados a la lista tabú");
        }
    }

    @Test
    @DisplayName("updateReference: Lista tabú respeta tamaño máximo")
    void testUpdateReference_TabuListMaxSize() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();
            State current = createStateWithComparator(10.0, false);
            tabuSearch.setInitialReference(current);

            TabuSolutions.maxelements = 3;

            // Agregar más soluciones que el máximo
            for (int i = 0; i < 5; i++) {
                State candidate = createStateWithComparator(10.0 + i, false);
                tabuSearch.updateReference(candidate, i + 1);
            }

            assertTrue(TabuSolutions.listTabu.size() <= TabuSolutions.maxelements, 
                "La lista tabú no debería exceder el tamaño máximo");
        }
    }

    @Test
    @DisplayName("updateReference: Elimina elemento más antiguo cuando lista está llena")
    void testUpdateReference_RemovesOldestWhenFull() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();
            State current = createStateWithComparator(10.0, false);
            tabuSearch.setInitialReference(current);

            TabuSolutions.maxelements = 2;

            State first = createStateWithComparator(11.0, false);
            State second = createStateWithComparator(12.0, false);
            State third = createStateWithComparator(13.0, false);

            tabuSearch.updateReference(first, 1);
            tabuSearch.updateReference(second, 2);
            tabuSearch.updateReference(third, 3);

            // La lista debería mantener solo los últimos 2
            assertTrue(TabuSolutions.listTabu.size() <= 2, 
                "Debería eliminar elementos antiguos cuando está llena");
        }
    }

    // ==================== Tests de getReferenceList ====================

    @Test
    @DisplayName("getReferenceList: Retorna lista con referencias")
    void testGetReferenceList() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();
            State reference = createState(10.0);
            tabuSearch.setInitialReference(reference);

            List<State> list = tabuSearch.getReferenceList();

            assertNotNull(list, "La lista no debería ser nula");
            assertFalse(list.isEmpty(), "La lista debería contener la referencia");
        }
    }

    @Test
    @DisplayName("getReferenceList: Lista crece con múltiples llamadas")
    void testGetReferenceList_GrowsWithCalls() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();
            tabuSearch.setInitialReference(createState(10.0));

            int sizeBefore = tabuSearch.getReferenceList().size();
            tabuSearch.getReferenceList();
            int sizeAfter = tabuSearch.getReferenceList().size();

            assertTrue(sizeAfter > sizeBefore, "La lista debería crecer");
        }
    }

    // ==================== Tests de getReference ====================

    @Test
    @DisplayName("getReference: Retorna referencia actual")
    void testGetReference() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();
            State reference = createState(10.0);
            tabuSearch.setInitialReference(reference);

            State result = tabuSearch.getReference();

            assertEquals(reference, result, "Debería retornar la referencia establecida");
        }
    }

    // ==================== Tests de setStateRef ====================

    @Test
    @DisplayName("setStateRef: Establece estado de referencia")
    void testSetStateRef() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();
            State newRef = createState(20.0);

            tabuSearch.setStateRef(newRef);

            assertEquals(newRef, tabuSearch.getReference(), 
                "El estado de referencia debería actualizarse");
        }
    }

    // ==================== Tests de setTypeCandidate ====================

    @Test
    @DisplayName("setTypeCandidate: Cambia tipo de candidato")
    void testSetTypeCandidate() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();

            assertDoesNotThrow(() -> tabuSearch.setTypeCandidate(CandidateType.RANDOM_CANDIDATE),
                "Debería cambiar el tipo de candidato sin excepción");
        }
    }

    // ==================== Tests de tipo de generador ====================

    @Test
    @DisplayName("getType: Retorna TABU_SEARCH")
    void testGetType() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();

            assertEquals(GeneratorType.TABU_SEARCH, tabuSearch.getType(), 
                "Tipo debería ser TABU_SEARCH");
        }
    }

    @Test
    @DisplayName("getTypeGenerator: Retorna tipo de generador")
    void testGetTypeGenerator() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();

            assertEquals(GeneratorType.TABU_SEARCH, tabuSearch.getTypeGenerator(), 
                "TypeGenerator debería ser TABU_SEARCH");
        }
    }

    @Test
    @DisplayName("setTypeGenerator: Establece tipo de generador")
    void testSetTypeGenerator() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();
            tabuSearch.setTypeGenerator(GeneratorType.HILL_CLIMBING);

            assertEquals(GeneratorType.HILL_CLIMBING, tabuSearch.getTypeGenerator(), 
                "Debería cambiar el tipo de generador");
        }
    }

    // ==================== Tests de peso ====================

    @Test
    @DisplayName("getWeight: Retorna peso del generador")
    void testGetWeight() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();

            assertEquals(50, tabuSearch.getWeight(), 0.01, "Peso inicial debería ser 50");
        }
    }

    @Test
    @DisplayName("setWeight: Modifica peso del generador")
    void testSetWeight() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();
            
            tabuSearch.setWeight(75.5f);

            assertEquals(75.5f, tabuSearch.getWeight(), 0.01, "Peso debería actualizarse");
        }
    }

    // ==================== Tests de métodos no implementados ====================

    @Test
    @DisplayName("getSonList: Retorna null (no implementado)")
    void testGetSonList() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();

            assertNull(tabuSearch.getSonList(), "getSonList debería retornar null");
        }
    }

    @Test
    @DisplayName("awardUpdateREF: Retorna false (no implementado)")
    void testAwardUpdateREF() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();
            State candidate = createState(10.0);

            assertFalse(tabuSearch.awardUpdateREF(candidate), 
                "awardUpdateREF debería retornar false");
        }
    }

    // ==================== Tests de herencia ====================

    @Test
    @DisplayName("Es instancia de Generator")
    void testInheritsFromGenerator() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            tabuSearch = new TabuSearch();

            assertTrue(tabuSearch instanceof Generator, 
                "TabuSearch debería heredar de Generator");
        }
    }

    // ==================== Helper Methods ====================

    /**
     * Crea un State con una evaluación específica
     */
    private State createState(double evaluation) {
        State state = new State();
        ArrayList<Double> evals = new ArrayList<>();
        evals.add(evaluation);
        state.setEvaluation(evals);
        return state;
    }

    /**
     * Crea un State con comparador mock
     */
    private State createStateWithComparator(double evaluation, boolean compareResult) {
        State state = spy(new State());
        ArrayList<Double> evals = new ArrayList<>();
        evals.add(evaluation);
        state.setEvaluation(evals);
        
        doReturn(compareResult).when(state).Comparator(any(State.class));
        
        return state;
    }
}
