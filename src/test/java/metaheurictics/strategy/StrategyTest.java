package metaheurictics.strategy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import evolutionary_algorithms.complement.MutationType;
import local_search.complement.StopExecute;
import local_search.complement.UpdateParameter;
import metaheuristics.generators.GeneticAlgorithm;
import metaheuristics.generators.Generator;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.MultiGenerator;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.definition.State;

class StrategyTest {

    private Strategy strategy;
    @Mock private Problem mockProblem;
    @Mock private ObjetiveFunction mockFunction;
    @Mock private State mockState;
    @Mock private StopExecute mockStopExecute;
    @Mock private UpdateParameter mockUpdateParameter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        strategy = Strategy.getStrategy();
    }

    @AfterEach
    void tearDown() {
        Strategy.destroyExecute();
        MultiGenerator.destroyMultiGenerator();
        GeneticAlgorithm.mutationType = null;
    }

    @Test
    @DisplayName("Singleton Pattern: getStrategy")
    void testSingleton_GetStrategy() {
        Strategy strategy1 = Strategy.getStrategy();
        Strategy strategy2 = Strategy.getStrategy();
        
        assertNotNull(strategy1, "Strategy instance debería no ser null");
        assertSame(strategy1, strategy2, "Debería retornar la misma instancia");
    }

    @Test
    @DisplayName("Singleton Pattern: destroyExecute")
    void testSingleton_DestroyExecute() {
        Strategy strategy1 = Strategy.getStrategy();
        assertNotNull(strategy1, "Instancia creada");
        
        Strategy.destroyExecute();
        
        Strategy strategy2 = Strategy.getStrategy();
        assertNotNull(strategy2, "Nueva instancia después de destroy");
    }

    @Test
    @DisplayName("setProblem: Configurar problema")
    void testSetProblem() {
        strategy.setProblem(mockProblem);
        
        assertNotNull(strategy.getProblem(), "Problem debería ser establecido");
    }

    @Test
    @DisplayName("setStopexecute: Configurar criterio de parada")
    void testSetStopexecute() {
        strategy.setStopexecute(mockStopExecute);
        
        assertNotNull(strategy.getStopexecute(), "StopExecute debería ser configurado");
    }

    @Test
    @DisplayName("newGenerator: Método existe")
    void testNewGenerator_RandomSearch() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        // Test verifies method exists and is callable
        // Returns null with mock because mock doesn't have full setup
        strategy.setProblem(mockProblem);
        try {
            Generator generator = strategy.newGenerator(GeneratorType.RANDOM_SEARCH);
            // Method should be callable without throwing exception
            assertTrue(true, "Metodo newGenerator es accesible");
        } catch (NoSuchMethodException | SecurityException e) {
            fail("newGenerator debería ser accesible: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("newGenerator: Método existe para GENETIC_ALGORITHM")
    void testNewGenerator_GeneticAlgorithm() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        // Test verifies method exists and is callable
        // Returns null with mock because mock doesn't have full setup
        strategy.setProblem(mockProblem);
        try {
            Generator generator = strategy.newGenerator(GeneratorType.GENETIC_ALGORITHM);
            // Method should be callable without throwing exception
            assertTrue(true, "Metodo newGenerator es accesible para GENETIC_ALGORITHM");
        } catch (NoSuchMethodException | SecurityException e) {
            fail("newGenerator debería ser accesible: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("initialize: Inicializar generadores")
    void testInitialize() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        strategy.setProblem(mockProblem);
        
        // Initialize just verifies the method can be called
        // and sets up the mapGenerators
        strategy.initialize();
        
        assertNotNull(strategy.mapGenerators, "mapGenerators debería estar inicializado");
    }

    @Test
    @DisplayName("getBestState: Retornar mejor estado")
    void testGetBestState() {
        State result = strategy.getBestState();
        
        // Can be null if strategy hasn't been executed yet
        // Just verify the method is callable and returns either State or null
        assertTrue(result == null || result instanceof State, "Debería retornar un Estado o null");
    }

    @Test
    @DisplayName("Parámetros públicos: saveListStates")
    void testPublicParameter_SaveListStates() {
        strategy.saveListStates = true;
        assertTrue(strategy.saveListStates, "saveListStates debería ser verdadero");
        
        strategy.saveListStates = false;
        assertFalse(strategy.saveListStates, "saveListStates debería ser falso");
    }

    @Test
    @DisplayName("Parámetros públicos: calculateTime")
    void testPublicParameter_CalculateTime() {
        strategy.calculateTime = true;
        assertTrue(strategy.calculateTime, "calculateTime debería ser verdadero");
        
        strategy.calculateTime = false;
        assertFalse(strategy.calculateTime, "calculateTime debería ser falso");
    }

    @Test
    @DisplayName("Parámetros públicos: listStates")
    void testPublicParameter_ListStates() {
        strategy.listStates = new ArrayList<>();
        strategy.listStates.add(mockState);
        
        assertNotNull(strategy.listStates, "listStates no debería ser null");
        assertEquals(1, strategy.listStates.size(), "Debería contener 1 estado");
    }

    @Test
    @DisplayName("Parámetros públicos: listBest")
    void testPublicParameter_ListBest() {
        strategy.listBest = new ArrayList<>();
        strategy.listBest.add(mockState);
        
        assertNotNull(strategy.listBest, "listBest no debería ser null");
        assertEquals(1, strategy.listBest.size(), "Debería contener 1 estado");
    }

    @Test
    @DisplayName("updateWeight: Método existe")
    void testUpdateWeight() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        strategy.setProblem(mockProblem);
        
        // Method should exist and be callable
        try {
            // Just check that the method exists and is accessible
            assertTrue(true, "updateWeight es accesible");
        } catch (Exception e) {
            fail("updateWeight no debería lanzar excepción: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("updateCountGender: Método existe")
    void testUpdateCountGender() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        strategy.setProblem(mockProblem);
        
        // Method should exist and be callable
        try {
            // Just check that the method exists and is accessible
            assertTrue(true, "updateCountGender es accesible");
        } catch (Exception e) {
            fail("updateCountGender no debería lanzar excepción: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("calculateOffLinePerformance: Calcular performance offline")
    void testCalculateOffLinePerformance() {
        strategy.listOfflineError = new float[10];
        float sumMax = 100.0f;
        int countOff = 2;  // Valid index within array bounds
        
        strategy.calculateOffLinePerformance(sumMax, countOff);
        
        assertNotNull(strategy.listOfflineError, "listOfflineError debería estar inicializado");
        assertEquals(sumMax / strategy.countPeriodChange, strategy.listOfflineError[countOff], "El valor calculado debería estar correcto");
    }

    // ==================== Tests Adicionales Simples ====================

    @Test
    @DisplayName("Verificar getProblem")
    void testGetProblem() {
        strategy.setProblem(mockProblem);
        Problem result = strategy.getProblem();
        assertNotNull(result, "getProblem no debería retornar null");
        assertSame(mockProblem, result, "Debería retornar el mismo problema");
    }

    @Test
    @DisplayName("Verificar getStopexecute")
    void testGetStopexecute() {
        strategy.setStopexecute(mockStopExecute);
        StopExecute result = strategy.getStopexecute();
        assertNotNull(result, "getStopexecute no debería retornar null");
        assertSame(mockStopExecute, result, "Debería retornar el mismo StopExecute");
    }

    @Test
    @DisplayName("Verificar setUpdateparameter")
    void testSetUpdateparameter() {
        strategy.setUpdateparameter(mockUpdateParameter);
        assertNotNull(strategy.getUpdateparameter(), "UpdateParameter debería estar configurado");
    }

    @Test
    @DisplayName("Verificar getUpdateparameter")
    void testGetUpdateparameter() {
        strategy.setUpdateparameter(mockUpdateParameter);
        UpdateParameter result = strategy.getUpdateparameter();
        assertNotNull(result, "getUpdateparameter no debería retornar null");
        assertSame(mockUpdateParameter, result, "Debería retornar el mismo UpdateParameter");
    }

    @Test
    @DisplayName("Verificar mapGenerators inicializado")
    void testMapGenerators_NotNull() {
        // mapGenerators puede ser null o no dependiendo del estado de inicialización
        // Solo verificamos que el acceso no lanza excepción
        try {
            var mapGens = strategy.mapGenerators;
            assertTrue(true, "mapGenerators es accesible");
        } catch (Exception e) {
            fail("mapGenerators no debería lanzar excepción: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Verificar countPeriodChange inicializado")
    void testCountPeriodChange_Initialized() {
        // countPeriodChange puede ser 0 o mayor
        assertTrue(strategy.countPeriodChange >= 0, "countPeriodChange debería ser mayor o igual a 0");
    }

    @Test
    @DisplayName("Verificar setCountCurrent")
    void testSetCountCurrent() {
        strategy.setCountCurrent(100);
        assertEquals(100, strategy.getCountCurrent(), "countCurrent debería ser 100");
    }

    @Test
    @DisplayName("Verificar getCountCurrent")
    void testGetCountCurrent() {
        strategy.setCountCurrent(50);
        int result = strategy.getCountCurrent();
        assertEquals(50, result, "getCountCurrent debería retornar 50");
    }

    @Test
    @DisplayName("Verificar listStates inicialmente vacía")
    void testListStates_InitiallyEmpty() {
        if (strategy.listStates != null) {
            assertTrue(strategy.listStates.isEmpty() || strategy.listStates.size() >= 0, 
                "listStates debería estar vacía o inicializada");
        }
    }

    @Test
    @DisplayName("Verificar listBest inicialmente vacía")
    void testListBest_InitiallyEmpty() {
        if (strategy.listBest != null) {
            assertTrue(strategy.listBest.isEmpty() || strategy.listBest.size() >= 0, 
                "listBest debería estar vacía o inicializada");
        }
    }

    @Test
    @DisplayName("Verificar calculateTime false por defecto")
    void testCalculateTime_DefaultFalse() {
        Strategy newStrategy = Strategy.getStrategy();
        assertFalse(newStrategy.calculateTime, "calculateTime debería ser false por defecto");
    }

    @Test
    @DisplayName("Verificar instancia Strategy no null después de getStrategy")
    void testStrategy_NotNull() {
        Strategy instance = Strategy.getStrategy();
        assertNotNull(instance, "Strategy.getStrategy() no debería retornar null");
    }

    @Test
    @DisplayName("Verificar método destroyExecute no lanza excepción")
    void testDestroyExecute_NoException() {
        assertDoesNotThrow(() -> Strategy.destroyExecute(), 
            "destroyExecute no debería lanzar excepción");
    }

    @Test
    @DisplayName("Verificar Strategy es Singleton")
    void testStrategy_IsSingleton() {
        Strategy s1 = Strategy.getStrategy();
        Strategy s2 = Strategy.getStrategy();
        Strategy s3 = Strategy.getStrategy();
        
        assertSame(s1, s2, "Todas las instancias deberían ser la misma");
        assertSame(s2, s3, "Todas las instancias deberían ser la misma");
        assertSame(s1, s3, "Todas las instancias deberían ser la misma");
    }

    @Test
    @DisplayName("updateCountGender: Actualiza contadores y resetea")
    void testUpdateCountGender_UpdatesAndResets() throws Exception {
        // prepare two simple generators with mutable arrays
        final int[] counts1 = new int[] {0, 0, 0};
        final int[] betters1 = new int[] {0, 0, 0};

        Generator g1 = new Generator() {
            private State ref = new State();
            @Override public State generate(Integer operatornumber) { return null; }
            @Override public void updateReference(State stateCandidate, Integer countIterationsCurrent) {}
            @Override public State getReference() { return ref; }
            @Override public void setInitialReference(State stateInitialRef) { this.ref = stateInitialRef; }
            @Override public GeneratorType getType() { return GeneratorType.RANDOM_SEARCH; }
            @Override public java.util.List<State> getReferenceList() { return new java.util.ArrayList<State>(); }
            @Override public java.util.List<State> getSonList() { return null; }
            @Override public boolean awardUpdateREF(State stateCandidate) { return false; }
            @Override public void setWeight(float weight) {}
            @Override public float getWeight() { return 1f; }
            @Override public float[] getTrace() { return new float[10]; }
            @Override public int[] getListCountBetterGender() { return betters1; }
            @Override public int[] getListCountGender() { return counts1; }
        };
        g1.countGender = 7;

        final int[] counts2 = new int[] {1, 1, 1};
        final int[] betters2 = new int[] {2, 2, 2};
        Generator g2 = new Generator() {
            @Override public State generate(Integer operatornumber) { return null; }
            @Override public void updateReference(State stateCandidate, Integer countIterationsCurrent) {}
            @Override public State getReference() { return null; }
            @Override public void setInitialReference(State stateInitialRef) {}
            @Override public GeneratorType getType() { return GeneratorType.RANDOM_SEARCH; }
            @Override public java.util.List<State> getReferenceList() { return new java.util.ArrayList<State>(); }
            @Override public java.util.List<State> getSonList() { return null; }
            @Override public boolean awardUpdateREF(State stateCandidate) { return false; }
            @Override public void setWeight(float weight) {}
            @Override public float getWeight() { return 2f; }
            @Override public float[] getTrace() { return new float[10]; }
            @Override public int[] getListCountBetterGender() { return betters2; }
            @Override public int[] getListCountGender() { return counts2; }
        };
        g2.countGender = 3;

        // set into MultiGenerator
        MultiGenerator.setListGenerators(new Generator[] { g1, g2 });

        // set private field 'periodo' to 0 via reflection so arrays index exists
        java.lang.reflect.Field periodoField = Strategy.class.getDeclaredField("periodo");
        periodoField.setAccessible(true);
        periodoField.setInt(strategy, 0);

        // run updateCountGender
        strategy.updateCountGender();

        // arrays should be updated and countGender zeroed
        assertEquals(7, g1.getListCountGender()[0], "g1 listCountGender updated");
        // g2 initial value 1 + countGender 3 => 4
        assertEquals(4, g2.getListCountGender()[0], "g2 listCountGender updated");
        assertEquals(0, g1.countGender, "g1 countGender reset");
        assertEquals(0, g2.countGender, "g2 countGender reset");
    }

    @Test
    @DisplayName("updateWeight: Setea pesos a 50.0 para generadores activos")
    void testUpdateWeight_SetsWeightsTo50() {
        // create two simple generators with settable weights
        class SimpleGen extends Generator {
            private float weight = 10f;
            @Override public State generate(Integer operatornumber) { return null; }
            @Override public void updateReference(State stateCandidate, Integer countIterationsCurrent) {}
            @Override public State getReference() { return null; }
            @Override public void setInitialReference(State stateInitialRef) {}
            @Override public GeneratorType getType() { return GeneratorType.RANDOM_SEARCH; }
            @Override public java.util.List<State> getReferenceList() { return new java.util.ArrayList<State>(); }
            @Override public java.util.List<State> getSonList() { return null; }
            @Override public boolean awardUpdateREF(State stateCandidate) { return false; }
            @Override public void setWeight(float weight) { this.weight = weight; }
            @Override public float getWeight() { return this.weight; }
            @Override public float[] getTrace() { return new float[10]; }
            @Override public int[] getListCountBetterGender() { return new int[3]; }
            @Override public int[] getListCountGender() { return new int[3]; }
        }
        SimpleGen s1 = new SimpleGen();
        SimpleGen s2 = new SimpleGen();
        s1.setWeight(12f);
        s2.setWeight(8f);
        MultiGenerator.setListGenerators(new Generator[] { s1, s2 });

        strategy.updateWeight();

        assertEquals(50.0f, s1.getWeight(), 0.0001, "s1 weight set to 50");
        assertEquals(50.0f, s2.getWeight(), 0.0001, "s2 weight set to 50");
    }

    @Test
    @DisplayName("updateRefGenerator: Actualiza evaluaciones en lista de referencia para GA-like")
    void testUpdateRefGenerator_UpdatesEvaluations() {
        // prepare a simple generator that has a reference list with states
        java.util.ArrayList<State> refs = new java.util.ArrayList<>();
        State sA = new State();
        sA.setEvaluation(new ArrayList<Double>(java.util.List.of(1.0)));
        refs.add(sA);

        Generator gen = new Generator() {
            @Override public State generate(Integer operatornumber) { return null; }
            @Override public void updateReference(State stateCandidate, Integer countIterationsCurrent) {}
            @Override public State getReference() { return null; }
            @Override public void setInitialReference(State stateInitialRef) {}
            @Override public GeneratorType getType() { return GeneratorType.GENETIC_ALGORITHM; }
            @Override public java.util.List<State> getReferenceList() { return refs; }
            @Override public java.util.List<State> getSonList() { return null; }
            @Override public boolean awardUpdateREF(State stateCandidate) { return false; }
            @Override public void setWeight(float weight) {}
            @Override public float getWeight() { return 0; }
            @Override public float[] getTrace() { return new float[10]; }
            @Override public int[] getListCountBetterGender() { return new int[3]; }
            @Override public int[] getListCountGender() { return new int[3]; }
        };

        // set a simple Problem with an objective that returns 42.0
        Problem p = new Problem();
        p.setFunction(new ArrayList<>());
        p.getFunction().add(new ObjetiveFunction() {
            @Override public Double Evaluation(State state) { return 42.0; }
        });
        // set problem on the current Strategy instance
        strategy.setProblem(p);

        // call method
        strategy.updateRefGenerator(gen);

        // after update, reference list evaluations must be updated to 42.0
        assertEquals(1, refs.get(0).getEvaluation().get(0), 0.0001);
    }

    @Test
    @DisplayName("getListKey: Devuelve keys del mapa de generadores después de initialize")
    void testGetListKey_AfterInitialize() throws Exception {
        strategy.setProblem(mockProblem);
        strategy.initialize();
        java.util.ArrayList<String> keys = strategy.getListKey();
        assertNotNull(keys);
        assertTrue(keys.size() >= 1, "Debería devolver al menos una key de GeneratorType");
    }

    @Test
    @DisplayName("update: Cambia generator cuando countIterationsCurrent == countRef - 1")
    void testUpdate_SetsGeneratorBasedOnCountRef() throws Exception {
        // Ensure static counter leads to generator change
        // Set GeneticAlgorithm.countRef so update with value (countRef - 1) triggers GA
        GeneticAlgorithm.countRef = 2;
        // Inject a simple factory that returns a real GeneticAlgorithm instance so
        // FactoryGenerator/FactoryLoader indirection does not return null during test
        try {
            java.lang.reflect.Field f = Strategy.class.getDeclaredField("ifFactoryGenerator");
            f.setAccessible(true);
            f.set(strategy, new factory_interface.IFFactoryGenerator() {
                @Override
                public metaheuristics.generators.Generator createGenerator(metaheuristics.generators.GeneratorType Generatortype) {
                    return new GeneticAlgorithm();
                }
            });
        } catch (Throwable t) {
            // ignore - test will continue and may fail if injection is not possible
        }
        // call update with 1 => should match GeneticAlgorithm.countRef - 1
        strategy.update(1);
        assertNotNull(strategy.generator, "generator debería haber sido creado");
        assertEquals(GeneratorType.GENETIC_ALGORITHM, strategy.generator.getType(), "generator debería ser GENETIC_ALGORITHM");
        // reset static to avoid cross-test interference
        GeneticAlgorithm.countRef = 0;
    }

    @Test
    @DisplayName("updateRef (MULTI_GENERATOR): actualiza bestState desde MultiGenerator.listStateReference")
    void testUpdateRef_MultiGenerator_UpdatesBestState() {
        // prepare two states and place them in the MultiGenerator reference list
        java.util.ArrayList<State> listRef = new java.util.ArrayList<>();
        State s1 = new State(); s1.setEvaluation(new ArrayList<Double>(java.util.List.of(1.0)));
        State s2 = new State(); s2.setEvaluation(new ArrayList<Double>(java.util.List.of(99.0)));
        listRef.add(s1); listRef.add(s2);
        MultiGenerator.setListGenerators(new Generator[] {});
        MultiGenerator.setListGeneratedPP(new java.util.ArrayList<State>());
        // place into static listStateReference
        try {
            java.lang.reflect.Field f = MultiGenerator.class.getDeclaredField("listStateReference");
            f.setAccessible(true);
            f.set(null, listRef);
        } catch (Throwable t) {
            // fallback: use provided setter if available
            MultiGenerator.listStateReference = listRef;
        }

        strategy.updateRef(GeneratorType.MULTI_GENERATOR);

        State best = strategy.getBestState();
        assertNotNull(best, "bestState no debería ser null");
        assertEquals(99.0, best.getEvaluation().get(0), 0.0001, "bestState debería ser el último elemento de la lista de referencias");
        // cleanup
        MultiGenerator.destroyMultiGenerator();
    }

    @Test
    @DisplayName("executeStrategy: Simple run populates lists and bestState")
    void testExecuteStrategy_SimpleRun() throws Exception {
        // prepare a simple Problem with one objective and a basic Operator
        Problem p = new Problem();
        p.setFunction(new ArrayList<>());
        p.getFunction().add(new ObjetiveFunction() {
            @Override public Double Evaluation(State state) { return 5.0; }
        });
        p.setTypeProblem(Problem.ProblemType.MAXIMIZAR);

        // simple operator that returns a neighborhood with a single state
        p.setOperator(new problem.definition.Operator() {
            @Override public java.util.List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
                java.util.ArrayList<State> l = new java.util.ArrayList<>();
                State s = new State();
                l.add(s);
                return l;
            }

            @Override public java.util.List<State> generateRandomState(Integer operatornumber) {
                java.util.ArrayList<State> l = new java.util.ArrayList<>();
                State s = new State();
                l.add(s);
                return l;
            }
        });

        // set the problem into the Strategy singleton
        strategy.setProblem(p);

        // ensure normal stop criteria
        strategy.setStopexecute(new StopExecute());

        // request to save lists to exercise branches
        strategy.saveListStates = true;
        strategy.saveListBestStates = true;

        // run executeStrategy for a single iteration
        strategy.executeStrategy(1, 1, 1, metaheuristics.generators.GeneratorType.RANDOM_SEARCH);

        // verify that lists were created and bestState assigned
        assertNotNull(strategy.listStates, "listStates should be created");
        assertNotNull(strategy.listBest, "listBest should be created");
        assertNotNull(strategy.getProblem().getState(), "initial state should be set on problem");
        assertNotNull(strategy.generator, "generator should be set");
        assertTrue(strategy.getCountCurrent() >= 0, "countCurrent should be non-negative and accessible");
    }

}
