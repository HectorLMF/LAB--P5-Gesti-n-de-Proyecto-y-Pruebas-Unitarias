package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.ObjetiveFunction;
import problem.definition.Operator;
import problem.definition.Problem;
import problem.definition.Problem.ProblemType;
import problem.definition.State;

public class HillClimbingRestartTest {

    private HillClimbingRestart gen;

    @BeforeEach
    void setUp() throws Exception {
        Strategy.destroyExecute();

        Problem p = new Problem();
        p.setTypeProblem(ProblemType.MAXIMIZAR);
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        funcs.add(new ObjetiveFunction() {
            @Override
            public Double Evaluation(State state) {
                if (state.getEvaluation() != null && !state.getEvaluation().isEmpty()) {
                    return state.getEvaluation().get(0);
                }
                return 0.0;
            }
        });
        p.setFunction(funcs);
        p.setOperator(new Operator() {
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
                ArrayList<State> l = new ArrayList<>();
                l.add(stateWithEval(1.0));
                l.add(stateWithEval(5.0));
                return l;
            }

            @Override
            public List<State> generateRandomState(Integer operatornumber) {
                ArrayList<State> l = new ArrayList<>();
                l.add(stateWithEval(3.0));
                return l;
            }
        });
        Strategy.setProblem(p);

        gen = new HillClimbingRestart();
        gen.setInitialReference(stateWithEval(2.0));

        HillClimbingRestart.count = 0;
        HillClimbingRestart.countCurrent = 1;
        Strategy.getStrategy().setCountCurrent(0);
    }

    @Test
    void type_isHillClimbingRestart() {
        assertEquals(GeneratorType.HILL_CLIMBING_RESTART, gen.getType());
        assertEquals(GeneratorType.HILL_CLIMBING_RESTART, gen.getGeneratorType());
    }

    @Test
    void generate_triggersRestart_whenCountEqualsCurrent() throws Exception {
        assertEquals(2.0, gen.getReference().getEvaluation().get(0));

        State candidate = gen.generate(1);
        assertNotNull(candidate);

        // reference should have been restarted to eval=3.0
        assertEquals(3.0, gen.getReference().getEvaluation().get(0));
    }

    @Test
    void updateReference_acceptBest_updatesReferenceWhenCandidateBetter() throws Exception {
        gen.setInitialReference(stateWithEval(1.0));
        State candidate = stateWithEval(10.0);

        gen.updateReference(candidate, 0);
        assertSame(candidate, gen.getReference());
    }

    @Test
    void updateReference_acceptBest_doesNotUpdateWhenCandidateWorse() throws Exception {
        State current = stateWithEval(10.0);
        gen.setInitialReference(current);
        State candidate = stateWithEval(1.0);

        gen.updateReference(candidate, 0);
        assertSame(current, gen.getReference());
    }

    @Test
    void getReferenceList_addsCurrentReference() {
        List<State> refs = gen.getReferenceList();
        assertFalse(refs.isEmpty());
        assertSame(gen.getReference(), refs.get(refs.size() - 1));
    }

    @Test
    void getters_coverSonList_weight_trace() {
        assertNull(gen.getSonList());
        assertEquals(0.0f, gen.getWeight());
        assertNotNull(gen.getTrace());
        assertTrue(gen.getTrace().length >= 1);
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
