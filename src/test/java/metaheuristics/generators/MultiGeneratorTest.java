/**
 * @file MultiGeneratorTest.java
 * @brief Tests unitarios completos para la clase MultiGenerator
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
import java.util.TreeMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import metaheurictics.strategy.Strategy;
import problem.definition.ObjetiveFunction;
import problem.definition.Operator;
import problem.definition.Problem;
import problem.definition.Problem.ProblemType;
import problem.definition.State;

/**
 * @class MultiGeneratorTest
 * @brief Suite de tests exhaustiva para MultiGenerator
 * 
 * Tests que verifican:
 * - Inicialización y destrucción
 * - Generación de estados
 * - Sistema de ruleta
 * - Actualización de pesos
 * - Torneos entre generadores
 * - Gestión de referencias
 */
@DisplayName("Tests para MultiGenerator - Generador Múltiple")
class MultiGeneratorTest {

    private MultiGenerator multiGenerator;
    private Strategy mockStrategy;
    private Problem mockProblem;
    private Operator mockOperator;
    private ObjetiveFunction mockFunction;
    private State mockState;

    @BeforeEach
    void setUp() {
        Strategy.destroyExecute();
        MultiGenerator.destroyMultiGenerator();
        
        mockProblem = mock(Problem.class);
        mockOperator = mock(Operator.class);
        mockFunction = mock(ObjetiveFunction.class);
        mockState = mock(State.class);

        when(mockProblem.getOperator()).thenReturn(mockOperator);
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        ArrayList<ObjetiveFunction> functions = new ArrayList<>();
        functions.add(mockFunction);
        when(mockProblem.getFunction()).thenReturn(functions);
        when(mockFunction.Evaluation(any(State.class))).thenReturn(10.0);
        
        ArrayList<Double> evaluation = new ArrayList<>();
        evaluation.add(10.0);
        when(mockState.getEvaluation()).thenReturn(evaluation);
        when(mockState.getCode()).thenReturn(new ArrayList<>());
        
        Strategy strategy = Strategy.getStrategy();
        strategy.setProblem(mockProblem);
        strategy.mapGenerators = new TreeMap<>();
    }

    @AfterEach
    void tearDown() {
        Strategy.destroyExecute();
        MultiGenerator.destroyMultiGenerator();
    }

    // ==================== Tests de Constructor ====================

    @Test
    @DisplayName("Constructor: Inicialización correcta")
    void testConstructor() {
        multiGenerator = new MultiGenerator();

        assertNotNull(multiGenerator, "MultiGenerator no debería ser null");
        assertEquals(GeneratorType.MULTI_GENERATOR, multiGenerator.getType(), 
            "Tipo debería ser MULTI_GENERATOR");
    }

    @Test
    @DisplayName("Constructor: setGeneratortype")
    void testSetGeneratortype() {
        multiGenerator = new MultiGenerator();
        multiGenerator.setGeneratortype(GeneratorType.MULTI_GENERATOR);

        assertEquals(GeneratorType.MULTI_GENERATOR, multiGenerator.getType(), 
            "Tipo debería ser MULTI_GENERATOR");
    }

    // ==================== Tests de destroyMultiGenerator ====================

    @Test
    @DisplayName("destroyMultiGenerator: Limpiar recursos correctamente")
    void testDestroyMultiGenerator() throws Exception {
        MultiGenerator.initializeListGenerator();
        MultiGenerator.listGeneratedPP.add(mockState);
        MultiGenerator.listStateReference.add(mockState);
        MultiGenerator.activeGenerator = new HillClimbing();

        MultiGenerator.destroyMultiGenerator();

        assertTrue(MultiGenerator.listGeneratedPP.isEmpty(), "listGeneratedPP debería estar vacío");
        assertTrue(MultiGenerator.listStateReference.isEmpty(), "listStateReference debería estar vacío");
        assertNull(MultiGenerator.activeGenerator, "activeGenerator debería ser null");
        assertNull(MultiGenerator.getListGenerators(), "listGenerators debería ser null");
    }

    // ==================== Tests de initializeListGenerator ====================

    @Test
    @DisplayName("initializeListGenerator: Crear lista de generadores")
    void testInitializeListGenerator() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            MultiGenerator.initializeListGenerator();

