package problem.extension;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import metaheurictics.strategy.Strategy;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.definition.State;

public class SolutionMethodsTest {

    private Problem buildProblem(Problem.ProblemType problemType) {
        Problem p = new Problem();
        p.setTypeProblem(problemType);
        ArrayList<ObjetiveFunction> functions = new ArrayList<>();
        // f1: returns 0.2
        ObjetiveFunction f1 = new ObjetiveFunction() {
            { setTypeProblem(Problem.ProblemType.Maximizar); setWeight(0.5f); }
            @Override public Double Evaluation(State state) { return 0.2; }
        };
        // f2: returns 0.6
        ObjetiveFunction f2 = new ObjetiveFunction() {
            { setTypeProblem(Problem.ProblemType.Minimizar); setWeight(0.5f); }
            @Override public Double Evaluation(State state) { return 0.6; }
        };
        functions.add(f1);
        functions.add(f2);
        p.setFunction(functions);
        return p;
    }

    @Test
    public void factoresPonderadosProducesSingleAggregatedValue() {
        Strategy.getStrategy().setProblem(buildProblem(Problem.ProblemType.Maximizar));
        State s = new State();
        FactoresPonderados fp = new FactoresPonderados();
        fp.evaluationState(s);
        assertNotNull(s.getEvaluation());
        assertEquals(1, s.getEvaluation().size());
        // For maximizing: f1 (0.2*0.5) + (1-0.6)*0.5 = 0.1 + 0.2 = 0.3
        assertEquals(0.3, s.getEvaluation().get(0), 1e-9);
    }

    @Test
    public void factoresPonderadosMinimizarBranch() {
        Strategy.getStrategy().setProblem(buildProblem(Problem.ProblemType.Minimizar));
        State s = new State();
        FactoresPonderados fp = new FactoresPonderados();
        fp.evaluationState(s);
        assertNotNull(s.getEvaluation());
        assertEquals(1, s.getEvaluation().size());
        // For minimizing: (1-0.2)*0.5 + 0.6*0.5 = 0.4 + 0.3 = 0.7
        assertEquals(0.7, s.getEvaluation().get(0), 1e-9);
    }

    @Test
    public void multiObjetivoPuroProducesPerObjectiveValues() {
        Strategy.getStrategy().setProblem(buildProblem(Problem.ProblemType.Minimizar));
        State s = new State();
        MultiObjetivoPuro mo = new MultiObjetivoPuro();
        mo.evaluationState(s);
        assertNotNull(s.getEvaluation());
        assertEquals(2, s.getEvaluation().size());
        // For minimizing: [1-0.2, 0.6] => [0.8, 0.6]
        assertEquals(0.8, s.getEvaluation().get(0), 1e-9);
        assertEquals(0.6, s.getEvaluation().get(1), 1e-9);
    }
}
