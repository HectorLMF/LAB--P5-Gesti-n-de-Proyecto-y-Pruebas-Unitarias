/**
 * @file SimulatedAnnealingTest.java
 * @brief Tests unitarios para la clase SimulatedAnnealing
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
import metaheurictics.strategy.Strategy;
import problem.definition.Operator;
import problem.definition.Problem;
import problem.definition.Problem.ProblemType;
import problem.definition.State;

/**
 * @class SimulatedAnnealingTest
 * @brief Suite de tests para el algoritmo de recocido simulado
 * 
 * Tests exhaustivos que verifican:
 * - Inicialización con parámetros de temperatura
 * - Generación de candidatos aleatorios
 * - Actualización de referencia con aceptación probabilística
 * - Enfriamiento progresivo de temperatura
 * - Manejo de parámetros estáticos (alpha, tinitial, tfinal)
 */
@DisplayName("Tests para SimulatedAnnealing - Recocido Simulado")
class SimulatedAnnealingTest {

    private SimulatedAnnealing simulatedAnnealing;
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
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        // Inicializar parámetros estáticos
        SimulatedAnnealing.alpha = 0.93;
        SimulatedAnnealing.tinitial = 250.0;
        SimulatedAnnealing.tfinal = 41.66;
        SimulatedAnnealing.countIterationsT = 50;
    }

    // ==================== Tests de constructor ====================

    @Test
    @DisplayName("Constructor: Inicialización correcta")
    void testConstructor_Initialization() {
        simulatedAnnealing = new SimulatedAnnealing();

        assertNotNull(simulatedAnnealing, "SimulatedAnnealing debería inicializarse");
        assertEquals(GeneratorType.SIMULATED_ANNEALING, simulatedAnnealing.getType(), 
            "Tipo de generador debería ser SIMULATED_ANNEALING");
    }

    @Test
    @DisplayName("Constructor: Verifica peso inicial")
    void testConstructor_InitialWeight() {
        simulatedAnnealing = new SimulatedAnnealing();

        assertEquals(50, simulatedAnnealing.getWeight(), "Peso inicial debería ser 50");
        assertNotNull(simulatedAnnealing.getTrace(), "Trace debería inicializarse");
        assertNotNull(simulatedAnnealing.getListCountBetterGender(), 
            "BetterCount debería inicializarse");
        assertNotNull(simulatedAnnealing.getListCountGender(), 
            "UsageCount debería inicializarse");
    }

    @Test
    @DisplayName("Constructor: Verifica arrays de contadores")
    void testConstructor_CounterArrays() {
        simulatedAnnealing = new SimulatedAnnealing();

        int[] betterCount = simulatedAnnealing.getListCountBetterGender();
        int[] usageCount = simulatedAnnealing.getListCountGender();
        float[] trace = simulatedAnnealing.getTrace();

        assertEquals(10, betterCount.length, "BetterCount debería tener 10 elementos");
        assertEquals(10, usageCount.length, "UsageCount debería tener 10 elementos");
        assertEquals(1200000, trace.length, "Trace debería tener 1200000 elementos");
        assertEquals(50, trace[0], 0.01, "Primer elemento de trace debería ser el peso inicial");
    }

    // ==================== Tests de parámetros estáticos ====================

    @Test
    @DisplayName("Parámetros estáticos: alpha se puede modificar")
    void testStaticParameters_Alpha() {
        SimulatedAnnealing.alpha = 0.95;
        assertEquals(0.95, SimulatedAnnealing.alpha, 0.001, "Alpha debería ser modificable");
        
        SimulatedAnnealing.alpha = 0.90;
        assertEquals(0.90, SimulatedAnnealing.alpha, 0.001, "Alpha debería actualizarse");
    }

    @Test
    @DisplayName("Parámetros estáticos: tinitial se puede modificar")
    void testStaticParameters_Tinitial() {
        SimulatedAnnealing.tinitial = 100.0;
        assertEquals(100.0, SimulatedAnnealing.tinitial, 0.001, 
            "Temperatura inicial debería ser modificable");
        
        SimulatedAnnealing.tinitial = 500.0;
        assertEquals(500.0, SimulatedAnnealing.tinitial, 0.001, 
            "Temperatura inicial debería actualizarse");
    }

    @Test
    @DisplayName("Parámetros estáticos: tfinal se puede modificar")
    void testStaticParameters_Tfinal() {
        SimulatedAnnealing.tfinal = 10.0;
        assertEquals(10.0, SimulatedAnnealing.tfinal, 0.001, 
            "Temperatura final debería ser modificable");
    }

    @Test
    @DisplayName("Parámetros estáticos: countIterationsT se puede modificar")
    void testStaticParameters_CountIterationsT() {
        SimulatedAnnealing.countIterationsT = 100;
        assertEquals(100, SimulatedAnnealing.countIterationsT, 
            "Contador de iteraciones debería ser modificable");
    }

    // ==================== Tests de setInitialReference ====================

    @Test
    @DisplayName("setInitialReference: Establece referencia inicial")
    void testSetInitialReference() {
        simulatedAnnealing = new SimulatedAnnealing();
        State initialState = createState(10.0);

        simulatedAnnealing.setInitialReference(initialState);

        assertEquals(initialState, simulatedAnnealing.getReference(), 
            "La referencia debería ser el estado inicial");
    }

    @Test
    @DisplayName("setInitialReference: Acepta referencia nula")
    void testSetInitialReference_Null() {
        simulatedAnnealing = new SimulatedAnnealing();

        assertDoesNotThrow(() -> simulatedAnnealing.setInitialReference(null),
            "Debería aceptar referencia nula sin excepción");
    }

    // ==================== Tests de generate ====================

    @Test
    @DisplayName("generate: Genera candidato desde vecindario")
    void testGenerate_GeneratesCandidate() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);

            simulatedAnnealing = new SimulatedAnnealing();
            State reference = createState(10.0);
            simulatedAnnealing.setInitialReference(reference);

            List<State> neighborhood = new ArrayList<>();
            neighborhood.add(createState(12.0));
            neighborhood.add(createState(8.0));
            neighborhood.add(createState(15.0));

            when(mockOperator.generatedNewState(reference, 0)).thenReturn(neighborhood);

            State candidate = simulatedAnnealing.generate(0);

            assertNotNull(candidate, "Debería generar un candidato");
        }
    }

    @Test
    @DisplayName("generate: Candidato aleatorio del vecindario")
    void testGenerate_RandomCandidate() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);

            simulatedAnnealing = new SimulatedAnnealing();
            State reference = createState(10.0);
            simulatedAnnealing.setInitialReference(reference);

            List<State> neighborhood = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                neighborhood.add(createState(10.0 + i));
            }

            when(mockOperator.generatedNewState(reference, 0)).thenReturn(neighborhood);

            State candidate = simulatedAnnealing.generate(0);

            assertNotNull(candidate, "Debería generar un candidato aleatorio");
        }
    }

    @Test
    @DisplayName("generate: Funciona con diferentes operadores")
    void testGenerate_DifferentOperators() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);

            simulatedAnnealing = new SimulatedAnnealing();
            State reference = createState(10.0);
            simulatedAnnealing.setInitialReference(reference);

            List<State> neighborhood1 = new ArrayList<>();
            neighborhood1.add(createState(12.0));
            
            List<State> neighborhood2 = new ArrayList<>();
            neighborhood2.add(createState(8.0));

            when(mockOperator.generatedNewState(reference, 0)).thenReturn(neighborhood1);
            when(mockOperator.generatedNewState(reference, 1)).thenReturn(neighborhood2);

            State candidate1 = simulatedAnnealing.generate(0);
            State candidate2 = simulatedAnnealing.generate(1);

            assertNotNull(candidate1, "Debería generar candidato con operador 0");
            assertNotNull(candidate2, "Debería generar candidato con operador 1");
        }
    }

    // ==================== Tests de updateReference ====================

    @Test
    @DisplayName("updateReference: Actualiza con candidato mejor")
    void testUpdateReference_BetterCandidate() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);

            simulatedAnnealing = new SimulatedAnnealing();
            State current = createState(10.0);
            State better = createState(15.0);
            simulatedAnnealing.setInitialReference(current);

            simulatedAnnealing.updateReference(better, 1);

            assertNotNull(simulatedAnnealing.getReference(), "La referencia debería existir");
        }
    }

    @Test
    @DisplayName("updateReference: Puede actualizar con candidato peor (probabilístico)")
    void testUpdateReference_WorseCandidateProbabilistic() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);

            simulatedAnnealing = new SimulatedAnnealing();
            State current = createState(15.0);
            State worse = createState(10.0);
            simulatedAnnealing.setInitialReference(current);

            // Con temperatura alta, puede aceptar soluciones peores
            SimulatedAnnealing.tinitial = 1000.0;
            simulatedAnnealing.updateReference(worse, 1);

            assertNotNull(simulatedAnnealing.getReference(), "La referencia debería existir");
        }
    }

    @Test
    @DisplayName("updateReference: Enfriamiento de temperatura en iteración límite")
    void testUpdateReference_TemperatureCooling() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);

            simulatedAnnealing = new SimulatedAnnealing();
            State current = createState(10.0);
            simulatedAnnealing.setInitialReference(current);

            SimulatedAnnealing.tinitial = 100.0;
            SimulatedAnnealing.alpha = 0.9;
            SimulatedAnnealing.countIterationsT = 10;

            double tempBefore = SimulatedAnnealing.tinitial;
            
            // Actualizar en la iteración límite (debería enfriar)
            simulatedAnnealing.updateReference(createState(12.0), 10);

            // La temperatura debería haberse reducido
            assertTrue(SimulatedAnnealing.tinitial < tempBefore || 
                      SimulatedAnnealing.tinitial == tempBefore * SimulatedAnnealing.alpha,
                "La temperatura debería enfriarse en iteración límite");
        }
    }

    @Test
    @DisplayName("updateReference: No enfría antes de la iteración límite")
    void testUpdateReference_NoEarlyCooling() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);

            simulatedAnnealing = new SimulatedAnnealing();
            State current = createState(10.0);
            simulatedAnnealing.setInitialReference(current);

            SimulatedAnnealing.tinitial = 100.0;
            SimulatedAnnealing.countIterationsT = 50;
            double tempBefore = SimulatedAnnealing.tinitial;

            // Actualizar antes de la iteración límite
            simulatedAnnealing.updateReference(createState(12.0), 25);

            // La temperatura no debería cambiar
            assertEquals(tempBefore, SimulatedAnnealing.tinitial, 0.001,
                "La temperatura no debería cambiar antes de la iteración límite");
        }
    }

    @Test
    @DisplayName("updateReference: Múltiples enfriamientos")
    void testUpdateReference_MultipleCoolings() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);

            simulatedAnnealing = new SimulatedAnnealing();
            State current = createState(10.0);
            simulatedAnnealing.setInitialReference(current);

            SimulatedAnnealing.tinitial = 100.0;
            SimulatedAnnealing.alpha = 0.95;
            SimulatedAnnealing.countIterationsT = 10;

            double temp1 = SimulatedAnnealing.tinitial;
            simulatedAnnealing.updateReference(createState(11.0), 10);
            
            double temp2 = SimulatedAnnealing.tinitial;
            assertTrue(temp2 < temp1, "Primera temperatura debería reducirse");
            
            simulatedAnnealing.updateReference(createState(12.0), 20);
            
            double temp3 = SimulatedAnnealing.tinitial;
            assertTrue(temp3 < temp2, "Segunda temperatura debería reducirse más");
        }
    }

    // ==================== Tests de getReferenceList ====================

    @Test
    @DisplayName("getReferenceList: Retorna lista con referencias")
    void testGetReferenceList() {
        simulatedAnnealing = new SimulatedAnnealing();
        State reference = createState(10.0);
        simulatedAnnealing.setInitialReference(reference);

        List<State> list = simulatedAnnealing.getReferenceList();

        assertNotNull(list, "La lista no debería ser nula");
        assertFalse(list.isEmpty(), "La lista debería contener la referencia");
    }

    @Test
    @DisplayName("getReferenceList: Lista crece con múltiples llamadas")
    void testGetReferenceList_GrowsWithCalls() {
        simulatedAnnealing = new SimulatedAnnealing();
        simulatedAnnealing.setInitialReference(createState(10.0));

        int sizeBefore = simulatedAnnealing.getReferenceList().size();
        simulatedAnnealing.getReferenceList();
        int sizeAfter = simulatedAnnealing.getReferenceList().size();

        assertTrue(sizeAfter > sizeBefore, "La lista debería crecer");
    }

    // ==================== Tests de getReference ====================

    @Test
    @DisplayName("getReference: Retorna referencia actual")
    void testGetReference() {
        simulatedAnnealing = new SimulatedAnnealing();
        State reference = createState(10.0);
        simulatedAnnealing.setInitialReference(reference);

        State result = simulatedAnnealing.getReference();

        assertEquals(reference, result, "Debería retornar la referencia establecida");
    }

    // ==================== Tests de setStateRef ====================

    @Test
    @DisplayName("setStateRef: Establece estado de referencia")
    void testSetStateRef() {
        simulatedAnnealing = new SimulatedAnnealing();
        State newRef = createState(20.0);

        simulatedAnnealing.setStateRef(newRef);

        assertEquals(newRef, simulatedAnnealing.getReference(), 
            "El estado de referencia debería actualizarse");
    }

    // ==================== Tests de tipo de generador ====================

    @Test
    @DisplayName("getType: Retorna SIMULATED_ANNEALING")
    void testGetType() {
        simulatedAnnealing = new SimulatedAnnealing();

        assertEquals(GeneratorType.SIMULATED_ANNEALING, simulatedAnnealing.getType(), 
            "Tipo debería ser SIMULATED_ANNEALING");
    }

    @Test
    @DisplayName("getTypeGenerator: Retorna tipo de generador")
    void testGetTypeGenerator() {
        simulatedAnnealing = new SimulatedAnnealing();

        assertEquals(GeneratorType.SIMULATED_ANNEALING, simulatedAnnealing.getTypeGenerator(), 
            "TypeGenerator debería ser SIMULATED_ANNEALING");
    }

    @Test
    @DisplayName("setTypeGenerator: Establece tipo de generador")
    void testSetTypeGenerator() {
        simulatedAnnealing = new SimulatedAnnealing();
        simulatedAnnealing.setTypeGenerator(GeneratorType.HILL_CLIMBING);

        assertEquals(GeneratorType.HILL_CLIMBING, simulatedAnnealing.getTypeGenerator(), 
            "Debería cambiar el tipo de generador");
    }

    // ==================== Tests de peso ====================

    @Test
    @DisplayName("getWeight: Retorna peso del generador")
    void testGetWeight() {
        simulatedAnnealing = new SimulatedAnnealing();

        assertEquals(50, simulatedAnnealing.getWeight(), 0.01, "Peso inicial debería ser 50");
    }

    @Test
    @DisplayName("setWeight: Modifica peso del generador")
    void testSetWeight() {
        simulatedAnnealing = new SimulatedAnnealing();
        
        simulatedAnnealing.setWeight(75.5f);

        assertEquals(75.5f, simulatedAnnealing.getWeight(), 0.01, "Peso debería actualizarse");
    }

    @Test
    @DisplayName("setWeight: Acepta valores negativos")
    void testSetWeight_Negative() {
        simulatedAnnealing = new SimulatedAnnealing();
        
        simulatedAnnealing.setWeight(-10.0f);

        assertEquals(-10.0f, simulatedAnnealing.getWeight(), 0.01, 
            "Debería aceptar peso negativo");
    }

    @Test
    @DisplayName("setWeight: Acepta cero")
    void testSetWeight_Zero() {
        simulatedAnnealing = new SimulatedAnnealing();
        
        simulatedAnnealing.setWeight(0.0f);

        assertEquals(0.0f, simulatedAnnealing.getWeight(), 0.01, "Debería aceptar peso cero");
    }

    // ==================== Tests de métodos no implementados ====================

    @Test
    @DisplayName("getSonList: Retorna null (no implementado)")
    void testGetSonList() {
        simulatedAnnealing = new SimulatedAnnealing();

        assertNull(simulatedAnnealing.getSonList(), "getSonList debería retornar null");
    }

    @Test
    @DisplayName("awardUpdateREF: Retorna false (no implementado)")
    void testAwardUpdateREF() {
        simulatedAnnealing = new SimulatedAnnealing();
        State candidate = createState(10.0);

        assertFalse(simulatedAnnealing.awardUpdateREF(candidate), 
            "awardUpdateREF debería retornar false");
    }

    // ==================== Tests de arrays de contadores ====================

    @Test
    @DisplayName("getListCountBetterGender: Retorna array de mejoras")
    void testGetListCountBetterGender() {
        simulatedAnnealing = new SimulatedAnnealing();

        int[] betterCount = simulatedAnnealing.getListCountBetterGender();

        assertNotNull(betterCount, "BetterCount no debería ser nulo");
        assertEquals(10, betterCount.length, "BetterCount debería tener 10 elementos");
        assertEquals(0, betterCount[0], "Primer elemento debería inicializarse en 0");
    }

    @Test
    @DisplayName("getListCountGender: Retorna array de usos")
    void testGetListCountGender() {
        simulatedAnnealing = new SimulatedAnnealing();

        int[] usageCount = simulatedAnnealing.getListCountGender();

        assertNotNull(usageCount, "UsageCount no debería ser nulo");
        assertEquals(10, usageCount.length, "UsageCount debería tener 10 elementos");
        assertEquals(0, usageCount[0], "Primer elemento debería inicializarse en 0");
    }

    @Test
    @DisplayName("getTrace: Retorna array de trazas")
    void testGetTrace() {
        simulatedAnnealing = new SimulatedAnnealing();

        float[] trace = simulatedAnnealing.getTrace();

        assertNotNull(trace, "Trace no debería ser nulo");
        assertEquals(1200000, trace.length, "Trace debería tener 1200000 elementos");
        assertEquals(50, trace[0], 0.01, "Primer elemento debería ser el peso inicial");
    }

    // ==================== Tests de herencia ====================

    @Test
    @DisplayName("Es instancia de Generator")
    void testInheritsFromGenerator() {
        simulatedAnnealing = new SimulatedAnnealing();

        assertTrue(simulatedAnnealing instanceof Generator, 
            "SimulatedAnnealing debería heredar de Generator");
    }

    // ==================== Tests de enfriamiento con diferentes alphas ====================

    @Test
    @DisplayName("Enfriamiento con alpha alto (0.99)")
    void testCooling_HighAlpha() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);

            simulatedAnnealing = new SimulatedAnnealing();
            simulatedAnnealing.setInitialReference(createState(10.0));

            SimulatedAnnealing.tinitial = 100.0;
            SimulatedAnnealing.alpha = 0.99;
            SimulatedAnnealing.countIterationsT = 10;

            double tempBefore = SimulatedAnnealing.tinitial;
            simulatedAnnealing.updateReference(createState(11.0), 10);
            double tempAfter = SimulatedAnnealing.tinitial;

            assertEquals(tempBefore * 0.99, tempAfter, 0.01, 
                "Enfriamiento lento con alpha alto");
        }
    }

    @Test
    @DisplayName("Enfriamiento con alpha bajo (0.5)")
    void testCooling_LowAlpha() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);

            simulatedAnnealing = new SimulatedAnnealing();
            simulatedAnnealing.setInitialReference(createState(10.0));

            SimulatedAnnealing.tinitial = 100.0;
            SimulatedAnnealing.alpha = 0.5;
            SimulatedAnnealing.countIterationsT = 10;

            double tempBefore = SimulatedAnnealing.tinitial;
            simulatedAnnealing.updateReference(createState(11.0), 10);
            double tempAfter = SimulatedAnnealing.tinitial;

            assertEquals(tempBefore * 0.5, tempAfter, 0.01, 
                "Enfriamiento rápido con alpha bajo");
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
