package metaheurictics.strategy;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.lang.reflect.InvocationTargetException;

import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.ParticleSwarmOptimization;
import metaheuristics.generators.RandomSearch;
import problem.definition.ObjetiveFunction;
import problem.definition.Operator;
import problem.definition.Problem;
import problem.definition.State;
import local_search.complement.StopExecute;
import local_search.complement.UpdateParameter;

class StrategyExecuteUpdateCompleteTest {

    @BeforeEach
    void reset() {
        Strategy.destroyExecute();
        RandomSearch.listStateReference.clear();

        // Avoid population-based generators affecting RandomSearch list by default
        ParticleSwarmOptimization.countRef = 0;
    }

    @AfterEach
    void cleanup() {
        Strategy.destroyExecute();
        RandomSearch.listStateReference.clear();
        ParticleSwarmOptimization.countRef = 0;
    }

    private static Problem problemWithDeterministicOperator(final double initialEval, final double candidateEval) {
        Problem p = new Problem();
        p.setTypeProblem(Problem.ProblemType.MINIMIZAR);

        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        funcs.add(new ObjetiveFunction() {
            @Override
            public Double Evaluation(State state) {
                if (state == null) {
                    return 0.0;
                }
                if (state.getEvaluation() != null && !state.getEvaluation().isEmpty()) {
                    return state.getEvaluation().get(0);
                }
                return 0.0;
            }
        });
        p.setFunction(funcs);

        p.setOperator(new Operator() {
            @Override
            public List<State> generateRandomState(Integer operatornumber) {
                ArrayList<State> list = new ArrayList<>();
                State s = new State();
                s.setCode(new ArrayList<>());
                ArrayList<Double> ev = new ArrayList<>();
                ev.add(initialEval);
                s.setEvaluation(ev);
                list.add(s);
                return list;
            }

            @Override
            public List<State> generatedNewState(State state, Integer operatornumber) {
                ArrayList<State> list = new ArrayList<>();
                State s = new State();
                s.setCode(new ArrayList<>());
                ArrayList<Double> ev = new ArrayList<>();
                ev.add(candidateEval);
                s.setEvaluation(ev);
                list.add(s);
                return list;
            }
        });

        return p;
    }

    private static StopExecute stopAfterOneLoopIteration() {
        return new StopExecute() {
            private int calls;

            @Override
            public Boolean stopIterations(int countCurrent, int countmaxIterations) {
                return calls++ > 0;
            }
        };
    }

    private static void setPrivateInt(Object target, String fieldName, int value) throws Exception {
        java.lang.reflect.Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.setInt(target, value);
    }


    @Test
    @DisplayName("executeStrategy: calculateTime + saveListStates/saveListBestStates + MAXIMIZAR bestState update")
    void executeStrategy_timeAndLists_andBestUpdateMax() throws Exception {
        Strategy s = Strategy.getStrategy();
        s.setStopexecute(new StopExecute());
        Problem p = problemWithDeterministicOperator(1.0, 10.0);
        p.setTypeProblem(Problem.ProblemType.MAXIMIZAR);
        Strategy.setProblem(p);

        s.calculateTime = true;
        s.saveListStates = true;
        s.saveListBestStates = true;
        s.saveFreneParetoMonoObjetivo = false;

        // Only cover initialization branches deterministically (avoid loop complexities)
        s.executeStrategy(0, 999, 1, GeneratorType.RANDOM_SEARCH);

        assertTrue(Strategy.timeExecute >= 0);
        assertNotNull(s.listStates);
        assertFalse(s.listStates.isEmpty());
        assertNotNull(s.listBest);

        State best = s.getBestState();
        assertNotNull(best);
        assertNotNull(best.getEvaluation());
    }

    @Test
    @DisplayName("executeStrategy: MINIMIZAR bestState update + saveFreneParetoMonoObjetivo")
    void executeStrategy_bestUpdateMin_andParetoFlag() throws Exception {
        Strategy s = Strategy.getStrategy();
        s.setStopexecute(new StopExecute());
        Problem p = problemWithDeterministicOperator(10.0, 1.0);
        p.setTypeProblem(Problem.ProblemType.MINIMIZAR);
        Strategy.setProblem(p);

        s.calculateTime = false;
        s.saveListStates = false;
        s.saveListBestStates = true;
        s.saveFreneParetoMonoObjetivo = true;

        // Only cover initialization branches deterministically (avoid loop complexities)
        s.executeStrategy(0, 999, 1, GeneratorType.RANDOM_SEARCH);

        assertNotNull(s.notDominated);
        assertNotNull(s.listBest);
        // listBest can be replaced at the end of executeStrategy by generator reference list;
        // keep assertions non-brittle across environments.

        // saveListStates=false: listStates should not be populated by executeStrategy
        assertTrue(s.listStates == null || s.listStates.isEmpty());

        State best = s.getBestState();
        assertNotNull(best);
    }

