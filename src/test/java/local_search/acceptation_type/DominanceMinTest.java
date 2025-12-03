package local_search.acceptation_type;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;

public class DominanceMinTest {

    private State eval(double... vals) {
        State s = new State();
        ArrayList<Double> e = new ArrayList<>();
        for (double v : vals) e.add(v);
        s.setEvaluation(e);
        return s;
    }

    @Test
    public void xDominatesYInMinimization() {
        Problem p = new Problem();
        p.setTypeProblem(Problem.ProblemType.MINIMIZAR);
        Strategy.getStrategy().setProblem(p);

        Dominance d = new Dominance();
        State x = eval(4.0, 1.0);
        State y = eval(4.0, 2.0);
        assertTrue(d.dominance(x, y));
        assertFalse(d.dominance(y, x));
    }
}
