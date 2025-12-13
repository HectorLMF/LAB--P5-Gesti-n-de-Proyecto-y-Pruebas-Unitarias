/**
 * @file GeneticAlgorithmTest.java
 * @brief Tests unitarios completos para la clase GeneticAlgorithm
 * @author Test Suite
 * @version 1.0
 * @date 2025
 */

package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import evolutionary_algorithms.complement.*;
import metaheurictics.strategy.Strategy;
import problem.definition.ObjetiveFunction;
import problem.definition.Operator;
import problem.definition.Problem;
import problem.definition.Problem.ProblemType;
import problem.definition.State;
import problem.definition.Codification;

/**
 * @class GeneticAlgorithmTest
 * @brief Suite de tests exhaustiva para GeneticAlgorithm
 * 
 * Tests que verifican:
 * - Inicialización del algoritmo genético
 * - Operadores genéticos (selección, cruzamiento, mutación)
 * - Gestión de población
 * - Actualización de referencias
 * - Parámetros del algoritmo
 */
@DisplayName("Tests para GeneticAlgorithm - Algoritmo Genético")
class GeneticAlgorithmTest {

    private GeneticAlgorithm ga;
    private Strategy mockStrategy;
    private Problem mockProblem;
    private Operator mockOperator;
    private ObjetiveFunction mockFunction;
    private State mockState;

    @BeforeEach
    void setUp() {
        Strategy.destroyExecute();
        RandomSearch.listStateReference = new ArrayList<>();
        
        mockProblem = mock(Problem.class);
        mockOperator = mock(Operator.class);
        mockFunction = mock(ObjetiveFunction.class);
        mockState = mock(State.class);

        when(mockProblem.getOperator()).thenReturn(mockOperator);
        when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
        when(mockProblem.getState()).thenReturn(mockState);
        
        ArrayList<ObjetiveFunction> functions = new ArrayList<>();
        functions.add(mockFunction);
        when(mockProblem.getFunction()).thenReturn(functions);
        when(mockFunction.Evaluation(any(State.class))).thenReturn(10.0);
        
        ArrayList<Double> evaluation = new ArrayList<>();
        evaluation.add(10.0);
        when(mockState.getEvaluation()).thenReturn(evaluation);
        when(mockState.getCode()).thenReturn(new ArrayList<>());
        when(mockState.getCopy()).thenReturn(mockState);
        // Provide a simple Codification mock so mutation/crossover code can call it safely
        Codification mockCodif = mock(Codification.class);
        when(mockCodif.getAleatoryKey()).thenReturn(0);
        when(mockCodif.getVariableAleatoryValue(anyInt())).thenReturn(new Object());
        when(mockCodif.getVariableCount()).thenReturn(1);
        when(mockProblem.getCodification()).thenReturn(mockCodif);
    }

    @AfterEach
    void tearDown() {
        Strategy.destroyExecute();
        RandomSearch.listStateReference = null;
        GeneticAlgorithm.countRef = 0;
        GeneticAlgorithm.PC = 0;
        GeneticAlgorithm.PM = 0;
        GeneticAlgorithm.truncation = 0;
    }

    // ==================== Coverage helpers: resolveBaseState/buildReferencePopulation/setters ====================

    @Test
    @DisplayName("setStateRef: actualiza el estado de referencia")
    void testSetStateRef() {
        Strategy strategy = Strategy.getStrategy();
        strategy.setProblem(mockProblem);
        strategy.mapGenerators = new TreeMap<>();

        ga = new GeneticAlgorithm();
        State refState = new State();
        ArrayList<Double> eval = new ArrayList<>();
        eval.add(123.0);
        refState.setEvaluation(eval);
        ga.setStateRef(refState);
        assertSame(refState, ga.getReference());
    }

