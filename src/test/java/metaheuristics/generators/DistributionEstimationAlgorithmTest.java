package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import evolutionary_algorithms.complement.ReplaceType;
import evolutionary_algorithms.complement.SamplingType;
import evolutionary_algorithms.complement.SelectionType;
import metaheurictics.strategy.Strategy;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.definition.Problem.ProblemType;
import problem.definition.State;

public class DistributionEstimationAlgorithmTest {

    @BeforeEach
    public void setUp() throws Exception {
        Strategy.destroyExecute();

        Strategy strategy = Strategy.getStrategy();
        strategy.initialize();

        Problem problem = new Problem();
        problem.setTypeProblem(ProblemType.MAXIMIZAR);
        problem.setPossibleValue(10);

        ArrayList<ObjetiveFunction> functions = new ArrayList<>();
        functions.add(new ObjetiveFunction() {
            @Override
            public Double Evaluation(State state) {
                double sum = 0;
                if (state.getCode() != null) {
                    for (Object o : state.getCode()) {
                        if (o instanceof Integer) {
                            sum += (Integer) o;
                        }
                    }
                }
                return sum;
            }
        });
        problem.setFunction(functions);
        Strategy.setProblem(problem);

        DistributionEstimationAlgorithm.selectionType = SelectionType.TRUNCATION_SELECTION;
        DistributionEstimationAlgorithm.truncation = 2;
        DistributionEstimationAlgorithm.replaceType = ReplaceType.GENERATIONAL_REPLACE;

        DistributionEstimationAlgorithm eda = (DistributionEstimationAlgorithm) strategy.mapGenerators
            .get(GeneratorType.DISTRIBUTION_ESTIMATION_ALGORITHM);

        // Force sampling type (FactorySampling uses SamplingType.toString() to build class name)
        setSamplingTypeViaReflection(eda, SamplingType.PROBABILISTIC_SAMPLING);

        // Provide a minimal reference population with evaluations set (needed by selection)
        List<State> refs = new ArrayList<>();
        refs.add(stateWithEval(1, 2, 3)); // sum=6
        refs.add(stateWithEval(4, 0, 0)); // sum=4
        refs.add(stateWithEval(9, 9, 9)); // sum=27

        eda.setListReference(refs);
    }

    @Test
    public void testConstructorAndType() {
        DistributionEstimationAlgorithm dea = new DistributionEstimationAlgorithm();
        assertEquals(GeneratorType.DISTRIBUTION_ESTIMATION_ALGORITHM, dea.getType());
    }

    @Test
    public void testListReferenceAccessors() {
        DistributionEstimationAlgorithm dea = new DistributionEstimationAlgorithm();
        State a = new State();
        ArrayList<Double> ea = new ArrayList<>(); ea.add(1.0); a.setEvaluation(ea);
        State b = new State();
        ArrayList<Double> eb = new ArrayList<>(); eb.add(2.0); b.setEvaluation(eb);

        List<State> list = new ArrayList<>();
        list.add(a); list.add(b);
        dea.setListReference(list);

        // getListReference returns the same internal list (by design)
        assertSame(list, dea.getListReference());

        // getReferenceList returns a copy
        List<State> copy = dea.getReferenceList();
        assertNotSame(list, copy);
        assertEquals(2, copy.size());
    }

    @Test
    public void testMaxValuePicksHighestEvaluation() {
        DistributionEstimationAlgorithm dea = new DistributionEstimationAlgorithm();
        State a = new State();
        ArrayList<Double> ea = new ArrayList<>(); ea.add(1.0); a.setEvaluation(ea);
        State b = new State();
        ArrayList<Double> eb = new ArrayList<>(); eb.add(5.5); b.setEvaluation(eb);
        List<State> list = new ArrayList<>(); list.add(a); list.add(b);

        State best = dea.MaxValue(list);
        assertEquals(5.5, best.getEvaluation().get(0));
    }

    @Test
    public void testAwardUpdateRefChecksPresenceByIdentity() {
        DistributionEstimationAlgorithm eda = (DistributionEstimationAlgorithm) Strategy.getStrategy().mapGenerators
                .get(GeneratorType.DISTRIBUTION_ESTIMATION_ALGORITHM);
        List<State> refs = eda.getListReference();
        assertFalse(refs.isEmpty());
        State sameInstance = refs.get(0);
        assertTrue(eda.awardUpdateREF(sameInstance));
        assertFalse(eda.awardUpdateREF(stateWithEval(100)));
    }

    @Test
    public void getReference_maximization_returnsBestInReferenceList() {
        DistributionEstimationAlgorithm eda = (DistributionEstimationAlgorithm) Strategy.getStrategy().mapGenerators
                .get(GeneratorType.DISTRIBUTION_ESTIMATION_ALGORITHM);
        State best = eda.getReference();
        assertNotNull(best);
        assertEquals(27.0, best.getEvaluation().get(0));
    }

    @Test
    public void setInitialReference_setsStateReferenceDA_withoutThrowing() {
        DistributionEstimationAlgorithm eda = (DistributionEstimationAlgorithm) Strategy.getStrategy().mapGenerators
                .get(GeneratorType.DISTRIBUTION_ESTIMATION_ALGORITHM);
        State s = stateWithEval(7, 7);
        assertDoesNotThrow(() -> eda.setInitialReference(s));
    }

    @Test
    public void getfathersList_truncationSelection_returnsLimitedBestStates() throws Exception {
        DistributionEstimationAlgorithm eda = (DistributionEstimationAlgorithm) Strategy.getStrategy().mapGenerators
                .get(GeneratorType.DISTRIBUTION_ESTIMATION_ALGORITHM);
        List<State> fathers = eda.getfathersList();
        assertNotNull(fathers);
        assertEquals(2, fathers.size());
        assertEquals(27.0, fathers.get(0).getEvaluation().get(0));
    }

    // NOTE: generate() currently relies on FactorySampling which builds the class name as
    // "evolutionary_algorithms.complement." + SamplingType.toString(). For PROBABILISTIC_SAMPLING this becomes
    // "evolutionary_algorithms.complement.PROBABILISTIC_SAMPLING" which does not exist (the class is ProbabilisticSampling).
    // Since the user requested not to touch production code, we avoid calling generate() here.

    @Test
    public void getters_coverDistributionType_sonList_counts_trace() {
        DistributionEstimationAlgorithm eda = (DistributionEstimationAlgorithm) Strategy.getStrategy().mapGenerators
                .get(GeneratorType.DISTRIBUTION_ESTIMATION_ALGORITHM);
        assertNotNull(eda.getDistributionType());
        assertNotNull(eda.getSonList());
        assertNotNull(eda.getListCountBetterGender());
        assertNotNull(eda.getListCountGender());
        assertNotNull(eda.getTrace());
        assertTrue(eda.getTrace().length >= 1);
    }

    private static State stateWithEval(Integer... codeValues) {
        State s = new State();
        ArrayList<Object> code = new ArrayList<>();
        for (Integer v : codeValues) {
            code.add(v);
        }
        s.setCode(code);

        double sum = 0;
        for (Integer v : codeValues) {
            sum += v;
        }
        ArrayList<Double> eval = new ArrayList<>();
        eval.add(sum);
        s.setEvaluation(eval);
        return s;
    }

    private static void setSamplingTypeViaReflection(DistributionEstimationAlgorithm eda, SamplingType type) throws Exception {
        java.lang.reflect.Field f = DistributionEstimationAlgorithm.class.getDeclaredField("Samplingtype");
        f.setAccessible(true);
        f.set(eda, type);
    }
}
