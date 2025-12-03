package local_search.acceptation_type;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;

public class DominanceMaxTest {

    private State eval(double... vals) {
        State s = new State();
        ArrayList<Double> e = new ArrayList<>();
        for (double v : vals) e.add(v);
        s.setEvaluation(e);
        return s;
    }

    @Test
    public void xDominatesYInMaximization() {
        Problem p = new Problem();
        p.setTypeProblem(Problem.ProblemType.Maximizar);
        Strategy.getStrategy().setProblem(p);

        Dominance d = new Dominance();
        State x = eval(5.0, 3.0);
        State y = eval(5.0, 2.5);
        assertTrue(d.dominance(x, y));
        assertFalse(d.dominance(y, x));
    }
}
