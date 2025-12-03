package local_search.candidate_type;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;

public class NotDominatedCandidateTest {

    private State eval(double... vals) {
        State s = new State();
        ArrayList<Double> e = new ArrayList<>();
        for (double v : vals) e.add(v);
        s.setEvaluation(e);
        return s;
    }

    @Test
    public void returnsNonDominatedFirst() throws Exception {
        // Prepare strategy with type Maximize
        Problem p = new Problem();
        p.setTypeProblem(Problem.ProblemType.MAXIMIZAR);
        // Minimal objective to allow Problem.Evaluate to work
        ArrayList<problem.definition.ObjetiveFunction> functions = new ArrayList<>();
        functions.add(new problem.definition.ObjetiveFunction() {
            { setTypeProblem(Problem.ProblemType.MAXIMIZAR); setWeight(1.0f); }
            @Override public Double Evaluation(State state) {
                // Preserve the first value from current evaluation if present
                return state.getEvaluation() == null || state.getEvaluation().isEmpty() ? 0.0 : state.getEvaluation().get(0);
            }
        });
        p.setFunction(functions);
        Strategy.getStrategy().setProblem(p);

        List<State> neighborhood = new ArrayList<>();
        State a = eval(5.0, 5.0);
        State b = eval(3.0, 4.0);
        neighborhood.add(a);
        neighborhood.add(b);

        NotDominatedCandidate ndc = new NotDominatedCandidate();
        State picked = ndc.stateSearch(neighborhood);
        assertEquals(a.getEvaluation(), picked.getEvaluation());
    }
}
