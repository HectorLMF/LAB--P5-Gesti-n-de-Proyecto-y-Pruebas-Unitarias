package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.Codification;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;
import metaheuristics.generators.Generator;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.RandomSearch;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class EvolutionStrategiesTest {

    @BeforeEach
    public void setUp() {
        // ensure fresh strategy & a minimal Problem so methods using Strategy won't NPE
        Strategy.destroyExecute();
        Problem problem = new Problem();
        problem.setTypeProblem(ProblemType.MAXIMIZAR);
        // Function is needed by other generators; keep a minimal one here too
        ArrayList<ObjetiveFunction> functions = new ArrayList<>();
        functions.add(new ObjetiveFunction() {
            @Override
            public Double Evaluation(State state) {
                if (state.getEvaluation() != null && !state.getEvaluation().isEmpty()) {
                    return state.getEvaluation().get(0);
                }
                return 0.0;
            }
        });
        problem.setFunction(functions);
        // Minimal codification to support mutation operators used by ES
        problem.setCodification(new Codification() {
            @Override public boolean validState(State state) { return true; }
            @Override public Object getVariableAleatoryValue(int key) { return "B"; }
            @Override public int getAleatoryKey() { return 0; }
            @Override public int getVariableCount() { return 1; }
        });
        Strategy.setProblem(problem);
        Strategy.getStrategy().setCountMax(10);

        // keep RandomSearch predictable across tests
        RandomSearch.listStateReference = new ArrayList<>();

        // install a minimal stub generator to avoid code paths that reference Strategy.generator
        Strategy.getStrategy().generator = new Generator() {
            @Override public State generate(Integer operatornumber) { return null; }
            @Override public void updateReference(State stateCandidate, Integer countIterationsCurrent) { }
            @Override public State getReference() { return null; }
            @Override public void setInitialReference(State stateInitialRef) { }
            @Override public GeneratorType getType() { return GeneratorType.EVOLUTION_STRATEGIES; }
            @Override public java.util.List<State> getReferenceList() { return new ArrayList<>(); }
            @Override public java.util.List<State> getSonList() { return new ArrayList<>(); }
            @Override public boolean awardUpdateREF(State stateCandidate) { return false; }
            @Override public void setWeight(float weight) { }
            @Override public float getWeight() { return 0; }
            @Override public float[] getTrace() { return new float[0]; }
            @Override public int[] getListCountBetterGender() { return new int[0]; }
            @Override public int[] getListCountGender() { return new int[0]; }
        };
    }

    @Test
    public void testGenerate_FillsReferenceListFromRandomSearchWhenEmpty() throws Exception {
        EvolutionStrategies es = new EvolutionStrategies();
        es.setListStateReference(new ArrayList<>());

        State rs = new State();
        rs.setNumber(11);
        rs.setCode(new ArrayList<>(List.of("A")));
        rs.setEvaluation(new ArrayList<>(List.of(3.0)));
        rs.setTypeGenerator(GeneratorType.EVOLUTION_STRATEGIES);
        RandomSearch.listStateReference.clear();
        RandomSearch.listStateReference.add(rs);

        State out = es.generate(0);
        assertNotNull(out);
        // It should have at least pulled from RandomSearch into its own list
        assertFalse(es.getListStateReference().isEmpty());
    }

    @Test
    public void testGenerate_ReturnsNullWhenNoReferencesAnywhere() throws Exception {
        EvolutionStrategies es = new EvolutionStrategies();
        es.setListStateReference(new ArrayList<>());
        RandomSearch.listStateReference.clear();

        State out = es.generate(0);
        assertNull(out);
    }

    @Test
    public void testGenerate_SelectionThrows_FallsBackToReferenceList() throws Exception {
        EvolutionStrategies es = new EvolutionStrategies();

        // Provide a local reference list
        State father = new State();
        father.setNumber(5);
        father.setCode(new ArrayList<>(List.of("X")));
        father.setEvaluation(new ArrayList<>(List.of(9.0)));
        father.setTypeGenerator(GeneratorType.EVOLUTION_STRATEGIES);
        es.setListStateReference(new ArrayList<>(List.of(father)));

        // Replace internal selection factory with a throwing proxy so catch(Exception) executes
        Object throwingFactory = Proxy.newProxyInstance(
            EvolutionStrategies.class.getClassLoader(),
            new Class<?>[]{factory_interface.IFFactoryFatherSelection.class},
            (proxy, method, args) -> {
                if (method.getName().equals("createSelectFather")) {
                    // Return a FatherSelection whose selection throws
                    return new evolutionary_algorithms.complement.FatherSelection() {
                        @Override
                        public List<State> selection(List<State> listState, int truncation) {
                            throw new RuntimeException("boom");
                        }
                    };
                }
                return null;
            }
        );
        Field f = EvolutionStrategies.class.getDeclaredField("iffatherselection");
        f.setAccessible(true);
        f.set(es, throwingFactory);

        State out = es.generate(0);
        assertNotNull(out);
    }

    @Test
    public void testGenerate_SelectionReturnsEmpty_FallsBackToRandomSearch() throws Exception {
        EvolutionStrategies es = new EvolutionStrategies();
        es.setListStateReference(new ArrayList<>());

        State rs = new State();
        rs.setNumber(99);
        rs.setCode(new ArrayList<>(List.of("R")));
        rs.setEvaluation(new ArrayList<>(List.of(1.0)));
        rs.setTypeGenerator(GeneratorType.EVOLUTION_STRATEGIES);
        RandomSearch.listStateReference.clear();
        RandomSearch.listStateReference.add(rs);

        // Replace internal selection factory with one that returns empty fathers
        Object emptyFactory = Proxy.newProxyInstance(
            EvolutionStrategies.class.getClassLoader(),
            new Class<?>[]{factory_interface.IFFactoryFatherSelection.class},
            (proxy, method, args) -> {
                if (method.getName().equals("createSelectFather")) {
                    return new evolutionary_algorithms.complement.FatherSelection() {
                        @Override
                        public List<State> selection(List<State> listState, int truncation) {
                            return new ArrayList<>();
                        }
                    };
                }
                return null;
            }
        );
        Field f = EvolutionStrategies.class.getDeclaredField("iffatherselection");
        f.setAccessible(true);
        f.set(es, emptyFactory);

        State out = es.generate(0);
        assertNotNull(out);
    }

    @Test
    public void testGenerate_CandidateNull_FallsBackToStrategyProblemStateCopy() throws Exception {
        EvolutionStrategies es = new EvolutionStrategies();

        // Fathers list must exist; use one father whose getCopy will throw -> candidate remains null
        State badCopyFather = new State() {
            @Override
            public State getCopy() {
                throw new RuntimeException("no copy");
            }
        };
        badCopyFather.setCode(new ArrayList<>(List.of("F")));
        badCopyFather.setEvaluation(new ArrayList<>(List.of(7.0)));
        badCopyFather.setNumber(1);
        badCopyFather.setTypeGenerator(GeneratorType.EVOLUTION_STRATEGIES);
        es.setListStateReference(new ArrayList<>(List.of(badCopyFather)));

        // Provide a base state in the strategy's problem
        State base = new State();
        base.setCode(new ArrayList<>(List.of("BASE")));
        base.setEvaluation(new ArrayList<>(List.of(0.0)));
        Strategy.getStrategy().getProblem().setState(base);

        State out = es.generate(0);
        assertNotNull(out);
        assertNotNull(out.getCode());
    }

    @Test
    public void testConstructorAndType() {
        EvolutionStrategies es = new EvolutionStrategies();
        assertEquals(GeneratorType.EVOLUTION_STRATEGIES, es.getType(), "Type should be EVOLUTION_STRATEGIES");
        assertNotNull(es.getListStateReference(), "List of references should not be null after construction");
    }

    @Test
    public void testSetAndGetListStateReference() {
        EvolutionStrategies es = new EvolutionStrategies();
        State s = new State();
        s.setNumber(42);
        List<State> list = new ArrayList<>();
        list.add(s);
        es.setListStateReference(list);
        assertEquals(1, es.getListStateReference().size());
        assertSame(list, es.getListStateReference());
        assertEquals(42, es.getListStateReference().get(0).getNumber());
    }

    @Test
    public void testGenerateReturnsCandidateWhenReferenceAvailable() throws Exception {
        EvolutionStrategies es = new EvolutionStrategies();
        State father = new State();
        father.setNumber(7);
        ArrayList<Object> code = new ArrayList<>(); code.add("A");
        father.setCode(code);
        ArrayList<Double> eval = new ArrayList<>(); eval.add(1.23);
        father.setEvaluation(eval);
        father.setTypeGenerator(GeneratorType.EVOLUTION_STRATEGIES);

        List<State> list = new ArrayList<>();
        list.add(father);
        es.setListStateReference(list);

        // With a single father, generate should return a (possibly mutated) candidate, not null
        State candidate = es.generate(0);
        assertNotNull(candidate, "generate should return a candidate when references exist");
        assertNotNull(candidate.getCode(), "candidate code should not be null");
        assertEquals(father.getNumber(), candidate.getNumber());
    }

    @Test
    public void testGetReference_maximizar_picksHighestEvaluation() {
        EvolutionStrategies es = new EvolutionStrategies();

        List<State> list = new ArrayList<>();
        list.add(stateWithEval(1.0));
        list.add(stateWithEval(5.0));
        list.add(stateWithEval(3.0));
        es.setListStateReference(list);

        Strategy.getStrategy().getProblem().setTypeProblem(ProblemType.MAXIMIZAR);
        State best = es.getReference();
        assertNotNull(best);
        assertEquals(5.0, best.getEvaluation().get(0));
    }

    @Test
    public void testGetReference_minimizar_picksLowestEvaluation() {
        EvolutionStrategies es = new EvolutionStrategies();

        List<State> list = new ArrayList<>();
        list.add(stateWithEval(10.0));
        list.add(stateWithEval(2.0));
        list.add(stateWithEval(7.0));
        es.setListStateReference(list);

        Strategy.getStrategy().getProblem().setTypeProblem(ProblemType.MINIMIZAR);
        State best = es.getReference();
        assertNotNull(best);
        assertEquals(2.0, best.getEvaluation().get(0));
    }

    @Test
    public void testGetReferenceList_returnsCopy() {
        EvolutionStrategies es = new EvolutionStrategies();
        List<State> list = new ArrayList<>();
        list.add(stateWithEval(1.0));
        list.add(stateWithEval(2.0));
        es.setListStateReference(list);

        List<State> copy = es.getReferenceList();
        assertNotSame(list, copy);
        assertEquals(2, copy.size());
        assertSame(list.get(0), copy.get(0));
    }

    @Test
    public void testSetStateRef_andTypeGenerator_andCountersAndTrace() {
        EvolutionStrategies es = new EvolutionStrategies();
        State s = stateWithEval(9.0);
        es.setStateRef(s);
        // getReference() reads from listStateReference, so populate it explicitly
        List<State> list = new ArrayList<>();
        list.add(s);
        es.setListStateReference(list);
        assertSame(s, es.getReference());

        es.setTypeGenerator(GeneratorType.EVOLUTION_STRATEGIES);
        assertEquals(GeneratorType.EVOLUTION_STRATEGIES, es.getTypeGenerator());

        assertNull(es.getSonList());
        assertFalse(es.awardUpdateREF(new State()));

        assertNotNull(es.getListCountBetterGender());
        assertNotNull(es.getListCountGender());
        assertNotNull(es.getTrace());
        assertTrue(es.getTrace().length >= 1);
    }

    private static State stateWithEval(double eval) {
        State s = new State();
        ArrayList<Double> e = new ArrayList<>();
        e.add(eval);
        s.setEvaluation(e);
        s.setCode(new ArrayList<>());
        return s;
    }

}
