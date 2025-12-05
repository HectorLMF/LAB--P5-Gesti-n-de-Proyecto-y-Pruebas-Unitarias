/**
 * @file ParticleSwarmOptimizationTest.java
 * @brief Tests unitarios completos para ParticleSwarmOptimization
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
 * @class ParticleSwarmOptimizationTest
 * @brief Suite de tests exhaustiva para PSO
 * 
 * Tests que verifican:
 * - Inicialización de enjambre
 * - Generación de partículas
 * - Actualización de lBest y gBest
 * - Gestión de referencias
 * - Parámetros del algoritmo
 */
@DisplayName("Tests para ParticleSwarmOptimization - PSO")
class ParticleSwarmOptimizationTest {

    private ParticleSwarmOptimization pso;
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
        
        ArrayList<ObjetiveFunction> functions = new ArrayList<>();
        functions.add(mockFunction);
        when(mockProblem.getFunction()).thenReturn(functions);
        when(mockFunction.Evaluation(any(State.class))).thenReturn(10.0);
        
        ArrayList<Double> evaluation = new ArrayList<>();
        evaluation.add(10.0);
        when(mockState.getEvaluation()).thenReturn(evaluation);
        when(mockState.getCode()).thenReturn(new ArrayList<>());
    }

    @AfterEach
    void tearDown() {
        Strategy.destroyExecute();
        RandomSearch.listStateReference = null;
        ParticleSwarmOptimization.countRef = 0;
        ParticleSwarmOptimization.countParticle = 0;
        ParticleSwarmOptimization.coutSwarm = 0;
        ParticleSwarmOptimization.countParticleBySwarm = 0;
        ParticleSwarmOptimization.lBest = null;
        ParticleSwarmOptimization.gBest = null;
        ParticleSwarmOptimization.countCurrentIterPSO = 0;
    }

    // ==================== Tests de Constructor ====================

    @Test
    @DisplayName("Constructor: Inicialización sin partículas")
    void testConstructor_EmptyParticles() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            ParticleSwarmOptimization.coutSwarm = 2;
            ParticleSwarmOptimization.countParticleBySwarm = 5;
            
            pso = new ParticleSwarmOptimization();

            assertNotNull(pso, "PSO no debería ser null");
            assertEquals(GeneratorType.PARTICLE_SWARM_OPTIMIZATION, pso.getType(), 
                "Tipo debería ser PARTICLE_SWARM_OPTIMIZATION");
            assertEquals(10, ParticleSwarmOptimization.countRef, 
                "countRef debería ser coutSwarm * countParticleBySwarm");
    }

    @Test
    @DisplayName("Constructor: Inicialización con partículas existentes")
    void testConstructor_WithExistingParticles() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            strategy.listBest = new ArrayList<>();
            
            // Crear estados de referencia
            for (int i = 0; i < 4; i++) {
                State state = new State();
                ArrayList<Double> eval = new ArrayList<>();
                eval.add(10.0 + i);
                state.setEvaluation(eval);
                state.setCode(new ArrayList<>());
                RandomSearch.listStateReference.add(state);
            }
            
            ParticleSwarmOptimization.coutSwarm = 2;
            ParticleSwarmOptimization.countParticleBySwarm = 2;
            
            pso = new ParticleSwarmOptimization();

            assertNotNull(pso, "PSO no debería ser null");
            assertFalse(pso.getListParticle().isEmpty(), "Lista de partículas no debería estar vacía");
            assertEquals(1, ParticleSwarmOptimization.countCurrentIterPSO, 
                "countCurrentIterPSO debería inicializarse en 1");
    }

    // ==================== Tests de Getters y Setters ====================

    @Test
    @DisplayName("Getters/Setters: StateReferencePSO")
    void testGetSetStateReferencePSO() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            pso = new ParticleSwarmOptimization();
            
            State refState = new State();
            pso.setStateReferencePSO(refState);

            assertEquals(refState, pso.getStateReferencePSO(), 
                "StateReferencePSO debería ser el configurado");
    }

    @Test
    @DisplayName("Getters/Setters: GeneratorType")
    void testGetSetGeneratorType() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            pso = new ParticleSwarmOptimization();
            pso.setGeneratorType(GeneratorType.PARTICLE_SWARM_OPTIMIZATION);

            assertEquals(GeneratorType.PARTICLE_SWARM_OPTIMIZATION, pso.getGeneratorType(), 
                "GeneratorType debería ser PARTICLE_SWARM_OPTIMIZATION");
    }

    @Test
    @DisplayName("Getters/Setters: CountRef estático")
    void testGetSetCountRef() {
        ParticleSwarmOptimization.setCountRef(100);

        assertEquals(100, ParticleSwarmOptimization.getCountRef(), "countRef debería ser 100");
    }

    @Test
    @DisplayName("Getters/Setters: ListParticle")
    void testGetSetListParticle() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            pso = new ParticleSwarmOptimization();
            
            List<Particle> particles = new ArrayList<>();
            Particle p1 = mock(Particle.class);
            particles.add(p1);
            
            pso.setListParticle(particles);

            assertEquals(1, pso.getListParticle().size(), "Lista de partículas debería tener 1 elemento");
    }

    @Test
    @DisplayName("Getters/Setters: ListStateReference")
    void testGetSetListStateReference() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            pso = new ParticleSwarmOptimization();
            
            List<State> references = new ArrayList<>();
            references.add(mockState);
            
            pso.setListStateReference(references);

            assertNotNull(pso.getListStateReference(), "Lista de referencias no debería ser null");
    }

    // ==================== Tests de getType ====================

    @Test
    @DisplayName("getType: Retornar tipo correcto")
    void testGetType() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            pso = new ParticleSwarmOptimization();

            assertEquals(GeneratorType.PARTICLE_SWARM_OPTIMIZATION, pso.getType(), 
                "Tipo debería ser PARTICLE_SWARM_OPTIMIZATION");
    }

    // ==================== Tests de getReference ====================

    @Test
    @DisplayName("getReference: Retornar null")
    void testGetReference() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            pso = new ParticleSwarmOptimization();

            assertNull(pso.getReference(), "getReference debería retornar null");
    }

    // ==================== Tests de getReferenceList ====================

    @Test
    @DisplayName("getReferenceList: Retornar lista de referencias")
    void testGetReferenceList() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            pso = new ParticleSwarmOptimization();
            
            List<State> refList = pso.getReferenceList();

            assertNotNull(refList, "Lista de referencias no debería ser null");
    }

    // ==================== Tests de getSonList ====================

    @Test
    @DisplayName("getSonList: Retornar null")
    void testGetSonList() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            pso = new ParticleSwarmOptimization();

            assertNull(pso.getSonList(), "getSonList debería retornar null");
    }

    // ==================== Tests de setInitialReference ====================

    @Test
    @DisplayName("setInitialReference: Método vacío")
    void testSetInitialReference() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            pso = new ParticleSwarmOptimization();
            
            // No debería lanzar excepción
            pso.setInitialReference(mockState);

            assertTrue(true, "setInitialReference debería ejecutarse sin errores");
    }

    // ==================== Tests de awardUpdateREF ====================

    @Test
    @DisplayName("awardUpdateREF: Retornar false")
    void testAwardUpdateREF() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            pso = new ParticleSwarmOptimization();

            assertFalse(pso.awardUpdateREF(mockState), "awardUpdateREF debería retornar false");
    }

    // ==================== Tests de setWeight ====================

    @Test
    @DisplayName("setWeight: Método vacío")
    void testSetWeight() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            pso = new ParticleSwarmOptimization();
            
            // No debería lanzar excepción
            pso.setWeight(50.0f);

            assertTrue(true, "setWeight debería ejecutarse sin errores");
    }

    // ==================== Tests de getWeight ====================

    @Test
    @DisplayName("getWeight: Retornar 0")
    void testGetWeight() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            pso = new ParticleSwarmOptimization();

            assertEquals(0, pso.getWeight(), "getWeight debería retornar 0");
    }

    // ==================== Tests de getListCountBetterGender ====================

    @Test
    @DisplayName("getListCountBetterGender: Retornar array")
    void testGetListCountBetterGender() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            pso = new ParticleSwarmOptimization();

            assertNotNull(pso.getListCountBetterGender(), 
                "getListCountBetterGender no debería retornar null");
    }

    // ==================== Tests de getListCountGender ====================

    @Test
    @DisplayName("getListCountGender: Retornar array con countGender")
    void testGetListCountGender() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            pso = new ParticleSwarmOptimization();
            pso.countGender = 5;

            int[] result = pso.getListCountGender();

            assertNotNull(result, "Array no debería ser null");
            assertEquals(10, result.length, "Array debería tener longitud 10");
            assertEquals(5, result[0], "Primer elemento debería ser countGender (5)");
    }

    // ==================== Tests de getTrace ====================

    @Test
    @DisplayName("getTrace: Retornar array de trace")
    void testGetTrace() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            pso = new ParticleSwarmOptimization();

            assertNotNull(pso.getTrace(), "getTrace no debería retornar null");
    }

    // ==================== Tests de generate ====================

    @Test
    @DisplayName("generate: Generar estado de partícula")
    void testGenerate() throws Exception {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            // Crear estados de referencia
            for (int i = 0; i < 4; i++) {
                State state = new State();
                ArrayList<Double> eval = new ArrayList<>();
                eval.add(10.0 + i);
                state.setEvaluation(eval);
                state.setCode(new ArrayList<>());
                RandomSearch.listStateReference.add(state);
            }
            
            ParticleSwarmOptimization.coutSwarm = 2;
            ParticleSwarmOptimization.countParticleBySwarm = 2;
            ParticleSwarmOptimization.countParticle = 0;
            
            pso = new ParticleSwarmOptimization();

            // Mock particle generation
            for (Particle p : pso.getListParticle()) {
                Particle mockParticle = spy(p);
                State generatedState = new State();
                ArrayList<Double> eval = new ArrayList<>();
                eval.add(12.0);
                generatedState.setEvaluation(eval);
                generatedState.setCode(new ArrayList<>());
                when(mockParticle.generate(1)).thenReturn(generatedState);
            }

            State result = pso.generate(1);

            assertNotNull(result, "Estado generado no debería ser null");
    }

    @Test
    @DisplayName("generate: Resetear contador cuando alcanza countRef")
    void testGenerate_ResetCounter() throws Exception {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            strategy.mapGenerators = new TreeMap<>();
            
            // Crear estados de referencia
            for (int i = 0; i < 4; i++) {
                State state = new State();
                ArrayList<Double> eval = new ArrayList<>();
                eval.add(10.0 + i);
                state.setEvaluation(eval);
                state.setCode(new ArrayList<>());
                RandomSearch.listStateReference.add(state);
            }
            
            ParticleSwarmOptimization.coutSwarm = 2;
            ParticleSwarmOptimization.countParticleBySwarm = 2;
            ParticleSwarmOptimization.countRef = 4;
            ParticleSwarmOptimization.countParticle = 4; // Igual a countRef
            
            pso = new ParticleSwarmOptimization();
            pso.generate(1);

            assertEquals(1, ParticleSwarmOptimization.countParticle, 
                "countParticle debería resetearse y luego incrementarse a 1");
    }

    // ==================== Tests de gBestInicial ====================

    @Test
    @DisplayName("gBestInicial: Maximización - Encontrar mejor")
    void testGBestInicial_Maximization() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
            strategy.mapGenerators = new TreeMap<>();
            
            ParticleSwarmOptimization.coutSwarm = 3;
            ParticleSwarmOptimization.lBest = new State[3];
            
            for (int i = 0; i < 3; i++) {
                State state = new State();
                ArrayList<Double> eval = new ArrayList<>();
                eval.add(10.0 + i * 5);
                state.setEvaluation(eval);
                ParticleSwarmOptimization.lBest[i] = state;
            }
            
            pso = new ParticleSwarmOptimization();
            State gBest = pso.gBestInicial();

            assertNotNull(gBest, "gBest no debería ser null");
            assertEquals(20.0, gBest.getEvaluation().get(0), 0.001, 
                "gBest debería tener la mejor evaluación (20.0)");
    }

    @Test
    @DisplayName("gBestInicial: Minimización - Encontrar mejor")
    void testGBestInicial_Minimization() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MINIMIZAR);
            strategy.mapGenerators = new TreeMap<>();
            
            ParticleSwarmOptimization.coutSwarm = 3;
            ParticleSwarmOptimization.lBest = new State[3];
            
            for (int i = 0; i < 3; i++) {
                State state = new State();
                ArrayList<Double> eval = new ArrayList<>();
                eval.add(20.0 - i * 5);
                state.setEvaluation(eval);
                ParticleSwarmOptimization.lBest[i] = state;
            }
            
            pso = new ParticleSwarmOptimization();
            State gBest = pso.gBestInicial();

            assertNotNull(gBest, "gBest no debería ser null");
            assertEquals(10.0, gBest.getEvaluation().get(0), 0.001, 
                "gBest debería tener la mejor evaluación (10.0)");

    }

    // ==================== Tests de updateReference ====================

    @Test
    @DisplayName("updateReference: Maximización - Actualizar lBest y gBest")
    void testUpdateReference_Maximization() throws Exception {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
            strategy.mapGenerators = new TreeMap<>();
            
            // Preparar lBest
            ParticleSwarmOptimization.coutSwarm = 2;
            ParticleSwarmOptimization.countParticleBySwarm = 2;
            ParticleSwarmOptimization.lBest = new State[2];
            ParticleSwarmOptimization.countParticle = 0;
            
            for (int i = 0; i < 2; i++) {
                State state = new State();
                ArrayList<Double> eval = new ArrayList<>();
                eval.add(10.0);
                state.setEvaluation(eval);
                state.setCode(new ArrayList<>());
                state.setTypeGenerator(GeneratorType.PARTICLE_SWARM_OPTIMIZATION);
                ParticleSwarmOptimization.lBest[i] = state;
            }
            
            pso = new ParticleSwarmOptimization();
            
            // Crear partícula con mejor pBest
            Particle particle = mock(Particle.class);
            State pBestState = new State();
            ArrayList<Double> pBestEval = new ArrayList<>();
            pBestEval.add(15.0);
            pBestState.setEvaluation(pBestEval);
            pBestState.setCode(new ArrayList<>());
            pBestState.setTypeGenerator(GeneratorType.PARTICLE_SWARM_OPTIMIZATION);
            when(particle.getStatePBest()).thenReturn(pBestState);
            
            pso.getListParticle().clear();
            pso.getListParticle().add(particle);
            
            // Estado de referencia inicial
            State refState = new State();
            ArrayList<Double> refEval = new ArrayList<>();
            refEval.add(8.0);
            refState.setEvaluation(refEval);
            pso.getReferenceList().add(refState);
            
            State candidate = new State();
            ArrayList<Double> candEval = new ArrayList<>();
            candEval.add(12.0);
            candidate.setEvaluation(candEval);

            pso.updateReference(candidate, 0);

            assertEquals(15.0, ParticleSwarmOptimization.lBest[0].getEvaluation().get(0), 0.001, 
                "lBest[0] debería actualizarse con mejor pBest");
            assertTrue(ParticleSwarmOptimization.countParticle > 0, 
                "countParticle debería incrementarse");
        
    }

    // ==================== Tests de inicialiceLBest ====================

    @Test
    @DisplayName("inicialiceLBest: Maximización - Inicializar lBest correctamente")
    void testInicialiceLBest_Maximization() {
        Strategy strategy = Strategy.getStrategy();
            strategy.setProblem(mockProblem);
            when(mockProblem.getTypeProblem()).thenReturn(ProblemType.MAXIMIZAR);
            strategy.mapGenerators = new TreeMap<>();
            
            // Crear estados de referencia
            for (int i = 0; i < 4; i++) {
                State state = new State();
                ArrayList<Double> eval = new ArrayList<>();
                eval.add(10.0 + i);
                state.setEvaluation(eval);
                state.setCode(new ArrayList<>());
                RandomSearch.listStateReference.add(state);
            }
            
            ParticleSwarmOptimization.coutSwarm = 2;
            ParticleSwarmOptimization.countParticleBySwarm = 2;
            ParticleSwarmOptimization.countParticle = 0;
            
            pso = new ParticleSwarmOptimization();
            pso.inicialiceLBest();

            assertNotNull(ParticleSwarmOptimization.lBest, "lBest no debería ser null");
            assertEquals(2, ParticleSwarmOptimization.lBest.length, "lBest debería tener 2 elementos");
        
    }

    // ==================== Tests de parámetros estáticos ====================

    @Test
    @DisplayName("Parámetros estáticos: Verificar valores por defecto")
    void testStaticParameters() {
        assertEquals(0.9, ParticleSwarmOptimization.wmax, 0.001, "wmax debería ser 0.9");
        assertEquals(0.2, ParticleSwarmOptimization.wmin, 0.001, "wmin debería ser 0.2");
        assertEquals(2, ParticleSwarmOptimization.learning1, "learning1 debería ser 2");
        assertEquals(2, ParticleSwarmOptimization.learning2, "learning2 debería ser 2");
        assertFalse(ParticleSwarmOptimization.binary, "binary debería ser false");
    }

    @Test
    @DisplayName("Parámetros estáticos: Modificar valores")
    void testStaticParameters_Modification() {
        ParticleSwarmOptimization.wmax = 0.95;
        ParticleSwarmOptimization.wmin = 0.15;
        ParticleSwarmOptimization.learning1 = 3;
        ParticleSwarmOptimization.learning2 = 3;
        ParticleSwarmOptimization.binary = true;

        assertEquals(0.95, ParticleSwarmOptimization.wmax, 0.001, "wmax modificado");
        assertEquals(0.15, ParticleSwarmOptimization.wmin, 0.001, "wmin modificado");
        assertEquals(3, ParticleSwarmOptimization.learning1, "learning1 modificado");
        assertEquals(3, ParticleSwarmOptimization.learning2, "learning2 modificado");
        assertTrue(ParticleSwarmOptimization.binary, "binary modificado");
        
        // Restore defaults
        ParticleSwarmOptimization.wmax = 0.9;
        ParticleSwarmOptimization.wmin = 0.2;
        ParticleSwarmOptimization.learning1 = 2;
        ParticleSwarmOptimization.learning2 = 2;
        ParticleSwarmOptimization.binary = false;
    }

    // ==================== Tests Adicionales Simples ====================

    @Test
    @DisplayName("Verificar contador countRef inicializado")
    void testCountRef_Initialized() {
        assertEquals(0, ParticleSwarmOptimization.countRef, "countRef debería ser 0 inicialmente");
    }

    @Test
    @DisplayName("Verificar contador countParticle inicializado")
    void testCountParticle_Initialized() {
        assertEquals(0, ParticleSwarmOptimization.countParticle, "countParticle debería ser 0 inicialmente");
    }

    @Test
    @DisplayName("Verificar contador coutSwarm inicializado")
    void testCoutSwarm_Initialized() {
        assertEquals(0, ParticleSwarmOptimization.coutSwarm, "coutSwarm debería ser 0 inicialmente");
    }

    @Test
    @DisplayName("Verificar contador countParticleBySwarm inicializado")
    void testCountParticleBySwarm_Initialized() {
        assertEquals(0, ParticleSwarmOptimization.countParticleBySwarm, "countParticleBySwarm debería ser 0");
    }

    @Test
    @DisplayName("Verificar gBest inicialmente null")
    void testGBest_InitiallyNull() {
        assertNull(ParticleSwarmOptimization.gBest, "gBest debería ser null inicialmente");
    }

    @Test
    @DisplayName("Verificar lBest inicialmente null")
    void testLBest_InitiallyNull() {
        assertNull(ParticleSwarmOptimization.lBest, "lBest debería ser null inicialmente");
    }

    @Test
    @DisplayName("Verificar parámetro constriction")
    void testConstriction_Parameter() {
        ParticleSwarmOptimization.constriction = 0.729;
        assertEquals(0.729, ParticleSwarmOptimization.constriction, 0.001, "constriction debería ser 0.729");
    }

    @Test
    @DisplayName("Verificar herencia de Generator")
    void testExtendsGenerator() {
        Strategy strategy = Strategy.getStrategy();
        strategy.setProblem(mockProblem);
        strategy.mapGenerators = new TreeMap<>();
        
        pso = new ParticleSwarmOptimization();
        assertTrue(pso instanceof Generator, "PSO debería extender Generator");
    }

    @Test
    @DisplayName("Verificar tipo de generador PSO")
    void testGeneratorType() {
        Strategy strategy = Strategy.getStrategy();
        strategy.setProblem(mockProblem);
        strategy.mapGenerators = new TreeMap<>();
        
        pso = new ParticleSwarmOptimization();
        assertEquals(GeneratorType.PARTICLE_SWARM_OPTIMIZATION, pso.getType(), "Tipo debería ser PARTICLE_SWARM_OPTIMIZATION");
    }

    @Test
    @DisplayName("Verificar countCurrentIterPSO inicialmente 0")
    void testCountCurrentIterPSO_Initially() {
        assertEquals(0, ParticleSwarmOptimization.countCurrentIterPSO, "countCurrentIterPSO debería ser 0");
    }

    @Test
    @DisplayName("Modificar countCurrentIterPSO")
    void testCountCurrentIterPSO_Modification() {
        ParticleSwarmOptimization.countCurrentIterPSO = 10;
        assertEquals(10, ParticleSwarmOptimization.countCurrentIterPSO, "countCurrentIterPSO debería ser 10");
        ParticleSwarmOptimization.countCurrentIterPSO = 0; // Reset
    }

    @Test
    @DisplayName("Verificar múltiples instancias PSO")
    void testMultipleInstances() {
        Strategy strategy = Strategy.getStrategy();
        strategy.setProblem(mockProblem);
        strategy.mapGenerators = new TreeMap<>();
        
        ParticleSwarmOptimization pso1 = new ParticleSwarmOptimization();
        ParticleSwarmOptimization pso2 = new ParticleSwarmOptimization();
        
        assertNotNull(pso1, "Primera instancia no debería ser null");
        assertNotNull(pso2, "Segunda instancia no debería ser null");
        assertNotSame(pso1, pso2, "Deberían ser instancias diferentes");
    }
}