            assertNotNull(MultiGenerator.getListGenerators(), "Lista de generadores no debería ser null");
            assertEquals(4, MultiGenerator.getListGenerators().length, "Debería tener 4 generadores");
            
            assertTrue(MultiGenerator.getListGenerators()[0] instanceof HillClimbing, 
                "Primer generador debería ser HillClimbing");
            assertTrue(MultiGenerator.getListGenerators()[1] instanceof EvolutionStrategies, 
                "Segundo generador debería ser EvolutionStrategies");
            assertTrue(MultiGenerator.getListGenerators()[2] instanceof LimitThreshold, 
                "Tercer generador debería ser LimitThreshold");
            assertTrue(MultiGenerator.getListGenerators()[3] instanceof GeneticAlgorithm, 
                "Cuarto generador debería ser GeneticAlgorithm");
        }
    }

    // ==================== Tests de getters y setters estáticos ====================

    @Test
    @DisplayName("Getters/Setters: listGenerators")
    void testGetSetListGenerators() throws Exception {
        MultiGenerator.initializeListGenerator();
        Generator[] generators = MultiGenerator.getListGenerators();

        MultiGenerator.setListGenerators(generators);

        assertNotNull(MultiGenerator.getListGenerators(), "Lista de generadores no debería ser null");
        assertEquals(4, MultiGenerator.getListGenerators().length, "Debería tener 4 generadores");
    }

    @Test
    @DisplayName("Getters/Setters: activeGenerator")
    void testGetSetActiveGenerator() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            Generator hc = new HillClimbing();
            MultiGenerator.setActiveGenerator(hc);

            assertEquals(hc, MultiGenerator.getActiveGenerator(), "activeGenerator debería ser HillClimbing");
        }
    }

    @Test
    @DisplayName("Getters/Setters: listGeneratedPP")
    void testGetSetListGeneratedPP() {
        List<State> states = new ArrayList<>();
        states.add(mockState);
        
        MultiGenerator.setListGeneratedPP(states);

        assertNotNull(MultiGenerator.listGeneratedPP, "listGeneratedPP no debería ser null");
        assertEquals(1, MultiGenerator.listGeneratedPP.size(), "Debería contener 1 estado");
    }

    // ==================== Tests de getType ====================

    @Test
    @DisplayName("getType: Retornar tipo correcto")
    void testGetType() {
        multiGenerator = new MultiGenerator();

        assertEquals(GeneratorType.MULTI_GENERATOR, multiGenerator.getType(), 
            "Tipo debería ser MULTI_GENERATOR");
    }

    // ==================== Tests de getReference ====================

    @Test
    @DisplayName("getReference: Retornar null")
    void testGetReference() {
        multiGenerator = new MultiGenerator();

        assertNull(multiGenerator.getReference(), "getReference debería retornar null");
    }

    // ==================== Tests de getReferenceList ====================

    @Test
    @DisplayName("getReferenceList: Retornar lista de referencias")
    void testGetReferenceList() {
        multiGenerator = new MultiGenerator();
        MultiGenerator.listStateReference.add(mockState);

        List<State> refList = multiGenerator.getReferenceList();

        assertNotNull(refList, "Lista de referencias no debería ser null");
        assertEquals(1, refList.size(), "Debería contener 1 estado");
    }

    // ==================== Tests de getSonList ====================

    @Test
    @DisplayName("getSonList: Retornar null")
    void testGetSonList() {
        multiGenerator = new MultiGenerator();

        assertNull(multiGenerator.getSonList(), "getSonList debería retornar null");
    }

    // ==================== Tests de setInitialReference ====================

    @Test
    @DisplayName("setInitialReference: Método vacío")
    void testSetInitialReference() {
        multiGenerator = new MultiGenerator();
        
        // No debería lanzar excepción
        multiGenerator.setInitialReference(mockState);
        
        assertTrue(true, "setInitialReference debería ejecutarse sin errores");
    }

    // ==================== Tests de getWeight ====================

    @Test
    @DisplayName("getWeight: Retornar 0")
    void testGetWeight() {
        multiGenerator = new MultiGenerator();

        assertEquals(0, multiGenerator.getWeight(), "getWeight debería retornar 0");
    }

    // ==================== Tests de setWeight ====================

    @Test
    @DisplayName("setWeight: Método vacío")
    void testSetWeight() {
        multiGenerator = new MultiGenerator();
        
        // No debería lanzar excepción
        multiGenerator.setWeight(50.0f);
        
        assertTrue(true, "setWeight debería ejecutarse sin errores");
    }

    // ==================== Tests de getTrace ====================

    @Test
    @DisplayName("getTrace: Retornar null")
    void testGetTrace() {
        multiGenerator = new MultiGenerator();

        assertNull(multiGenerator.getTrace(), "getTrace debería retornar null");
    }

    // ==================== Tests de awardUpdateREF ====================

    @Test
    @DisplayName("awardUpdateREF: Retornar false")
    void testAwardUpdateREF() {
        multiGenerator = new MultiGenerator();

        assertFalse(multiGenerator.awardUpdateREF(mockState), 
            "awardUpdateREF debería retornar false");
    }

    // ==================== Tests de getListCountBetterGender ====================

    @Test
    @DisplayName("getListCountBetterGender: Retornar null")
    void testGetListCountBetterGender() {
        multiGenerator = new MultiGenerator();

        assertNull(multiGenerator.getListCountBetterGender(), 
            "getListCountBetterGender debería retornar null");
    }

    // ==================== Tests de getListCountGender ====================

    @Test
    @DisplayName("getListCountGender: Retornar null")
    void testGetListCountGender() {
        multiGenerator = new MultiGenerator();

        assertNull(multiGenerator.getListCountGender(), 
            "getListCountGender debería retornar null");
    }

    // ==================== Tests de searchState ====================

    @Test
    @DisplayName("searchState: Maximización - Mejor estado encontrado")
    void testSearchState_Maximization_BetterFound() {
        Strategy strategy = Strategy.getStrategy();
        strategy.setProblem(mockProblem);
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State bestState = new State();
        ArrayList<Double> bestEval = new ArrayList<>();
        bestEval.add(5.0);
        bestState.setEvaluation(bestEval);
        strategy.setBestState(bestState);
        
        State candidate = new State();
        ArrayList<Double> candEval = new ArrayList<>();
        candEval.add(10.0);
        candidate.setEvaluation(candEval);
        
        Generator activeGen = new HillClimbing();
        activeGen.countBetterGender = 0;
        MultiGenerator.setActiveGenerator(activeGen);
        
        multiGenerator = new MultiGenerator();
        boolean result = multiGenerator.searchState(candidate);

        assertTrue(result, "Debería retornar true para mejor estado en maximización");
        assertEquals(1, activeGen.countBetterGender, "countBetterGender debería incrementarse");
    }

    @Test
    @DisplayName("searchState: Maximización - Estado peor")
    void testSearchState_Maximization_Worse() {
        Strategy strategy = Strategy.getStrategy();
        strategy.setProblem(mockProblem);
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State bestState = new State();
        ArrayList<Double> bestEval = new ArrayList<>();
        bestEval.add(15.0);
        bestState.setEvaluation(bestEval);
        strategy.setBestState(bestState);
        
        State candidate = new State();
        ArrayList<Double> candEval = new ArrayList<>();
        candEval.add(10.0);
        candidate.setEvaluation(candEval);
        
        Generator activeGen = new HillClimbing();
        activeGen.countBetterGender = 0;
        MultiGenerator.setActiveGenerator(activeGen);
        
        multiGenerator = new MultiGenerator();
        boolean result = multiGenerator.searchState(candidate);

        assertFalse(result, "Debería retornar false para estado peor en maximización");
        assertEquals(0, activeGen.countBetterGender, "countBetterGender no debería incrementarse");
    }

    @Test
    @DisplayName("searchState: Minimización - Mejor estado encontrado")
    void testSearchState_Minimization_BetterFound() {
        Strategy strategy = Strategy.getStrategy();
        strategy.setProblem(mockProblem);
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        
        State bestState = new State();
        ArrayList<Double> bestEval = new ArrayList<>();
        bestEval.add(15.0);
        bestState.setEvaluation(bestEval);
        strategy.setBestState(bestState);
        
        State candidate = new State();
        ArrayList<Double> candEval = new ArrayList<>();
        candEval.add(10.0);
        candidate.setEvaluation(candEval);
        
        Generator activeGen = new HillClimbing();
        activeGen.countBetterGender = 0;
        MultiGenerator.setActiveGenerator(activeGen);
        
        multiGenerator = new MultiGenerator();
        boolean result = multiGenerator.searchState(candidate);

        assertTrue(result, "Debería retornar true para mejor estado en minimización");
        assertEquals(1, activeGen.countBetterGender, "countBetterGender debería incrementarse");
    }

    @Test
    @DisplayName("searchState: Minimización - Estado peor")
    void testSearchState_Minimization_Worse() {
        Strategy strategy = Strategy.getStrategy();
        strategy.setProblem(mockProblem);
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
        
        State bestState = new State();
        ArrayList<Double> bestEval = new ArrayList<>();
        bestEval.add(5.0);
        bestState.setEvaluation(bestEval);
        strategy.setBestState(bestState);
        
        State candidate = new State();
        ArrayList<Double> candEval = new ArrayList<>();
        candEval.add(10.0);
        candidate.setEvaluation(candEval);
        
        Generator activeGen = new HillClimbing();
        activeGen.countBetterGender = 0;
        MultiGenerator.setActiveGenerator(activeGen);
        
        multiGenerator = new MultiGenerator();
        boolean result = multiGenerator.searchState(candidate);

        assertFalse(result, "Debería retornar false para estado peor en minimización");
        assertEquals(0, activeGen.countBetterGender, "countBetterGender no debería incrementarse");
    }

    // ==================== Tests de updateWeight ====================

    @Test
    @DisplayName("updateWeight: Mejora encontrada - actualizar peso")
    void testUpdateWeight_ImprovementFound() {
        Strategy strategy = Strategy.getStrategy();
        strategy.setCountCurrent(0);
        strategy.setProblem(mockProblem);
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State bestState = new State();
        ArrayList<Double> bestEval = new ArrayList<>();
        bestEval.add(5.0);
        bestState.setEvaluation(bestEval);
        strategy.setBestState(bestState);
        
        State candidate = new State();
        ArrayList<Double> candEval = new ArrayList<>();
        candEval.add(10.0);
        candidate.setEvaluation(candEval);
        
        Generator activeGen = new HillClimbing();
        activeGen.setWeight(50.0f);
        activeGen.countBetterGender = 0;
        MultiGenerator.setActiveGenerator(activeGen);
        MultiGenerator.setListGenerators(new Generator[]{activeGen});
        
        multiGenerator = new MultiGenerator();
        multiGenerator.updateWeight(candidate);

        // Verificar que el peso se actualizó (SC - Success Case)
        float expectedWeight = (float) (50.0 * (1 - 0.1) + 10);
        assertEquals(expectedWeight, activeGen.getWeight(), 0.001f, 
            "Peso debería actualizarse para success case");
    }

    @Test
    @DisplayName("updateWeight: Sin mejora - actualizar peso")
    void testUpdateWeight_NoImprovement() {
        Strategy strategy = Strategy.getStrategy();
        strategy.setCountCurrent(0);
        strategy.setProblem(mockProblem);
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State bestState = new State();
        ArrayList<Double> bestEval = new ArrayList<>();
        bestEval.add(15.0);
        bestState.setEvaluation(bestEval);
        strategy.setBestState(bestState);
        
        State candidate = new State();
        ArrayList<Double> candEval = new ArrayList<>();
        candEval.add(10.0);
        candidate.setEvaluation(candEval);
        
        Generator activeGen = new HillClimbing();
        activeGen.setWeight(50.0f);
        activeGen.countBetterGender = 0;
        MultiGenerator.setActiveGenerator(activeGen);
        MultiGenerator.setListGenerators(new Generator[]{activeGen});
        
        multiGenerator = new MultiGenerator();
        multiGenerator.updateWeight(candidate);

        // Verificar que el peso se actualizó (Imp - Improvement Case, pero sin mejora)
        float expectedWeight = (float) (50.0 * (1 - 0.1));
        assertEquals(expectedWeight, activeGen.getWeight(), 0.001f, 
            "Peso debería actualizarse para no improvement case");
    }

    // ==================== Tests de roulette ====================

    @Test
    @DisplayName("roulette: Seleccionar generador mediante ruleta")
    void testRoulette() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            MultiGenerator.initializeListGenerator();
            
            // Configurar pesos
            for (int i = 0; i < MultiGenerator.getListGenerators().length; i++) {
                MultiGenerator.getListGenerators()[i].setWeight(25.0f);
            }
            
            multiGenerator = new MultiGenerator();
            Generator selected = multiGenerator.roulette();

            assertNotNull(selected, "Generador seleccionado no debería ser null");
            assertTrue(selected instanceof Generator, "Debería ser instancia de Generator");
        }
    }

    @Test
    @DisplayName("roulette: Diferentes pesos - mayor probabilidad")
    void testRoulette_DifferentWeights() throws Exception {
        try (MockedStatic<Strategy> strategyMock = mockStatic(Strategy.class)) {
            strategyMock.when(Strategy::getStrategy).thenReturn(mockStrategy);
            
            MultiGenerator.initializeListGenerator();
            
            // Configurar pesos diferentes - dar mucho peso al primero
            MultiGenerator.getListGenerators()[0].setWeight(90.0f);
            for (int i = 1; i < MultiGenerator.getListGenerators().length; i++) {
                MultiGenerator.getListGenerators()[i].setWeight(3.0f);
            }
            
            multiGenerator = new MultiGenerator();
            
            // Ejecutar múltiples veces para verificar distribución
            int countFirstGenerator = 0;
            int totalRuns = 100;
            for (int i = 0; i < totalRuns; i++) {
                Generator selected = multiGenerator.roulette();
                if (selected == MultiGenerator.getListGenerators()[0]) {
                    countFirstGenerator++;
                }
            }

            // Con 90% de peso, debería seleccionarse la mayoría de veces
            assertTrue(countFirstGenerator > totalRuns / 2, 
                "Generador con mayor peso debería seleccionarse más frecuentemente");
        }
    }

    // ==================== Tests de copy ====================

    @Test
    @DisplayName("copy: Crear copia de MultiGenerator")
    void testCopy() {
        multiGenerator = new MultiGenerator();
        multiGenerator.setGeneratortype(GeneratorType.MULTI_GENERATOR);

        MultiGenerator copy = multiGenerator.copy();

        assertNotNull(copy, "Copia no debería ser null");
        assertEquals(multiGenerator.getType(), copy.getType(), "Tipo debería ser el mismo");
        assertNotSame(multiGenerator, copy, "Debería ser una instancia diferente");
    }

    // ==================== Tests de createInstanceGeneratorsBPP ====================

    // TODO: Fix createInstanceGeneratorsBPP - void method call compilation issue

    // ==================== Tests de generate ====================

    @Test
    @DisplayName("generate: Generar estado mediante ruleta")
    void testGenerate() throws Exception {
        Strategy strategy = Strategy.getStrategy();
        strategy.setProblem(mockProblem);
        strategy.generator = new HillClimbing();
        
        MultiGenerator.initializeListGenerator();
        
        // Configurar generadores para que puedan generar estados
        State refState = new State();
        ArrayList<Double> eval = new ArrayList<>();
        eval.add(10.0);
        refState.setEvaluation(eval);
        refState.setCode(new ArrayList<>());
        
        for (int i = 0; i < MultiGenerator.getListGenerators().length; i++) {
            MultiGenerator.getListGenerators()[i].setInitialReference(refState);
            MultiGenerator.getListGenerators()[i].setWeight(25.0f);
        }
        
        multiGenerator = new MultiGenerator();
        State generated = multiGenerator.generate(1);

        assertNotNull(generated, "Estado generado no debería ser null");
        assertNotNull(MultiGenerator.getActiveGenerator(), 
            "activeGenerator debería estar configurado");
    }

    // ==================== Tests de updateReference ====================

    @Test
    @DisplayName("updateReference: Actualizar referencia con torneo")
    void testUpdateReference() throws Exception {
        Strategy strategy = Strategy.getStrategy();
        strategy.setProblem(mockProblem);
        strategy.setCountCurrent(0);
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        
        State bestState = new State();
        ArrayList<Double> bestEval = new ArrayList<>();
        bestEval.add(5.0);
        bestState.setEvaluation(bestEval);
        strategy.setBestState(bestState);
        
        State candidate = new State();
        ArrayList<Double> candEval = new ArrayList<>();
        candEval.add(10.0);
        candidate.setEvaluation(candEval);
        candidate.setCode(new ArrayList<>());
        
        MultiGenerator.initializeListGenerator();
        
        // Inicializar referencias para cada generador
        State refState = new State();
        ArrayList<Double> refEval = new ArrayList<>();
        refEval.add(8.0);
        refState.setEvaluation(refEval);
        refState.setCode(new ArrayList<>());
        
        for (int i = 0; i < MultiGenerator.getListGenerators().length; i++) {
            MultiGenerator.getListGenerators()[i].setInitialReference(refState);
            MultiGenerator.getListGenerators()[i].setWeight(50.0f);
        }
        
        Generator activeGen = MultiGenerator.getListGenerators()[0];
        MultiGenerator.setActiveGenerator(activeGen);
        
        multiGenerator = new MultiGenerator();
        multiGenerator.updateReference(candidate, 0);

        // Verificar que se ejecutó sin errores
        assertTrue(true, "updateReference debería completarse sin errores");
    }

    // ==================== Tests de tournament ====================

    @Test
    @DisplayName("tournament: Actualizar referencias de todos los generadores")
    void testTournament() throws Exception {
        Strategy strategy = Strategy.getStrategy();
        strategy.setProblem(mockProblem);
        
        State candidate = new State();
        ArrayList<Double> candEval = new ArrayList<>();
        candEval.add(10.0);
        candidate.setEvaluation(candEval);
        candidate.setCode(new ArrayList<>());
        
        MultiGenerator.initializeListGenerator();
        
        // Inicializar referencias
        State refState = new State();
        ArrayList<Double> refEval = new ArrayList<>();
        refEval.add(8.0);
        refState.setEvaluation(refEval);
        refState.setCode(new ArrayList<>());
        
        for (int i = 0; i < MultiGenerator.getListGenerators().length; i++) {
            MultiGenerator.getListGenerators()[i].setInitialReference(refState);
        }
        
        multiGenerator = new MultiGenerator();
        multiGenerator.tournament(candidate, 0);

        // Verificar que se ejecutó sin errores
        assertTrue(true, "tournament debería completarse sin errores");
    }

    // ==================== Tests de updateAwardSC ====================

    @Test
    @DisplayName("updateAwardSC: Actualizar premio por éxito")
    void testUpdateAwardSC() throws Exception {
        Strategy strategy = Strategy.getStrategy();
            strategy.setCountCurrent(0);
            
            Generator activeGen = new HillClimbing();
            activeGen.setWeight(50.0f);
            MultiGenerator.setActiveGenerator(activeGen);
            MultiGenerator.setListGenerators(new Generator[]{activeGen});
            
            multiGenerator = new MultiGenerator();
            multiGenerator.updateAwardSC();

            float expectedWeight = (float) (50.0 * (1 - 0.1) + 10);
            assertEquals(expectedWeight, activeGen.getWeight(), 0.001f, 
                "Peso debería actualizarse correctamente para success case");
    }

    // ==================== Tests de updateAwardImp ====================

    @Test
    @DisplayName("updateAwardImp: Actualizar premio sin mejora")
    void testUpdateAwardImp() throws Exception {
        Strategy strategy = Strategy.getStrategy();
            strategy.setCountCurrent(0);
            
            Generator activeGen = new HillClimbing();
            activeGen.setWeight(50.0f);
            MultiGenerator.setActiveGenerator(activeGen);
            MultiGenerator.setListGenerators(new Generator[]{activeGen});
            
            multiGenerator = new MultiGenerator();
            multiGenerator.updateAwardImp();

            float expectedWeight = (float) (50.0 * (1 - 0.1));
            assertEquals(expectedWeight, activeGen.getWeight(), 0.001f, 
                "Peso debería decrecer para no improvement case");
    }

    // ==================== Tests de initializeGenerators ====================

    @Test
    @DisplayName("initializeGenerators: Inicializar todos los generadores del portafolio")
    void testInitializeGenerators() throws Exception {
        Strategy strategy = Strategy.getStrategy();
        strategy.setProblem(mockProblem);
        
        State stateREF = new State();
        ArrayList<Double> eval = new ArrayList<>();
        eval.add(10.0);
        stateREF.setEvaluation(eval);
        stateREF.setCode(new ArrayList<>());
        when(mockProblem.getState()).thenReturn(stateREF);
        
        EvolutionStrategies.countRef = 4;
        
        MultiGenerator.initializeGenerators();

        assertNotNull(MultiGenerator.getListGenerators(), "Lista de generadores no debería ser null");
        assertFalse(MultiGenerator.listStateReference.isEmpty(), 
            "listStateReference debería tener estado inicial");
        assertEquals(4, MultiGenerator.listGeneratedPP.size(), 
            "listGeneratedPP debería tener estados generados");
    }

    // ==================== Tests Adicionales Simples ====================

    @Test
    @DisplayName("Verificar lista estática listGeneratedPP inicializada")
    void testListGeneratedPP_Initialized() {
        assertNotNull(MultiGenerator.listGeneratedPP, "listGeneratedPP debería estar inicializada");
        assertTrue(MultiGenerator.listGeneratedPP instanceof ArrayList, "Debería ser un ArrayList");
    }

    @Test
    @DisplayName("Verificar lista estática listStateReference inicializada")
    void testListStateReference_Initialized() {
        assertNotNull(MultiGenerator.listStateReference, "listStateReference debería estar inicializada");
        assertTrue(MultiGenerator.listStateReference instanceof ArrayList, "Debería ser un ArrayList");
    }

    @Test
    @DisplayName("Verificar getter de tipo de generador adicional")
    void testGetTypeAdditional() {
        multiGenerator = new MultiGenerator();
        GeneratorType type = multiGenerator.getType();
        assertNotNull(type, "Tipo no debería ser null");
        assertEquals(GeneratorType.MULTI_GENERATOR, type, "Tipo debería ser MULTI_GENERATOR");
    }

    @Test
    @DisplayName("Verificar setter de tipo de generador adicional")
    void testSetGeneratortypeAdditional() {
        multiGenerator = new MultiGenerator();
        multiGenerator.setGeneratortype(GeneratorType.MULTI_GENERATOR);
        assertEquals(GeneratorType.MULTI_GENERATOR, multiGenerator.getType(), "Tipo debería ser actualizado");
    }

    @Test
    @DisplayName("Verificar activeGenerator inicialmente null")
    void testActiveGenerator_InitiallyNull() {
        MultiGenerator.destroyMultiGenerator();
        assertNull(MultiGenerator.activeGenerator, "activeGenerator debería ser null inicialmente");
    }

    @Test
    @DisplayName("Verificar setActiveGenerator")
    void testSetActiveGenerator() {
        Generator gen = new HillClimbing();
        MultiGenerator.setActiveGenerator(gen);
        assertNotNull(MultiGenerator.activeGenerator, "activeGenerator no debería ser null");
        assertSame(gen, MultiGenerator.activeGenerator, "Debería ser el mismo generador");
    }

    @Test
    @DisplayName("Verificar getListGenerators accesible")
    void testGetListGenerators() {
        Generator[] generators = MultiGenerator.getListGenerators();
        // Puede ser null o tener elementos dependiendo del estado de inicialización
        // Solo verificamos que el método es accesible
        assertTrue(generators == null || generators.length >= 0, "getListGenerators debería ser accesible");
    }

    @Test
    @DisplayName("Verificar setListGenerators")
    void testSetListGenerators() {
        Generator[] newGens = new Generator[5];
        newGens[0] = new HillClimbing();
        MultiGenerator.setListGenerators(newGens);
        assertEquals(5, MultiGenerator.getListGenerators().length, "Debería tener 5 elementos");
    }

    @Test
    @DisplayName("Verificar herencia de Generator")
    void testExtendsGenerator() {
        multiGenerator = new MultiGenerator();
        assertTrue(multiGenerator instanceof Generator, "MultiGenerator debería extender Generator");
    }

    @Test
    @DisplayName("Verificar múltiples instancias de MultiGenerator")
    void testMultipleInstances() {
        MultiGenerator mg1 = new MultiGenerator();
        MultiGenerator mg2 = new MultiGenerator();
        assertNotNull(mg1, "Primera instancia no debería ser null");
        assertNotNull(mg2, "Segunda instancia no debería ser null");
        assertNotSame(mg1, mg2, "Deberían ser instancias diferentes");
    }
}

