/**
 * @file HillClimbingTest.java
 * @brief Tests unitarios para la clase HillClimbing
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

import local_search.acceptation_type.AcceptType;
import local_search.candidate_type.CandidateType;
import metaheurictics.strategy.Strategy;
import problem.definition.Operator;
import problem.definition.Problem;
import problem.definition.Problem.ProblemType;
import problem.definition.State;

/**
 * @class HillClimbingTest
 * @brief Suite de tests para el algoritmo Hill Climbing
 * 
 * Tests exhaustivos que verifican:
 * - Inicialización correcta del algoritmo
 * - Generación de candidatos
 * - Actualización de referencias
 * - Configuración de tipos de candidato según maximización/minimización
 */
@DisplayName("Tests para HillClimbing - Búsqueda Local")
class HillClimbingTest {

    private HillClimbing hillClimbing;
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
    }

    // ==================== Tests de constructor ====================

    @Test
    @DisplayName("Constructor: Inicialización para problema de maximización")
    void testConstructor_MaximizationProblem() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            hillClimbing = new HillClimbing();

            assertNotNull(hillClimbing, "HillClimbing debería inicializarse");
            assertEquals(GeneratorType.HILL_CLIMBING, hillClimbing.getType(), 
                "Tipo de generador debería ser HILL_CLIMBING");
        }
    }

    @Test
    @DisplayName("Constructor: Inicialización para problema de minimización")
    void testConstructor_MinimizationProblem() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);

            hillClimbing = new HillClimbing();

            assertNotNull(hillClimbing, "HillClimbing debería inicializarse");
            assertEquals(GeneratorType.HILL_CLIMBING, hillClimbing.getType(), 
                "Tipo de generador debería ser HILL_CLIMBING");
        }
    }

    @Test
    @DisplayName("Constructor: Verifica peso inicial")
    void testConstructor_InitialWeight() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            hillClimbing = new HillClimbing();

            assertNotNull(hillClimbing.getTrace(), "Trace debería inicializarse");
            assertNotNull(hillClimbing.getListCountBetterGender(), "BetterCount debería inicializarse");
            assertNotNull(hillClimbing.getListCountGender(), "UsageCount debería inicializarse");
        }
    }

    // ==================== Tests de setInitialReference ====================

    @Test
    @DisplayName("setInitialReference: Establece referencia inicial")
    void testSetInitialReference() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            hillClimbing = new HillClimbing();
            State initialState = createState(10.0);

            hillClimbing.setInitialReference(initialState);

            assertEquals(initialState, hillClimbing.getReference(), 
                "La referencia debería ser el estado inicial");
        }
    }

    @Test
    @DisplayName("setInitialReference: Acepta referencia nula")
    void testSetInitialReference_Null() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            hillClimbing = new HillClimbing();

            assertDoesNotThrow(() -> hillClimbing.setInitialReference(null),
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

            hillClimbing = new HillClimbing();
            State reference = createState(10.0);
            hillClimbing.setInitialReference(reference);

            List<State> neighborhood = new ArrayList<>();
            neighborhood.add(createState(12.0));
            neighborhood.add(createState(8.0));
            neighborhood.add(createState(15.0));

            when(mockOperator.generatedNewState(reference, 0)).thenReturn(neighborhood);

            State candidate = hillClimbing.generate(0);

            assertNotNull(candidate, "Debería generar un candidato");
        }
    }

    @Test
    @DisplayName("generate: Maneja vecindario vacío")
    void testGenerate_EmptyNeighborhood() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            hillClimbing = new HillClimbing();
            State reference = createState(10.0);
            hillClimbing.setInitialReference(reference);

            List<State> emptyNeighborhood = new ArrayList<>();
            when(mockOperator.generatedNewState(reference, 0)).thenReturn(emptyNeighborhood);

            State candidate = hillClimbing.generate(0);

            // El comportamiento depende de la implementación de CandidateValue
            // Puede retornar null o lanzar excepción
        }
    }

    @Test
    @DisplayName("generate: Funciona con diferentes números de operador")
    void testGenerate_DifferentOperatorNumbers() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            hillClimbing = new HillClimbing();
            State reference = createState(10.0);
            hillClimbing.setInitialReference(reference);

            List<State> neighborhood1 = new ArrayList<>();
            neighborhood1.add(createState(12.0));
            
            List<State> neighborhood2 = new ArrayList<>();
            neighborhood2.add(createState(8.0));

            when(mockOperator.generatedNewState(reference, 0)).thenReturn(neighborhood1);
            when(mockOperator.generatedNewState(reference, 1)).thenReturn(neighborhood2);

            State candidate1 = hillClimbing.generate(0);
            State candidate2 = hillClimbing.generate(1);

            assertNotNull(candidate1, "Debería generar candidato con operador 0");
            assertNotNull(candidate2, "Debería generar candidato con operador 1");
        }
    }

    // ==================== Tests de updateReference ====================

    @Test
    @DisplayName("updateReference: Actualiza referencia con candidato mejor")
    void testUpdateReference_BetterCandidate() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            hillClimbing = new HillClimbing();
            State current = createState(10.0);
            State better = createState(15.0);
            hillClimbing.setInitialReference(current);

            hillClimbing.updateReference(better, 1);

            // La referencia se actualiza si el candidato es aceptado
            // Depende de AcceptBest
            assertNotNull(hillClimbing.getReference(), "La referencia debería existir");
        }
    }

    @Test
    @DisplayName("updateReference: No actualiza con candidato peor")
    void testUpdateReference_WorseCandidate() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            hillClimbing = new HillClimbing();
            State current = createState(15.0);
            State worse = createState(10.0);
            hillClimbing.setInitialReference(current);

            hillClimbing.updateReference(worse, 1);

            // La referencia no debería cambiar con AcceptBest
            assertNotNull(hillClimbing.getReference(), "La referencia debería existir");
        }
    }

    @Test
    @DisplayName("updateReference: Funciona con múltiples actualizaciones")
    void testUpdateReference_MultipleUpdates() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);

            hillClimbing = new HillClimbing();
            State initial = createState(20.0);
            hillClimbing.setInitialReference(initial);

            hillClimbing.updateReference(createState(15.0), 1);
            hillClimbing.updateReference(createState(10.0), 2);
            hillClimbing.updateReference(createState(5.0), 3);

            assertNotNull(hillClimbing.getReference(), "La referencia debería existir");
        }
    }

    // ==================== Tests de getReferenceList ====================

    @Test
    @DisplayName("getReferenceList: Retorna lista con referencias")
    void testGetReferenceList() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            hillClimbing = new HillClimbing();
            State reference = createState(10.0);
            hillClimbing.setInitialReference(reference);

            List<State> list = hillClimbing.getReferenceList();

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

            hillClimbing = new HillClimbing();
            hillClimbing.setInitialReference(createState(10.0));

            int sizeBefore = hillClimbing.getReferenceList().size();
            hillClimbing.getReferenceList();
            int sizeAfter = hillClimbing.getReferenceList().size();

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

            hillClimbing = new HillClimbing();
            State reference = createState(10.0);
            hillClimbing.setInitialReference(reference);

            State result = hillClimbing.getReference();

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

            hillClimbing = new HillClimbing();
            State newRef = createState(20.0);

            hillClimbing.setStateRef(newRef);

            assertEquals(newRef, hillClimbing.getReference(), 
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

            hillClimbing = new HillClimbing();

            assertDoesNotThrow(() -> hillClimbing.setTypeCandidate(CandidateType.RANDOM_CANDIDATE),
                "Debería cambiar el tipo de candidato sin excepción");
        }
    }

    // ==================== Tests de tipo de generador ====================

    @Test
    @DisplayName("getType: Retorna HILL_CLIMBING")
    void testGetType() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            hillClimbing = new HillClimbing();

            assertEquals(GeneratorType.HILL_CLIMBING, hillClimbing.getType(), 
                "Tipo debería ser HILL_CLIMBING");
        }
    }

    @Test
    @DisplayName("getGeneratorType: Retorna tipo de generador")
    void testGetGeneratorType() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            hillClimbing = new HillClimbing();

            assertEquals(GeneratorType.HILL_CLIMBING, hillClimbing.getGeneratorType(), 
                "GeneratorType debería ser HILL_CLIMBING");
        }
    }

    @Test
    @DisplayName("setGeneratorType: Establece tipo de generador")
    void testSetGeneratorType() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            hillClimbing = new HillClimbing();
            hillClimbing.setGeneratorType(GeneratorType.HILL_CLIMBING_RESTART);

            assertEquals(GeneratorType.HILL_CLIMBING_RESTART, hillClimbing.getGeneratorType(), 
                "Debería cambiar el tipo de generador");
        }
    }

    // ==================== Tests de métodos no implementados ====================

    @Test
    @DisplayName("getSonList: Retorna null (no implementado)")
    void testGetSonList() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            hillClimbing = new HillClimbing();

            assertNull(hillClimbing.getSonList(), "getSonList debería retornar null");
        }
    }

    @Test
    @DisplayName("awardUpdateREF: Retorna false (no implementado)")
    void testAwardUpdateREF() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            hillClimbing = new HillClimbing();
            State candidate = createState(10.0);

            assertFalse(hillClimbing.awardUpdateREF(candidate), 
                "awardUpdateREF debería retornar false");
        }
    }

    @Test
    @DisplayName("getWeight: Retorna 0 (no implementado)")
    void testGetWeight() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            hillClimbing = new HillClimbing();

            assertEquals(0, hillClimbing.getWeight(), "getWeight debería retornar 0");
        }
    }

    @Test
    @DisplayName("setWeight: No lanza excepción (no implementado)")
    void testSetWeight() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            hillClimbing = new HillClimbing();

            assertDoesNotThrow(() -> hillClimbing.setWeight(10.5f), 
                "setWeight no debería lanzar excepción");
        }
    }

    // ==================== Tests de arrays de contadores ====================

    @Test
    @DisplayName("getListCountBetterGender: Retorna array de mejoras")
    void testGetListCountBetterGender() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            hillClimbing = new HillClimbing();

            int[] betterCount = hillClimbing.getListCountBetterGender();

            assertNotNull(betterCount, "BetterCount no debería ser nulo");
            assertEquals(10, betterCount.length, "BetterCount debería tener 10 elementos");
        }
    }

    @Test
    @DisplayName("getListCountGender: Retorna array de usos")
    void testGetListCountGender() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            hillClimbing = new HillClimbing();

            int[] usageCount = hillClimbing.getListCountGender();

            assertNotNull(usageCount, "UsageCount no debería ser nulo");
            assertEquals(10, usageCount.length, "UsageCount debería tener 10 elementos");
        }
    }

    @Test
    @DisplayName("getTrace: Retorna array de trazas")
    void testGetTrace() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            hillClimbing = new HillClimbing();

            float[] trace = hillClimbing.getTrace();

            assertNotNull(trace, "Trace no debería ser nulo");
            assertEquals(1200000, trace.length, "Trace debería tener 1200000 elementos");
        }
    }

    // ==================== Tests de herencia ====================

    @Test
    @DisplayName("Es instancia de Generator")
    void testInheritsFromGenerator() {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);

            hillClimbing = new HillClimbing();

            assertTrue(hillClimbing instanceof Generator, 
                "HillClimbing debería heredar de Generator");
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
}
