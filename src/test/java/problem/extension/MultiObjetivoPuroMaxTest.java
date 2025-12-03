package problem.extension;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import metaheurictics.strategy.Strategy;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.definition.State;

public class MultiObjetivoPuroMaxTest {

    private Problem buildProblem() {
        Problem p = new Problem();
        p.setTypeProblem(Problem.ProblemType.Maximizar);
        ArrayList<ObjetiveFunction> functions = new ArrayList<>();
        ObjetiveFunction f1 = new ObjetiveFunction() {
            { setTypeProblem(Problem.ProblemType.Maximizar); }
            @Override public Double Evaluation(State state) { return 0.4; }
        };
        ObjetiveFunction f2 = new ObjetiveFunction() {
            { setTypeProblem(Problem.ProblemType.Minimizar); }
            @Override public Double Evaluation(State state) { return 0.7; }
        };
        functions.add(f1);
        functions.add(f2);
        p.setFunction(functions);
        return p;
    }

    @Test
    public void evaluatesPerObjectiveForMaximization() {
        Strategy.getStrategy().setProblem(buildProblem());
        State s = new State();
        MultiObjetivoPuro mo = new MultiObjetivoPuro();
        mo.evaluationState(s);
        assertEquals(2, s.getEvaluation().size());
        assertEquals(0.4, s.getEvaluation().get(0), 1e-9);
        assertEquals(0.3, s.getEvaluation().get(1), 1e-9);
    }
}
