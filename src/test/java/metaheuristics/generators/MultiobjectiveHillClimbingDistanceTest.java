package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.ObjetiveFunction;
import problem.definition.Operator;
import problem.definition.Problem;
import problem.definition.State;

public class MultiobjectiveHillClimbingDistanceTest {

    @BeforeEach
    void setUp() {
        Strategy.destroyExecute();
        Problem p = new Problem();
        p.setTypeProblem(Problem.ProblemType.MINIMIZAR);
        // Minimal objective function
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        funcs.add(new ObjetiveFunction() {
            @Override public Double Evaluation(State state) { return state.getEvaluation() == null || state.getEvaluation().isEmpty() ? 0.0 : state.getEvaluation().get(0); }
        });
        p.setFunction(funcs);
        // Minimal operator returning empty neighborhood and a single random state
        p.setOperator(new Operator() {
            @Override public List<State> generatedNewState(State stateCurrent, Integer operatornumber) { return new ArrayList<>(); }
            @Override public List<State> generateRandomState(Integer operatornumber) { ArrayList<State> l = new ArrayList<>(); l.add(new State()); return l; }
        });
        Strategy.setProblem(p);
        // Ensure static distance list starts clean between tests
        MultiobjectiveHillClimbingDistance.distanceSolution.clear();
    }

    @Test
    @DisplayName("Constructor sets generator type")
    void testConstructorAndType() {
        MultiobjectiveHillClimbingDistance gen = new MultiobjectiveHillClimbingDistance();
        assertEquals(GeneratorType.MULTIOBJECTIVE_HILL_CLIMBING_DISTANCE, gen.getType());
    }

    @Test
    @DisplayName("getReferenceList stores a copy of reference")
    void testReferenceListCopy() {
        MultiobjectiveHillClimbingDistance gen = new MultiobjectiveHillClimbingDistance();
        State ref = new State();
        ref.setCode(new ArrayList<>());
        ArrayList<Double> ev = new ArrayList<>(); ev.add(2.0); ref.setEvaluation(ev);
        gen.setInitialReference(ref);
        assertSame(ref, gen.getReference());
        List<State> refs = gen.getReferenceList();
        assertTrue(refs.size() >= 1);
        State last = refs.get(refs.size() - 1);
        assertNotSame(ref, last);
        assertEquals(ref.getEvaluation().get(0), last.getEvaluation().get(0));
    }

    @Test
    @DisplayName("DistanceCalculateAdd updates accumulated distances")
    void testDistanceCalculateAdd() {
        // Build three states with simple codes (Hamming distance)
        State s1 = new State(); s1.setCode(new ArrayList<>(List.of(0,0,0)));
        State s2 = new State(); s2.setCode(new ArrayList<>(List.of(1,0,0))); // d(s1,s2)=1
        State s3 = new State(); s3.setCode(new ArrayList<>(List.of(1,1,0))); // d to s1=2, to s2=1

        ArrayList<State> sols = new ArrayList<>();
        sols.add(s1);
        List<Double> d1 = MultiobjectiveHillClimbingDistance.DistanceCalculateAdd(sols);
        assertEquals(1, d1.size());
        assertEquals(0.0, d1.get(0));

        sols.add(s2);
        List<Double> d2 = MultiobjectiveHillClimbingDistance.DistanceCalculateAdd(sols);
        assertEquals(List.of(1.0, 1.0), d2);

        sols.add(s3);
        List<Double> d3 = MultiobjectiveHillClimbingDistance.DistanceCalculateAdd(sols);
        // Expected: previous [1,1] + distances to new last [2,1] -> [3,2], and last sum 3 -> [3,2,3]
        assertEquals(3, d3.size());
        assertEquals(3.0, d3.get(0));
        assertEquals(2.0, d3.get(1));
        assertEquals(3.0, d3.get(2));
    }

    @Test
    @DisplayName("updateReference accepts better candidate and updates reference")
    void testUpdateReferenceAcceptsBetter() throws Exception {
        MultiobjectiveHillClimbingDistance gen = new MultiobjectiveHillClimbingDistance();
        State ref = new State(); ref.setCode(new ArrayList<>());
        ArrayList<Double> evRef = new ArrayList<>(); evRef.add(10.0); ref.setEvaluation(evRef);
        gen.setInitialReference(ref);

        State cand = new State(); cand.setCode(new ArrayList<>());
        ArrayList<Double> evCand = new ArrayList<>(); evCand.add(5.0); cand.setEvaluation(evCand);

        gen.updateReference(cand, 0);
        assertEquals(5.0, gen.getReference().getEvaluation().get(0));
    }
}