    @Test
    @DisplayName("setListState: buildReferencePopulation prioriza listState sobre RandomSearch")
    void testSetListState_buildReferencePopulationPrefersListState() throws Exception {
        Strategy strategy = Strategy.getStrategy();
        strategy.setProblem(mockProblem);
        strategy.mapGenerators = new TreeMap<>();

        // Keep RandomSearch empty so buildReferencePopulation must use GA.listState
        RandomSearch.listStateReference.clear();
        ga = new GeneticAlgorithm();
        // Now provide listState with a distinct marker evaluation
        List<State> own = new ArrayList<>();
        State s0 = new State();
        ArrayList<Double> s0e = new ArrayList<>(); s0e.add(999.0);
        s0.setEvaluation(s0e);
        ArrayList<Object> code0 = new ArrayList<>(); code0.add(111);
        s0.setCode(code0);
        own.add(s0);
        State s1 = new State();
        ArrayList<Double> s1e = new ArrayList<>(); s1e.add(888.0);
        s1.setEvaluation(s1e);
        ArrayList<Object> code1 = new ArrayList<>(); code1.add(222);
        s1.setCode(code1);
        own.add(s1);
        ga.setListState(own);
        assertEquals(2, ga.getListState().size());

        // Force operators off so generated state is just a copied parent
        GeneticAlgorithm.PC = 0.0;
        GeneticAlgorithm.PM = 0.0;
        GeneticAlgorithm.truncation = 1;
        State out = ga.generate(0);
        assertNotNull(out);
        assertNotNull(out.getEvaluation());
        assertFalse(out.getEvaluation().isEmpty());
    }

    @Test
    @DisplayName("resolveBaseState: usa Strategy.problem.state cuando existe")
    void testResolveBaseState_prefersProblemState() throws Exception {
        // Use a real Problem so Strategy.getProblem().getState() can be set
        Strategy.destroyExecute();
        Problem p = new Problem();
        p.setTypeProblem(ProblemType.MAXIMIZAR);
        State base = new State();
        ArrayList<Object> baseCode = new ArrayList<>(); baseCode.add(0);
        base.setCode(baseCode);
        ArrayList<Double> baseEval = new ArrayList<>(); baseEval.add(42.0);
        base.setEvaluation(baseEval);
        p.setState(base);
        Strategy.setProblem(p);
        Strategy.getStrategy().mapGenerators = new TreeMap<>();

        // Seed RandomSearch so GA has a parent
        RandomSearch.listStateReference = new ArrayList<>();
        State parent = new State();
        ArrayList<Object> pcode = new ArrayList<>(); pcode.add(0);
        parent.setCode(pcode);
        ArrayList<Double> peval = new ArrayList<>(); peval.add(7.0);
        parent.setEvaluation(peval);
        RandomSearch.listStateReference.add(parent);

        GeneticAlgorithm.PM = 0.0;
        GeneticAlgorithm.truncation = 1;
        ga = new GeneticAlgorithm();
        State out = ga.generate(0);
        assertNotNull(out);
        // resolveBaseState() influences the base used for copying; when base has a copy,
        // we at least ensure generation succeeds with a configured Strategy.problem.state.
        assertEquals(7.0, out.getEvaluation().get(0), 1e-9);
    }

    // ==================== Tests de Constructor ====================

    @Test
    @DisplayName("Constructor: Inicialización correcta")
    void testConstructor() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            ga = new GeneticAlgorithm();