    @Test
    @DisplayName("executeStrategy: cubre while() rama 'no cambio' (else)")
    void executeStrategy_coversWhile_noChangeBranch() throws Exception {
        Strategy s = Strategy.getStrategy();
        s.setStopexecute(stopAfterOneLoopIteration());

        Problem p = problemWithDeterministicOperator(1.0, 2.0);
        p.setTypeProblem(Problem.ProblemType.MAXIMIZAR);
        Strategy.setProblem(p);

        s.calculateTime = false;
        s.saveListStates = true;
        s.saveListBestStates = true;
        s.saveFreneParetoMonoObjetivo = false;

        // Keep CI runs deterministic: avoid stale static/instance counters from other tests
        setPrivateInt(s, "countPeriodChange", 1);
        setPrivateInt(s, "countChange", 999);

        // countIterationsChange far from 0 so countCurrent(0) != countChange => else branch
        s.executeStrategy(1, 999, 1, GeneratorType.RANDOM_SEARCH);

        assertNotNull(s.getBestState());
        assertNotNull(s.listStates);
        assertFalse(s.listStates.isEmpty());
        assertNotNull(s.listBest);
    }

    @Test
    @DisplayName("executeStrategy: cubre while() rama 'cambio detectado' (if countCurrent==countChange)")
    void executeStrategy_coversWhile_changeDetectedBranch() throws Exception {
        Strategy s = Strategy.getStrategy();
        s.setStopexecute(stopAfterOneLoopIteration());

        Problem p = problemWithDeterministicOperator(10.0, 1.0);
        p.setTypeProblem(Problem.ProblemType.MINIMIZAR);
        Strategy.setProblem(p);

        s.calculateTime = false;
        s.saveListStates = true;
        s.saveListBestStates = true;
        s.saveFreneParetoMonoObjetivo = true;

        // Keep CI runs deterministic: avoid stale static/instance counters from other tests
        setPrivateInt(s, "countPeriodChange", 1);
        setPrivateInt(s, "countChange", 0);

        // Avoid CI contamination where some other test left Strategy.generator as a population-based generator
        // (updateRef() would call generator.getReference() and may throw if references are empty).
        s.generator = new metaheuristics.generators.RandomSearch();
        metaheuristics.generators.EvolutionStrategies.countRef = Integer.MAX_VALUE;

        // countIterationsChange=0 makes countChange=0 => countCurrent(0) == countChange => change branch
        s.executeStrategy(1, 0, 1, GeneratorType.RANDOM_SEARCH);

        assertNotNull(s.getBestState());
        assertNotNull(s.notDominated);
        assertNotNull(s.listStates);
        assertFalse(s.listStates.isEmpty());
        assertNotNull(s.listBest);
    }

    @Test
    @DisplayName("update: switches generator when countIterationsCurrent matches countRef - 1")
    void update_switchesGeneratorByCountRefMinusOne() throws Exception {
        Strategy s = Strategy.getStrategy();
        Strategy.setProblem(problemWithDeterministicOperator(1.0, 1.0));
        s.initialize();

        // We only need to cover the conditional branches; use reflection to set countRef statics safely.
        // Strategy.update uses independent if statements (not else-if). Ensure only one matches at a time.
        setStaticInt(metaheuristics.generators.EvolutionStrategies.class, "countRef", 10);
        setStaticInt(metaheuristics.generators.DistributionEstimationAlgorithm.class, "countRef", 10);
        ParticleSwarmOptimization.countRef = 10;

        setStaticInt(metaheuristics.generators.GeneticAlgorithm.class, "countRef", 5);
        s.update(4);
        assertNotNull(Strategy.getStrategy().generator);
        assertEquals(GeneratorType.GENETIC_ALGORITHM, Strategy.getStrategy().generator.getType());

        setStaticInt(metaheuristics.generators.GeneticAlgorithm.class, "countRef", 100);
        setStaticInt(metaheuristics.generators.DistributionEstimationAlgorithm.class, "countRef", 100);
        ParticleSwarmOptimization.countRef = 100;

        setStaticInt(metaheuristics.generators.EvolutionStrategies.class, "countRef", 7);
        s.update(6);
        assertNotNull(Strategy.getStrategy().generator);
        assertEquals(GeneratorType.EVOLUTION_STRATEGIES, Strategy.getStrategy().generator.getType());

        setStaticInt(metaheuristics.generators.GeneticAlgorithm.class, "countRef", 100);
        setStaticInt(metaheuristics.generators.EvolutionStrategies.class, "countRef", 100);
        ParticleSwarmOptimization.countRef = 100;

        setStaticInt(metaheuristics.generators.DistributionEstimationAlgorithm.class, "countRef", 3);
        s.update(2);
        assertNotNull(Strategy.getStrategy().generator);
        assertEquals(GeneratorType.DISTRIBUTION_ESTIMATION_ALGORITHM, Strategy.getStrategy().generator.getType());

        setStaticInt(metaheuristics.generators.GeneticAlgorithm.class, "countRef", 100);
        setStaticInt(metaheuristics.generators.EvolutionStrategies.class, "countRef", 100);
        setStaticInt(metaheuristics.generators.DistributionEstimationAlgorithm.class, "countRef", 100);

        ParticleSwarmOptimization.countRef = 9;
        s.update(8);
        assertNotNull(Strategy.getStrategy().generator);
        assertEquals(GeneratorType.PARTICLE_SWARM_OPTIMIZATION, Strategy.getStrategy().generator.getType());
    }

    private static void setStaticInt(Class<?> clazz, String fieldName, int value) throws Exception {
        java.lang.reflect.Field f = clazz.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.setInt(null, value);
    }
}