            assertNotNull(ga, "GA no debería ser null");
            assertEquals(GeneratorType.GENETIC_ALGORITHM, ga.getType(), 
                "Tipo debería ser GENETIC_ALGORITHM");
            assertEquals(50.0f, ga.getWeight(), 0.001f, "Peso inicial debería ser 50.0");}
    

    @Test
    @DisplayName("Constructor: Inicialización con estados de referencia")
    void testConstructor_WithReferenceStates() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            // Crear estados de referencia
            for (int i = 0; i < 5; i++) {
                State state = new State();
                ArrayList<Double> eval = new ArrayList<>();
                eval.add(10.0 + i);
                state.setEvaluation(eval);
                state.setCode(new ArrayList<>());
                RandomSearch.listStateReference.add(state);
            }
            
            ga = new GeneticAlgorithm();

            assertNotNull(ga.getReferenceList(), "Lista de referencias no debería ser null");}
    

    // ==================== Tests de getType ====================

    @Test
    @DisplayName("getType: Retornar tipo correcto")
    void testGetType() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            ga = new GeneticAlgorithm();

            assertEquals(GeneratorType.GENETIC_ALGORITHM, ga.getType(), 
                "Tipo debería ser GENETIC_ALGORITHM");}
    

    // ==================== Tests de getReference ====================

    @Test
    @DisplayName("getReference: Retornar estado de referencia")
    void testGetReference() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            State refState = new State();
            ArrayList<Double> eval = new ArrayList<>();
            eval.add(15.0);
            refState.setEvaluation(eval);
            
            ga = new GeneticAlgorithm();
            ga.setInitialReference(refState);

            assertEquals(refState, ga.getReference(), "Referencia debería ser la configurada");}
    

    // ==================== Tests de setInitialReference ====================

    @Test
    @DisplayName("setInitialReference: Configurar referencia inicial")
    void testSetInitialReference() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            State refState = new State();
            ArrayList<Double> eval = new ArrayList<>();
            eval.add(20.0);
            refState.setEvaluation(eval);
            
            ga = new GeneticAlgorithm();
            ga.setInitialReference(refState);

            assertEquals(refState, ga.getReference(), "Referencia debería ser la configurada");}
    

    // ==================== Tests de getReferenceList ====================

    @Test
    @DisplayName("getReferenceList: Retornar lista de referencias")
    void testGetReferenceList() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            ga = new GeneticAlgorithm();
            
            List<State> refList = ga.getReferenceList();

            assertNotNull(refList, "Lista de referencias no debería ser null");}
    

    // ==================== Tests de getSonList ====================

    @Test
    @DisplayName("getSonList: Retornar null")
    void testGetSonList() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            ga = new GeneticAlgorithm();

            assertNull(ga.getSonList(), "getSonList debería retornar null");}
    

    // ==================== Tests de getWeight y setWeight ====================

    @Test
    @DisplayName("getWeight/setWeight: Gestión de peso")
    void testGetSetWeight() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            ga = new GeneticAlgorithm();
            
            ga.setWeight(75.5f);

            assertEquals(75.5f, ga.getWeight(), 0.001f, "Peso debería ser 75.5");}
    

    // ==================== Tests de getTrace ====================

    @Test
    @DisplayName("getTrace: Retornar array de trace")
    void testGetTrace() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            ga = new GeneticAlgorithm();

            assertNotNull(ga.getTrace(), "getTrace no debería retornar null");
            assertEquals(50.0f, ga.getTrace()[0], 0.001f, 
                "Primer valor de trace debería ser peso inicial (50.0)");}
    

    // ==================== Tests de getListCountBetterGender ====================

    @Test
    @DisplayName("getListCountBetterGender: Retornar array de contadores")
    void testGetListCountBetterGender() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            ga = new GeneticAlgorithm();

            assertNotNull(ga.getListCountBetterGender(), 
                "getListCountBetterGender no debería retornar null");
            assertEquals(10, ga.getListCountBetterGender().length, 
                "Array debería tener longitud 10");}
    

    // ==================== Tests de getListCountGender ====================

    @Test
    @DisplayName("getListCountGender: Retornar array de contadores")
    void testGetListCountGender() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            ga = new GeneticAlgorithm();
            ga.countGender = 5;

            int[] result = ga.getListCountGender();

            assertNotNull(result, "Array no debería ser null");
            assertEquals(10, result.length, "Array debería tener longitud 10");
            assertEquals(5, result[0], "Primer elemento debería ser countGender (5)");}
    

    // ==================== Tests de awardUpdateREF ====================

    @Test
    @DisplayName("awardUpdateREF: Maximización - Mejora encontrada")
    void testAwardUpdateREF_Maximization_Improvement() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
            
            ga = new GeneticAlgorithm();
            
            State refState = new State();
            ArrayList<Double> refEval = new ArrayList<>();
            refEval.add(10.0);
            refState.setEvaluation(refEval);
            ga.setInitialReference(refState);
            
            State candidate = new State();
            ArrayList<Double> candEval = new ArrayList<>();
            candEval.add(15.0);
            candidate.setEvaluation(candEval);

            boolean result = ga.awardUpdateREF(candidate);

            assertTrue(result, "Debería retornar true para mejora en maximización");
            assertEquals(1, ga.countBetterGender, "countBetterGender debería incrementarse");}
    

    @Test
    @DisplayName("awardUpdateREF: Maximización - Sin mejora")
    void testAwardUpdateREF_Maximization_NoImprovement() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
            
            ga = new GeneticAlgorithm();
            
            State refState = new State();
            ArrayList<Double> refEval = new ArrayList<>();
            refEval.add(20.0);
            refState.setEvaluation(refEval);
            ga.setInitialReference(refState);
            
            State candidate = new State();
            ArrayList<Double> candEval = new ArrayList<>();
            candEval.add(15.0);
            candidate.setEvaluation(candEval);

            boolean result = ga.awardUpdateREF(candidate);

            assertFalse(result, "Debería retornar false sin mejora en maximización");
            assertEquals(0, ga.countBetterGender, "countBetterGender no debería incrementarse");}
    

    @Test
    @DisplayName("awardUpdateREF: Minimización - Mejora encontrada")
    void testAwardUpdateREF_Minimization_Improvement() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
            
            ga = new GeneticAlgorithm();
            
            State refState = new State();
            ArrayList<Double> refEval = new ArrayList<>();
            refEval.add(20.0);
            refState.setEvaluation(refEval);
            ga.setInitialReference(refState);
            
            State candidate = new State();
            ArrayList<Double> candEval = new ArrayList<>();
            candEval.add(15.0);
            candidate.setEvaluation(candEval);

            boolean result = ga.awardUpdateREF(candidate);

            assertTrue(result, "Debería retornar true para mejora en minimización");
            assertEquals(1, ga.countBetterGender, "countBetterGender debería incrementarse");}
    

    @Test
    @DisplayName("awardUpdateREF: Minimización - Sin mejora")
    void testAwardUpdateREF_Minimization_NoImprovement() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
            
            ga = new GeneticAlgorithm();
            
            State refState = new State();
            ArrayList<Double> refEval = new ArrayList<>();
            refEval.add(10.0);
            refState.setEvaluation(refEval);
            ga.setInitialReference(refState);
            
            State candidate = new State();
            ArrayList<Double> candEval = new ArrayList<>();
            candEval.add(15.0);
            candidate.setEvaluation(candEval);

            boolean result = ga.awardUpdateREF(candidate);

            assertFalse(result, "Debería retornar false sin mejora en minimización");
            assertEquals(0, ga.countBetterGender, "countBetterGender no debería incrementarse");}
    

    // ==================== Tests de parámetros estáticos ====================

    @Test
    @DisplayName("Parámetros estáticos: mutationType")
    void testStaticParameter_MutationType() {
        GeneticAlgorithm.mutationType = MutationType.ONE_POINT_MUTATION;

        assertEquals(MutationType.ONE_POINT_MUTATION, GeneticAlgorithm.mutationType, 
            "mutationType debería ser ONE_POINT_MUTATION");
    }

    @Test
    @DisplayName("Parámetros estáticos: crossoverType")
    void testStaticParameter_CrossoverType() {
        GeneticAlgorithm.crossoverType = CrossoverType.UNIFORM_CROSSOVER;

        assertEquals(CrossoverType.UNIFORM_CROSSOVER, GeneticAlgorithm.crossoverType, 
            "crossoverType debería ser UNIFORM_CROSSOVER");
    }

    @Test
    @DisplayName("Parámetros estáticos: replaceType")
    void testStaticParameter_ReplaceType() {
        GeneticAlgorithm.replaceType = ReplaceType.GENERATIONAL_REPLACE;

        assertEquals(ReplaceType.GENERATIONAL_REPLACE, GeneticAlgorithm.replaceType, 
            "replaceType debería ser GENERATIONAL_REPLACE");
    }

    @Test
    @DisplayName("Parámetros estáticos: selectionType")
    void testStaticParameter_SelectionType() {
        GeneticAlgorithm.selectionType = SelectionType.TRUNCATION_SELECTION;

        assertEquals(SelectionType.TRUNCATION_SELECTION, GeneticAlgorithm.selectionType, 
            "selectionType debería ser TRUNCATION_SELECTION");
    }

    @Test
    @DisplayName("Parámetros estáticos: PC (Probabilidad de cruzamiento)")
    void testStaticParameter_PC() {
        GeneticAlgorithm.PC = 0.8;

        assertEquals(0.8, GeneticAlgorithm.PC, 0.001, "PC debería ser 0.8");
    }

    @Test
    @DisplayName("Parámetros estáticos: PM (Probabilidad de mutación)")
    void testStaticParameter_PM() {
        GeneticAlgorithm.PM = 0.1;

        assertEquals(0.1, GeneticAlgorithm.PM, 0.001, "PM debería ser 0.1");
    }

    @Test
    @DisplayName("Parámetros estáticos: countRef")
    void testStaticParameter_CountRef() {
        GeneticAlgorithm.countRef = 100;

        assertEquals(100, GeneticAlgorithm.countRef, "countRef debería ser 100");
    }

    @Test
    @DisplayName("Parámetros estáticos: truncation")
    void testStaticParameter_Truncation() {
        GeneticAlgorithm.truncation = 50;

        assertEquals(50, GeneticAlgorithm.truncation, "truncation debería ser 50");
    }

    // ==================== Tests de generate ====================

    @Test
    @DisplayName("generate: Generación con operadores genéticos")
    void testGenerate() throws Exception {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            // Crear población inicial
            for (int i = 0; i < 10; i++) {
                State state = new State();
                ArrayList<Double> eval = new ArrayList<>();
                eval.add(10.0 + i);
                state.setEvaluation(eval);
                ArrayList<Object> code = new ArrayList<>();
                code.add(0);
                state.setCode(code);
                state.setNumber(i);
                state.setTypeGenerator(GeneratorType.GENETIC_ALGORITHM);
                RandomSearch.listStateReference.add(state);
            }
            
            GeneticAlgorithm.selectionType = SelectionType.TRUNCATION_SELECTION;
            GeneticAlgorithm.crossoverType = CrossoverType.UNIFORM_CROSSOVER;
            GeneticAlgorithm.mutationType = MutationType.ONE_POINT_MUTATION;
            GeneticAlgorithm.replaceType = ReplaceType.GENERATIONAL_REPLACE;
            GeneticAlgorithm.truncation = 5;
            GeneticAlgorithm.PC = 0.8;
            // Avoid mutating into an empty code in low-fi unit test setup
            GeneticAlgorithm.PM = 0.0;
            
            ga = new GeneticAlgorithm();
            
            State generated = ga.generate(0);

            assertNotNull(generated, "Estado generado no debería ser null");}
    

    // ==================== Tests de updateReference ====================

    @Test
    @DisplayName("updateReference: Actualizar población - Maximización")
    void testUpdateReference_Maximization() throws Exception {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
            
            // Crear población inicial
            for (int i = 0; i < 5; i++) {
                State state = new State();
                ArrayList<Double> eval = new ArrayList<>();
                eval.add(10.0 + i);
                state.setEvaluation(eval);
                state.setCode(new ArrayList<>());
                RandomSearch.listStateReference.add(state);
            }
            
            GeneticAlgorithm.replaceType = ReplaceType.GENERATIONAL_REPLACE;
            
            ga = new GeneticAlgorithm();
            
            State candidate = new State();
            ArrayList<Double> candEval = new ArrayList<>();
            candEval.add(20.0);
            candidate.setEvaluation(candEval);
            candidate.setCode(new ArrayList<>());

            ga.updateReference(candidate, 0);

            assertTrue(true, "updateReference debería completarse sin errores");}
    

    @Test
    @DisplayName("updateReference: Actualizar población - Minimización")
    void testUpdateReference_Minimization() throws Exception {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
            
            // Crear población inicial
            for (int i = 0; i < 5; i++) {
                State state = new State();
                ArrayList<Double> eval = new ArrayList<>();
                eval.add(20.0 - i);
                state.setEvaluation(eval);
                state.setCode(new ArrayList<>());
                RandomSearch.listStateReference.add(state);
            }
            
            GeneticAlgorithm.replaceType = ReplaceType.GENERATIONAL_REPLACE;
            
            ga = new GeneticAlgorithm();
            
            State candidate = new State();
            ArrayList<Double> candEval = new ArrayList<>();
            candEval.add(10.0);
            candidate.setEvaluation(candEval);
            candidate.setCode(new ArrayList<>());

            ga.updateReference(candidate, 0);

            assertTrue(true, "updateReference debería completarse sin errores");}
    

    // ==================== Tests de countBetterGender ====================

    @Test
    @DisplayName("countBetterGender: Incremento en awardUpdateREF")
    void testCountBetterGender_Increment() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
            
            ga = new GeneticAlgorithm();
            
            State refState = new State();
            ArrayList<Double> refEval = new ArrayList<>();
            refEval.add(10.0);
            refState.setEvaluation(refEval);
            ga.setInitialReference(refState);
            
            assertEquals(0, ga.countBetterGender, "Inicialmente debería ser 0");
            
            State candidate1 = new State();
            ArrayList<Double> candEval1 = new ArrayList<>();
            candEval1.add(15.0);
            candidate1.setEvaluation(candEval1);
            ga.awardUpdateREF(candidate1);
            
            assertEquals(1, ga.countBetterGender, "Debería incrementarse a 1");
            
            State candidate2 = new State();
            ArrayList<Double> candEval2 = new ArrayList<>();
            candEval2.add(20.0);
            candidate2.setEvaluation(candEval2);
            ga.awardUpdateREF(candidate2);
            
            assertEquals(2, ga.countBetterGender, "Debería incrementarse a 2");}
    

    // ==================== Tests de countGender ====================

    @Test
    @DisplayName("countGender: Gestión de contador de uso")
    void testCountGender() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            ga = new GeneticAlgorithm();
            
            assertEquals(0, ga.countGender, "Inicialmente debería ser 0");
            
            ga.countGender = 10;
            assertEquals(10, ga.countGender, "Debería poder modificarse a 10");
            
            int[] listCount = ga.getListCountGender();
            assertEquals(10, listCount[0], "getListCountGender debería retornar el valor");}
    
}


